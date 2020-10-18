package com.veryfit.sdkdemo.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ScrollView;
import android.widget.TextView;

import com.veryfit.multi.nativedatabase.RealTimeHealthData;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.sdkdemo.R;

@SuppressLint("HandlerLeak")
public class LiveDataActivity extends BaseActivity {
	private TextView tvLiveData;
	private ScrollView scrollView;
	private Handler mHandler = new Handler();
	private StringBuffer livedatSb=new StringBuffer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_livedata);
		initView();
	}
	
	public void initView(){
		ProtocolUtils.getInstance().setProtocalCallBack(this);
		tvLiveData=(TextView)findViewById(R.id.tv_livedata);
		scrollView=(ScrollView)findViewById(R.id.sv_livedata);
		loadStart();
	}
	
	public Timer healthTimer;

	public void close(){
		if (healthTimer != null) {
			healthTimer.cancel();
			healthTimer = null;
		}
	}
	
	// 开始计时
	public void loadStart() {
		if (healthTimer != null) {
			healthTimer.cancel();
			healthTimer = null;
		}
		healthTimer = new Timer();
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					//BLUETOOTH_NOT_OPEN
					//DEVICE_NOT_CONNECT
					//DEVICE_NO_BLUEETOOTH
					//SUCCESS
					if(ProtocolUtils.getInstance().isAvailable() == ProtocolUtils.SUCCESS){
						ProtocolUtils.getInstance().getLiveData();
					}else {
						setTitle("Device not connected");
					}
					break;
				}
			}
		};
		healthTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				handler.sendEmptyMessage(1);
			}
		}, 0, 2000);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		close();
	}

	@Override
	public void onLiveData(final RealTimeHealthData arg0) {
		if (arg0 != null) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					livedatSb.append(arg0.toString()+"\n\n");
					tvLiveData.setText("Real-time data:\n\n" + livedatSb.toString());
					scrollView.fullScroll(ScrollView.FOCUS_DOWN);
				}
			}, 200);
		}
	}
}
