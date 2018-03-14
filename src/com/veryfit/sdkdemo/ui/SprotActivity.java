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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.btn_sport_bydate:
			//根据日期获取数据只需传递想查询的那一天的年月日即可，为方便调试以今天为例  获取运动数据前务必同步 ，同步后才会有相应的那一天的运动数据
			@SuppressWarnings("deprecation")
			HealthSport sport = ProtocolUtils.getInstance().getHealthSport(new Date(year, month, day));//(月份从0开始例如8月传7)
			if(sport!=null){
				tvData.setText(sport.toString());
			}else {
				tvData.setText("今日无数据");
			}
			break;
		case R.id.btn_sportitem_bydate:
			Sb.delete(0, Sb.length());
			//根据日期获取数据item只需传递想查询的那一天的年月日即可 item 固定每天96条 每隔15分钟就会有一条item
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
			//获取周数据  参数1：（当前周传0 上一周传-1再上一周传-2，以此类推）
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
			//获取月数据  参数1：（当前月传0 上一月传-1再上一月传-2，以此类推）
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
			//获取年数据  参数1：（当年周传0 上一年传-1再上一年传-2，以此类推）
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
			//获取所有数据库中已存的汇总数据  有几天就返回几条汇总 
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
