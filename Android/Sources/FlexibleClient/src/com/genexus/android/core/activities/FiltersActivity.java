package com.genexus.android.core.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatRadioButton;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.genexus.android.R;
import com.genexus.android.core.actions.UIContext;
import com.genexus.android.core.adapters.FilterAdapter;
import com.genexus.android.core.base.metadata.DataItem;
import com.genexus.android.core.base.metadata.DomainDefinition;
import com.genexus.android.core.base.metadata.EnumValuesDefinition;
import com.genexus.android.core.base.metadata.IDataSourceDefinition;
import com.genexus.android.core.base.metadata.OrderDefinition;
import com.genexus.android.core.base.metadata.RelationDefinition;
import com.genexus.android.core.base.metadata.WorkWithDefinition;
import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.metadata.enums.ControlTypes;
import com.genexus.android.core.base.metadata.filter.FilterAttributeDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.providers.GxUri;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.ParametersStringUtil;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.FiltersHelper;
import com.genexus.android.core.common.IntentHelper;
import com.genexus.android.core.common.PromptHelper;
import com.genexus.android.core.controls.GxButton;
import com.genexus.android.core.controls.GxDateTimeEdit;
import com.genexus.android.core.controls.GxLinearLayout;
import com.genexus.android.core.controls.GxRadioGroupThemeable;
import com.genexus.android.core.fragments.IInspectableComponent;

public class FiltersActivity
	extends GxBaseActivity
	implements AdapterView.OnItemClickListener, GxDateTimeEdit.OnDateTimeChangedListener, IInspectableComponent
{
	// Data source to which the filters apply.
	private IDataSourceDefinition mFilteredDataSource;
	private int mDataSourceId;
	private GxUri mUri;

	private RelationDefinition mPickedRelation = null;
	private Connectivity mConnectivity;

	private LayoutItemDefinition mformAttDef;
	private FilterAdapter mAdapterFilter;
	private List<FilterAttributeDefinition> arrayFilter = null;
	private List<String> filterRangeBegin = null;
	private List<String> filterRangeEnd = null;
	private List<String> filterDefault = null;
	private List<String> filterFK = null;
	private List<String> selectedIndexFilter = null;
	private List<String> arrayOrder = null;

	// UI Fixed Elements
	private TextView mTextViewFilter;
	private TextView mTextViewOrder;

	private GxLinearLayout mGxLinearLayout;
	private GxLinearLayout mLayoutOrder;
	private GxButton mSearchButton;
	private GxButton mResetButton;

	private GxRadioGroupThemeable mRadioGroupOrder;

	private FilterAttributeDefinition attSelect;
	private int orderSelect;
	private List<CharSequence> listAttSelect = null;
	private Entity mEntity;

	private GxDateTimeEdit mDatepicker;

	private final String prefix0 = Strings.ZERO;
	private final String prefixTrue = Strings.ONE;
	private final String prefixFalse = "2";
	private final String prefixEmpty = Strings.EMPTY;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        ActivityHelper.initialize(this, savedInstanceState);

		setContentView(R.layout.filter);

		// set support toolbar
		ActivityHelper.setSupportActionBarAndShadow(this);

		ActivityHelper.applyStyle(this, null);

		Intent intent = getIntent();
		mConnectivity = Connectivity.fromIntent(intent);
		// Load definition of data source to be filtered.
		mFilteredDataSource = IntentHelper.getObject(intent, IntentParameters.Filters.DATA_SOURCE, IDataSourceDefinition.class);
		mDataSourceId = intent.getIntExtra(IntentParameters.Filters.DATA_SOURCE_ID, 0);
		mUri = IntentHelper.getObject(intent, IntentParameters.Filters.URI, GxUri.class);

        //Get FK Filters
        String filtersFK = intent.getStringExtra(IntentParameters.Filters.FILTERS_FK);

		// mStructure = mSection.getStructure();
		mEntity = EntityFactory.newEntity();

		ListView listViewFilters = findViewById(R.id.FiltersListView);
		listViewFilters.setItemsCanFocus(false);
		listViewFilters.setOnItemClickListener(this);

		mGxLinearLayout = findViewById(R.id.GxLinearLayoutFilter);
		mTextViewFilter = findViewById(R.id.textViewFilter);
		mTextViewOrder = findViewById(R.id.textViewOrder);
		mLayoutOrder = findViewById(R.id.layoutOrder);
		mRadioGroupOrder = findViewById(R.id.radioGroupOrder);

		setAttributes(listViewFilters, mUri, filtersFK);

		// Start search when the "Search" button is clicked
		mSearchButton = findViewById(R.id.formSearchButton);
		mSearchButton.setOnClickListener(mDoSearch);

		// Reset the filters when the "Reset" button is clicked
		mResetButton = findViewById(R.id.formResetButton);
		mResetButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				ListView listView = findViewById(R.id.FiltersListView);
				mUri.resetFilter();
				setAttributes(listView, mUri, prefixEmpty);
			}
		});

		FiltersHelper.setButtonAttributes(mSearchButton, mResetButton, R.string.GX_BtnSearch, R.string.GXM_Reset);
		// labels order att. should be GxTextBlockTextView to apply theme
		FiltersHelper.setThemeFilters(mGxLinearLayout, mLayoutOrder, null, null, mRadioGroupOrder, mSearchButton, mResetButton);
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

	private void setAttributes(ListView listViewFilters, GxUri uri, String filtersFK)
	{
		arrayFilter = new ArrayList<>();
		filterRangeBegin = new ArrayList<>();
		filterRangeEnd = new ArrayList<>();
		filterDefault = new ArrayList<>();
		filterFK = new ArrayList<>();
		selectedIndexFilter = new ArrayList<>();

		for (FilterAttributeDefinition filterAtt : mFilteredDataSource.getFilter().getAttributes())
		{
			arrayFilter.add(filterAtt);
			Object[] filterValues = uri.getFilter(filterAtt);
			if (filterValues != null)
			{
				String valueToFilter = prefixEmpty;
				String valueToFilterFrom = prefixEmpty;
				String valueToFilterTo = prefixEmpty;

				if (filterAtt.getType().equalsIgnoreCase(FilterAttributeDefinition.TYPE_STANDARD)) {
					valueToFilter = filterValues[0].toString();
				}
				else if (filterAtt.getType().equalsIgnoreCase(FilterAttributeDefinition.TYPE_RANGE)) {
					valueToFilterFrom = filterValues[0].toString();
					valueToFilterTo = filterValues[1].toString();
				}

				DataItem attDef = FiltersHelper.getFilterDataItem(mFilteredDataSource, filterAtt.getName());
				LayoutItemDefinition formAttDef = FiltersHelper.getFormAttDef(attDef, mFilteredDataSource.getStructure());
				if (formAttDef.getControlType().equals(ControlTypes.ENUM_COMBO)) {
					if (attDef.getIsEnumeration()) {
						DomainDefinition enumDefinition = Services.Application.getDefinition().getDomain(attDef.getDataTypeName().getDataType());

						List<EnumValuesDefinition> enumValuesDefinition = enumDefinition.getEnumValues();
						boolean findValue = false;
						int countValue = 0;
						for (EnumValuesDefinition enumValueDefinition: enumValuesDefinition) {
							if (enumValueDefinition.getValue().equalsIgnoreCase(valueToFilter)) {
								setAttribute(enumValueDefinition.getDescription(), prefixEmpty, filterAtt.getDefaultValue(), String.valueOf(countValue + 1));
								findValue = true;
								break;
							}
							countValue ++;
						}

						if (!findValue)
							setDefaultAttribute();
					}
				}
				else if (formAttDef.getControlType().equals(ControlTypes.CHECK_BOX))
				{
					setAttribute(valueToFilter, prefixEmpty, filterAtt.getDefaultValue(), (Services.Strings.parseBoolean(valueToFilter) ? prefixTrue : prefixFalse));
				}
				else if (formAttDef.getControlType().equals(ControlTypes.DATE_BOX))
				{
					String attributeInputType = formAttDef.getDataItem().getInputPicture();
					if (filterAtt.getType().equalsIgnoreCase(FilterAttributeDefinition.TYPE_STANDARD)) {
						setAttribute(Services.Strings.getDateString(Services.Strings.getDate(valueToFilter), attributeInputType), prefixEmpty, filterAtt.getDefaultValue(), valueToFilter);
					}
					else if (filterAtt.getType().equalsIgnoreCase(FilterAttributeDefinition.TYPE_RANGE)) {
						setAttribute(Services.Strings.getDateString(Services.Strings.getDate(valueToFilterFrom), attributeInputType),
									Services.Strings.getDateString(Services.Strings.getDate(valueToFilterTo), attributeInputType),
									filterAtt.getDefaultBeginValue() + Strings.AND + filterAtt.getDefaultEndValue(), valueToFilterFrom + "&" + valueToFilterTo);
					}
				}
				else
				{
					if (filterAtt.getType().equalsIgnoreCase(FilterAttributeDefinition.TYPE_STANDARD)) {
    	    			String valueToFilterFK = ParametersStringUtil.getValueFromFilter(filtersFK, filterAtt.getName());
	    	    		String valueFKV = null;
    	    			if (valueToFilterFK.length()>0) {
    	    				if (valueToFilter.length()>0 && !valueToFilterFK.equalsIgnoreCase(valueToFilter))
    	    				{
    	    					valueFKV = valueToFilter;
    	    				}
	    	    			valueToFilter = valueToFilterFK;
	    	    		}

						setAttribute(valueToFilter, prefixEmpty, filterAtt.getDefaultValue(), valueFKV, prefix0);
					}
					else if (filterAtt.getType().equalsIgnoreCase(FilterAttributeDefinition.TYPE_RANGE)) {
						String strValueToFilterFrom = ParametersStringUtil.getValueFromFilter(filtersFK, filterAtt.getName() + FiltersHelper.PREFIX_FROM);
						String strVvalueToFilterTo = ParametersStringUtil.getValueFromFilter(filtersFK, filterAtt.getName() + FiltersHelper.PREFIX_TO);
						if (strValueToFilterFrom.length() > 0 || strVvalueToFilterTo.length() > 0) {
							setAttribute(strValueToFilterFrom, strVvalueToFilterTo, filterAtt.getDefaultBeginValue() + Strings.AND + filterAtt.getDefaultEndValue(), prefix0);
							if (filterFK!=null) {
								int posAtt = filterFK.size() - 1;
								if (posAtt != -1) {
									List<String> filterAttRange = new ArrayList<>();
									List<String> filterAttRangeValue = new ArrayList<>();
									filterAttRange.add(filterAtt.getName() + FiltersHelper.PREFIX_FROM);
									filterAttRange.add(filterAtt.getName() + FiltersHelper.PREFIX_TO);
									filterAttRangeValue.add(valueToFilterFrom);
									filterAttRangeValue.add(valueToFilterTo);
									String valueToFk = FiltersHelper.makeGetFilterWithValue(filterAttRange, filterAttRangeValue);

									filterFK.remove(posAtt);
									filterFK.add(posAtt, valueToFk);
								}
							}
						} else {
							setAttribute(valueToFilterFrom, valueToFilterTo, filterAtt.getDefaultBeginValue() + Strings.AND + filterAtt.getDefaultEndValue(), prefix0);
						}
					}
				}
			}
			else
				setDefaultValues(filterAtt);
		}

		mAdapterFilter = new FilterAdapter(this, arrayFilter, filterRangeBegin, filterRangeEnd);
		listViewFilters.setAdapter(mAdapterFilter);
		mAdapterFilter.notifyDataSetChanged();

		setOrderAttribute(uri);
		setEmptyFilterAttribute();
	}

	private void setDefaultValues(FilterAttributeDefinition att)
	{
		DataItem attDef = FiltersHelper.getFilterDataItem(mFilteredDataSource, att.getName());
		LayoutItemDefinition formAttDef = FiltersHelper.getFormAttDef(attDef, mFilteredDataSource.getStructure());
		String valueToFilter = att.getDefaultValue();
		String valueToFilterFrom = att.getDefaultBeginValue();
		String valueToFilterTo = att.getDefaultEndValue();
		if (formAttDef.getControlType().equals(ControlTypes.ENUM_COMBO)) {
			if (attDef.getIsEnumeration()) {
				DomainDefinition enumDefinition = Services.Application.getDefinition().getDomain(attDef.getDataTypeName().getDataType());

				List<EnumValuesDefinition> enumValuesDefinition = enumDefinition.getEnumValues();
				boolean findValue = false;
				int countValue = 0;
				for (EnumValuesDefinition enumValueDefinition: enumValuesDefinition) {
					if (enumValueDefinition.getName().equalsIgnoreCase(valueToFilter) ||
						enumValueDefinition.getValue().equalsIgnoreCase(valueToFilter))
					{
						setAttribute(prefixEmpty, prefixEmpty, valueToFilter, String.valueOf(countValue + 1));
						findValue = true;
						break;
					}
					countValue ++;
				}
				if (!findValue){
					setDefaultAttribute();
				}
			}
		}
		else if (formAttDef.getControlType().equals(ControlTypes.CHECK_BOX))
		{
			setDefaultAttribute();
			int posVector = selectedIndexFilter.size() - 1;
			updateIndexFilter(posVector, (Services.Strings.parseBoolean(valueToFilter) ? prefixTrue : prefixFalse));
		}
		else if (formAttDef.getControlType().equals(ControlTypes.DATE_BOX))
		{
			String attributeInputType = formAttDef.getDataItem().getInputPicture();
			if (att.getType().equalsIgnoreCase(FilterAttributeDefinition.TYPE_STANDARD)){
				String dateToFilter = Services.Strings.getDateString(Services.Strings.getDate(valueToFilter), attributeInputType);
				if (dateToFilter.length() > 0){
					setAttribute(prefixEmpty, prefixEmpty, valueToFilter, valueToFilter);
				} else {
					setDefaultAttribute();
				}
			} else if (att.getType().equalsIgnoreCase(FilterAttributeDefinition.TYPE_RANGE)) {
				String dateToFilterFrom = Services.Strings.getDateString(Services.Strings.getDate(valueToFilterFrom), attributeInputType);
				String dateToFilterTo = Services.Strings.getDateString(Services.Strings.getDate(valueToFilterTo), attributeInputType);
				if (dateToFilterFrom.length() > 0 && dateToFilterTo.length() > 0){
					setAttribute(prefixEmpty, prefixEmpty, valueToFilterFrom + Strings.AND + valueToFilterTo, valueToFilterFrom + "&" + valueToFilterTo);
				} else {
					setDefaultAttribute();
				}
			}
		} else {
			if (att.getType().equalsIgnoreCase(FilterAttributeDefinition.TYPE_STANDARD)) {
				if (valueToFilter.length() > 0){
					setAttribute(prefixEmpty, prefixEmpty, valueToFilter, prefix0);
				} else {
					setDefaultAttribute();
				}
			} else if (att.getType().equalsIgnoreCase(FilterAttributeDefinition.TYPE_RANGE)) {
				String valueToFilterBegin = att.getDefaultBeginValue();
				String valueToFilterEnd = att.getDefaultEndValue();
				if (valueToFilterBegin.length() > 0 && valueToFilterEnd.length() > 0)
				{
					setAttribute(prefixEmpty, prefixEmpty, valueToFilterFrom + Strings.AND + valueToFilterTo, prefix0);
				} else {
					setDefaultAttribute();
				}
			}
		}
	}

	private void setDefaultAttribute()
	{
		setAttribute(prefixEmpty, prefixEmpty, prefixEmpty, prefix0);
	}

	private void setAttribute(String valueToFilterFrom, String valueToFilterTo, String valueDefault, String indexFilter)
	{
		setAttribute(valueToFilterFrom, valueToFilterTo, valueDefault, null, indexFilter);
	}

	private void setAttribute(String valueToFilterFrom, String valueToFilterTo, String valueDefault, String filterFKValue, String indexFilter) {
		filterRangeBegin.add(valueToFilterFrom);
		filterRangeEnd.add(valueToFilterTo);
		filterDefault.add(valueDefault);
		filterFK.add(filterFKValue);
		selectedIndexFilter.add(indexFilter);
	}

	private void setOrderAttribute(GxUri uri)
	{
		if (mFilteredDataSource.getOrders().size() > 1) {
			this.setTitle(getText(R.string.GXM_FilterAndOrder));
			mTextViewFilter.setVisibility(View.VISIBLE);
			mTextViewFilter.setText(getText( R.string.GXM_Filter));
			mTextViewOrder.setVisibility(View.VISIBLE);
			mTextViewOrder.setText(getText(R.string.GXM_Order));

			arrayOrder = new ArrayList<>();
			for (OrderDefinition order : mFilteredDataSource.getOrders())
				arrayOrder.add(order.getName());

			orderSelect = 0;
			if (uri.getOrder() != null)
				orderSelect = uri.getOrder().getId();

			//For orders
			createRadioButton();

		} else {
			orderSelect = 0;
			mLayoutOrder.setVisibility(View.GONE);
			mTextViewFilter.setVisibility(View.GONE);
			mTextViewOrder.setVisibility(View.GONE);
		}
	}

	private void createRadioButton() {
		CharSequence[] arrayChar = {};
		arrayChar = arrayOrder.toArray(arrayChar);
		final int size = arrayChar.length;
	    final RadioButton[] rb = new RadioButton[size];

	    // orderSelect
	    mRadioGroupOrder.removeAllViews();
	    for(int i=0; i<size; i++){
	        rb[i]  = new AppCompatRadioButton(this);
	        mRadioGroupOrder.addView(rb[i]); //the RadioButtons are added to the radioGroup instead of the layout
	        rb[i].setText(arrayChar[i]);
	        if (i == orderSelect )
	        	rb[i].setChecked(true);
	    }

	    //Set the attribute Label Theme for radio group
	    ThemeClassDefinition theme = Services.Themes.getThemeClass(FiltersHelper.THEME_LABEL);
	  	if (theme != null)
	  		mRadioGroupOrder.setThemeClass(theme);

	    mRadioGroupOrder.setOnCheckedChangeListener(mOnOrderCheckedChange);
	}

	private OnCheckedChangeListener mOnOrderCheckedChange = new OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			int selected = group.getCheckedRadioButtonId();
			if (selected!=-1)
			{
				RadioButton rb = findViewById(selected);
				if (rb!=null)
				{
					int idx = group.indexOfChild(rb);
					//String strOrderSelect = arrayOrder.get(idx);
					orderSelect = idx;
				}
			}
		}
	};

	private void setEmptyFilterAttribute()
	{
		if (mFilteredDataSource.getFilter().getAttributes().size() == 0) {
			this.setTitle(getText(R.string.GXM_Order));
			ListView listView = findViewById(R.id.FiltersListView);
			listView.setVisibility(View.GONE);
			mTextViewFilter.setVisibility(View.GONE);
			mTextViewOrder.setVisibility(View.GONE);
			mLayoutOrder.setGravity(Gravity.TOP);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Object item = arg0.getItemAtPosition(arg2);
		attSelect = (FilterAttributeDefinition) item;
		String attName = attSelect.getName();
		String type = attSelect.getType();
		String rangeBegin = filterRangeBegin.get(arg2);
		String rangeEnd = filterRangeEnd.get(arg2);
		String strFilterDefault = filterDefault.get(arg2);
		String strPosSelectedIndexFilter = selectedIndexFilter.get(arg2);
		String strFilterFk = filterFK.get(arg2);

		DataItem attDef = FiltersHelper.getFilterDataItem(mFilteredDataSource, attName);
		mformAttDef = FiltersHelper.getFormAttDef(attDef, mFilteredDataSource.getStructure());
		CharSequence[] arrayChar = {};

		if (mformAttDef.getControlType().equals(ControlTypes.ENUM_COMBO)) {
			listAttSelect = FiltersHelper.obtainAttributeDefinitionEnumCombo(mformAttDef, this);
			showAlertDialog(this, R.string.GXM_Filter, listAttSelect.toArray(arrayChar), Integer.parseInt(strPosSelectedIndexFilter), onDialogclick);
		} else if (mformAttDef.getControlType().equals(ControlTypes.CHECK_BOX)) {
			listAttSelect = FiltersHelper.obtainAttributeDefinitionCheckBox(mformAttDef, this);
			showAlertDialog(this, R.string.GXM_Filter, listAttSelect.toArray(arrayChar), Integer.parseInt(strPosSelectedIndexFilter), onDialogclick);
		} else if (mformAttDef.getControlType().equals(ControlTypes.DATE_BOX)) {
			if (type.equalsIgnoreCase(FilterAttributeDefinition.TYPE_STANDARD)) {
				mDatepicker = new GxDateTimeEdit(this, null, mformAttDef);
				String attributeType = mformAttDef.getDataTypeName().getDataType();
				String attributePicture = mformAttDef.getDataItem().getInputPicture();
				mDatepicker.setDataType(attributeType, attributePicture);
				//Define if you a date, date/time or time
				mDatepicker.setGxValue(strPosSelectedIndexFilter);
				mDatepicker.showDateTimeDialog(this);
			} else if (type.equalsIgnoreCase(FilterAttributeDefinition.TYPE_RANGE)) {
				if ((rangeBegin.length() > 0) || (rangeEnd.length() > 0)){
					String [] vals = Services.Strings.split(strPosSelectedIndexFilter, '&');
					rangeBegin = vals[0];
					rangeEnd = vals[1];
				}
				ActivityLauncher.callDetailFilters(getUIContext(), mFilteredDataSource, attName, rangeBegin, rangeEnd, strFilterDefault, strFilterFk);
			}
		} else {
			if (type.equalsIgnoreCase(FilterAttributeDefinition.TYPE_RANGE))
				ActivityLauncher.callDetailFilters(getUIContext(), mFilteredDataSource, attName, rangeBegin, rangeEnd, strFilterDefault, strFilterFk);
			else {
				if (callFKforView(attDef) == null) {
					ActivityLauncher.callDetailFilters(getUIContext(), mFilteredDataSource, attName, rangeBegin, rangeEnd, strFilterDefault, strFilterFk);
				}
			}
		}
	}

	private RelationDefinition callFKforView(DataItem attDef)
	{
		mformAttDef = FiltersHelper.getFormAttDef(attDef, mFilteredDataSource.getPattern().getBusinessComponent());
		mPickedRelation = mformAttDef.getFK();
		if (mPickedRelation != null)
			PromptHelper.callPrompt(getUIContext(), mPickedRelation);

		return mPickedRelation;
	}

	private UIContext getUIContext() {
		return UIContext.base(this, mConnectivity);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		int posAttAdv = arrayFilter.indexOf(attSelect);

		if (resultCode == Activity.RESULT_OK && requestCode == FiltersHelper.SELECT_FK)
		{
			if (data != null)
			{
				String metaBCString = data.getStringExtra("MetaBCName");
				WorkWithDefinition meta = Services.Application.getDefinition().getWorkWithForBC(metaBCString);
				if (meta != null)
				{
					List<DataItem> keysList = meta.getBusinessComponent().Root.getKeys();
					for(int i = 0; i< keysList.size(); i++)
					{
						DataItem att = keysList.get(i);
						String attName = FiltersHelper.calculateAttName(att.getName(), mPickedRelation);
						String selectValue = data.getStringExtra(att.getName());

						if (attSelect.getName().equalsIgnoreCase(attName))
						{
							filterFK.remove(posAttAdv);
							filterFK.add(posAttAdv, selectValue);
						}
						setValue(attName, selectValue);
					}

					if (meta.getBusinessComponent().Root.getDescriptionAttribute() != null)
					{
						DataItem att = meta.getBusinessComponent().Root.getDescriptionAttribute();
						String attName = FiltersHelper.calculateAttName(att.getName(), mPickedRelation);

						String selectValue = data.getStringExtra(att.getName());
						updateFilter(posAttAdv, selectValue, selectValue);
						setValue(attName , data.getStringExtra(att.getName()));

						int posAttInf = 0;
						boolean foundAtt = false;
						for (int i = 0; i < arrayFilter.size(); i++)
						{
							if (attName.equalsIgnoreCase(arrayFilter.get(i).getName()))
							{
								foundAtt = true;
								posAttInf = i;
								break;
							}
						}

						if (foundAtt)
							updateFilter(posAttInf, selectValue, selectValue);
					}
				}
			}
			mAdapterFilter.notifyDataSetChanged();
		}
		else if(resultCode == Activity.RESULT_OK)
		{
			Bundle b = data.getExtras();

			String strAttSelectBegin = b.getString(IntentParameters.RANGE_BEGIN);
			String strAttSelectEnd =  b.getString(IntentParameters.RANGE_END);

			if (mformAttDef.getControlType().equals(ControlTypes.DATE_BOX)) {
				String attributeInputType = mformAttDef.getDataItem().getInputPicture();
				if (attSelect.getType().equalsIgnoreCase(FilterAttributeDefinition.TYPE_STANDARD)) {
					String dateAttSelect = Services.Strings.getDateString(Services.Strings.getDate(strAttSelectBegin), attributeInputType);
					updateFilterRangeBegin(posAttAdv, dateAttSelect);
				} else if (attSelect.getType().equalsIgnoreCase(FilterAttributeDefinition.TYPE_RANGE)) {
					String dateAttSelectBegin = Services.Strings.getDateString(Services.Strings.getDate(strAttSelectBegin), attributeInputType);
					String dateAttSelectEnd = Services.Strings.getDateString(Services.Strings.getDate(strAttSelectEnd), attributeInputType);
					updateFilterRangeBegin(posAttAdv, dateAttSelectBegin);
					updateFilterRangeEnd(posAttAdv, dateAttSelectEnd);
					updateIndexFilter(posAttAdv, strAttSelectBegin + Strings.AND + strAttSelectEnd);
				}
			} else {
				if (attSelect.getType().equalsIgnoreCase(FilterAttributeDefinition.TYPE_STANDARD)) {
					updateFilterRangeBegin(posAttAdv, strAttSelectBegin);
				} else if (attSelect.getType().equalsIgnoreCase(FilterAttributeDefinition.TYPE_RANGE)) {
					updateFilterRangeBegin(posAttAdv, strAttSelectBegin);
					updateFilterRangeEnd(posAttAdv, strAttSelectEnd);
					String strFilterRangeFk = b.getString(IntentParameters.FILTER_RANGE_FK);
					if (strFilterRangeFk != null && strFilterRangeFk.length() > 0) {
						//If att is FK and type is range
						filterFK.remove(posAttAdv);
						filterFK.add(posAttAdv, strFilterRangeFk);
					}
				}
			}
			mAdapterFilter.notifyDataSetChanged();
		}
	}

	private void setValue(String name, String value) {
		mEntity.setProperty(name, value);
	}

	private DialogInterface.OnClickListener onDialogclick = new DialogInterface.OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int item) {
			dialog.dismiss();
			//Call WW with filter
			//Change datauri?
			int posAttAdv;

			posAttAdv = arrayFilter.indexOf(attSelect);
			if (item==0) {
				updateFilterRangeBegin(posAttAdv, prefixEmpty);
			} else {
				updateFilterRangeBegin(posAttAdv, (String) listAttSelect.get(item));
			}
			mAdapterFilter.notifyDataSetChanged();

			updateIndexFilter(posAttAdv, Integer.toString(item));

		}
	};

	//Click event
	private final OnClickListener mDoSearch = new OnClickListener()
	{
		@Override
		public void onClick(View v) {
			// Clear and reload filter values.
			mUri.resetFilter();

			List<String> attToSend = new ArrayList<>();
			List<String> valuesFkToSend = new ArrayList<>();
			int positionVector = 0;
			for (FilterAttributeDefinition att : arrayFilter) {

				if ((filterRangeBegin.get(positionVector).length() > 0) || (filterRangeEnd.get(positionVector).length() > 0))
					setAttributeToSearch(attToSend, valuesFkToSend, positionVector, att);

				positionVector++;
			}
			String strToFkSearch = FiltersHelper.makeGetFilterWithValue(attToSend, valuesFkToSend);

			if (mFilteredDataSource.getOrders().size() > 1)
				mUri.setOrder(orderSelect);

			// Send response via loaded GxUri.
			Intent resultIntent = new Intent();
			IntentHelper.putObject(resultIntent, IntentParameters.Filters.URI, GxUri.class, mUri);
			resultIntent.putExtra(IntentParameters.Filters.FILTERS_FK, strToFkSearch);
			resultIntent.putExtra(IntentParameters.Filters.DATA_SOURCE_ID, mDataSourceId);
			setResult(RESULT_OK, resultIntent);
			finish();
		}

		private void setAttributeToSearch(List<String> attToSend, List<String> valuesFkToSend, int positionVector, FilterAttributeDefinition att) {
			String strValue = prefixEmpty;
			String strValueBegin = prefixEmpty;
			String strValueEnd = prefixEmpty;
			DataItem attDef = FiltersHelper.getFilterDataItem(mFilteredDataSource, att.getName());
			LayoutItemDefinition formAttDef = FiltersHelper.getFormAttDef(attDef, mFilteredDataSource.getStructure());

			if (formAttDef.getControlType().equals(ControlTypes.ENUM_COMBO)) {
				if (attDef.getIsEnumeration()) {
					DomainDefinition enumDefinition = Services.Application.getDefinition().getDomain(attDef.getDataTypeName().getDataType());

					List<EnumValuesDefinition> enumValuesDefinition = enumDefinition.getEnumValues();
					for (EnumValuesDefinition enumValueDefinition: enumValuesDefinition) {
						if (enumValueDefinition.getDescription().equalsIgnoreCase(filterRangeBegin.get(positionVector))) {
							strValue = enumValueDefinition.getValue();
							break;
						}
					}
				}
			} else if (formAttDef.getControlType().equals(ControlTypes.DATE_BOX)) {
				if (att.getType().equalsIgnoreCase(FilterAttributeDefinition.TYPE_STANDARD)) {
					strValue = selectedIndexFilter.get(positionVector);
				} else if (att.getType().equalsIgnoreCase(FilterAttributeDefinition.TYPE_RANGE)) {
					String [] vals = Services.Strings.split(selectedIndexFilter.get(positionVector), '&');
					strValueBegin = vals[0];
					strValueEnd = vals[1];
				}
			} else {
				if (att.getType().equalsIgnoreCase(FilterAttributeDefinition.TYPE_STANDARD)) {
					strValue = filterRangeBegin.get(positionVector);
				} else if (att.getType().equalsIgnoreCase(FilterAttributeDefinition.TYPE_RANGE)) {
					strValueBegin = filterRangeBegin.get(positionVector);
					strValueEnd = filterRangeEnd.get(positionVector);
				}
			}

			if (att.getType().equalsIgnoreCase(FilterAttributeDefinition.TYPE_STANDARD)) {
				//Check is FK
				if (filterFK.get(positionVector)!=null) {
					attToSend.add(att.getName());
					valuesFkToSend.add(filterRangeBegin.get(positionVector));
					mUri.setFilter(att.getName(), filterFK.get(positionVector));
				} else {
					mUri.setFilter(att.getName(), strValue);
				}
			} else if (att.getType().equalsIgnoreCase(FilterAttributeDefinition.TYPE_RANGE)) {
				//Check is FK
				if (filterFK.get(positionVector)!=null) {
					attToSend.add(att.getName() + FiltersHelper.PREFIX_FROM);
					attToSend.add(att.getName() + FiltersHelper.PREFIX_TO);
					String [] filterRangeFk = Services.Strings.split(filterFK.get(positionVector), '&');
					String [] filterRangeFkFrom = Services.Strings.split(filterRangeFk[0], '=');
					String [] filterRangeFkTo = Services.Strings.split(filterRangeFk[1], '=');

					valuesFkToSend.add(filterRangeBegin.get(positionVector));
					valuesFkToSend.add(filterRangeEnd.get(positionVector));

					if (filterRangeFkFrom.length == 1)
						//When not set From
						mUri.setFilter(att.getName(), prefixEmpty, filterRangeFkTo[1]);
					else if (filterRangeFkTo.length == 1)
						//When not set To
						mUri.setFilter(att.getName(), filterRangeFkFrom[1], prefixEmpty);
					else
						//When set From and To
						mUri.setFilter(att.getName(), filterRangeFkFrom[1], filterRangeFkTo[1]);

				} else {
					mUri.setFilter(att.getName(), strValueBegin, strValueEnd);
				}
			}
		}
	};


	private void showAlertDialog(Context context, int idText, CharSequence[] items, int checkedItem, DialogInterface.OnClickListener listener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getResources().getText(idText));
		builder.setSingleChoiceItems(items, checkedItem, listener);
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public void onDateTimeChanged(String value, String text)
	{
		int posAttAdv = arrayFilter.indexOf(attSelect);
		updateFilter(posAttAdv, text, value);
		mAdapterFilter.notifyDataSetChanged();
	}

	private void updateFilter(int posAttribute, String attributeSelect, String attibuteIndex) {
		updateFilterRangeBegin(posAttribute, attributeSelect);
		updateIndexFilter(posAttribute, attibuteIndex);
	}

	private void updateFilterRangeBegin(int posAttribute, String attributeSelect) {
		filterRangeBegin.remove(posAttribute);
		filterRangeBegin.add(posAttribute, attributeSelect);
	}

	private void updateFilterRangeEnd(int posAttribute, String attributeSelect) {
		filterRangeEnd.remove(posAttribute);
		filterRangeEnd.add(posAttribute, attributeSelect);
	}

	private void updateIndexFilter(int posAttribute, String attibuteIndex) {
		selectedIndexFilter.remove(posAttribute);
		selectedIndexFilter.add(posAttribute, attibuteIndex);
	}

	@Override
	public View getRootView() {
		return findViewById(android.R.id.content);
	}
}
