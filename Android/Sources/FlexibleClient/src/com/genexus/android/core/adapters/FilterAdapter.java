package com.genexus.android.core.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.genexus.android.R;
import com.genexus.android.core.base.metadata.filter.FilterAttributeDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.common.FiltersHelper;
import com.genexus.android.core.controls.GxLinearLayout;
import com.genexus.android.core.controls.GxTextBlockTextView;
import com.genexus.android.core.resources.BuiltInResources;

import java.util.List;

public class FilterAdapter extends BaseAdapter
{
	private final LayoutInflater mInflater;
	private final Context mContext;
    private final List<FilterAttributeDefinition> mArrayFilter;
    private final List<String> mFilterRangeBegin;
    private final List<String> mFilterRangeEnd;

    private static class ViewHolder
    {
        TextView description;
        TextView filter;
        // ImageView icon;
    }

    public FilterAdapter(Context context, List<FilterAttributeDefinition> arrayFilter, List<String> filterRangeBegin, List<String>  filterRangeEnd)
    {
    	// Cache the LayoutInflate to avoid asking for a new one each time.
    	mInflater = LayoutInflater.from(context);
    	//Metadata
    	mArrayFilter = arrayFilter;
    	mFilterRangeBegin = filterRangeBegin;
    	mFilterRangeEnd = filterRangeEnd;

    	mContext = context;
    }

	@Override
	public int getCount() {
		return mArrayFilter.size();
	}

	@Override
	public Object getItem(int position) {
		return mArrayFilter.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		if (convertView == null)
		{
	    	convertView = mInflater.inflate(R.layout.filterrow , parent, false);

	    	holder = new ViewHolder();
	    	GxTextBlockTextView textDescription = convertView.findViewById(R.id.description );
	    	GxTextBlockTextView searchFilter = convertView.findViewById(R.id.searchfilter );
	    	textDescription.setGravity(Gravity.CENTER_VERTICAL);
	    	searchFilter.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
	    	holder.description= textDescription;
            holder.filter = searchFilter;
            holder.description.setGravity(Gravity.CENTER_VERTICAL);
    		holder.filter.setGravity(Gravity.CENTER_VERTICAL | Gravity.END);
            convertView.setTag(holder);

            ImageView icon = convertView.findViewById(R.id.icon);
            icon.setImageResource(BuiltInResources.getResource(mContext, R.drawable.gx_field_prompt_dark, R.drawable.gx_field_prompt_light, R.drawable.gx_field_prompt_light));

            GxLinearLayout linearLayoutFilterRow = convertView.findViewById(R.id.GxLinearLayoutFilterRow);
            FiltersHelper.setThemeFilters(null, linearLayoutFilterRow, searchFilter, textDescription, null, null, null);

		}
		else
			holder = (ViewHolder) convertView.getTag();

		FilterAttributeDefinition filterAtt = mArrayFilter.get(position);
		CharSequence stringFiter = filterAtt.getDescription();

		String strRangeBegin = mFilterRangeBegin.get(position);
		String strRangeEnd = mFilterRangeEnd.get(position);
        if (holder.description != null) {
        	holder.description.setText(stringFiter);
       	}
        if(holder.filter != null)
        {
        	String toView = strRangeBegin;
        	if (filterAtt.getType().equalsIgnoreCase(FilterAttributeDefinition.TYPE_STANDARD))
        	{
        		if (strRangeBegin.length() == 0)
        			toView = (String)mContext.getResources().getText(R.string.GX_AllItems);
        	}
        	else if (filterAtt.getType().equalsIgnoreCase(FilterAttributeDefinition.TYPE_RANGE))
        	{
        		if (isNull(strRangeBegin) && isNull(strRangeEnd))
        			toView = (String)mContext.getResources().getText(R.string.GX_AllItems);
        		else if (!isNull(strRangeBegin) && !isNull(strRangeEnd))
        			toView = String.format(Services.Strings.getResource(R.string.GXM_FilterRange), strRangeBegin, strRangeEnd);
        		else if (!isNull(strRangeBegin) && isNull(strRangeEnd))
	    			toView = String.format(Services.Strings.getResource(R.string.GXM_FilterRangeFrom), strRangeBegin);
	    		else if (isNull(strRangeBegin) && !isNull(strRangeEnd))
	    			toView = String.format(Services.Strings.getResource(R.string.GXM_FilterRangeTo), strRangeEnd);
        	}

    		holder.filter.setText(toView);
        }

		return convertView;
	}

	private static boolean isNull(String filterValue)
	{
		// Null or empty.
		if (!Services.Strings.hasValue(filterValue))
			return true;

		// Default value of the datatype.
		// TODO: This is a horrendous hack. The FilterAttributeDefinition should have the type, currently it does not.
		return filterValue.equals(Strings.ZERO);
	}
}
