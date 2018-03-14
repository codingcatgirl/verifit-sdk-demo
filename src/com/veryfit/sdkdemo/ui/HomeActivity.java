package com.veryfit.sdkdemo.ui;

import java.util.Calendar;
import java.util.Date;

import android.bluetooth.BluetoothGatt;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.veryfit.multi.nativeprotocol.Protocol;
import com.veryfit.multi.nativeprotocol.ProtocolEvt;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.multi.util.DebugLog;
import com.veryfit.sdkdemo.R;
import com.veryfit.sdkdemo.model.DialogUtil;
import com.veryfit.sdkdemo.model.UpdateModel;
import com.veryfit.sdkdemo.view.BufferDialog;

public class HomeActivity extends BaseActivity implements OnClickListener {
	private Button btnSyncConfig, btnSyncHealth, btnSport, btnSleep, btnHeart, btnDeviceInfo, btnCallAndMsg,
	btnWearMode, btnUpHand, btnLost, btnFindPhone, btnLongSit, btnUnDisturb, btnDisPlay,
			btnHeartInterval, btnHeartMode, btnPhoto, btnMusic, btnAlarm, btnGoal, btnUserInfo,
			btnUnit, btnRestart, btnLiveData, btnSos, btnUnBind, btnTest, btnFuncInfos,
			btnSendLog,btnUnConnect,btnConnect,btnCommission;
	private BufferDialog dialog;
	
	private Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		ProtocolUtils.getInstance().setCanConnect(true);
		initView();
		addListener();
	}

	public void initView() {
		dialog = new BufferDialog(HomeActivity.this);
		btnAlarm = (Button) findViewById(R.id.btn_alarm);
		btnCallAndMsg = (Button) findViewById(R.id.btn_call_msg);
		btnDeviceInfo = (Button) findViewById(R.id.btn_device_info);
		btnDisPlay = (Button) findViewById(R.id.btn_display_mode);
		btnFindPhone = (Button) findViewById(R.id.btn_find_phone);
		btnGoal = (Button) findViewById(R.id.btn_goal);
		btnHeart = (Button) findViewById(R.id.btn_heart_data);
		btnHeartInterval = (Button) findViewById(R.id.btn_heart_Interval);
		btnHeartMode = (Button) findViewById(R.id.btn_heart_mode);
		btnLongSit = (Button) findViewById(R.id.btn_long_sit);
		btnLost = (Button) findViewById(R.id.btn_lost);
		btnMusic = (Button) findViewById(R.id.btn_music);
		btnPhoto = (Button) findViewById(R.id.btn_photo);
		btnRestart = (Button) findViewById(R.id.btn_restart);
		btnSleep = (Button) findViewById(R.id.btn_sleep_data);
		btnSport = (Button) findViewById(R.id.btn_sport_data);
		btnSyncConfig = (Button) findViewById(R.id.btn_sync_config);
		btnSyncHealth = (Button) findViewById(R.id.btn_sync_health);
		btnUnDisturb = (Button) findViewById(R.id.btn_undisturb_mode);
		btnUnit = (Button) findViewById(R.id.btn_unit);
		btnSendLog = (Button) findViewById(R.id.btn_sendlog);
		btnUpHand = (Button) findViewById(R.id.btn_up_hand);
		btnUserInfo = (Button) findViewById(R.id.btn_userinfo);
		btnWearMode = (Button) findViewById(R.id.btn_wear_mode);
		btnLiveData = (Button) findViewById(R.id.btn_live_data);
		btnSos = (Button) findViewById(R.id.btn_sos);
		btnUnBind = (Button) findViewById(R.id.btn_unbind);
		btnTest = (Button) findViewById(R.id.btn_test);
		btnFuncInfos = (Button) findViewById(R.id.btn_function_infos);
		btnUnConnect = (Button) findViewById(R.id.btn_unconnect);
		btnConnect = (Button) findViewById(R.id.btn_connect);
		btnCommission = (Button) findViewById(R.id.btn_commission);
	}

	public void addListener() {
		btnAlarm.setOnClickListener(this);
		btnCallAndMsg.setOnClickListener(this);
		btnDeviceInfo.setOnClickListener(this);
		btnDisPlay.setOnClickListener(this);
		btnFindPhone.setOnClickListener(this);
		btnGoal.setOnClickListener(this);
		btnHeart.setOnClickListener(this);
		btnHeartInterval.setOnClickListener(this);
		btnHeartMode.setOnClickListener(this);
		btnLongSit.setOnClickListener(this);
		btnLost.setOnClickListener(this);
		btnMusic.setOnClickListener(this);
		btnPhoto.setOnClickListener(this);
		btnSendLog.setOnClickListener(this);
		btnRestart.setOnClickListener(this);
		btnSleep.setOnClickListener(this);
		btnSport.setOnClickListener(this);
		btnTest.setOnClickListener(this);
		btnSyncConfig.setOnClickListener(this);
		btnSyncHealth.setOnClickListener(this);
		btnUnDisturb.setOnClickListener(this);
		btnUnit.setOnClickListener(this);
		btnFuncInfos.setOnClickListener(this);
		btnUnBind.setOnClickListener(this);
		btnUpHand.setOnClickListener(this);
		btnUserInfo.setOnClickListener(this);
		btnWearMode.setOnClickListener(this);
		btnLiveData.setOnClickListener(this);
		btnSos.setOnClickListener(this);
		btnUnConnect.setOnClickListener(this);
		btnCommission.setOnClickListener(this);
		btnConnect.setOnClickListener(this);
		findViewById(R.id.btn_update).setOnClickListener(this);
	}

	public static String bytesToHexString(byte src) {
		StringBuilder stringBuilder = new StringBuilder("");
		int v = src & 0xFF;
		String hv = Integer.toHexString(v);
		if (hv.length() < 2) {
			stringBuilder.append(0);
		}
		stringBuilder.append(hv);

		return stringBuilder.toString();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_sync_health:// 同步健康数据
			Intent syncHealthIntent = new Intent(HomeActivity.this, SyncHealthActivity.class);
			startActivity(syncHealthIntent);
			break;
		case R.id.btn_alarm:// 闹钟提醒
			Intent alarmIntent = new Intent(HomeActivity.this, AlarmActivity.class);
			startActivity(alarmIntent);
			break;
		case R.id.btn_call_msg:// 电话提醒与短信提醒
			Intent msgCallIntent = new Intent(HomeActivity.this, CallAndMsgActivity.class);
			startActivity(msgCallIntent);
			break;
		case R.id.btn_device_info:// 设备信息
			Intent deviceIntent = new Intent(HomeActivity.this, DeviceInfoActivity.class);
			startActivity(deviceIntent);
			break;
		case R.id.btn_function_infos:// 功能列表
			Intent funcIntent = new Intent(HomeActivity.this, FunctionInfosActivity.class);
			startActivity(funcIntent);
			break;
		case R.id.btn_display_mode:// 显示模式
			Intent disPlayIntent = new Intent(HomeActivity.this, DisPlayModeActivity.class);
			startActivity(disPlayIntent);
			break;
		case R.id.btn_find_phone:// 寻找手机
			Intent findPhoneIntent = new Intent(HomeActivity.this, FindPhoneActivity.class);
			startActivity(findPhoneIntent);
			break;
		case R.id.btn_goal:// 目标设置
			Intent goalIntent = new Intent(HomeActivity.this, SetGoalActivity.class);
			startActivity(goalIntent);
			break;
		case R.id.btn_heart_data:// 心率数据
			Intent heartRateIntent = new Intent(HomeActivity.this, HeartRateActivity.class);
			startActivity(heartRateIntent);
			break;
		case R.id.btn_sos:// 一键求助
			Intent intervalIntent = new Intent(HomeActivity.this, SosActivity.class);
			startActivity(intervalIntent);
			break;
		case R.id.btn_heart_Interval:// 心率区间
			Intent sosIntent = new Intent(HomeActivity.this, HeartRateIntervalActivity.class);
			startActivity(sosIntent);
			break;
		case R.id.btn_heart_mode:// 心率模式
			Intent hearRateModeIntent = new Intent(HomeActivity.this, HeartRateModeActivity.class);
			startActivity(hearRateModeIntent);
			break;
		case R.id.btn_live_data: // 实时数据
			Intent livedataIntent = new Intent(HomeActivity.this, LiveDataActivity.class);
			startActivity(livedataIntent);
			break;
		case R.id.btn_long_sit:// 久坐提醒
			Intent longSitIntent = new Intent(HomeActivity.this, LongSitActivity.class);
			startActivity(longSitIntent);
			break;
		case R.id.btn_lost:// 防丢提醒
			Intent antiLostIntent = new Intent(HomeActivity.this, AntilostActivity.class);
			startActivity(antiLostIntent);
			break;
		case R.id.btn_music:// 音乐提醒
			Intent musicIntent = new Intent(HomeActivity.this, MusicActivity.class);
			startActivity(musicIntent);
			break;
		case R.id.btn_photo:// 拍照控制
			Intent cameraIntent = new Intent(HomeActivity.this, CameraActivity.class);
			startActivity(cameraIntent);
			break;
		case R.id.btn_test:// 测试界面
			Intent intent = new Intent(HomeActivity.this, DemoTestActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_sendlog:// 发送日志
			Intent logIntent = new Intent(HomeActivity.this, SendLogActivity.class);
			startActivity(logIntent);
			break;
		case R.id.btn_restart:// 重启设备
			dialog.show();
			ProtocolUtils.getInstance().reStartDevice();
			break;
		case R.id.btn_unbind:// 解绑
			dialog.show();
			// 解绑当设备未连接的情况下只是删除当天的数据，并删除一些绑定状态，重置APP为未绑定设备的状态
			if (ProtocolUtils.getInstance().isAvailable() != ProtocolUtils.SUCCESS) {// 判断是否连接了设备
				ProtocolUtils.getInstance().setBindMode(Protocol.SYS_MODE_SET_NOBIND);
				Calendar mCalendar1 = Calendar.getInstance();
				int year = mCalendar1.get(Calendar.YEAR);
				int month = mCalendar1.get(Calendar.MONTH);
				int day = mCalendar1.get(Calendar.DAY_OF_MONTH);
				ProtocolUtils.getInstance().enforceUnBind(new Date(year, month, day));// (月份从0开始例如8月传7)
				Intent intent1 = new Intent(HomeActivity.this, ScanDeviceActivity.class);
				startActivity(intent1);

			} else {
				// 如果设备处于连接状态，则用如下方法解绑
				ProtocolUtils.getInstance().setBindMode(Protocol.SYS_MODE_SET_NOBIND);
				ProtocolUtils.getInstance().setUnBind();
			}
			break;
		case R.id.btn_sleep_data:// 睡眠数据
			Intent sleepIntent = new Intent(HomeActivity.this, SleepActivity.class);
			startActivity(sleepIntent);
			break;
		case R.id.btn_sport_data:// 运动数据
			Intent sportIntent = new Intent(HomeActivity.this, SprotActivity.class);
			startActivity(sportIntent);
			break;
		case R.id.btn_sync_config:// 同步配置信息
			dialog.setTitle("正在同步配置信息");
			dialog.show();
			ProtocolUtils.getInstance().StartSyncConfigInfo();
			break;
		case R.id.btn_undisturb_mode:// 勿扰模式
			Intent disIntent = new Intent(HomeActivity.this, DoNotDisturbActivity.class);
			startActivity(disIntent);
			break;
		case R.id.btn_unit:// 单位设置
			Intent unitIntent = new Intent(HomeActivity.this, UnitActivity.class);
			startActivity(unitIntent);
			break;
		case R.id.btn_up_hand:// 抬碗识别
			Intent upHandIntent = new Intent(HomeActivity.this, UpHandActivity.class);
			startActivity(upHandIntent);
			break;
		case R.id.btn_userinfo:// 用户信息
			Intent userInfoIntent = new Intent(HomeActivity.this, UserInfosActivity.class);
			startActivity(userInfoIntent);
			break;
		case R.id.btn_wear_mode:// 佩戴模式
			Intent handmodeIntent = new Intent(HomeActivity.this, HandModeActivity.class);
			startActivity(handmodeIntent);
			break;
		case R.id.btn_unconnect:// 设置断线
			ProtocolUtils.getInstance().setNewUnConnect();
			break;
		case R.id.btn_connect:// 设置重连
			ProtocolUtils.getInstance().reConnect();
			break;
		case R.id.btn_commission:// 定制
			Intent commissionIntent=new Intent(HomeActivity.this, CommissionActivity.class);
			startActivity(commissionIntent);
			break;
		case R.id.btn_update:
			final View view = findViewById(R.id.btn_update);
			final UpdateModel updateModel = new UpdateModel(this);
			updateModel.setIUpdateListener(new UpdateModel.IUpdateListener() {
				@Override
				public void updateFaild() {
					view.setEnabled(true);
					DialogUtil.closeAlertDialog();
					DialogUtil.showToast(HomeActivity.this, "升级失败");
				}

				@Override
				public void updateProgressBar(int progress) {
					DialogUtil.updateWaitDialog("正在升级..." + Math.abs(progress) + "%");
				}

				@Override
				public void updateSuccess() {
					view.setEnabled(true);
					DialogUtil.closeAlertDialog();
					DialogUtil.showToast(HomeActivity.this, "升级成功");
				}

				@Override
				public void synchroData(int progress) {
				}
			});
			DialogUtil.showWaitDialog(this, "正在升级...");
			updateModel.downAndUpdate();
			view.setEnabled(false);
			break;
		default:
			break;
		}
	}

	@Override
	public void onSysEvt(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		DebugLog.d("HomeProtocolEvt=" + arg1);

		if (arg1 == ProtocolEvt.SYNC_EVT_CONFIG_SYNC_COMPLETE.toIndex() && arg2 == ProtocolEvt.SUCCESS) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					Toast.makeText(HomeActivity.this, "同步配置信息成功", Toast.LENGTH_LONG).show();
				}
			}, 200);
		} else if (arg1 == ProtocolEvt.REBOOT_CMD.toIndex() && arg2 == ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					Toast.makeText(HomeActivity.this, "重启成功", Toast.LENGTH_LONG).show();
				}
			});
		} else if (arg1 == ProtocolEvt.BIND_CMD_REMOVE.toIndex() && arg2 == ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					Toast.makeText(HomeActivity.this, "解绑成功", Toast.LENGTH_LONG).show();
					Intent intent = new Intent(HomeActivity.this, ScanDeviceActivity.class);
					startActivity(intent);
					finish();
				}
			});
		} else if (arg1 == ProtocolEvt.BLE_TO_APP_FIND_PHONE_START.toIndex()) {// 收到手环发送过来的寻找手机命令，客户端收到这条命令做相应处理，比如让手机响铃等操作
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					DebugLog.d("收到寻找手机的命令");
					Toast.makeText(HomeActivity.this, "收到寻找手机的命令", Toast.LENGTH_LONG).show();
				}
			});
		}
	}

	@Override
	public void onBLEConnectTimeOut() {
		// TODO Auto-generated method stub
		setTitle("连接超时");

	}
	@Override
	public void onBLEConnected(BluetoothGatt arg0) {
		// TODO Auto-generated method stub
		setTitle("已连接");
	}

	@Override
	public void onBLEConnecting(String arg0) {
		// TODO Auto-generated method stub
		setTitle("连接中");
	}

	@Override
	public void onBLEDisConnected(String arg0) {
		// TODO Auto-generated method stub
		setTitle("连接已断开");
	}
	
}
