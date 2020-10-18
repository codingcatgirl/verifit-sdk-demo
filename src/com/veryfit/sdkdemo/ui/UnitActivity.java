package com.veryfit.sdkdemo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;

import com.veryfit.multi.config.Constants;
import com.veryfit.multi.nativedatabase.Units;
import com.veryfit.multi.nativedatabase.Userinfos;
import com.veryfit.multi.nativeprotocol.ProtocolEvt;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.sdkdemo.R;
import com.veryfit.sdkdemo.view.BufferDialog;

public class UnitActivity extends BaseActivity implements OnCheckedChangeListener{
	private Switch switchTime,switchLanguage,switchGongyingzhi;
	private int gongyingzhi,timemode,language;
	private Button btnCommit;
	private Handler mHandler = new Handler();
	private BufferDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_unit);
		initView();
		initData();
		addListener();
	}
	
	@Override
	public void initView() {
		super.initView();
		dialog=new BufferDialog(this);
		switchGongyingzhi=(Switch)findViewById(R.id.switch_unit_gongyingzhi);
		switchLanguage=(Switch)findViewById(R.id.switch_unit_language);
		switchTime=(Switch)findViewById(R.id.switch_unit_time);
		btnCommit=(Button)findViewById(R.id.btn_unit_commit);
	}
	
	@Override
	public void initData() {
		super.initData();
		Units units=ProtocolUtils.getInstance().getUnits();
		if(units!=null){
			// METRIC
			// BRITISH
			gongyingzhi=units.getMode();
			switchGongyingzhi.setChecked(units.getMode()==Constants.METRIC);
			//LANGUAGE_ZH
			//LANGUAGE_EN
			language=units.getLanguage();
			switchLanguage.setChecked(units.getLanguage()==Constants.LANGUAGE_ZH);
			//TIME_MODE_24
			//TIME_MODE_12
			timemode=units.getTimeMode();
			switchTime.setChecked(units.getTimeMode()==Constants.TIME_MODE_24);
		}
	}
	
	
	@Override
	public void addListener() {
		super.addListener();
		switchGongyingzhi.setOnCheckedChangeListener(this);
		switchLanguage.setOnCheckedChangeListener(this);
		switchTime.setOnCheckedChangeListener(this);
		btnCommit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Userinfos info=ProtocolUtils.getInstance().getUserinfos();
				int stride=0;// The stride unit is calculated based on height and gender
				if(info.getGender()==Constants.FEMALE){
					stride=(int) (0.413*info.getHeight());
				}else {
					stride=(int) (0.415*info.getHeight());
				}
				dialog.show();
				ProtocolUtils.getInstance().setUint(gongyingzhi,stride,language,timemode);
			}
		});
	}
	
	
	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		switch (arg0.getId()) {
		case R.id.switch_unit_gongyingzhi:
			gongyingzhi=arg1?Constants.METRIC:Constants.BRITISH;
			break;
		case R.id.switch_unit_language:
			language=arg1?Constants.LANGUAGE_ZH:Constants.LANGUAGE_EN;
			break;
		case R.id.switch_unit_time:
			timemode=arg1?Constants.TIME_MODE_24:Constants.TIME_MODE_12;
			break;

		default:
			break;
		}
	}
	
	@Override
	public void onSysEvt(int arg0, int arg1, int arg2, int arg3) {
		super.onSysEvt(arg0, arg1, arg2, arg3);
		if (arg1==ProtocolEvt.SET_CMD_UINT.toIndex()&& arg2==ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					Toast.makeText(UnitActivity.this, "Unit settings successfully set", Toast.LENGTH_LONG).show();
				}
			});
		}
	}

}
