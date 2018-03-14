package com.veryfit.sdkdemo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.veryfit.multi.nativedatabase.HeartRateInterval;
import com.veryfit.multi.nativeprotocol.ProtocolEvt;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.sdkdemo.R;
import com.veryfit.sdkdemo.view.BufferDialog;

public class HeartRateIntervalActivity extends BaseActivity {

	private EditText edBurn, edAerobic, edLimit;
	private Button btnCommit;
	private Handler mHandler = new Handler();
	private BufferDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_heartrateinterval);
		initView();
		initData();
		addListener();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		super.initView();
		dialog = new BufferDialog(this);
		edBurn = (EditText) findViewById(R.id.ed_burn_fat_threshold);// 脂肪燃烧
		edAerobic = (EditText) findViewById(R.id.ed_aerobic_threshold);// 有氧锻炼
		edLimit = (EditText) findViewById(R.id.ed_limit_threshold);// 极限锻炼
		btnCommit = (Button) findViewById(R.id.btn_heartrateinterval_commit);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		HeartRateInterval interval = ProtocolUtils.getInstance().getHeartRateInterval();
		if (interval != null) {
			edAerobic.setText(interval.getAerobicThreshold() + "");
			edBurn.setText(interval.getBurnFatThreshold() + "");
			edLimit.setText(interval.getLimintThreshold() + "");
		}
	}

	@Override
	public void addListener() {
		// TODO Auto-generated method stub
		super.addListener();
		btnCommit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				int burn = Integer.parseInt(edBurn.getText().toString());// 脂肪燃烧
				int aerobic = Integer.parseInt(edAerobic.getText().toString());// 有氧锻炼
				int limit = Integer.parseInt(edLimit.getText().toString());// 极限锻炼
				HeartRateInterval rateInterval = new HeartRateInterval(0, burn, aerobic, limit, 0);// 只需传三个阈值即可
																									// ,脂肪燃烧＜有氧锻炼＜极限锻炼
																									// ,只支持带心率的设备
				dialog.show();
				ProtocolUtils.getInstance().setHeartRateInterval(rateInterval);
			}
		});
	}

	@Override
	public void onSysEvt(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		super.onSysEvt(arg0, arg1, arg2, arg3);
		if (arg1 == ProtocolEvt.SET_HEART_RATE_INTERVAL.toIndex() && arg2 == ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					Toast.makeText(HeartRateIntervalActivity.this, "心率区间设置成功", Toast.LENGTH_LONG).show();
				}
			});
		}
	}

}
