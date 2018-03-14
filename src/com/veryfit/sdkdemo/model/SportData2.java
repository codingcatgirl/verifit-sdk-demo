package com.veryfit.sdkdemo.model;

import java.util.Arrays;
import java.util.Map;

public class SportData2 {
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
	
	public int avg_hr_value;
	public int max_hr_value;
	public int burn_fat_mins;
	public int aerobic_mins;
	public int limit_mins;
	
	public float averageSpeed;//均速
	public float withSpeed;//配速
	
	public boolean isHaveSerial=true;//是否有序列号
	public int[] hr_data_vlaue;	//最大保存2小时,5s一个,最大1440
	public Map<Integer, int[]> hr;	//

	
	
	public SportData2() {
		super();
		// TODO Auto-generated constructor stub
	}



	public SportData2(long dId, long date, boolean isUpLoad, int year, int month, int day, int hour, int minute, int second, int data_length, int hr_data_interval_minute, int hr_item_count,
			int packet_count, int type, int step, int durations, int calories, int distance, int avg_hr_value, int max_hr_value, int burn_fat_mins, int aerobic_mins, int limit_mins,
			float averageSpeed, float withSpeed, boolean isHaveSerial, int[] hr_data_vlaue, Map<Integer, int[]> hr) {
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
		this.avg_hr_value = avg_hr_value;
		this.max_hr_value = max_hr_value;
		this.burn_fat_mins = burn_fat_mins;
		this.aerobic_mins = aerobic_mins;
		this.limit_mins = limit_mins;
		this.averageSpeed = averageSpeed;
		this.withSpeed = withSpeed;
		this.isHaveSerial = isHaveSerial;
		this.hr_data_vlaue = hr_data_vlaue;
		this.hr = hr;
	}



	public float getAverageSpeed() {
		return averageSpeed;
	}

	public void setAverageSpeed(float averageSpeed) {
		this.averageSpeed = averageSpeed;
	}

	public float getWithSpeed() {
		return withSpeed;
	}

	public void setWithSpeed(float withSpeed) {
		this.withSpeed = withSpeed;
	}

	public boolean isHaveSerial() {
		return isHaveSerial;
	}

	public void setHaveSerial(boolean isHaveSerial) {
		this.isHaveSerial = isHaveSerial;
	}

	public boolean isUpLoad() {
		return isUpLoad;
	}

	public void setUpLoad(boolean isUpLoad) {
		this.isUpLoad = isUpLoad;
	}

	public long getdId() {
		return dId;
	}

	public void setdId(long dId) {
		this.dId = dId;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) {
		this.hour = hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) {
		this.minute = minute;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) {
		this.second = second;
	}

	public int getData_length() {
		return data_length;
	}

	public void setData_length(int data_length) {
		this.data_length = data_length;
	}

	public int getHr_data_interval_minute() {
		return hr_data_interval_minute;
	}

	public void setHr_data_interval_minute(int hr_data_interval_minute) {
		this.hr_data_interval_minute = hr_data_interval_minute;
	}

	public int getHr_item_count() {
		return hr_item_count;
	}

	public void setHr_item_count(int hr_item_count) {
		this.hr_item_count = hr_item_count;
	}

	public int getPacket_count() {
		return packet_count;
	}

	public void setPacket_count(int packet_count) {
		this.packet_count = packet_count;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public int getDurations() {
		return durations;
	}

	public void setDurations(int durations) {
		this.durations = durations;
	}

	public int getCalories() {
		return calories;
	}

	public void setCalories(int calories) {
		this.calories = calories;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getAvg_hr_value() {
		return avg_hr_value;
	}

	public void setAvg_hr_value(int avg_hr_value) {
		this.avg_hr_value = avg_hr_value;
	}

	public int getMax_hr_value() {
		return max_hr_value;
	}

	public void setMax_hr_value(int max_hr_value) {
		this.max_hr_value = max_hr_value;
	}

	public int getBurn_fat_mins() {
		return burn_fat_mins;
	}

	public void setBurn_fat_mins(int burn_fat_mins) {
		this.burn_fat_mins = burn_fat_mins;
	}

	public int getAerobic_mins() {
		return aerobic_mins;
	}

	public void setAerobic_mins(int aerobic_mins) {
		this.aerobic_mins = aerobic_mins;
	}

	public int getLimit_mins() {
		return limit_mins;
	}

	public void setLimit_mins(int limit_mins) {
		this.limit_mins = limit_mins;
	}

	
	public int[] getHr_data_vlaue() {
		return hr_data_vlaue;
	}

	public void setHr_data_vlaue(int[] hr_data_vlaue) {
		this.hr_data_vlaue = hr_data_vlaue;
	}

	public Map<Integer, int[]> getHr() {
		return hr;
	}

	public void setHr(Map<Integer, int[]> hr) {
		this.hr = hr;
	}

	@Override
	public String toString() {
		return "SportData [dId=" + dId + ", date=" + date + ", isUpLoad=" + isUpLoad + ", year=" + year + ", month=" + month + ", day=" + day + ", hour=" + hour + ", minute=" + minute + ", second="
				+ second + ", data_length=" + data_length + ", hr_data_interval_minute=" + hr_data_interval_minute + ", hr_item_count=" + hr_item_count + ", packet_count=" + packet_count + ", type="
				+ type + ", step=" + step + ", durations=" + durations + ", calories=" + calories + ", distance=" + distance + ", avg_hr_value=" + avg_hr_value + ", max_hr_value=" + max_hr_value
				+ ", burn_fat_mins=" + burn_fat_mins + ", aerobic_mins=" + aerobic_mins + ", limit_mins=" + limit_mins + ", averageSpeed=" + averageSpeed + ", withSpeed=" + withSpeed
				+ ", isHaveSerial=" + isHaveSerial + ", hr_data_vlaue=" + Arrays.toString(hr_data_vlaue) + ", hr=" + hr + "]";
	}

}
