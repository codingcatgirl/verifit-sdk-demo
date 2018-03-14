package com.veryfit.sdkdemo.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

public class HttpUtil {
	
	public static final String PATH = "http://www.youduoyun.com/apps/firmwares/firmware.json";
	
	
	public static final int DOWN_LOAD_PROGRESS = 1;
	
	public static final int CONN_ERROR = 2;
	
	public static void downLoad(Handler handler , String path , String urlStr){
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(urlStr).build();
		try {
	    	Call call = client.newCall(request);
			Response response = call.execute();    
			if (response.isSuccessful()) {
				ResponseBody body = response.body();
				long size = body.contentLength();
				long progress = 0;
				
				InputStream is = body.byteStream();
				 OutputStream fos = new FileOutputStream(path);
		            int len;
		            byte[] b = new byte[512];
		            
		            while ((len = is.read(b)) != -1)
		            {
		                fos.write(b, 0, len);
		                progress += len;
		                handler.sendMessage(handler.obtainMessage(DOWN_LOAD_PROGRESS,  Math.round(100 * progress / (size * 1f))));
		            }
				handler.sendMessage(handler.obtainMessage(DOWN_LOAD_PROGRESS,  110));
		            fos.close();
			}
		} catch (IOException e) {
			handler.sendMessage(handler.obtainMessage(CONN_ERROR));
			e.printStackTrace();
		}
	}

	public static String get(String urlStr, Map<String, Object> params){

		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder().url(urlStr).build();

	    try {
	    	Call call = client.newCall(request);
			Response response = call.execute();    
			if (response.isSuccessful()) {
				return response.body().string();
			}else{
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static boolean isNetworkConnected(Context context){
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;

	}

}
