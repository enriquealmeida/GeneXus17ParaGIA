package com.genexus.android.core.controls;

import java.util.Date;

import android.widget.DatePicker;

public class GxDateTimeValueRangeHelper {

	private Date mMinDate;
	private Date mMaxDate;
	
	private int mYear;
	private int mMonthOfYear;
	private int mDayOfMonth;
	
	public int getYear() {
		return mYear;
	}

	public int getMonthOfYear() {
		return mMonthOfYear;
	}

	public int getDayOfMonth() {
		return mDayOfMonth;
	}
	
	// Set min Date
	public void setMinDate(long minDate) {
		mMinDate = new Date(minDate);
	}
	
	// Set max Date
	public void setMaxDate(long maxDate) {
		mMaxDate = new Date(maxDate);
	}
	
	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		boolean updateDate = false;
		mYear = year;
		mMonthOfYear = monthOfYear;
		mDayOfMonth = dayOfMonth;
		
		if (year > getYear(mMaxDate)) {
			mYear = getYear(mMaxDate);
			updateDate = true;
		}
		if (monthOfYear > getMonth(mMaxDate) && year == getYear(mMaxDate)) {
			mMonthOfYear = getMonth(mMaxDate);
			updateDate = true;
		}
		if (dayOfMonth > getDate(mMaxDate) && monthOfYear == getMonth(mMaxDate) && year == getYear(mMaxDate)) {
			mDayOfMonth = getDate(mMaxDate);
			updateDate = true;
		}
		
		if (year < getYear(mMinDate)) {
			mYear = getYear(mMinDate);
			updateDate = true;
		}
		if (monthOfYear < getMonth(mMinDate) && year == getYear(mMinDate)) {
			mMonthOfYear = getMonth(mMinDate);
			updateDate = true;
		}
		if (dayOfMonth < getDate(mMinDate) && monthOfYear == getMonth(mMinDate) && year == getYear(mMinDate)) {
			mDayOfMonth = getDate(mMinDate);
			updateDate = true;
		}
		
		if (updateDate)
			view.updateDate(mYear, mMonthOfYear, mDayOfMonth);
	}
	
	@SuppressWarnings("deprecation")
	private int getYear(Date date) {
		return date.getYear()+1900;
	}
	
	@SuppressWarnings("deprecation")
	private int getMonth(Date date) {
		return date.getMonth();
	}
	
	@SuppressWarnings("deprecation")
	private int getDate(Date date) {
		return date.getDate();
	}
}
