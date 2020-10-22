package com.veryfit.sdkdemo.config;

import android.app.Application;
import android.content.Intent;
import android.os.Build;

import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.sdkdemo.service.CallService;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Intent intent = new Intent(getBaseContext(), CallService.class);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			startForegroundService(intent);
		} else {
			startService(intent);
		}
		ProtocolUtils.getInstance().init(this);
	}
}
