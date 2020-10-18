package com.veryfit.sdkdemo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;

import com.veryfit.multi.config.Constants;
import com.veryfit.multi.nativeprotocol.ProtocolEvt;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.sdkdemo.R;
import com.veryfit.sdkdemo.view.BufferDialog;

public class HeartRateModeActivity extends BaseActivity {
	private Switch switchHeartRateMode;
	private Handler mHandler = new Handler();
	private BufferDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_heartrate_mode);
		initView();
		initData();
		addListener();
	}

	@Override
	public void initView() {
		super.initView();
		dialog = new BufferDialog(this);
		switchHeartRateMode = (Switch) findViewById(R.id.switch_heartrate_mode);
	}

	@Override
	public void initData() {
		super.initData();
		int mode = ProtocolUtils.getInstance().getHeartRateMode();
		//HEARTRATE_MODE_MANUAL
		//HEARTRATE_MODE_AUTOMATIC
		switchHeartRateMode.setChecked(mode==Constants.HEARTRATE_MODE_AUTOMATIC);
	}

	@Override
	public void addListener() {
		// TODO Auto-generated method stub
		super.addListener();
		switchHeartRateMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// The data set after each successful setting will be saved in the database for interface display.
				dialog.show();
				//HEARTRATE_MODE_MANUAL
				//HEARTRATE_MODE_AUTOMATIC
				ProtocolUtils.getInstance().setHeartRateMode(arg1?Constants.HEARTRATE_MODE_AUTOMATIC:Constants.HEARTRATE_MODE_MANUAL);
			}
		});
	}

	@Override
	public void onSysEvt(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		super.onSysEvt(arg0, arg1, arg2, arg3);
		if (arg1 == ProtocolEvt.SET_HEART_RATE_MODE.toIndex() && arg2 == ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					Toast.makeText(HeartRateModeActivity.this, "Heart rate mode set successfully", Toast.LENGTH_LONG).show();
				}
			});
		}
	}

}
