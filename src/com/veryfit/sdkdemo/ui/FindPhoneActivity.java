package com.veryfit.sdkdemo.ui;

import android.os.Bundle;
import android.os.Handler;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;

import com.veryfit.multi.nativedatabase.FindPhoneOnOff;
import com.veryfit.multi.nativeprotocol.ProtocolEvt;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.multi.util.DebugLog;
import com.veryfit.sdkdemo.R;
import com.veryfit.sdkdemo.view.BufferDialog;

public class FindPhoneActivity extends BaseActivity {
	private Switch switchFindPhone;
	private Handler mHandler = new Handler();
	private BufferDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findphone);
		initView();
		initData();
		addListener();
	}

	@Override
	public void initView() {
		// TODO Auto-generated method stub
		super.initView();
		dialog = new BufferDialog(this);
		switchFindPhone = (Switch) findViewById(R.id.switch_findphone);
	}

	@Override
	public void initData() {
		// TODO Auto-generated method stub
		super.initData();
		// 初始化寻找手机开关数据
		FindPhoneOnOff find = ProtocolUtils.getInstance().getFindPhone();
		switchFindPhone.setChecked(find.getOnOff());
	}

	@Override
	public void addListener() {
		// TODO Auto-generated method stub
		super.addListener();
		switchFindPhone.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// 每次设置成功后设置的数据会保存在数据库中,用于界面展示。
				dialog.show();
				ProtocolUtils.getInstance().setFindPhone(arg1);//设置寻找手机开关
			}
		});
	}

	@Override
	public void onSysEvt(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		super.onSysEvt(arg0, arg1, arg2, arg3);
		DebugLog.d("收到寻找手机的命令");
		if (arg1 == ProtocolEvt.SET_CMD_FIND_PHONE.toIndex() && arg2 == ProtocolEvt.SUCCESS) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					Toast.makeText(FindPhoneActivity.this, "寻找手机开关设置成功", Toast.LENGTH_LONG).show();
				}
			});
		}else if (arg1 == ProtocolEvt.BLE_TO_APP_FIND_PHONE_START.toIndex()) {//收到手环发送过来的寻找手机命令，客户端收到这条命令做相应处理，比如让手机响铃等操作
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					Toast.makeText(FindPhoneActivity.this, "收到寻找手机的命令", Toast.LENGTH_LONG).show();
				}
			});
		}
	}

}
