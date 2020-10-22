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
 * There are two ways to get the feature list
 * The first one is obtained from the bracelet, and the app needs to be connected to the bracelet
 * The second is to obtain from the database. The function list in the database is cached in the
 * database after obtaining the function list from the bracelet (provided that the function list
 * has been obtained from the exchange, otherwise the data will not be saved in the database )
 */
public class FunctionInfosActivity extends BaseActivity{
	private TextView tvData;
	private Button btnFunc,btnFuncDb;
	private Handler mHandler = new Handler();
	private BufferDialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
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
				final FunctionInfos infos=ProtocolUtils.getInstance().getFunctionInfosByDb();//从数据库中获取
				if(infos!=null){
					mHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							tvData.setText("Function list:\n\n" + infos.toString());
						}
					}, 200);
				}
			}
		});
		btnFunc.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				dialog.show();
				ProtocolUtils.getInstance().getFunctionInfos();//从手环上直接获取获取
			}
		});
	}
	
	@Override
	public void onFuncTable(final FunctionInfos arg0) {
		super.onFuncTable(arg0);
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				dialog.dismiss();
				tvData.setText("Function list:\n\n" + arg0.toString());
			}
		}, 200);
	}

}
