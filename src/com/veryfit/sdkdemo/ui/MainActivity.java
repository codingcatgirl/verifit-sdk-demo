package com.veryfit.sdkdemo.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.veryfit.multi.share.BleSharedPreferences;
import com.veryfit.sdkdemo.R;

public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(BleSharedPreferences.getInstance().getIsBind()){
			Intent intent =new Intent(MainActivity.this, HomeActivity.class);
			startActivity(intent);
			finish();
		}else {
			Intent intent =new Intent(MainActivity.this, ScanDeviceActivity.class);
			startActivity(intent);
			finish();
		}
	}

	
}
