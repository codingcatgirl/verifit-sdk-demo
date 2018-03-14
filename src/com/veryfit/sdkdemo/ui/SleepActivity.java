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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_sleep_bydate:
			//根据日期获取数据只需传递想查询的那一天的年月日即可，为方便调试以今天为例  获取睡眠数据前务必同步 ，同步后才会有相应的那一天的睡眠数据
			@SuppressWarnings("deprecation")
			healthSleep sleep = ProtocolUtils.getInstance().getHealthSleep(new Date(year, month, day));//(月份从0开始例如8月传7)
			if(sleep!=null){
				tvData.setText(sleep.toString());
			}else {
				tvData.setText("今日无数据");
			}
			break;
		case R.id.btn_sleepitem_bydate:
			Sb.delete(0, Sb.length());
			//根据日期获取数据item只需传递想查询的那一天的年月日即可 (不带手环睡觉时不会有睡眠数据的) 睡眠数据item条数不固定 时长也是不固定的
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
			//获取周数据  参数1：（当前周传0 上一周传-1再上一周传-2，以此类推）
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
			//获取月数据  参数1：（当前月传0 上一月传-1再上一月传-2，以此类推）
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
			//获取年数据  参数1：（当年周传0 上一年传-1再上一年传-2，以此类推）
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
			//获取所有数据库中已存的汇总数据  有几天就返回几条汇总 
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
