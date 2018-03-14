package com.veryfit.sdkdemo.model;


public class SportData1 {
	public long dId;
	public long date;
	public boolean isUpLoad;
	public int year;
	public int month;
	public int day;
	public int hour;
	public int minute;
	public int second;
	
	
	public int data_length;
	public int hr_data_interval_minute;
	public int hr_item_count;
	public int packet_count;
	
	public int type;
	public int step;
	public int durations;
	public int calories;
	public int distance;


	
	
	public SportData1() {
		super();
		// TODO Auto-generated constructor stub
	}




	public SportData1(long dId, long date, boolean isUpLoad, int year, int month, int day, int hour, int minute, int second, int data_length, int hr_data_interval_minute, int hr_item_count,
			int packet_count, int type, int step, int durations, int calories, int distance) {
		super();
		this.dId = dId;
		this.date = date;
		this.isUpLoad = isUpLoad;
		this.year = year;
		this.month = month;
		this.day = day;
		this.hour = hour;
		this.minute = minute;
		this.second = second;
		this.data_length = data_length;
		this.hr_data_interval_minute = hr_data_interval_minute;
		this.hr_item_count = hr_item_count;
		this.packet_count = packet_count;
		this.type = type;
		this.step = step;
		this.durations = durations;
		this.calories = calories;
		this.distance = distance;
	}




}
