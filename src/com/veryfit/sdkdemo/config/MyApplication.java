package com.veryfit.sdkdemo.config;

import android.app.Application;
import android.content.Intent;

import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.sdkdemo.service.CallService;

public class MyApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();
		Intent intent = new Intent(getBaseContext(), CallService.class);
        startService(intent);
		ProtocolUtils.getInstance().init(this);
	}
}
