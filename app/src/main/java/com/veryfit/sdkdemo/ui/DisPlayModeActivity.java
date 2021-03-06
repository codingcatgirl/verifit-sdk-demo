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

public class DisPlayModeActivity extends BaseActivity {
	private Switch switchDisPlay;
	private Handler mHandler = new Handler();
	private BufferDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display_mode);
		initView();
		initData();
		addListener();
	}

	@Override
	public void initView() {
		super.initView();
		dialog = new BufferDialog(this);
		switchDisPlay = (Switch) findViewById(R.id.switch_display);
	}

	@Override
	public void initData() {
		super.initData();
		int mode = ProtocolUtils.getInstance().getDisplayMode();
		switchDisPlay.setChecked(mode==Constants.HORIZONTAL_SCREEN_MODE?true:false);
	}

	@Override
	public void addListener() {
		super.addListener();
		switchDisPlay.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// The data set after each successful setting will be saved in the database for interface display.
				dialog.show();
				ProtocolUtils.getInstance().setDisplayMode(arg1?Constants.HORIZONTAL_SCREEN_MODE:Constants.VERTICAL_SCREEN_MODE);
			}
		});
	}

	@Override
	public void onSysEvt(int arg0, int arg1, int arg2, int arg3) {
		super.onSysEvt(arg0, arg1, arg2, arg3);
		if (arg1 == ProtocolEvt.SET_CMD_DISPLAY_MODE.toIndex() && arg2 == ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					Toast.makeText(DisPlayModeActivity.this, "Display mode set successfully", Toast.LENGTH_LONG).show();
				}
			});
		}
	}

}
