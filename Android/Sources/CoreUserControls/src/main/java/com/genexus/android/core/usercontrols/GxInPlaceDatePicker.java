package com.genexus.android.core.usercontrols;

import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.genexus.android.core.base.metadata.enums.DataTypes;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.controls.DateTimePicker;
import com.genexus.android.core.controls.GxTextView;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.ui.Coordinator;

@SuppressLint("ViewConstructor")
public class GxInPlaceDatePicker extends DateTimePicker implements IGxEdit {
	public static final String NAME = "SDCalendar";
	private boolean mShowDate= true;
	private boolean mShowTime= false;
	private Context mContext;
	private LayoutItemDefinition mDefinition;
	private Calendar currentValue;
	private String mInputType;
	private String mAttType;
	private int mLength;
	private int mDecimals;

	private boolean useMinDate = false;
	private long mMinDate;
	private boolean useMaxDate = false;
	private long mMaxDate;

	public GxInPlaceDatePicker(Context context, Coordinator coordinator, LayoutItemDefinition def) {
		super(context);
		mContext = context;
		// Check is system is set to use 24h time (this doesn't seem to work as expected though)
		final String timeS = android.provider.Settings.System.getString(mContext.getContentResolver(), android.provider.Settings.System.TIME_12_24);
		final boolean is24h = !(timeS == null || timeS.equals("12"));

		// Setup TimePicker
		setIs24HourView(is24h);

		setShowDate(mShowDate);
		setShowTime(mShowTime);

		// If set min and/or max date
		if (useMinDate || useMaxDate)
			setDateTimeValueRangeHelper();
		if (useMinDate)
			setMinDate(mMinDate);
		if (useMaxDate)
			setMaxDate(mMaxDate);

		if (currentValue!=null)
			setCurrentValue(currentValue);
		setLayoutDefinition(def);
		setCoordinator(coordinator, this);
	}

	private static Date calendarToDate(Calendar calendar)
	{
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	@Override
	public String getGxValue() {
		Calendar calendar = getCalendar();
		Date date = calendarToDate(calendar);
		if (DataTypes.isTime(mAttType, mLength))
			return Services.Strings.getDateTimeStringForServer(date, true, mDecimals==12);
		else if (mAttType.equals(DataTypes.DATE))
			return Services.Strings.getDateStringForServer(date);
		else
			return Services.Strings.getDateTimeStringForServer(date, false, mDecimals==12);
	}

	@Override
	public void setGxValue(String value) {
		if (value!=null && value.length()>0)
		{
			Date date ;
			if (DataTypes.isTime(mAttType, mLength))
				date = Services.Strings.getDateTime(value, true, mDecimals>=8, mDecimals==12);
			else if (mAttType.equals(DataTypes.DATE))
				date = Services.Strings.getDate(value);
			else if (mAttType.equals(DataTypes.DTIME) || mAttType.equals(DataTypes.DATETIME))
				date = Services.Strings.getDateTime(value, false, mDecimals>=8, mDecimals==12);
			else
				date = Services.Strings.getDate(value);
			if (date!=null)
			{
				Calendar calendarInstance = Calendar.getInstance();
				calendarInstance.setTime(date);
				currentValue = calendarInstance;
				setCurrentValue(currentValue);
			}
		}
	}

	@Override
	public String getGxTag() {
		return (String)this.getTag();
	}

	@Override
	public void setGxTag(String data) {
		this.setTag(data);
	}

	@Override
	public void setValueFromIntent(Intent data) {
	}

	@Override
	public IGxEdit getViewControl()
	{
		return new GxTextView(getContext(), mDefinition);
	}

	@Override
	public IGxEdit getEditControl() {
		return this;
	}

	private void setLayoutDefinition(LayoutItemDefinition layoutItemDefinition) {
		mDefinition = layoutItemDefinition;
		String attributeType = mDefinition.getDataTypeName().getDataType();
		String attributeInputType = mDefinition.getDataItem().getInputPicture();
		mLength = mDefinition.getDataItem().getLength();
		mDecimals = mDefinition.getDataItem().getDecimals();

		setInputType(attributeInputType);
		//Define if you a date, date/time or time
		setAttType(attributeType);
	}
	
	private void setInputType(String inputType)
	{
		mInputType = inputType;
	}

	private void setAttType(String attType)
	{
		mAttType = attType;
		if (mAttType.equals(DataTypes.DATE)){
			mShowDate = true;
			mShowTime = false;
		} else if (mAttType.equals(DataTypes.DTIME) || mAttType.equals(DataTypes.DATETIME)){
			mShowDate = true;
			mShowTime = true;
			if ( (Strings.hasValue(mInputType) && mInputType.length() <= 5)  || mLength==0 )// Special case, datetime with time picture.
				mShowDate = false;
		} else if (mAttType.equals(DataTypes.TIME))	{
			mShowDate = false;
			mShowTime = true;
		}
	}


	@Override
	public void setMinDate(long minDate) {
		useMinDate = true;
		mMinDate = minDate;
	}

	@Override
	public void setMaxDate(long maxDate) {
		useMaxDate = true;
		mMaxDate = maxDate;
	}

	@Override
	public boolean isEditable()
	{
		return isEnabled(); // Editable when enabled.
	}
}

