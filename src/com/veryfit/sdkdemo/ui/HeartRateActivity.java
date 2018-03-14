package com.veryfit.sdkdemo.ui;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.veryfit.multi.nativedatabase.HealthHeartRate;
import com.veryfit.multi.nativedatabase.HealthHeartRateItem;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.sdkdemo.R;

public class HeartRateActivity extends Activity implements OnClickListener{
	private Button btnHeartRateByDate,btnHeartRateItemByDate,btnAll,btnWeek,btnMonth,btnYear;
	private TextView tvData;
	private int year,month,day;
	private StringBuffer Sb=new StringBuffer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_heartrate);
		initView();
		addListener();
	}
	
	public void initView(){
		Calendar mCalendar = Calendar.getInstance();
		year = mCalendar.get(Calendar.YEAR);
		month=mCalendar.get(Calendar.MONTH);
		day = mCalendar.get(Calendar.DAY_OF_MONTH);
		btnAll=(Button)findViewById(R.id.btn_heartrate_all);
		btnHeartRateByDate=(Button)findViewById(R.id.btn_heartrate_bydate);
		btnHeartRateItemByDate=(Button)findViewById(R.id.btn_heartrateitem_bydate);
		btnWeek=(Button)findViewById(R.id.btn_heartrate_week);
		btnMonth=(Button)findViewById(R.id.btn_heartrate_month);
		btnYear=(Button)findViewById(R.id.btn_heartrate_year);
		tvData=(TextView)findViewById(R.id.tv_data);
		
	}
	
	
	public void initData(){
		
	}
	
	public void addListener(){
		btnAll.setOnClickListener(this);
		btnMonth.setOnClickListener(this);
		btnHeartRateByDate.setOnClickListener(this);
		btnHeartRateItemByDate.setOnClickListener(this);
		btnWeek.setOnClickListener(this);
		btnYear.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_heartrate_bydate:
			//根据日期获取数据只需传递想查询的那一天的年月日即可，为方便调试以今天为例  获取运动数据前务必同步 ，同步后才会有相应的那一天的心率数据(前提保证手环带有心率功能并中有心率数据)
			@SuppressWarnings("deprecation")
			HealthHeartRate heartRate = ProtocolUtils.getInstance().getHealthRate(new Date(year, month, day));//(月份从0开始例如8月传7)
			if(heartRate!=null){
				tvData.setText(heartRate.toString());
			}else {
				tvData.setText("今日无数据");
			}
			break;
		case R.id.btn_heartrateitem_bydate:
			Sb.delete(0, Sb.length());
			//根据日期获取数据item只需传递想查询的那一天的年月日即可 item（手环中要有心率数据）,以今天为例
			//心率数据的item条数不固定,一般相隔五分钟左右会采集一条心率数据，如果一直没有带手环的话就不会有心率数据以及item
			@SuppressWarnings("deprecation")
			//关于item 各时间点的心率值   时间点是根据 item中 offsetMinute 计算得出的  offsetMinute是距离上一条item偏移的分钟数  第一条item的offsetMinute是距离零点的分钟数
			List<HealthHeartRateItem> items = ProtocolUtils.getInstance().getHeartRateItems(new Date(year, month, day));//(月份从0开始例如8月传7)
			if(items!=null){
				for (int i = 0; i < items.size(); i++) {
					Sb.append("item:"+i+"   "+items.get(i).toString()+"\n\n");
				}
				tvData.setText(Sb.toString());
			}
			break;
		case R.id.btn_heartrate_week:
			Sb.delete(0, Sb.length());
			//获取周数据  参数1：（当前周传0 上一周传-1再上一周传-2，以此类推）
			List<HealthHeartRate> weekHeartRates=ProtocolUtils.getInstance().getWeekHealthHeartRate(0);
			if(weekHeartRates!=null&&weekHeartRates.size()>0){
				for (int i = 0; i < weekHeartRates.size(); i++) {
					Sb.append(weekHeartRates.get(i).toString()+"\n\n");
				}
				tvData.setText(Sb.toString());
			}
			break;
		case R.id.btn_heartrate_month:
			Sb.delete(0, Sb.length());
			//获取月数据  参数1：（当前月传0 上一月传-1再上一月传-2，以此类推）
			List<HealthHeartRate> monthHeartRates=ProtocolUtils.getInstance().getMonthHeartRate(0);
			if(monthHeartRates!=null&&monthHeartRates.size()>0){
				for (int i = 0; i < monthHeartRates.size(); i++) {
					Sb.append(monthHeartRates.get(i).toString()+"\n\n");
				}
				tvData.setText(Sb.toString());
			}
			break;
		case R.id.btn_heartrate_year:
			Sb.delete(0, Sb.length());
			//获取年数据  参数1：（当年周传0 上一年传-1再上一年传-2，以此类推）
			List<HealthHeartRate> yearHealthHeartRates=ProtocolUtils.getInstance().getYearHealthHeartRate(0);
			if(yearHealthHeartRates!=null&&yearHealthHeartRates.size()>0){
				for (int i = 0; i < yearHealthHeartRates.size(); i++) {
					Sb.append(yearHealthHeartRates.get(i).toString()+"\n\n");
				}
				tvData.setText(Sb.toString());
			}
			break;
		case R.id.btn_heartrate_all:
			Sb.delete(0, Sb.length());
			//获取所有数据库中已存的汇总数据  有几天就返回几条汇总 
			List<HealthHeartRate> healthHeartRates=ProtocolUtils.getInstance().getAllHealthRate();
			if(healthHeartRates!=null&&healthHeartRates.size()>0){
				for (int i = 0; i < healthHeartRates.size(); i++) {
					Sb.append(healthHeartRates.get(i).toString()+"\n\n");
				}
				tvData.setText(Sb.toString());
			}
			break;

		default:
			break;
		}
	}
	
	

}
