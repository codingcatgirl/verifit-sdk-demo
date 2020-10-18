package com.veryfit.sdkdemo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;

import com.veryfit.multi.nativedatabase.FindPhoneOnOff;
import com.veryfit.multi.nativeprotocol.ProtocolEvt;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.multi.util.DebugLog;
import com.veryfit.sdkdemo.R;
import com.veryfit.sdkdemo.view.BufferDialog;

public class FindPhoneActivity extends BaseActivity {
	private Switch switchFindPhone;
	private Handler mHandler = new Handler();
	private BufferDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findphone);
		initView();
		initData();
		addListener();
	}

	@Override
	public void initView() {
		super.initView();
		dialog = new BufferDialog(this);
		switchFindPhone = (Switch) findViewById(R.id.switch_findphone);
	}

	@Override
	public void initData() {
		super.initData();
		FindPhoneOnOff find = ProtocolUtils.getInstance().getFindPhone();
		switchFindPhone.setChecked(find.getOnOff());
	}

	@Override
	public void addListener() {
		super.addListener();
		switchFindPhone.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// The data set after each successful setting will be saved in the database for interface display.
				dialog.show();
				ProtocolUtils.getInstance().setFindPhone(arg1);
			}
		});
	}

	@Override
	public void onSysEvt(int arg0, int arg1, int arg2, int arg3) {
		super.onSysEvt(arg0, arg1, arg2, arg3);
		DebugLog.d("Received a find phone request");
		if (arg1 == ProtocolEvt.SET_CMD_FIND_PHONE.toIndex() && arg2 == ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					Toast.makeText(FindPhoneActivity.this, "Find phone setting set successfully", Toast.LENGTH_LONG).show();
				}
			});
		}else if (arg1 == ProtocolEvt.BLE_TO_APP_FIND_PHONE_START.toIndex()) {
			// After receiving the mobile phone search command sent by the bracelet,
			// the client receives this command and does the corresponding processing, such as making the mobile phone ring.
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					Toast.makeText(FindPhoneActivity.this, "Received a find phone request", Toast.LENGTH_LONG).show();
				}
			});
		}
	}

}
