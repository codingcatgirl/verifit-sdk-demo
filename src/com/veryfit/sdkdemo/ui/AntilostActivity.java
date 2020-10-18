package com.veryfit.sdkdemo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;

import com.veryfit.multi.config.Constants;
import com.veryfit.multi.nativedatabase.AntilostInfos;
import com.veryfit.multi.nativeprotocol.ProtocolEvt;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.sdkdemo.R;
import com.veryfit.sdkdemo.view.BufferDialog;

public class AntilostActivity extends BaseActivity {
	private Switch switchAntiLost;
	private Handler mHandler = new Handler();
	private BufferDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_antilost);
		initView();
		initData();
		addListener();
	}

	@Override
	public void initView() {
		super.initView();
		dialog = new BufferDialog(this);
		switchAntiLost = (Switch) findViewById(R.id.switch_antilost);
	}

	@Override
	public void initData() {
		super.initData();
		AntilostInfos infos = ProtocolUtils.getInstance().getAntilostInfos();
		//LOSE_MODE_NO_ANTI
		//LOSE_MODE_NEAR_ANTI
		//LOSE_MODE_MID_ANTI
		//LOSE_MODE_FAR_ANTI
		// If the anti-lost type is LOSE_MODE_NO_ANTI, it means it is not anti-lost. The anti-lost switch is turned off.
		// For other types, the anti-lost switch is turned on.
		switchAntiLost.setChecked(infos.getMode()!=Constants.LOSE_MODE_NO_ANTI);

	}

	@Override
	public void addListener() {
		super.addListener();
		switchAntiLost.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// The data set after each successful setting will be saved in the database for interface display.
				dialog.show();
				// Set anti-lost, pass parameter anti-lost type LOSE_MODE_NO_ANTI not anti-lost,
				// LOSE_MODE_NEAR_ANTI short-range anti-lost, LOSE_MODE_MID_ANTI middle-range anti-lost,
				// LOSE_MODE_FAR_ANTI long-distance anti-lost
				ProtocolUtils.getInstance().setAntilost(arg1?Constants.LOSE_MODE_MID_ANTI:Constants.LOSE_MODE_NO_ANTI);
			}
		});
	}

	@Override
	public void onSysEvt(int arg0, int arg1, int arg2, int arg3) {
		super.onSysEvt(arg0, arg1, arg2, arg3);
		if (arg1 == ProtocolEvt.SET_CMD_LOST_FIND.toIndex() && arg2 == ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					Toast.makeText(AntilostActivity.this, "Anti-lost settings successfully saved", Toast.LENGTH_LONG).show();
				}
			});
		}
	}

}
