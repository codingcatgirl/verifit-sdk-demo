package com.veryfit.sdkdemo.ui;

import java.util.ArrayList;
import java.util.Collections;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.veryfit.multi.entity.BleDevice;
import com.veryfit.multi.nativeprotocol.Protocol;
import com.veryfit.multi.nativeprotocol.ProtocolEvt;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.multi.util.DebugLog;
import com.veryfit.sdkdemo.R;
import com.veryfit.sdkdemo.adapter.ScanAdapter;
import com.veryfit.sdkdemo.config.MyPreference;
import com.veryfit.sdkdemo.view.BufferDialog;

public class ScanDeviceActivity extends BaseActivity implements OnClickListener {
	private ListView lView;
	private Button btnScan, btnBind;
	private ScanAdapter adapter;
	private Handler mHandler = new Handler();
	private Handler mHandler1 = new Handler();
	private MyPreference pref;
	private ArrayList<BleDevice> list = new ArrayList<BleDevice>();
	private int index;
	private BufferDialog mBufferDialog;
	private MyBroadCastReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan);
		initView();
		addListener();
		addFilter();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				ProtocolUtils.getInstance().scanDevices(true);
			}
		});
	}

	public void initView() {
		receiver=new MyBroadCastReceiver();
		pref = MyPreference.getInstance(this);
		mBufferDialog = new BufferDialog(this);
		lView = (ListView) findViewById(R.id.lv_device);
		btnBind = (Button) findViewById(R.id.btn_bind);
		btnScan = (Button) findViewById(R.id.btn_scan);
		adapter = new ScanAdapter(this, null);
		lView.setAdapter(adapter);
		lView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				index = arg2;
				adapter.setSelectItem(arg2);
			}
		});
	}

	public void addListener() {
		btnBind.setOnClickListener(this);
		btnScan.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_bind:
			DebugLog.d("bind");
			mBufferDialog.show();
			ProtocolUtils.getInstance().connect(list.get(index).mDeviceAddress);
			break;
		case R.id.btn_scan:
			list.clear();
			adapter.clear();
			mHandler.post(new Runnable() {
				
				@Override
				public void run() {
					ProtocolUtils.getInstance().scanDevices();
				}
			});
			break;
		default:
			break;
		}
	}

	// 发现设备
	@Override
	public void onFind(BleDevice device) {
		super.onFind(device);
		showList(device);
		list.add(device);
		Collections.sort(list);
	}

	// 扫描结束
	@Override
	public void onFinish() {
		super.onFinish();
		DebugLog.d("onFinish");
	}

	private void showList(final BleDevice device) {
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				adapter.upDada(device);
			}
		});
	}

	@Override
	public void onServiceDiscover(BluetoothGatt gatt, int status) {
		super.onServiceDiscover(gatt, status);
		ProtocolUtils.getInstance().setBind();
	}

	@Override
	public void onSysEvt(int evt_base, int evt_type, int error, int value) {
		super.onSysEvt(evt_base, evt_type, error, value);
		if (evt_type == ProtocolEvt.BIND_CMD_REQUEST.toIndex() && error == ProtocolEvt.SUCCESS) {
			if (mBufferDialog != null) {
				mBufferDialog.setTitle("Binding successful, syncing configuration...");
			}
			pref.setIsFirst(true);
			mHandler1.post(new Runnable() {

				@Override
				public void run() {
					ProtocolUtils.getInstance().setBindMode(Protocol.SYS_MODE_SET_BIND);
					ProtocolUtils.getInstance().StartSyncConfigInfo();
				}
			});
		} else if (evt_type == ProtocolEvt.SYNC_EVT_CONFIG_SYNC_COMPLETE.toIndex() && error == ProtocolEvt.SUCCESS) {
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mBufferDialog.dismiss();
					Intent intent = new Intent(ScanDeviceActivity.this, UserInfosActivity.class);
					startActivity(intent);
					finish();
					Toast.makeText(ScanDeviceActivity.this, "Syncing configuration complete", Toast.LENGTH_LONG).show();
				}
			}, 200);
		}

	}
	
	public void addFilter(){
		IntentFilter filter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(receiver, filter);
	}
	
	class MyBroadCastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context arg0, Intent arg1) {
			if(arg1.getAction().equals(BluetoothDevice.ACTION_FOUND)){
				BluetoothDevice device=arg1.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				DebugLog.d("find"+device.toString());
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
}
