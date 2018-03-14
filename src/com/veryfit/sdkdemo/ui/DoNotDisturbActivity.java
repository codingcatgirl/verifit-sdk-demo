package com.veryfit.sdkdemo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.veryfit.multi.nativedatabase.DoNotDisturb;
import com.veryfit.multi.nativeprotocol.ProtocolEvt;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.sdkdemo.R;
import com.veryfit.sdkdemo.view.BufferDialog;

public class DoNotDisturbActivity extends BaseActivity{
	private Button btnCommit;
	private Switch switchDoNotDisturb;
	private EditText edStartHour,edStartMin,edEndHour,edEndMin;
	private boolean onOff;
	private Handler mHandler = new Handler();
	private BufferDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donotdisturb);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		initView();
		initData();
		addListener();
	}
	
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		super.initView();
		dialog=new BufferDialog(this);
		edEndHour=(EditText)findViewById(R.id.ed_donotdisturb_end_hour);
		edEndMin=(EditText)findViewById(R.id.ed_donotdisturb_end_min);
		edStartHour=(EditText)findViewById(R.id.ed_donotdisturb_start_hour);
		edStartMin=(EditText)findViewById(R.id.ed_donotdisturb_start_min);
		btnCommit=(Button)findViewById(R.id.btn_donotdisturb_commit);
		switchDoNotDisturb=(Switch)findViewById(R.id.switch_donotdisturb);
	}
	
	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		//获取已设置勿扰模式数据
		DoNotDisturb disturb=ProtocolUtils.getInstance().getDoNotDisturb();
		if(disturb!=null){
			edEndHour.setText(disturb.getEndHour()+"");
			edEndMin.setText(disturb.getEndMinute()+"");
			edStartHour.setText(disturb.getStartHour()+"");
			edStartMin.setText(disturb.getStartMinute()+"");
			onOff=disturb.getOnOFf();
			switchDoNotDisturb.setChecked(disturb.getOnOFf());
		}
	}
	
	@Override
	public void addListener() {
		// TODO Auto-generated method stub
		super.addListener();
		btnCommit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				int startHour=Integer.parseInt(edStartHour.getText().toString());
				int startMin=Integer.parseInt(edStartMin.getText().toString());
				int endHour=Integer.parseInt(edEndHour.getText().toString());
				int endMin=Integer.parseInt(edEndMin.getText().toString());
				DoNotDisturb disturb = new DoNotDisturb(0, onOff,startHour,startMin,endHour,endMin);
				dialog.show();
				ProtocolUtils.getInstance().setDisturbMode(disturb);
			}
		});
		
		switchDoNotDisturb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				onOff=arg1;
			}
		});
	}
	
	@Override
	public void onSysEvt(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		super.onSysEvt(arg0, arg1, arg2, arg3);
		if (arg1==ProtocolEvt.SET_CMD_DO_NOT_DISTURB.toIndex()&&arg2==ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					Toast.makeText(DoNotDisturbActivity.this, "勿扰设置成功", Toast.LENGTH_LONG).show();
				}
			});
		}
	}

}
