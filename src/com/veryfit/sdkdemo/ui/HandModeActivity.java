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

public class HandModeActivity extends BaseActivity{
	private Switch switchHandMode;
	private Handler mHandler=new Handler();
	private BufferDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hand_mode);
		initView();
		initData();
		addListener();
	}
	
	@Override
	public void initView() {
		dialog=new BufferDialog(this);
		switchHandMode=(Switch)findViewById(R.id.switch_handmode);
	}
	
	@Override
	public void initData() {
		//HAND_MODE_LEFT
		//HAND_MODE_RIGHT
		//Get the wearing mode, the data set will be saved in the database every time the setting is successful
		int mode=ProtocolUtils.getInstance().getHandMode();
		switchHandMode.setChecked(mode==Constants.HAND_MODE_RIGHT);
	}
	
	@Override
	public void addListener() {
		switchHandMode.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				//The data set will be saved in the database every time the setting is successful
				dialog.show();
				ProtocolUtils.getInstance().setHandMode(arg1?Constants.HAND_MODE_RIGHT:Constants.HAND_MODE_LEFT);
			}
		});
	}
	
	@Override
	public void onSysEvt(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		super.onSysEvt(arg0, arg1, arg2, arg3);
		if(arg1==ProtocolEvt.SET_CMD_HAND.toIndex()&&arg2==ProtocolUtils.SUCCESS){
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					Toast.makeText(HandModeActivity.this, "Set successfully", Toast.LENGTH_LONG).show();
				}
			}, 200);
		}
	}

}
