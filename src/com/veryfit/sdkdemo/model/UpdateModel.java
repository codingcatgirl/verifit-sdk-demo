package com.veryfit.sdkdemo.model;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.UUID;

import no.nordicsemi.android.dfu.DfuService;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.veryfit.multi.ble.BleManager;
import com.veryfit.multi.entity.BleDevice;
import com.veryfit.multi.nativeprotocol.ProtocolEvt;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.multi.share.BleSharedPreferences;
import com.veryfit.multi.util.BleScanTool;
import com.veryfit.multi.util.Constant;
import com.veryfit.multi.util.DebugLog;

/**
 * Created by asus on 2016/7/6.
 */
public class UpdateModel {

	public static final byte STATUS_SUCCESS = 0x00;
	public static final byte STATUS_LOW_BATTERY = 0x01;
	public static final byte STATUS_NOT_SUPPORT = 0x02;
	public static final int UPDATE_CMD_RESULT = 10;
	public static final int UPDATE_CMD_FAILED = 11;
	public static final int UPDATE_FAILED = 12;
	public static final int BLUETOOTH_NOT_OPEN = 100;
	public static final int BLEDISCONNECTED = 10086;
	public static final String ACTION_UPDATE_SUCEESS = "com.veryfit2.2.update.success";
	private final static int REUPDATEE_MAX_COUNT = 6;
	private static final UUID DFU_SERVICE_UUID = new UUID(0x000015301212EFDEl, 0x1523785FEABCD123l);
	private static final int DELAY = 500;
	private ProtocolUtils protocolUtils = ProtocolUtils.getInstance();
	private boolean isUpdateSuccess;
	private LocalBroadcastManager broadcastManager;
	private int updateCount;
	private int faildCode;
	private int failed_reason;

	private IUpdateListener iUpdateListener;
	private BleScanTool sTool = BleScanTool.getInstance();

	private MyAppBleListener myAppBleListener = new MyAppBleListener();
	private int connetCount = 0;
	/**
	 * 是否正在升级
	 */
	private boolean isUpdating = false;
	private IGetDeviceUpdateInfoListener listener;
	private boolean isDfuMode;
	private String deviceAdd;
	private int deviceId;
	private long startTime = 0;
	/**
     *
     */
	private boolean isSynComplted = false;
	private boolean isConnection = true;
	private Context mContext;
	/**
	 * 发送升级命令的次数
	 */
	private int sendUpdateCmdCount = 0;
	private String demoPath;
	private String filePath;
	/**
	 * DfuService发出的设备升级事件 设备升级事件广播接收器
	 */
	private final BroadcastReceiver mDfuUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			// DFU is in progress or an error occurred
			final String action = intent.getAction();
			if (isUpdateSuccess()) {
				return;
			}
			if (DfuService.BROADCAST_PROGRESS.equals(action)) {
				final int progress = intent.getIntExtra(DfuService.EXTRA_DATA, 0);
				final int currentPart = intent.getIntExtra(DfuService.EXTRA_PART_CURRENT, 1);
				final int totalParts = intent.getIntExtra(DfuService.EXTRA_PARTS_TOTAL, 1);
				d("currentPart:" + currentPart + ",totalParts:" + totalParts + ",progress:" + progress);

				if (iUpdateListener != null) {
					iUpdateListener.updateProgressBar(progress);
				}
				if (progress >= 99) {
					setIsUpdateSuccess(true);
					iUpdateListener.updateSuccess();
				}

			} else if (DfuService.BROADCAST_ERROR.equals(action)) {
				updateFaild(intent);
			} else if ("com.veryfit.multi.ACTION_SINGLE_BANK_WARE_UPDATE".equals(action)) {
				updateFaild(intent);

			}
		}
	};
	/**
	 * 是否同步数据
	 */
	private boolean isSyn = false;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case HttpUtil.DOWN_LOAD_PROGRESS:
				int pro = (Integer) msg.obj;
				d("正在下载升级文件 进度:" + msg.obj);
				if (pro == 110) {
					d("升级文件 下载完毕。。开始升级");
					update();
				}
				break;
			}
			;
		}
	};
	private BaseCallBack protocalCallBack = new BaseCallBack() {
		@Override
		public void onSysEvt(int evt_base, int evt_type, int error, int value) {
			if (!isUpdating) {
				return;
			}

			UpdateModel.this.d("onSysEvt，evt_type:" + evt_type + ",value:" + value + ",error:" + error);
			// 设备进入升级模式
			if (!isDfuMode && evt_type == ProtocolEvt.OTA_START.toIndex()) {

				//
				if (error != 0) {
					if (sendUpdateCmdCount < REUPDATEE_MAX_COUNT) {

						UpdateModel.this.d("升级命令发送失败....继续发送升级命令,发送次数:" + sendUpdateCmdCount);
						sendUpdateCmd();
					} else {
						if (iUpdateListener != null) {
							iUpdateListener.updateFaild();
						}

					}

				} else {
					UpdateModel.this.d("设备进入升级模式....");
					ProtocolUtils.getInstance().setUnConnect();
					setIsDfuMode(true);
					// 设备初始化升级模式需要一定时间,因此延迟开启升级服务.
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							startDfuService();
						}
					}, 3000);
				}

			} else if (evt_type == ProtocolEvt.SYNC_EVT_HEALTH_PROGRESS.toIndex()) {
				UpdateModel.this.d("同步中,value:" + value);// 其中value为同步的百分比
				if (iUpdateListener != null) {
					iUpdateListener.synchroData(value);
				}
				if (value == 100) {
					isSyn = true;
				}
			} else if (evt_type == ProtocolEvt.SYNC_EVT_HEALTH_SYNC_COMPLETE.toIndex()) {
				isSyn = true;
				if (!isSynComplted) {
					UpdateModel.this.d("同步完成,发送升级命令");
					sendUpdateCmd();
					isSynComplted = true;
				}
			}

		}

	};
	private BleScanTool.ScanDeviceListener mScanDeviceListener = new BleScanTool.ScanDeviceListener() {

		boolean isSearch;

		@Override
		public void onFinish() {
			close();
			if (!isSearch) {
				startDfuService();
			}
		}

		@Override
		public void onFind(BleDevice device) {
			d("onFind....device.mDeviceAddress:" + device.mDeviceAddress);
			isSearch = false;

			d("mDeviceId:" + device.mDeviceId + ",mId:" + device.mId + ",mIs:" + device.mIs + ",mLen:" + device.mLen + ",mRssi" + device.mRssi);
			if (device.mId == 10 && device.mIs == 240) {
				d("-----------------------扫描成功 -----------------------");
				isSearch = true;
				startDfuService();
				close();

				// }
			}
		}

		private void close() {
			// sTool.removeScanDeviceListener(mScanDeviceListener);
			if (sTool.isScanning()) {
				// sTool.scanLeDeviceDFU(false);
				sTool.scanLeDevice(false,8000L);
			}
		}
	};

	public UpdateModel(Context context) {
		setDeviceAdd(BleSharedPreferences.getInstance().getBindDeviceAddr());
		sendUpdateCmdCount = 0;
		mContext = context;
		broadcastManager = LocalBroadcastManager.getInstance(context);
		protocolUtils.setBleListener(myAppBleListener);
		protocolUtils.setProtocalCallBack(protocalCallBack);
		broadcastManager.registerReceiver(mDfuUpdateReceiver, makeDfuUpdateIntentFilter());

		sTool.addScanDeviceListener(mScanDeviceListener);

		File dir = new File(Constant.APP_ROOT_PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		filePath = Constant.APP_ROOT_PATH + "/sdk_demo_update.zip";
		demoPath = filePath;

	}

	/**
	 * 发送升级命令
	 */
	public void sendUpdateCmd() {

		if (connetCount > REUPDATEE_MAX_COUNT) {
			if (iUpdateListener != null) {
				iUpdateListener.updateFaild();
			}
			iUpdateListener.updateFaild();
			return;
		}

		if (!isBluetoothOpen()) {
			return;
		}
		d("sendUpdateCmd isDfuMode:" + isDfuMode());

		/**
		 * 1.设备处于升级模式,直接开启服务
		 */
		if (isDfuMode()) {
			startDfuService();
			// 设备没有处于升级状态,发送升级命令....设备重启后.isDeviceConnected为true.
		} else if (isConnection && (BleManager.getInstance().isDeviceConnected()) && !isDfuMode()) {
			DebugLog.d("发送升级命令");
			sendUpdateCmdCount++;
			protocolUtils.upgradeMode();

		} else if ((!BleManager.getInstance().isDeviceConnected() || !isConnection)) {
			protocolUtils.connect(getDeviceAdd());
		}

	}

	public void closeResource() {
		if (broadcastManager != null) {
			broadcastManager.unregisterReceiver(mDfuUpdateReceiver);
		}
		protocolUtils.removeProtocalCallBack(protocalCallBack);
		protocolUtils.removeListener(myAppBleListener);
		sTool.removeScanDeviceListener(mScanDeviceListener);

	}

	public void update() {

		updateCount = 0;
		isUpdating = true;
		sendUpdateCmdCount = 0;
		sendUpdateCmd();

	}

	public void downAndUpdate() {
		listener = new IGetDeviceUpdateInfoListener() {
			@Override
			public void getUpdateDeviceInfo(DeviceUpdateInfo deviceUpdateInfo) {
				// 下载文件
				d("下载升级文件.....");
				downLoadFile(deviceUpdateInfo, demoPath, handler);
			}

			@Override
			public void getUpdateDeviceFaild() {
				if (iUpdateListener != null) {
					iUpdateListener.updateFaild();
				}
			}
		};
		// 获取下载文件的地址
		getDeviceUpdateInfo(true);
	}

	private void sendSynCmd() {

		if (!isBluetoothOpen()) {
			return;
		}

		if (BleManager.getInstance().isDeviceConnected()) {
			d("发送同步数据命令");
			protocolUtils.StartSyncHealthData();
		} else {
			protocolUtils.connect(getDeviceAdd());
		}

	}

	public void setIGetDeviceUpdateInfoListener(IGetDeviceUpdateInfoListener i) {
		this.listener = i;
	}

	public void setIUpdateListener(IUpdateListener i) {
		this.iUpdateListener = i;
	}

	public void getDeviceUpdateInfo(boolean isShowTips) {
		d("BleSharedPreferences.getDeviceAdd():" + BleSharedPreferences.getInstance().getBindDeviceAddr());
		d("getDeviceAdd:" + getDeviceAdd());
		setDeviceAdd(BleSharedPreferences.getInstance().getBindDeviceAddr());
		if (NetWorkUtil.isNetWorkConnected(mContext) && (!TextUtils.isEmpty(BleSharedPreferences.getInstance().getBindDeviceAddr()) || !TextUtils.isEmpty(getDeviceAdd()))) {
			new Thread(new GetUpdateInfoTask()).start();
		} else {
			if (isShowTips) {
				showToast("httpConnError or getDeviceAdd is null");
			}
			if (listener != null) {
				listener.getUpdateDeviceFaild();
			}

		}
	}

	private IntentFilter makeDfuUpdateIntentFilter() {
		final IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(DfuService.BROADCAST_PROGRESS);
		intentFilter.addAction(DfuService.BROADCAST_ERROR);
		intentFilter.addAction("com.veryfit2.second.ACTION_SINGLE_BANK_WARE_UPDATE");
		return intentFilter;
	}

	/**
	 * 下载文件
	 */
	public void downLoadFile(final DeviceUpdateInfo updateInfo, final String filePath, final Handler handler) {
		File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		if (updateInfo.file.isEmpty()) {
			d("updateInfo.file.isEmpty()");
			return;
		}
		if (NetWorkUtil.isNetWorkConnected(mContext)) {
			new Thread() {
				public void run() {
					HttpUtil.downLoad(handler, filePath, updateInfo.file);

				}
			}.start();
		} else {
			d("error .............");
		}
	}

	private void showToast(String msg) {
		Toast.makeText(mContext, msg, 0).show();
	}

	/**
	 * 开启DfuService服务
	 */
	private void startDfuService() {
		if (isUpdateSuccess() && !isBluetoothOpen() && mContext != null) {

			return;
		}
		final boolean keepBond = false;
		final Intent service = new Intent(mContext, DfuService.class);
		String addr = getDeviceAdd();
		if (TextUtils.isEmpty(addr)) {
			d("设备都没有，不升级");
			isUpdating = false;
			return;
		}
		d("开始升级 addr:" + addr + ",filePath:" + getFilePath() + ",");
		service.putExtra(DfuService.EXTRA_DEVICE_ADDRESS, addr);
		service.putExtra(DfuService.EXTRA_DEVICE_NAME, "DfuTarg");
		service.putExtra(DfuService.EXTRA_FILE_PATH, getFilePath());
		service.putExtra(DfuService.EXTRA_KEEP_BOND, keepBond);
		startTime = System.currentTimeMillis();
		mContext.startService(service);
	}

	public void updateFaild(Intent intent) {

		int errorData = intent.getIntExtra(DfuService.EXTRA_DATA, -1);
		int errorType = intent.getIntExtra(DfuService.EXTRA_ERROR_TYPE, -1);
		d("*****EXTRA_DATA:" + intent.getIntExtra(DfuService.EXTRA_DATA, -1));
		d("*****EXTRA_ERROR_TYPE:" + intent.getIntExtra(DfuService.EXTRA_ERROR_TYPE, -1));
		faildCode = errorData;
		failed_reason = errorType;
		d("showUpdateFaildTip  faildCode:" + faildCode);
		if (isUpdateSuccess()) {
			return;
		}
		updateCount++;
		d("升级失败次数:" + updateCount);
		if (updateCount > REUPDATEE_MAX_COUNT) {
			isUpdating = false;
			if (iUpdateListener != null) {
				iUpdateListener.updateFaild();
			}

			// 蓝牙断开
			if (faildCode == 4106) {
				return;
			}
		} else {

			long end = System.currentTimeMillis();
			long dt = (end - startTime) / 1000;
			d("dt:" + dt);
			// 设备重启了
			if (dt > 60) {
				d("设备重启了....");
				updateCount = 0;
				isDfuMode = false;
				isConnection = false;
				isUpdating = false;
				if (iUpdateListener != null) {
					iUpdateListener.updateFaild();
				}
				// sendUpdateCmd();
				return;
			}
			/**
			 * 设备未进入升级模式 4098升级文件错误
			 */
			if (faildCode == 4102 && isDfuMode) {
				if (isDfuMode) {
					// 发送升级命令成功,手环进入升级模式但没有初始化升级参数(DFU Service is not exist).
					handler.postDelayed(new Runnable() {
						@Override
						public void run() {
							sendUpdateCmd();
						}
					}, DELAY);
				}
			} else {
				scanDevice();
				// startDfuService();
			}

		}
	}

	private void scanDevice() {
		d("scanDevice");
		if (!isBluetoothOpen()) {
			d("isBluetoothOpen false");
			updateCount = 0;
			return;
		}
		d("scanLeDeviceByService true");

		// sTool.scanLeDeviceDFU(false);
		sTool.scanLeDevice(false,8000L);
		if (sTool != null && !sTool.isScanning()) {
			// 扫描升级设备
			// sTool.scanLeDeviceDFU(true);
			// sTool.scanLeDevice(true);
			sTool.scanLeDeviceByService(true, DFU_SERVICE_UUID,8000L);
		}
	}

	private boolean isBluetoothOpen() {

		boolean isOpen = BleScanTool.getInstance().isBluetoothOpen();

		if (!isOpen && handler != null) {
			isUpdating = false;
			handler.sendEmptyMessage(BLUETOOTH_NOT_OPEN);

		}
		return isOpen;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Handler getHandler() {
		return handler;
	}

	public void setHandler(Handler handler) {
		this.handler = handler;
	}

	public boolean isUpdateSuccess() {
		return isUpdateSuccess;
	}

	public void setIsUpdateSuccess(boolean isUpdateSuccess) {
		this.isUpdateSuccess = isUpdateSuccess;
	}

	private void d(String msg) {
		Log.d(this.getClass().getSimpleName(), "---------------->" + msg);
	}

	/**
	 * 是否同步过数据
	 */
	public boolean isSyn() {
		return isSyn;
	}

	public void setIsSyn(boolean isSyn) {
		this.isSyn = isSyn;
	}

	public boolean isUpdating() {
		return isUpdating;
	}

	public void setIsUpdating(boolean isUpdating) {
		this.isUpdating = isUpdating;
	}

	/**
	 * 设备是否处于升级模式
	 */
	public boolean isDfuMode() {
		return isDfuMode;
	}

	public void setIsDfuMode(boolean isDfuMode) {
		this.isDfuMode = isDfuMode;
	}

	public String getDeviceAdd() {
		return deviceAdd;
	}

	public void setDeviceAdd(String deviceAdd) {
		this.deviceAdd = deviceAdd;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public interface IGetDeviceUpdateInfoListener {
		void getUpdateDeviceInfo(DeviceUpdateInfo deviceUpdateInfo);

		void getUpdateDeviceFaild();
	}

	public interface IUpdateListener {
		void updateFaild();

		void updateProgressBar(int progress);

		void updateSuccess();

		void synchroData(int progress);
	}

	private class MyAppBleListener extends BaseAppBleListener {
		@Override
		public void onBLEConnected(BluetoothGatt bluetoothGatt) {
			isConnection = true;

			if (!isDfuMode) {
				// 同步数据发送的
				if (isUpdating && !isSyn) {
					UpdateModel.this.d("蓝牙连接发送同步命令");
					sendSynCmd();
					return;
				}
				if (isUpdating && !isDfuMode) {
					UpdateModel.this.d("蓝牙连接.........发送升级命令");
					sendUpdateCmd();
					return;
				}
			}
			//

		}

		@Override
		public void onBLEDisConnected(String s) {
			UpdateModel.this.d("设备断开连接.........");
		}

		@Override
		public void onBLEConnectTimeOut() {
			super.onBLEConnectTimeOut();
			connetCount++;
			UpdateModel.this.d("设备连接超时.........");
			d("connetCount:" + connetCount);
			if (connetCount > REUPDATEE_MAX_COUNT && iUpdateListener != null) {
				iUpdateListener.updateFaild();
			}
		}

		@Override
		public void onDataSendTimeOut(byte[] bytes) {
			super.onDataSendTimeOut(bytes);
			UpdateModel.this.d("发送数据超时.........");
			// sendUpdateCmd();
		}

		@Override
		public void onServiceDiscover(BluetoothGatt bluetoothGatt, int i) {
			super.onServiceDiscover(bluetoothGatt, i);
			// sendUpdateCmd();
		}
	}

	private class GetUpdateInfoTask implements Runnable {
		@Override
		public void run() {
			String json = HttpUtil.get(HttpUtil.PATH, null);
			String strUTF8 = "";
			try {
				strUTF8 = URLDecoder.decode(json, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
			// d(json);
			if (null == json) {
				if (listener != null) {
					listener.getUpdateDeviceFaild();
				}
				return;
			}
			Type listType = new TypeToken<DeviceUpdateList>() {
			}.getType();
			DeviceUpdateList list = new Gson().fromJson(strUTF8, listType);
			if (list != null && !list.firmwareInfo.isEmpty()) {
				if (protocolUtils.getDeviceByDb() == null) {
					d("getDeviceByDb is null....");
					if (listener != null) {
						listener.getUpdateDeviceFaild();
					}
					return;
				}
				int id = protocolUtils.getDeviceByDb().getDeivceId();
				if (id == 0) {
					id = getDeviceId();
				}
				d("deviceId:" + id);
				DeviceUpdateInfo updateInfo = list.getMyDevice(id);
				if (updateInfo != null && listener != null) {
					d("获取到文件下载地址.....");
					listener.getUpdateDeviceInfo(updateInfo);
				}
			}
		}
	}

}
