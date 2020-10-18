package com.veryfit.sdkdemo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.veryfit.multi.ble.ProtocalCallBack;
import com.veryfit.multi.nativedatabase.HealthHeartRate;
import com.veryfit.multi.nativedatabase.HealthHeartRateAndItems;
import com.veryfit.multi.nativedatabase.HealthSport;
import com.veryfit.multi.nativedatabase.HealthSportAndItems;
import com.veryfit.multi.nativedatabase.healthSleep;
import com.veryfit.multi.nativedatabase.healthSleepAndItems;
import com.veryfit.multi.nativeprotocol.ProtocolEvt;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.sdkdemo.R;
import com.veryfit.sdkdemo.config.MyPreference;
import com.veryfit.sdkdemo.view.BufferDialog;

public class SyncHealthActivity extends BaseActivity implements ProtocalCallBack, OnClickListener {
	private Button btnSync;
	private TextView tvSportData;
	private TextView tvSleepData;
	private TextView tvHeartData;
	private MyPreference pref;
	private Handler mHandler = new Handler();
	private BufferDialog dialog;
	private StringBuffer sportSb=new StringBuffer();
	private StringBuffer sleepSb=new StringBuffer();
	private StringBuffer heartSb=new StringBuffer();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync_health);
		initView();
		addListener();
	}

	public void initView() {
		pref=MyPreference.getInstance(this);
		dialog=new BufferDialog(SyncHealthActivity.this);
		ProtocolUtils.getInstance().setProtocalCallBack(this);
		btnSync = (Button) findViewById(R.id.btn_sync_health);
		tvSportData = (TextView) findViewById(R.id.tv_sport_data);
		tvSleepData = (TextView) findViewById(R.id.tv_sleep_data);
		tvHeartData = (TextView) findViewById(R.id.tv_heart_data);
	}

	public void addListener() {
		btnSync.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_sync_health:
			tvHeartData.setText("");
			tvSleepData.setText("");
			tvSportData.setText("");
			sportSb.delete(0,sportSb.length()); // clear data
			sleepSb.delete(0,sleepSb.length()); // clear data
			heartSb.delete(0,heartSb.length()); // clear data

			if(pref.getIsFirstSync()){// On the first sync we need to call firstStartSyncHealthData()
				ProtocolUtils.getInstance().firstStartSyncHealthData();
				pref.setIsFirstSync(false);
			}else {
				ProtocolUtils.getInstance().StartSyncHealthData();
			}
			setTitle("Syncing");
			dialog.setTitle("Please wait");
			dialog.show();
			break;

		default:
			break;
		}
	}

	@Override
	public void onSysEvt(int evt_base, final int evt_type, int error, final int value) {
		if(evt_type == ProtocolEvt.SYNC_EVT_HEALTH_PROGRESS.toIndex()){
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					setTitle("Syncing: " + value + "%");
				}
			}, 200);
		}else if (evt_type == ProtocolEvt.SYNC_EVT_HEALTH_SYNC_COMPLETE.toIndex()) {//Synchronization is complete
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
				}
			}, 200);
		}
	}

	@Override
	public void onHealthHeartRate(final HealthHeartRate arg0, HealthHeartRateAndItems arg1) {
		if (arg0 != null) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					heartSb.append(arg0.toString()+"\n");
					tvHeartData.setText("Heart rate data:\n\n" + heartSb.toString());
				}
			}, 200);
		}
	}

	@Override
	public void onHealthSport(final HealthSport arg0, HealthSportAndItems arg1) {
		if (arg0 != null) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					sportSb.append(arg0.toString()+"\n");
					tvSportData.setText("Exercise data:\n\n" + sportSb.toString());
				}
			}, 200);
		}
	}

	@Override
	public void onSleepData(final healthSleep arg0, healthSleepAndItems arg1) {
		if (arg0 != null) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					sleepSb.append(arg0.toString()+"\n");
					tvSleepData.setText("Sleep data:\n\n" + sleepSb.toString());
				}
			}, 200);
		}
	}
}
