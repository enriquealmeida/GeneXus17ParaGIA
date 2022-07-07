package com.genexus.android.core.controls;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.genexus.android.R;
import com.genexus.android.core.activities.ActivityLauncher;
import com.genexus.android.core.base.metadata.IDataViewDefinition;
import com.genexus.android.core.base.metadata.enums.RequestCodes;
import com.genexus.android.core.base.metadata.layout.ContentDefinition;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.fragments.LayoutFragment;
import com.genexus.android.core.resources.StandardImages;

public class GxSectionLink extends GxLinearLayout
{
	private ContentDefinition mDefinition;

	public GxSectionLink(Context context)
	{
		super(context);
	}

	public GxSectionLink(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public void setDefinition(ContentDefinition content)
	{
		mDefinition = content;

		TextView title = findViewById(R.id.listViewTitle);
		TextView description= findViewById(R.id.listViewDescription);
		LinearLayout actions= findViewById(R.id.listViewActionList);
		LinearLayout listViewTexts = findViewById(R.id.listViewTexts);

		if (content.getObject() != null)
		{
			title.setText(content.getObject().getCaption());

			title.setVisibility(View.VISIBLE);
			description.setVisibility(View.VISIBLE);
			actions.setVisibility(View.VISIBLE);
			listViewTexts.setVisibility(View.VISIBLE);

			description.setText(Strings.EMPTY);

			actions.removeAllViews();
    		ImageView linkImage = new ImageView(getContext());
			StandardImages.setLinkImage(linkImage);
    		actions.addView(linkImage);
		}
	}

	public void callDataView(LayoutFragment from)
	{
		if (mDefinition != null && mDefinition.getObject() instanceof IDataViewDefinition)
		{
			// Get parameters from caller fragment (by default same as the fragment parameters).
       		ActivityLauncher.callForResult(from.getUIContext(), (IDataViewDefinition)mDefinition.getObject(), from.getInnerDataViewParameters(mDefinition), RequestCodes.ACTION, false);
		}
    }
}
