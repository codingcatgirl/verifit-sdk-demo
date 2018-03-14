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
import com.veryfit.multi.util.DebugLog;
import com.veryfit.sdkdemo.R;
import com.veryfit.sdkdemo.view.BufferDialog;

public class CameraActivity extends BaseActivity{
	
	private Switch switchCamera;
	private TextView tvReceive;
	private Handler mHandler = new Handler();
	private BufferDialog dialog;
	private StringBuffer receiveSb=new StringBuffer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);
		initView();
		addListener();
	}
	
	@Override
	public void initView() {
		// TODO Auto-generated method stub
		super.initView();
		dialog = new BufferDialog(this);
		switchCamera = (Switch) findViewById(R.id.switch_camera);
		tvReceive=(TextView)findViewById(R.id.tv_receive);
	}
	
	
	@Override
	public void addListener() {
		// TODO Auto-generated method stub
		super.addListener();
		switchCamera.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				if(arg1){
					dialog.show();
					ProtocolUtils.getInstance().startPhoto();//进入拍照模式
				}else {
					dialog.show();
					receiveSb.delete(0,receiveSb.length());//清空数据
					ProtocolUtils.getInstance().stopPhoto();//退出拍照模式
				}
			}
		});
	}
	
	@Override
	public void onSysEvt(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		super.onSysEvt(arg0, arg1, arg2, arg3);
		DebugLog.d("type="+arg1);
		if(arg1==ProtocolEvt.APP_TO_BLE_PHOTO_START.toIndex()&&arg2==ProtocolEvt.SUCCESS){
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					dialog.dismiss();
					Toast.makeText(CameraActivity.this, "进入拍照模式成功", Toast.LENGTH_LONG).show();
				}
			});
		}else if (arg1==ProtocolEvt.BLE_TO_APP_PHOTO_SINGLE_SHOT.toIndex()) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					//执行拍照处理回调
					receiveSb.append("接收到了单拍命令，开发者可在此回调中处理拍照逻辑"+"\n\n");
					tvReceive.setText(receiveSb.toString());
				}
			});
		}else if (arg1==ProtocolEvt.BLE_TO_APP_PHOTO_BURES.toIndex()) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					receiveSb.append("接收到了连拍命令，开发者可在此回调中处理拍照逻辑"+"\n\n");
					tvReceive.setText(receiveSb.toString());
				}
			});
		}
	}
}
