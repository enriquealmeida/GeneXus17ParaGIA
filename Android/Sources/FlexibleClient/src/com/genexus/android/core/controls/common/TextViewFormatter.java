package com.genexus.android.core.controls.common;

import android.widget.TextView;

import com.genexus.android.core.base.services.IValuesFormatter;
import com.genexus.android.core.utils.TaskRunner;

public class TextViewFormatter
{
	private final TextView mTextView;
	private final IValuesFormatter mValuesFormatter;

	public TextViewFormatter(TextView textView, IValuesFormatter valuesFormatter)
	{
		mTextView = textView;
		mValuesFormatter = valuesFormatter;
	}

	public void setText(String value)
	{
		if (mValuesFormatter != null)
		{
			if (mValuesFormatter.needsAsync())
				TaskRunner.execute(new BackgroundTask(value));
			else
				mTextView.setText(mValuesFormatter.format(value));
 		}
		else
			mTextView.setText(value);
	}

	private class BackgroundTask extends TaskRunner.BaseTask<CharSequence>
	{
		private final String mValue;

		public BackgroundTask(String value) {
			mValue = value;
		}

		@Override
		public CharSequence doInBackground()
		{
			return mValuesFormatter.format(mValue);
		}

		@Override
		public void onPostExecute(CharSequence result)
		{
			if (result != null)
				mTextView.setText(result);
		}
	}
}
