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
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_music);
		initView();
		initData();
		addListener();
	}

	@Override
	public void initView() {
		super.initView();
		dialog=new BufferDialog(this);
		switchOnOff=(Switch)findViewById(R.id.switch_music_onoff);
		switchStartOrStop=(Switch)findViewById(R.id.switch_music_startorend);
		tvReceive=(TextView)findViewById(R.id.tv_receive);
	}
	
	@Override
	public void initData() {
		super.initData();
		switchOnOff.setChecked(ProtocolUtils.getInstance().getMusicOnoff());
		
	}
	
	@Override
	public void addListener() {
		super.addListener();
		switchOnOff.setOnCheckedChangeListener(this);
		switchStartOrStop.setOnCheckedChangeListener(this);
	}
	
	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		switch (arg0.getId()) {
		case R.id.switch_music_onoff:
			dialog.show();
			ProtocolUtils.getInstance().setMusicOnoff(arg1);
			break;
		case R.id.switch_music_startorend:
			// Playing/Paused state displayed on the watch
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
		super.onSysEvt(arg0, arg1, arg2, arg3);
		if(arg1==ProtocolEvt.SET_CMD_MUSIC_ONOFF.toIndex()&&arg2==ProtocolEvt.SUCCESS){
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					Toast.makeText(MusicActivity.this, "Set successfully", Toast.LENGTH_LONG).show();
				}
			});
		}else if (arg1==ProtocolEvt.APP_TO_BLE_MUSIC_START.toIndex()&&arg2==ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					Toast.makeText(MusicActivity.this, "Playing state set successfully", Toast.LENGTH_LONG).show();
				}
			});
		}else if (arg1==ProtocolEvt.APP_TO_BLE_MUSIC_STOP.toIndex()&&arg2==ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					Toast.makeText(MusicActivity.this, "Paused state set successfully", Toast.LENGTH_LONG).show();
				}
			});
		}else if (arg1==ProtocolEvt.BLE_TO_APP_MUSIC_LAST.toIndex()&&arg2==ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					receiveSb.append("Received music previous track request"+"\n\n");
					tvReceive.setText(receiveSb.toString());
				}
			});
		}else if (arg1==ProtocolEvt.BLE_TO_APP_MUSIC_NEXT.toIndex()&&arg2==ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					receiveSb.append("Received music next track request"+"\n\n");
					tvReceive.setText(receiveSb.toString());
				}
			});
		}else if (arg1==ProtocolEvt.BLE_TO_APP_MUSIC_PAUSE.toIndex()&&arg2==ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					receiveSb.append("Received music pause request"+"\n\n");
					tvReceive.setText(receiveSb.toString());
				}
			});
		}else if (arg1==ProtocolEvt.BLE_TO_APP_MUSIC_START.toIndex()&&arg2==ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					receiveSb.append("Received music play request"+"\n\n");
					tvReceive.setText(receiveSb.toString());
				}
			});
		}else if (arg1==ProtocolEvt.BLE_TO_APP_MUSIC_STOP.toIndex()&&arg2==ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					receiveSb.append("Received music stop request"+"\n\n");
					tvReceive.setText(receiveSb.toString());
				}
			});
		}
	}
}
