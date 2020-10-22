package com.veryfit.sdkdemo.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;

import com.veryfit.multi.ble.AppBleListener;
import com.veryfit.multi.ble.ProtocalCallBack;
import com.veryfit.multi.config.Constants;
import com.veryfit.multi.entity.SportData;
import com.veryfit.multi.entity.SwitchDataAppBleEnd;
import com.veryfit.multi.entity.SwitchDataAppBlePause;
import com.veryfit.multi.entity.SwitchDataAppBleRestore;
import com.veryfit.multi.entity.SwitchDataAppEndReply;
import com.veryfit.multi.entity.SwitchDataAppIngReply;
import com.veryfit.multi.entity.SwitchDataAppPauseReply;
import com.veryfit.multi.entity.SwitchDataAppRestoreReply;
import com.veryfit.multi.entity.SwitchDataAppStartReply;
import com.veryfit.multi.entity.SwitchDataBleEnd;
import com.veryfit.multi.entity.SwitchDataBleIng;
import com.veryfit.multi.entity.SwitchDataBlePause;
import com.veryfit.multi.entity.SwitchDataBleRestore;
import com.veryfit.multi.entity.SwitchDataBleStart;
import com.veryfit.multi.nativedatabase.ActivityData;
import com.veryfit.multi.nativedatabase.BasicInfos;
import com.veryfit.multi.nativedatabase.FunctionInfos;
import com.veryfit.multi.nativedatabase.GsensorParam;
import com.veryfit.multi.nativedatabase.HealthHeartRate;
import com.veryfit.multi.nativedatabase.HealthHeartRateAndItems;
import com.veryfit.multi.nativedatabase.HealthSport;
import com.veryfit.multi.nativedatabase.HealthSportAndItems;
import com.veryfit.multi.nativedatabase.HrSensorParam;
import com.veryfit.multi.nativedatabase.NoticeOnOff;
import com.veryfit.multi.nativedatabase.RealTimeHealthData;
import com.veryfit.multi.nativedatabase.Units;
import com.veryfit.multi.nativedatabase.healthSleep;
import com.veryfit.multi.nativedatabase.healthSleepAndItems;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.multi.util.DebugLog;

public class CallService extends Service implements AppBleListener, ProtocalCallBack {

	@Override
	public void onCreate() {
		super.onCreate();

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
			String NOTIFICATION_CHANNEL_ID = "com.verifit.sdkdemo";
			String channelName = "Background Service";
			NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
			chan.setLightColor(Color.BLUE);
			chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			assert manager != null;
			manager.createNotificationChannel(chan);

			Notification.Builder notificationBuilder = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID);
			Notification notification = notificationBuilder.setOngoing(true)
					.setContentTitle("App is running in background")
					//.setPriority(NotificationManager.IMPORTANCE_MIN)
					.setCategory(Notification.CATEGORY_SERVICE)
					.build();
			startForeground(2, notification);
		} else {
			startForeground(1, new Notification());
		}

		ProtocolUtils.getInstance().setBleListener(this);
		ProtocolUtils.getInstance().setProtocalCallBack(this);

		IntentFilter filter = new IntentFilter();
		filter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
		filter.addAction(Intent.ACTION_TIME_CHANGED);
		registerReceiver(receiver, filter);
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)){
				TelephonyManager tm = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
				switch (tm.getCallState()) {
				case TelephonyManager.CALL_STATE_RINGING:
					// Initialize the smart reminder information switch
					NoticeOnOff onOff = ProtocolUtils.getInstance().getNotice();
					String phoneNumber = intent.getStringExtra("incoming_number");
					DebugLog.d("onOff=" + onOff.getCallonOff());
					if (onOff.getCallonOff()) {
						ProtocolUtils.getInstance().setCallEvt("小田", phoneNumber);
					}
					DebugLog.d("phoneNumber=" + phoneNumber);
					break;
				}
			}else if (intent.getAction().equals(Intent.ACTION_TIME_CHANGED)) {
				DebugLog.d("Time_Changed");//Changed the time system
				Units units=ProtocolUtils.getInstance().getUnits();
				ProtocolUtils.getInstance().setUint(units.getMode(), units.getStride(), units.getLanguage(),is24HourFormat()?Constants.TIME_MODE_24:Constants.TIME_MODE_12 );
			}
		}
	};
	public boolean is24HourFormat() {
		return DateFormat.is24HourFormat(this);
	}

	public void onDestroy() {
		unregisterReceiver(receiver);
		ProtocolUtils.getInstance().removeListener(this);
		ProtocolUtils.getInstance().removeProtocalCallBack(this);
	};

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void healthData(byte[] arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDeviceInfo(BasicInfos arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFuncTable(FunctionInfos arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onGsensorParam(GsensorParam arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHealthHeartRate(HealthHeartRate arg0, HealthHeartRateAndItems arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHealthSport(HealthSport arg0, HealthSportAndItems arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHrSensorParam(HrSensorParam arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLiveData(RealTimeHealthData arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLogData(byte[] arg0, boolean arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMacAddr(byte[] arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorData(byte[] arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSleepData(healthSleep arg0, healthSleepAndItems arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSysEvt(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onWriteDataToBle(byte[] arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBLEConnectTimeOut() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBLEConnected(BluetoothGatt arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBLEConnecting(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBLEDisConnected(String arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBlueToothError(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDataSendTimeOut(byte[] arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onServiceDiscover(BluetoothGatt arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSwitchDataAppEnd(SwitchDataAppEndReply arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSwitchDataAppIng(SwitchDataAppIngReply arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSwitchDataAppStart(SwitchDataAppStartReply arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSwitchDataBleEnd(SwitchDataBleEnd arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSwitchDataBleIng(SwitchDataBleIng arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSwitchDataBleStart(SwitchDataBleStart arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onActivityData(SportData arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSwitchDataAppPause(SwitchDataAppPauseReply arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSwitchDataAppRestore(SwitchDataAppRestoreReply arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSwitchDataBlePause(SwitchDataBlePause arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSwitchDataBleRestore(SwitchDataBleRestore arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSwitchDataAppBleEnd(SwitchDataAppBleEnd arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSwitchDataAppBlePause(SwitchDataAppBlePause arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSwitchDataAppBleRestore(SwitchDataAppBleRestore arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}
