package com.genexus.android.core.ui.navigation.tabbed;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.genexus.android.R;

public class TabPlaceholderFragment extends Fragment
{
	private static final String KEY_TAB_INDEX = "tabIndex";

	static TabPlaceholderFragment newInstance(int tabIndex)
	{
		TabPlaceholderFragment f = new TabPlaceholderFragment();
		Bundle args = new Bundle();
		args.putInt(KEY_TAB_INDEX, tabIndex);
		
		f.setArguments(args);
		return f;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		FrameLayout view = new FrameLayout(inflater.getContext());
		view.setId(R.id.tab_navigation_tab_content);
		return view;
	}

	public void setContentFragment(Fragment fragment)
	{
		FragmentTransaction ft = getChildFragmentManager().beginTransaction();
		ft.replace(R.id.tab_navigation_tab_content, fragment);
		// Avoid illegal state exception: using commitAllowingStateLoss, anyway we recreate fragments later. using from tab replace content.
		ft.commitAllowingStateLoss();
	}
}
