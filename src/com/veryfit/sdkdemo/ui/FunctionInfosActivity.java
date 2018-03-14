package com.veryfit.sdkdemo.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.veryfit.multi.nativedatabase.FunctionInfos;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.sdkdemo.R;
import com.veryfit.sdkdemo.view.BufferDialog;

@SuppressLint("HandlerLeak")
/**
 * 功能列表有两种获取方式
 * 第一种是从手环上获取，需要app与手环保持连接
 * 第二种是从数据库中获取，数据库中的功能列表是从手环上获取功能列表后缓存在数据库中的（前提是从收换上获取过功能列表，不然数据库中并不会保存此数据）
 * @author Administrator
 *
 */
public class FunctionInfosActivity extends BaseActivity{
	private TextView tvData;
	private Button btnFunc,btnFuncDb;
	private Handler mHandler = new Handler();
	private BufferDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_functioninfos);
		initView();
	}
	
	
	public void initView(){
		dialog=new BufferDialog(this);
		tvData=(TextView)findViewById(R.id.tv_data);
		btnFunc=(Button)findViewById(R.id.btn_functioninfos);
		btnFuncDb=(Button)findViewById(R.id.btn_functioninfosbydb);
		btnFuncDb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final FunctionInfos infos=ProtocolUtils.getInstance().getFunctionInfosByDb();//从数据库中获取
				if(infos!=null){
					mHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							tvData.setText("功能列表:\n\n" + infos.toString());
						}
					}, 200);
				}
			}
		});
		btnFunc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.show();
				ProtocolUtils.getInstance().getFunctionInfos();//从手环上直接获取获取
			}
		});
	}
	
	@Override
	public void onFuncTable(final FunctionInfos arg0) {
		// TODO Auto-generated method stub
		super.onFuncTable(arg0);
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				dialog.dismiss();
				tvData.setText("功能列表:\n\n" + arg0.toString());
			}
		}, 200);
	}

}
