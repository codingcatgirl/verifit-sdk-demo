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

import com.veryfit.multi.nativedatabase.HealthSport;
import com.veryfit.multi.nativedatabase.HealthSportItem;
import com.veryfit.multi.nativeprotocol.ProtocolUtils;
import com.veryfit.sdkdemo.R;

public class SprotActivity extends Activity implements OnClickListener{
	private Button btnSportByDate,btnSportItemByDate,btnAll,btnWeek,btnMonth,btnYear;
	private TextView tvData;
	private int year,month,day;
	private StringBuffer Sb=new StringBuffer();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sport);
		initView();
		addListener();
	}
	
	public void initView(){
		Calendar mCalendar = Calendar.getInstance();
		year = mCalendar.get(Calendar.YEAR);
		month=mCalendar.get(Calendar.MONTH);
		day = mCalendar.get(Calendar.DAY_OF_MONTH);
		btnAll=(Button)findViewById(R.id.btn_sport_all);
		btnSportByDate=(Button)findViewById(R.id.btn_sport_bydate);
		btnSportItemByDate=(Button)findViewById(R.id.btn_sportitem_bydate);
		btnWeek=(Button)findViewById(R.id.btn_sport_week);
		btnMonth=(Button)findViewById(R.id.btn_sport_month);
		btnYear=(Button)findViewById(R.id.btn_sport_year);
		tvData=(TextView)findViewById(R.id.tv_data);
	}
	
	
	public void initData(){
		
	}
	
	public void addListener(){
		btnAll.setOnClickListener(this);
		btnMonth.setOnClickListener(this);
		btnSportByDate.setOnClickListener(this);
		btnSportItemByDate.setOnClickListener(this);
		btnWeek.setOnClickListener(this);
		btnYear.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_sport_bydate:
			// To obtain data based on the date, you only need to pass the year, month, and day of the day you want to query.
			// For the convenience of debugging, take today as an example. Be sure to synchronize before obtaining the sports data.
			// After synchronization, the sports data of the corresponding day will be available.
			@SuppressWarnings("deprecation")
			HealthSport sport = ProtocolUtils.getInstance().getHealthSport(new Date(year, month, day));//(月份从0开始例如8月传7)
			if(sport!=null){
				tvData.setText(sport.toString());
			}else {
				tvData.setText("No data today");
			}
			break;
		case R.id.btn_sportitem_bydate:
			Sb.delete(0, Sb.length());
			// To get the data item according to the date, you only need to pass the year, month, and day of the day you want to query.
			// The item is fixed at 96 per day. There will be an item every 15 minutes.
			@SuppressWarnings("deprecation")
			List<HealthSportItem> items = ProtocolUtils.getInstance().getHealthSportItem(new Date(year, month, day));//(月份从0开始例如8月传7)
			if(items!=null){
				for (int i = 0; i < items.size(); i++) {
					Sb.append("item:"+i+"   "+items.get(i).toString()+"\n\n");
				}
				tvData.setText(Sb.toString());
			}
			break;
		case R.id.btn_sport_week:
			Sb.delete(0, Sb.length());
			// Get weekly data Parameter 1: (pass 0 in the current week, pass -1 in the previous week, and pass -2 in the previous week, and so on)
			List<HealthSport> weekSprots=ProtocolUtils.getInstance().getWeekHealthSport(0);
			if(weekSprots!=null&&weekSprots.size()>0){
				for (int i = 0; i < weekSprots.size(); i++) {
					Sb.append(weekSprots.get(i).toString()+"\n\n");
				}
				tvData.setText(Sb.toString());
			}
			break;
		case R.id.btn_sport_month:
			Sb.delete(0, Sb.length());
			// Get monthly data Parameter 1: (pass 0 in the current month, pass -1 in the previous month, pass -2 in the previous month, and so on)
			List<HealthSport> monthSprots=ProtocolUtils.getInstance().getMonthHealthSprort(0);
			if(monthSprots!=null&&monthSprots.size()>0){
				for (int i = 0; i < monthSprots.size(); i++) {
					Sb.append(monthSprots.get(i).toString()+"\n\n");
				}
				tvData.setText(Sb.toString());
			}
			break;
		case R.id.btn_sport_year:
			Sb.delete(0, Sb.length());
			// Obtain annual data Parameter 1: (Weekly pass 0 in the current year, pass -1 in the previous year, pass -2 in the previous year, and so on)
			List<HealthSport> yearSprots=ProtocolUtils.getInstance().getYearHealthSport(0);
			if(yearSprots!=null&&yearSprots.size()>0){
				for (int i = 0; i < yearSprots.size(); i++) {
					Sb.append(yearSprots.get(i).toString()+"\n\n");
				}
				tvData.setText(Sb.toString());
			}
			break;
		case R.id.btn_sport_all:
			Sb.delete(0, Sb.length());
			// Get all the summary data stored in the database. Return a few summaries within a few days
			List<HealthSport> sports=ProtocolUtils.getInstance().getAllHealthSport();
			if(sports!=null&&sports.size()>0){
				for (int i = 0; i < sports.size(); i++) {
					Sb.append(sports.get(i).toString()+"\n\n");
				}
				tvData.setText(Sb.toString());
			}
			break;

		default:
			break;
		}
	}
}
