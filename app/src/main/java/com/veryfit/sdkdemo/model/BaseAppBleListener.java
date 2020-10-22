package com.veryfit.sdkdemo.model;

import android.bluetooth.BluetoothGatt;
import android.util.Log;

import com.veryfit.multi.ble.AppBleListener;

/**
 * Created by asus on 2016/7/7.
 */
public class BaseAppBleListener implements AppBleListener{
    @Override
    public void onBlueToothError(int i) {
        d("onBlueToothError");
    }

    @Override
    public void onBLEConnecting(String s) {
        d("onBLEConnecting");
    }

    @Override
    public void onBLEConnected(BluetoothGatt bluetoothGatt) {
        d("onBLEConnected");
    }

    @Override
    public void onServiceDiscover(BluetoothGatt bluetoothGatt, int i) {
        d("onServiceDiscover");
    }

    @Override
    public void onBLEDisConnected(String s) {
        d("onBLEDisConnected");
    }

    @Override
    public void onBLEConnectTimeOut() {
        d("onBLEConnectTimeOut");
    }

    @Override
    public void onDataSendTimeOut(byte[] bytes) {
        d("onDataSendTimeOut");
    }
    public void d(String msg) {
        Log.d(this.getClass().getSimpleName(), msg);
    }

}
