package com.genexus.android.core.activities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genexus.android.R;
import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.base.controls.IGxEditFinishAware;
import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.metadata.RelationDefinition;
import com.genexus.android.core.base.metadata.StructureDefinition;
import com.genexus.android.core.base.metadata.WorkWithDefinition;
import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.metadata.enums.ControlTypes;
import com.genexus.android.core.base.metadata.enums.DisplayModes;
import com.genexus.android.core.base.metadata.filter.FilterAttributeDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.FiltersHelper;
import com.genexus.android.core.common.IntentHelper;
import com.genexus.android.core.common.PromptHelper;
import com.genexus.android.core.common.TrnHelper;
import com.genexus.android.core.controls.GxButton;
import com.genexus.android.core.controls.GxLinearLayout;
import com.genexus.android.core.controls.IGxEdit;
import com.genexus.android.core.utils.SparseArrayEx;

public class DetailFiltersActivity extends GxBaseActivity
{
	// Metadata - Filter attribute begin edited.
	private IDataSourceDefinition mFilteredDataSource;
	private FilterAttributeDefinition mFilterAttribute;
	private StructureDefinition mStructure;
	private boolean mIsRange;

	//UI Fixed Elements
	private LinearLayout mData;
	private GxLinearLayout mLinearLayout;
	private GxButton mOkButton;
	private GxButton mCancelButton;

	private final ArrayList<IGxEdit> mEditables = new ArrayList<>();

	private Entity mEntity;

	private String rangeBegin;
	private String rangeEnd;
	private String filterDefault;
	private String filterRangeFk;

	private final String prefixFrom = "From";
	private final String prefixTo = "To";
	private final String prefixCero = Strings.ZERO;

	private String [] filterRangeFkFrom;
	private String [] filterRangeFkTo;

	private LayoutItemDefinition mFormAttDef;
	private RelationDefinition mPickedRelation = null;

	private int mItemSelected = 0;
	private final HashMap<String, String> mEditablesFK = new LinkedHashMap<>();
	private Connectivity mConnectivity;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        ActivityHelper.initialize(this, savedInstanceState);

		setContentView(R.layout.detailfilter);

		// set support toolbar
		ActivityHelper.setSupportActionBarAndShadow(this);
		
		ActivityHelper.applyStyle(this, null);

		if (!initialize(getIntent()))
		{
			Services.Log.error("Insufficient information to initialize DetailsFilterActivity.");
			finish();
		}
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		ActivityHelper.onNewIntent(this, intent);
	}

	@Override
	protected void onStart()
	{
		super.onStart();
		ActivityHelper.onStart(this);
	}

	@Override
	protected void onResume()
	{
		super.onResume();
		ActivityHelper.onResume(this);
	}

	@Override
	protected void onPause()
	{
		ActivityHelper.onPause(this);
		super.onPause();
	}

	@Override
	protected void onStop()
	{
		ActivityHelper.onStop(this);
		super.onStop();
	}

	@Override
	protected void onDestroy()
	{
		ActivityHelper.onDestroy(this);
		super.onDestroy();
	}

	private boolean initialize(Intent intent)
	{
		mFilteredDataSource = IntentHelper.getObject(intent, IntentParameters.Filters.DATA_SOURCE, IDataSourceDefinition.class);
		if (mFilteredDataSource == null)
			return false;

		mConnectivity = Connectivity.fromIntent(intent);

		mStructure = mFilteredDataSource.getStructure();

		String attName = intent.getStringExtra(IntentParameters.ATT_NAME);
		mFilterAttribute = mFilteredDataSource.getFilter().getAttribute(attName);
		if (mFilterAttribute == null)
			return false;

		mIsRange = mFilterAttribute.getType().equalsIgnoreCase(FilterAttributeDefinition.TYPE_RANGE);

		//Fixed Controls
		mData = findViewById(R.id.LinearLayoutData);

		mLinearLayout = findViewById(R.id.LinearLayoutDetailfilter);
		mOkButton = findViewById(R.id.formOkButtonDetailFilters);
		mCancelButton = findViewById(R.id.formCancelButtonDetailFilters);

		rangeBegin = intent.getStringExtra(IntentParameters.RANGE_BEGIN);
		rangeEnd = intent.getStringExtra(IntentParameters.RANGE_END);

		filterDefault = intent.getStringExtra(IntentParameters.FILTER_DEFAULT);

		filterRangeFk = intent.getStringExtra(IntentParameters.FILTER_RANGE_FK);
		if (filterRangeFk != null) {
			String [] filterRangeFkSplit = Services.Strings.split(filterRangeFk, '&');
			filterRangeFkFrom = Services.Strings.split(filterRangeFkSplit[0], '=');
			filterRangeFkTo = Services.Strings.split(filterRangeFkSplit[1], '=');
		}

		setTitle(getText(R.string.GXM_Filter) + Strings.SPACE + mFilterAttribute.getDescription());
		DataItem attDef = FiltersHelper.getFilterDataItem(mFilteredDataSource, attName);

		mEntity = EntityFactory.newEntity(mStructure);
		if (mIsRange)
			createAdapterRange(attDef);
		else
			createAdapter(attDef);

		TrnHelper.setEnumCombosData(mEditables);

		if (mIsRange)
		{
			if (rangeBegin.length() > 0 || rangeEnd.length() > 0)
			{
				setControlValue(attName + prefixFrom, rangeBegin);
				setControlValue(attName + prefixTo, rangeEnd);
			}
			else
			{
				if (filterDefault.length() > 1)
				{
					String [] strSplitFilterDefault = filterDefault.split(Strings.AND, -1);
					setControlValue(attName + prefixFrom, strSplitFilterDefault[0]);
					setControlValue(attName + prefixTo, strSplitFilterDefault[1]);
				} else {
					LayoutItemDefinition formAttDef = FiltersHelper.getFormAttDef(attDef, mStructure);
					if (formAttDef.getControlType().equals(ControlTypes.NUMERIC_TEXT_BOX) && (mPickedRelation == null))
					{
						setControlValue(attName + prefixFrom, prefixCero);
						setControlValue(attName + prefixTo, prefixCero);
					}
				}
			}
		}
		else
		{
			if (rangeBegin.length() > 0)
				setControlValue(attName, rangeBegin);
			else
			{
				if (filterDefault.length() > 0)
					setControlValue(attName, filterDefault);
				else {
					LayoutItemDefinition formAttDef = FiltersHelper.getFormAttDef(attDef, mStructure);
					if (formAttDef.getControlType().equals(ControlTypes.NUMERIC_TEXT_BOX))
						setControlValue(attName, prefixCero);
				}
			}
		}

		// Update demo TextViews when the "OK" button is clicked
		findViewById(R.id.formOkButtonDetailFilters ).findViewById(R.id.formOkButtonDetailFilters).setOnClickListener(myOkClickListener);

		// Cancel the dialog when the "Cancel" button is clicked
		findViewById(R.id.formCancelButtonDetailFilters ).findViewById(R.id.formCancelButtonDetailFilters).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent resultIntent = new Intent();
				setResult(RESULT_CANCELED, resultIntent);
				finish();
			}
		});

		FiltersHelper.setButtonAttributes(mOkButton, mCancelButton, R.string.GXM_button_ok, R.string.GXM_cancel);
		FiltersHelper.setThemeFilters(mLinearLayout, null, null, null, null, mOkButton, mCancelButton);

		return true;
	}

	private UIContext getUIContext() {
		return UIContext.base(this, mConnectivity);
	}

	//Click event
	OnClickListener myOkClickListener = new OnClickListener() {
		@Override
		public void onClick(View v)
		{
			String attName = mFilterAttribute.getName();
			String rangeFk = Strings.EMPTY;

			//Need to call finishEditing so values are updated to what currently is on screen since onFocusChange has not been fired here
			finishEditing();

			if (mIsRange)
			{
				rangeBegin = getControlValue(attName + prefixFrom);
				rangeEnd = getControlValue(attName + prefixTo);
				if (mPickedRelation != null)
				{
					List<String> filterAttRange = new ArrayList<>();
					List<String> filterAttRangeValue = new ArrayList<>();
					filterAttRange.add(attName + prefixFrom);
					filterAttRange.add(attName + prefixTo);
					filterAttRangeValue.add(getControlFkId(attName + prefixFrom));
					filterAttRangeValue.add(getControlFkId(attName + prefixTo));

					rangeFk = FiltersHelper.makeGetFilterWithValue(filterAttRange, filterAttRangeValue);
				}
			}
			else
				rangeBegin = getControlValue(attName);

			Intent resultIntent = new Intent();
			resultIntent.putExtra(IntentParameters.ATT_NAME, attName);
			resultIntent.putExtra(IntentParameters.RANGE_BEGIN, rangeBegin);
			resultIntent.putExtra(IntentParameters.RANGE_END, rangeEnd);
			resultIntent.putExtra(IntentParameters.FILTER_RANGE_FK, rangeFk);
			setResult(RESULT_OK, resultIntent);
			finish();
		}
	};

	private void finishEditing() {
		for (int index = 0; index< mEditables.size(); index++) {
			IGxEdit gxEdit = mEditables.get(index);
			if (gxEdit instanceof IGxEditFinishAware) ((IGxEditFinishAware) gxEdit).finishEdit();
		}
	}

	private String getControlFkId(String name)
	{
		for (int index = 0; index< mEditables.size(); index++)
		{
			IGxEdit text = mEditables.get(index);
			if (text.getGxTag() != null && mEntity.getProperty(text.getGxTag())!=null)
			{
				if (mEntity.getProperty(name) != null) {
					return mEntity.getProperty(name).toString();
				} else {
					if (filterRangeFkFrom != null) {
						if (filterRangeFkFrom[0].equalsIgnoreCase(name))
							return filterRangeFkFrom[1];
						else if (filterRangeFkTo[0].equalsIgnoreCase(name))
							return filterRangeFkTo[1];
					} else {
						return Strings.EMPTY;
					}
				}
			}
		}
		return null;
	}

	private String getControlValue(String name)
	{
		for (int index = 0; index< mEditables.size(); index++)
		{
			IGxEdit text = mEditables.get(index);
			String nameControl = text.getGxTag();
			if (name.equalsIgnoreCase(nameControl))
			{
				return  text.getGxValue();
			}
		}
		return null;
	}

	private void setControlValue(String name, String value)
	{
		//mEntity.setProperty(name, value);
		for (int index = 0; index< mEditables.size(); index++)
		{
			IGxEdit text = mEditables.get(index);
			String nameControl = text.getGxTag();
			if (name.equalsIgnoreCase(nameControl))
			{
				text.setGxValue(value);
			}
		}
	}

	private void createAdapter(DataItem attDef) {
		LayoutItemDefinition formAttDef = FiltersHelper.getFormAttDef(attDef, mStructure);
		boolean readOnly = formAttDef.getDataItem().getReadOnly();
		formAttDef.getDataItem().setProperty("ReadOnly", String.valueOf(false));
		LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.itemeditform , null);
		TrnHelper.createEditRow(this, null, layout, mEntity, formAttDef, mEditables, null);
		setLabelText(layout);
		formAttDef.getDataItem().setProperty("ReadOnly", String.valueOf(readOnly));
		mItems.put(0, layout);
		addViews(mData);
	}

	private void createAdapterRange(DataItem attDef) {
		//mFormAttDef = FiltersHelper.getFormAttDef(attDef, mStructure);
		mFormAttDef = FiltersHelper.getFormAttDef(attDef, mFilteredDataSource.getPattern().getBusinessComponent());

		LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.itemeditform , null);
		String formAttDefName = mFormAttDef.getDataId();

		boolean readOnly = mFormAttDef.getDataItem().getReadOnly();
		boolean isKey = mFormAttDef.getDataItem().isKey();
		mPickedRelation = mFormAttDef.getFK();
		if (mPickedRelation == null)
		{
			mFormAttDef.getDataItem().setProperty("ReadOnly", String.valueOf(false));
			mFormAttDef.getDataItem().setProperty("IsKey", String.valueOf(false));

			createEditRowRange(layout, formAttDefName, null, null);
		} else {
			mFormAttDef.getDataItem().setProperty("ReadOnly", String.valueOf(true));
			mFormAttDef.getDataItem().setProperty("IsKey", String.valueOf(true));

			createEditRowRange(layout, formAttDefName, mRelationActionHandlerFrom, mRelationActionHandlerTo);
		}
		mFormAttDef.getDataItem().setProperty("ReadOnly", String.valueOf(readOnly));
		mFormAttDef.getDataItem().setProperty("IsKey", String.valueOf(isKey));

		mItems.put(0, layout);
		addViews(mData);
	}

	private void createEditRowRange(LinearLayout layout, String formAttDefName, OnClickListener relationActionHandlerFrom, OnClickListener relationActionHandlerTo)
	{
		String fromCaption = Services.Strings.getResource(R.string.GXM_FilterRangeFrom, Strings.EMPTY);
		TrnHelper.createEditRowRange(this, null, layout,DisplayModes.INSERT, mEntity, mFormAttDef, mEditables, relationActionHandlerFrom, fromCaption);
		setNameLayoutItem(0, formAttDefName + prefixFrom);

		String toCaption = Services.Strings.getResource(R.string.GXM_FilterRangeTo, Strings.EMPTY);
		TrnHelper.createEditRowRange(this, null, layout,DisplayModes.INSERT, mEntity, mFormAttDef, mEditables, relationActionHandlerTo, toCaption);
		setNameLayoutItem(1, formAttDefName + prefixTo);
	}

	private final OnClickListener mRelationActionHandlerFrom = new OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			mItemSelected = 0;
			callFKforView(view);
		}
	};

	private final OnClickListener mRelationActionHandlerTo = new OnClickListener()
	{
		@Override
		public void onClick(View view)
		{
			mItemSelected = 1;
			callFKforView(view);
		}
	};

	private void callFKforView(View v)
	{
		if (mPickedRelation != null)
			PromptHelper.callPrompt(getUIContext(), mPickedRelation);
	}

	private void setNameLayoutItem(int index, String name)
	{
		IGxEdit text = mEditables.get(index);
		text.setGxTag(name);
	}

	private final SparseArrayEx<View> mItems = new SparseArrayEx<>();

	private void addViews(LinearLayout parent)
	{
		for (View v : mItems.values())
			parent.addView(v);
	}

	private void setLabelText(LinearLayout layout) {
		int count = layout.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = layout.getChildAt(i);
			if (child instanceof TextView) {
				((TextView) child).setText(mFilterAttribute.getDescription());
				break;
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK && requestCode == FiltersHelper.SELECT_FK) {
			if (data!=null) {
				String metaBCString = data.getStringExtra("MetaBCName");
				WorkWithDefinition meta = Services.Application.getDefinition().getWorkWithForBC(metaBCString);
				if (meta != null) {
					List<DataItem> keysList = meta.getBusinessComponent().Root.getKeys();
					String attName = Strings.EMPTY;

					for(int i = 0; i< keysList.size(); i++)
					{
						DataItem att = keysList.get(i);
						attName = FiltersHelper.calculateAttName(att.getName(), mPickedRelation);
						if (mItemSelected == 0)
							setValue(attName + prefixFrom, data.getStringExtra(att.getName()));
						else {
							if (mItemSelected == 1)
								setValue(attName + prefixTo, data.getStringExtra(att.getName()));
						}
					}

					if (meta.getBusinessComponent().Root.getDescriptionAttribute()!=null)
					{
						DataItem att = meta.getBusinessComponent().Root.getDescriptionAttribute();
						if (mItemSelected == 0)
							setFKValue(attName + prefixFrom, data.getStringExtra(att.getName()));
						else {
							if (mItemSelected == 1)
								setFKValue(attName + prefixTo, data.getStringExtra(att.getName()));
						}
					}
					dataToControls(mEntity);
				}
			}
		}
	}

	private void setValue(String name, String value) {
		mEntity.setProperty(name, value);
	}

	private void setFKValue(String name, String id) {
		mEditablesFK.put(name, id);
	}

	private void dataToControls(Entity entity)
	{
		if (entity == null) return;
		for (int index = 0; index< mEditables.size(); index++)
		{
			IGxEdit text = mEditables.get(index);
			if (text.getGxTag() != null && entity.getProperty(text.getGxTag())!=null)
			{
				text.setGxValue(mEditablesFK.get(text.getGxTag()));
			}
		}
	}
}
