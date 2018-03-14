package com.veryfit.sdkdemo.model;

import android.util.Log;

import com.veryfit.multi.ble.ProtocalCallBack;
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
import com.veryfit.multi.nativedatabase.RealTimeHealthData;
import com.veryfit.multi.nativedatabase.healthSleep;
import com.veryfit.multi.nativedatabase.healthSleepAndItems;
import com.veryfit.multi.util.DebugLog;

/**
 * Created by asus on 2016/7/6.
 */
public class BaseCallBack implements ProtocalCallBack{
    @Override
    public void onWriteDataToBle(byte[] bytes) {
        d("onWriteDataToBle");
    }

    @Override
    public void onDeviceInfo(BasicInfos basicInfos) {
        d("onDeviceInfo");
    }

    @Override
    public void healthData(byte[] bytes) {
        d("healthData");
    }

    @Override
    public void onSensorData(byte[] bytes) {
        d("onSensorData");
    }

    @Override
    public void onFuncTable(FunctionInfos functionInfos) {
        d("onFuncTable");
    }

    @Override
    public void onSleepData(healthSleep healthSleep, healthSleepAndItems healthSleepAndItems) {
        d("onSleepData");
    }

    @Override
    public void onHealthSport(HealthSport healthSport, HealthSportAndItems healthSportAndItems) {
        d("onHealthSport");
    }

    @Override
    public void onHealthHeartRate(HealthHeartRate healthHeartRate, HealthHeartRateAndItems healthHeartRateAndItems) {
        d("onHealthHeartRate");
    }

    @Override
    public void onLiveData(RealTimeHealthData realTimeHealthData) {
        d("onLiveData");
    }

    @Override
    public void onGsensorParam(GsensorParam gsensorParam) {
        d("onGsensorParam");
    }

    @Override
    public void onHrSensorParam(HrSensorParam hrSensorParam) {
        d("onHrSensorParam");
    }

    @Override
    public void onMacAddr(byte[] bytes) {
        d("onMacAddr");
    }

    @Override
    public void onSysEvt(int evt_base, int evt_type, int error, int value) {
        d("onSysEvt");
    }

    @Override
    public void onLogData(byte[] bytes, boolean b) {

    }

    public void d(String msg) {
        Log.d(this.getClass().getSimpleName(), msg);
    }

	@Override
	public void onSwitchDataAppEnd(SwitchDataAppEndReply arg0, int arg1) {
		// TODO Auto-generated method stub
		DebugLog.d("app交换数据结束的回调");
	}

	@Override
	public void onSwitchDataAppIng(SwitchDataAppIngReply arg0, int arg1) {
		// TODO Auto-generated method stub
		DebugLog.d("app交换数据的回调");
	}

	@Override
	public void onSwitchDataAppStart(SwitchDataAppStartReply arg0, int arg1) {
		// TODO Auto-generated method stub
		DebugLog.d("app交换数据开始的回调");
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
