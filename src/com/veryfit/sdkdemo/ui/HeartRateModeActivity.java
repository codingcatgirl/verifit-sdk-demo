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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_heartrate_mode);
		initView();
		initData();
		addListener();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		super.initView();
		dialog = new BufferDialog(this);
		switchHeartRateMode = (Switch) findViewById(R.id.switch_heartrate_mode);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		// 初始化心率模式
		int mode = ProtocolUtils.getInstance().getHeartRateMode();
		//心率检测模式类型
		//HEARTRATE_MODE_MANUAL 手动模式
		//HEARTRATE_MODE_AUTOMATIC 自动模式
		switchHeartRateMode.setChecked(mode==Constants.HEARTRATE_MODE_AUTOMATIC);
	}

	@Override
	public void addListener() {
		// TODO Auto-generated method stub
		super.addListener();
		switchHeartRateMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// 每次设置成功后设置的数据会保存在数据库中,用于界面展示。
				dialog.show();
				//心率检测模式类型
				//HEARTRATE_MODE_MANUAL 手动模式
				//HEARTRATE_MODE_AUTOMATIC 自动模式
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
					Toast.makeText(HeartRateModeActivity.this, "心率模式设置成功", Toast.LENGTH_LONG).show();
				}
			});
		}
	}

}
