package com.genexus.android.core.controls;

import java.util.Calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.RelativeLayout;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.genexus.android.R;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.ui.Coordinator;

public class DateTimePicker extends RelativeLayout implements View.OnClickListener, OnDateChangedListener, OnTimeChangedListener {

	// DatePicker reference
	private DatePicker		datePicker;
	// TimePicker reference
	private TimePicker		timePicker;
	// Calendar reference
	private Calendar		mCalendar;
	// Form Coordinator
	private Coordinator 	mCoordinator;
	// Site for this control
	private IGxEdit 		mEdit;
	private long mTicksCurrentValue;
	// Indicates if the update of the control value is batch or not
	private boolean mBatchUpdate = false;

	private boolean mShowDate = true;
	private boolean mShowTime = true;
	
	// Constructor start
	public DateTimePicker(Context context) {
		this(context, null);
	}

	public DateTimePicker(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public void setCoordinator(Coordinator coordinator, IGxEdit edit) {
		mCoordinator = coordinator;
		mEdit = edit;
	}

	@SuppressWarnings("deprecation")
	public DateTimePicker(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		// Get LayoutInflater instance
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// Inflate myself
		inflater.inflate(R.layout.datetimepicker, this, true);

		// Grab a Calendar instance
		mCalendar = Calendar.getInstance();

		// Init date picker
		datePicker = findViewById(R.id.DatePicker);
		datePicker.init(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), this);
		if (Services.Device.getScreenSmallestWidth() < 720)
			datePicker.setCalendarViewShown(false); // Doesn't fit well on phones or landscape 7" tablets.

		// Init time picker
		timePicker = findViewById(R.id.TimePicker);
		timePicker.setOnTimeChangedListener(this);

	}
	// Constructor end

	// Called every time the user changes DatePicker values
	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

		// Update date if use max and min date
		if (mDateTimeValueRangeHelper!=null) {
			mDateTimeValueRangeHelper.onDateChanged(view, year, monthOfYear, dayOfMonth);
			year = mDateTimeValueRangeHelper.getYear();
			monthOfYear = mDateTimeValueRangeHelper.getMonthOfYear();
			dayOfMonth = mDateTimeValueRangeHelper.getDayOfMonth();
		}

		// Update the internal Calendar instance
		mCalendar.set(year, monthOfYear, dayOfMonth, mCalendar.get(Calendar.HOUR_OF_DAY), mCalendar.get(Calendar.MINUTE));

		if (mCoordinator != null && mEdit != null && mCalendar.getTimeInMillis() != mTicksCurrentValue && !mBatchUpdate) {
			mCoordinator.onValueChanged(mEdit, true);
			mTicksCurrentValue = mCalendar.getTimeInMillis();
		}
	}

	// Called every time the user changes TimePicker values
	@Override
	public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
		// Update the internal Calendar instance
		mCalendar.set(mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute, 0);
	}

	// Convenience wrapper for internal Calendar instance
	public int get(final int field) {
		return mCalendar.get(field);
	}

	// Reset DatePicker, TimePicker and internal Calendar instance
	public void reset() {
		final Calendar c = Calendar.getInstance();
		updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		updateTime(c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE));
	}

	// Convenience wrapper for internal Calendar instance
	public long getDateTimeMillis() {
		return mCalendar.getTimeInMillis();
	}

	// Convenience wrapper for internal TimePicker instance
	@SuppressWarnings("deprecation")
	public void setIs24HourView(boolean is24HourView)
	{
		// Workaround for Android bug: setIs24HourView() changes 17:00 to 5:00 in 24-hour mode.
		// See: http://stackoverflow.com/questions/13662288/timepicker-hour-doesnt-update-after-switching-to-24h
		Integer prevHour = null;
		if (is24HourView)
			prevHour = timePicker.getCurrentHour();

		timePicker.setIs24HourView(is24HourView);

		if (prevHour != null)
			timePicker.setCurrentHour(prevHour);
	}

	// Convenience wrapper for internal TimePicker instance
	public boolean is24HourView() {
		return timePicker.is24HourView();
	}

	// Convenience wrapper for internal DatePicker instance
	public void updateDate(int year, int monthOfYear, int dayOfMonth) {
		datePicker.updateDate(year, monthOfYear, dayOfMonth);
	}

	// Convenience wrapper for internal TimePicker instance
	@SuppressWarnings("deprecation")
	public void updateTime(int currentHour, int currentMinute) {
		timePicker.setCurrentHour(currentHour);
		timePicker.setCurrentMinute(currentMinute);
	}

	public void setShowDate(boolean showDate)
	{
		mShowDate = showDate;
		datePicker.setVisibility(showDate ? VISIBLE : GONE);
	}

	public void setShowTime(boolean showTime)
	{
		mShowTime = showTime;
		timePicker.setVisibility(showTime ? VISIBLE : GONE);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	public Calendar getCalendar()
	{
		return mCalendar;
	}

	public void setCurrentValue(Calendar c) {
		mBatchUpdate  = true;
		updateDate(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		updateTime(c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE));
		mTicksCurrentValue = c.getTimeInMillis();
		mBatchUpdate = false;
	}

	// Helper to set the correct value to date if contain max and min Date
	private GxDateTimeValueRangeHelper mDateTimeValueRangeHelper = null;

	public void setDateTimeValueRangeHelper() {
		mDateTimeValueRangeHelper = new GxDateTimeValueRangeHelper();
	}

	// Set Min Date
	public void setMinDate(long minDate) {
		mDateTimeValueRangeHelper.setMinDate(minDate);
	}

	// Set Max Date
	public void setMaxDate(long maxDate) {
		mDateTimeValueRangeHelper.setMaxDate(maxDate);
	}

}
