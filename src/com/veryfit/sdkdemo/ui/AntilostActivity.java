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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_antilost);
		initView();
		initData();
		addListener();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		super.initView();
		dialog = new BufferDialog(this);
		switchAntiLost = (Switch) findViewById(R.id.switch_antilost);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		// 初始化防丢数据
		AntilostInfos infos = ProtocolUtils.getInstance().getAntilostInfos();
		// 防丢类型
		//LOSE_MODE_NO_ANTI 不防丢  
		//LOSE_MODE_NEAR_ANTI 近距离防丢
		//LOSE_MODE_MID_ANTI 中距离防丢
		//LOSE_MODE_FAR_ANTI 远距离防丢
		switchAntiLost.setChecked(infos.getMode()!=Constants.LOSE_MODE_NO_ANTI);//如果防丢类型为LOSE_MODE_NO_ANTI 说明不防丢  防丢开关也就是关闭，其他类型的话就是防丢开启
	}

	@Override
	public void addListener() {
		// TODO Auto-generated method stub
		super.addListener();
		switchAntiLost.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// 每次设置成功后设置的数据会保存在数据库中,用于界面展示。
				dialog.show();
				// 设置防丢， 传参防丢类型  LOSE_MODE_NO_ANTI 不防丢   ， LOSE_MODE_NEAR_ANTI 近距离防丢  ，LOSE_MODE_MID_ANTI 中距离防丢  ，LOSE_MODE_FAR_ANTI 远距离防丢
				ProtocolUtils.getInstance().setAntilost(arg1?Constants.LOSE_MODE_MID_ANTI:Constants.LOSE_MODE_NO_ANTI);
			}
		});
	}

	@Override
	public void onSysEvt(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		super.onSysEvt(arg0, arg1, arg2, arg3);
		if (arg1 == ProtocolEvt.SET_CMD_LOST_FIND.toIndex() && arg2 == ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					Toast.makeText(AntilostActivity.this, "防丢设置成功", Toast.LENGTH_LONG).show();
				}
			});
		}
	}

}
