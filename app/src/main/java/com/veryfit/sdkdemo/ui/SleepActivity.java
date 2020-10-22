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

import com.veryfit.multi.nativedatabase.healthSleep;
import com.veryfit.multi.nativedatabase.healthSleepItem;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.sdkdemo.R;

public class SleepActivity extends Activity implements OnClickListener{
	private Button btnSleepByDate,btnSleepItemByDate,btnAll,btnWeek,btnMonth,btnYear;
	private TextView tvData;
	private int year,month,day;
	private StringBuffer Sb=new StringBuffer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sleep);
		initView();
		addListener();
	}
	
	public void initView(){
		Calendar mCalendar = Calendar.getInstance();
		year = mCalendar.get(Calendar.YEAR);
		month=mCalendar.get(Calendar.MONTH);
		day = mCalendar.get(Calendar.DAY_OF_MONTH);
		btnAll=(Button)findViewById(R.id.btn_sleep_all);
		btnSleepByDate=(Button)findViewById(R.id.btn_sleep_bydate);
		btnSleepItemByDate=(Button)findViewById(R.id.btn_sleepitem_bydate);
		btnWeek=(Button)findViewById(R.id.btn_sleep_week);
		btnMonth=(Button)findViewById(R.id.btn_sleep_month);
		btnYear=(Button)findViewById(R.id.btn_sleep_year);
		tvData=(TextView)findViewById(R.id.tv_data);
		
	}
	
	
	public void initData(){
		
	}
	
	public void addListener(){
		btnAll.setOnClickListener(this);
		btnMonth.setOnClickListener(this);
		btnSleepByDate.setOnClickListener(this);
		btnSleepItemByDate.setOnClickListener(this);
		btnWeek.setOnClickListener(this);
		btnYear.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_sleep_bydate:
			// To obtain data based on the date, you only need to pass the year, month, and day of the day you want to query.
			// For the convenience of debugging, take today as an example. Be sure to synchronize before obtaining sleep data.
			// After synchronization, the sleep data of the corresponding day will be available.
			@SuppressWarnings("deprecation")
			healthSleep sleep = ProtocolUtils.getInstance().getHealthSleep(new Date(year, month, day));//(月份从0开始例如8月传7)
			if(sleep!=null){
				tvData.setText(sleep.toString());
			}else {
				tvData.setText("No data today");
			}
			break;
		case R.id.btn_sleepitem_bydate:
			Sb.delete(0, Sb.length());
			// To get the data item according to the date, you only need to pass the year, month and day of the day you want to query
			// (there will be no sleep data when you sleep without a bracelet) The number of sleep data items is not fixed, and the duration is not fixed.
			@SuppressWarnings("deprecation")
			List<healthSleepItem> items = ProtocolUtils.getInstance().getHealthSleepItem(new Date(year, month, day));//(月份从0开始例如8月传7)
			if(items!=null){
				for (int i = 0; i < items.size(); i++) {
					Sb.append("item:"+i+"   "+items.get(i).toString()+"\n\n");
				}
				tvData.setText(Sb.toString());
			}
			break;
		case R.id.btn_sleep_week:
			Sb.delete(0, Sb.length());
			// Get weekly data Parameter 1: (pass 0 in the current week, pass -1 in the previous week, pass -2 in the previous week, and so on)
			List<healthSleep> weekSleeps=ProtocolUtils.getInstance().getWeekHealthSleep(0);
			if(weekSleeps!=null&&weekSleeps.size()>0){
				for (int i = 0; i < weekSleeps.size(); i++) {
					Sb.append(weekSleeps.get(i).toString()+"\n\n");
				}
				tvData.setText(Sb.toString());
			}
			break;
		case R.id.btn_sleep_month:
			Sb.delete(0, Sb.length());
			// Get monthly data Parameter 1: (pass 0 in the current month, pass -1 in the previous month, pass -2 in the previous month, and so on)
			List<healthSleep> monthSleeps=ProtocolUtils.getInstance().getMonthHealthSleep(0);
			if(monthSleeps!=null&&monthSleeps.size()>0){
				for (int i = 0; i < monthSleeps.size(); i++) {
					Sb.append(monthSleeps.get(i).toString()+"\n\n");
				}
				tvData.setText(Sb.toString());
			}
			break;
		case R.id.btn_sleep_year:
			Sb.delete(0, Sb.length());
			// Obtain annual data Parameter 1: (Weekly pass 0 in the current year, pass -1 in the previous year, pass -2 in the previous year, and so on)
			List<healthSleep> yearSleeps=ProtocolUtils.getInstance().getYearHealthSleep(0);
			if(yearSleeps!=null&&yearSleeps.size()>0){
				for (int i = 0; i < yearSleeps.size(); i++) {
					Sb.append(yearSleeps.get(i).toString()+"\n\n");
				}
				tvData.setText(Sb.toString());
			}
			break;
		case R.id.btn_sleep_all:
			Sb.delete(0, Sb.length());
			// Get all the summary data stored in the database. Return a few summaries within a few days
			List<healthSleep> sleeps=ProtocolUtils.getInstance().getAllHealthSleep();
			if(sleeps!=null&&sleeps.size()>0){
				for (int i = 0; i < sleeps.size(); i++) {
					Sb.append(sleeps.get(i).toString()+"\n\n");
				}
				tvData.setText(Sb.toString());
			}
			break;

		default:
			break;
		}
	}
	
	

}
