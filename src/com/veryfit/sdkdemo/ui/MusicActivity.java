package com.veryfit.sdkdemo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.veryfit.multi.nativeprotocol.ProtocolEvt;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.sdkdemo.R;
import com.veryfit.sdkdemo.view.BufferDialog;

public class MusicActivity extends BaseActivity implements OnCheckedChangeListener{
	private Switch switchOnOff,switchStartOrStop;
	private Handler mHandler = new Handler();
	private BufferDialog dialog;
	private TextView tvReceive;
	private StringBuffer receiveSb=new StringBuffer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music);
		initView();
		initData();
		addListener();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		super.initView();
		dialog=new BufferDialog(this);
		switchOnOff=(Switch)findViewById(R.id.switch_music_onoff);
		switchStartOrStop=(Switch)findViewById(R.id.switch_music_startorend);
		tvReceive=(TextView)findViewById(R.id.tv_receive);
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		switchOnOff.setChecked(ProtocolUtils.getInstance().getMusicOnoff());
		
	}
	
	@Override
	public void addListener() {
		// TODO Auto-generated method stub
		super.addListener();
		switchOnOff.setOnCheckedChangeListener(this);
		switchStartOrStop.setOnCheckedChangeListener(this);
	}
	
	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.switch_music_onoff:
			dialog.show();
			ProtocolUtils.getInstance().setMusicOnoff(arg1);//设置音乐控制开关
			break;
		case R.id.switch_music_startorend:
			//App控制手环音乐开始与结束
			if(arg1){
				dialog.show();
				ProtocolUtils.getInstance().startMusic();
			}else {
				dialog.show();
				ProtocolUtils.getInstance().stopMusic();
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onSysEvt(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		super.onSysEvt(arg0, arg1, arg2, arg3); 
		if(arg1==ProtocolEvt.SET_CMD_MUSIC_ONOFF.toIndex()&&arg2==ProtocolEvt.SUCCESS){
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					Toast.makeText(MusicActivity.this, "设置成功", Toast.LENGTH_LONG).show();
				}
			});
		}else if (arg1==ProtocolEvt.APP_TO_BLE_MUSIC_START.toIndex()&&arg2==ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					Toast.makeText(MusicActivity.this, "app控制手环音乐开始设置成功", Toast.LENGTH_LONG).show();
				}
			});
		}else if (arg1==ProtocolEvt.APP_TO_BLE_MUSIC_STOP.toIndex()&&arg2==ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					Toast.makeText(MusicActivity.this, "app控制手环音乐结束设置成功", Toast.LENGTH_LONG).show();
				}
			});
		}else if (arg1==ProtocolEvt.BLE_TO_APP_MUSIC_LAST.toIndex()&&arg2==ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					receiveSb.append("接收到了上一曲命令，开发者可在此回调中处理相应逻辑"+"\n\n");
					tvReceive.setText(receiveSb.toString());
				}
			});
		}else if (arg1==ProtocolEvt.BLE_TO_APP_MUSIC_NEXT.toIndex()&&arg2==ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					receiveSb.append("接收到了下一曲命令，开发者可在此回调中处理相应逻辑"+"\n\n");
					tvReceive.setText(receiveSb.toString());
				}
			});
		}else if (arg1==ProtocolEvt.BLE_TO_APP_MUSIC_PAUSE.toIndex()&&arg2==ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					receiveSb.append("接收到了暂停命令，开发者可在此回调中处理相应逻辑"+"\n\n");
					tvReceive.setText(receiveSb.toString());
				}
			});
		}else if (arg1==ProtocolEvt.BLE_TO_APP_MUSIC_START.toIndex()&&arg2==ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					receiveSb.append("接收到了音乐开始命令，开发者可在此回调中处理相应逻辑"+"\n\n");
					tvReceive.setText(receiveSb.toString());
				}
			});
		}else if (arg1==ProtocolEvt.BLE_TO_APP_MUSIC_STOP.toIndex()&&arg2==ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					receiveSb.append("接收到了音乐停止命令，开发者可在此回调中处理相应逻辑"+"\n\n");
					tvReceive.setText(receiveSb.toString());
				}
			});
		}
	}
}
