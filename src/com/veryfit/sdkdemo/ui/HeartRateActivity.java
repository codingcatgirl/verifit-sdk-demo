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
			// To obtain data based on the date, you only need to pass the year, month, and day of the day you want to query.
			// To facilitate debugging, take today as an example. You must synchronize before obtaining the exercise data.
			// After synchronization, the heart rate data of the corresponding day will be available.
			// (With heart rate function and heart rate data)
			@SuppressWarnings("deprecation")
			HealthHeartRate heartRate = ProtocolUtils.getInstance().getHealthRate(new Date(year, month, day));// Months start at 0 (August = 7)
			if(heartRate!=null){
				tvData.setText(heartRate.toString());
			}else {
				tvData.setText("No data today");
			}
			break;
		case R.id.btn_heartrateitem_bydate:
			Sb.delete(0, Sb.length());
			// To obtain data according to the date item, you only need to pass the year, month,
			// and day of the day you want to query item (heart rate data in the bracelet),
			// take today as an example
			// The number of items in the heart rate data is not fixed.
			// Generally, one heart rate data will be collected about five minutes apart.
			// If there is no bracelet, there will be no heart rate data and items.
			@SuppressWarnings("deprecation")
			// Regarding the heart rate value of each time point of the item, the time point is calculated according
			// to the offsetMinute in the item. offsetMinute is the number of minutes offset from the previous item.
			// The offsetMinute of the first item is the number of minutes from zero.
			List<HealthHeartRateItem> items = ProtocolUtils.getInstance().getHeartRateItems(new Date(year, month, day));// Months start at 0 (August = 7)
			if(items!=null){
				for (int i = 0; i < items.size(); i++) {
					Sb.append("item:"+i+"   "+items.get(i).toString()+"\n\n");
				}
				tvData.setText(Sb.toString());
			}
			break;
		case R.id.btn_heartrate_week:
			Sb.delete(0, Sb.length());
			// Get weekly data Parameter 1: (pass 0 in the current week, pass -1 in the previous week, pass -2 in the previous week, and so on)
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
			// Get monthly data Parameter 1: (pass 0 in the current month, pass -1 in the previous month, pass -2 in the previous month, and so on)
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
			// Obtain yearly data Parameter 1: (Weekly pass 0 in the current year, pass -1 in the previous year, pass -2 in the previous year, and so on)
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
			// Get all the summary data stored in the database. Return a few summaries within a few days
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
