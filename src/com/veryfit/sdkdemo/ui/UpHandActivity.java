package com.veryfit.sdkdemo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;

import com.veryfit.multi.nativedatabase.UpHandGestrue;
import com.veryfit.multi.nativeprotocol.ProtocolEvt;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.sdkdemo.R;
import com.veryfit.sdkdemo.view.BufferDialog;

public class UpHandActivity extends BaseActivity {
	private Switch switchUpHand;
	private Handler mHandler = new Handler();
	private BufferDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_uphand);
		initView();
		initData();
		addListener();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		super.initView();
		dialog = new BufferDialog(this);
		switchUpHand = (Switch) findViewById(R.id.switch_uphand);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		// 初始化抬腕识别数据，获取设置过的抬碗数据
		UpHandGestrue gestrue = ProtocolUtils.getInstance().getUpHandGestrue();
		switchUpHand.setChecked(gestrue.getOnOff());
	}

	@Override
	public void addListener() {
		// TODO Auto-generated method stub
		super.addListener();
		switchUpHand.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// 每次设置成功后设置的数据会保存在数据库中,用于界面展示。
				// 设置手势识别
				dialog.show();
				ProtocolUtils.getInstance().setUPHandGestrue(arg1, 5);// 参数1为开关，参数2为显示秒数
			}
		});
	}

	@Override
	public void onSysEvt(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		super.onSysEvt(arg0, arg1, arg2, arg3);
		if (arg1 == ProtocolEvt.SET_UP_HAND_GESTURE.toIndex() && arg2 == ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					Toast.makeText(UpHandActivity.this, "抬腕设置成功", Toast.LENGTH_LONG).show();
				}
			});
		}
	}

}
