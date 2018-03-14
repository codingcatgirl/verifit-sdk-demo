package com.veryfit.sdkdemo.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 网络检测辅助类
 */
public class NetWorkUtil {


	private static ConnectivityManager connectivityManager = null;
	private static NetworkInfo niInfo = null;

	/**
	 * 检测网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnected(Context context) {
		connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		niInfo = connectivityManager.getActiveNetworkInfo();
		return niInfo != null && niInfo.isConnected();
	}
	/**
	 * 检测网络是否可用
	 *
	 * @return
	 */
//	public static boolean isNetWorkConnected() {
//		connectivityManager = (ConnectivityManager) MyApplication.getInstance()
//				.getSystemService(Context.CONNECTIVITY_SERVICE);
//		niInfo = connectivityManager.getActiveNetworkInfo();
//		return niInfo != null && niInfo.isConnected();
//	}

	public static int getNetWorkType(Context context) {

		if (niInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return niInfo.getType();
		} else {
			return isFastMobileNetwork(context);
		}

	}

	private static int isFastMobileNetwork(Context context) {
		TelephonyManager telephonyManager = (TelephonyManager) context
				.getSystemService(Context.TELEPHONY_SERVICE);
		int type = 0;
		switch (telephonyManager.getNetworkType()) {
		// 联通3g
		case TelephonyManager.NETWORK_TYPE_UMTS:
		case TelephonyManager.NETWORK_TYPE_HSDPA:
			// 电信3g
		case TelephonyManager.NETWORK_TYPE_EVDO_0:
		case TelephonyManager.NETWORK_TYPE_EVDO_A:
		case TelephonyManager.NETWORK_TYPE_EVDO_B:
			type = 3;
			break;
		// 移动联通2g
		case TelephonyManager.NETWORK_TYPE_GPRS:
		case TelephonyManager.NETWORK_TYPE_EDGE:
			// 电信2g
		case TelephonyManager.NETWORK_TYPE_CDMA:

			type = 2;
			break;
		}
		return type;
	}
}