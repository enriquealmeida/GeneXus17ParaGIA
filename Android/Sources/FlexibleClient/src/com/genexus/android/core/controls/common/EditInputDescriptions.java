package com.genexus.android.core.controls.common;

import java.util.LinkedHashMap;

import com.genexus.android.core.base.controls.MappedValue;
import com.genexus.android.core.base.metadata.DataTypeDefinition;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.providers.IApplicationServer;
import com.genexus.android.core.base.services.IValuesFormatter;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.TaskRunner;

public class EditInputDescriptions extends EditInput
{
	private final LayoutItemDefinition mLayoutItem;
	private final Coordinator mCoordinator;

	private final IApplicationServer mServer;
	private final ControlServiceDefinition mGetValueFromDescriptionDefinition;
	private final ControlServiceDefinition mGetDescriptionFromValueDefinition;

	private String mValue;
	private String mText;
	private GetMappedValueTask mCurrentTask;

	public EditInputDescriptions(Coordinator coordinator, LayoutItemDefinition layoutItem)
	{
		if (!isInputTypeDescriptions(layoutItem))
			throw new IllegalArgumentException("LayoutItemDefinition does not have input type descriptions");

		mLayoutItem = layoutItem;
		mCoordinator = coordinator;
		mServer = Services.Application.getApplicationServer(coordinator.getUIContext().getConnectivitySupport());

		mGetValueFromDescriptionDefinition = new GetValueFromDescriptionService(layoutItem);
		mGetDescriptionFromValueDefinition = new GetDescriptionFromValueService(layoutItem);

		mValue = Strings.EMPTY;
		mText = Strings.EMPTY;
	}

	public static boolean isInputTypeDescriptions(LayoutItemDefinition layoutItem)
	{
		return (layoutItem != null &&
				layoutItem.getControlInfo() != null &&
				layoutItem.getControlInfo().optStringProperty("@InputType").equalsIgnoreCase("Descriptions"));
	}

	@Override
	public void setValue(String value, OnMappedAvailable onTextAvailable)
	{
		mValue = value;
		GetMappedValueTask task = new GetDescriptionFromValueTask(onTextAvailable, value);
		runTask(task);
	}

	@Override
	public void setText(String text, OnMappedAvailable onValueAvailable)
	{
		mText = text;
		GetMappedValueTask task = new GetValueFromDescriptionTask(onValueAvailable, text);
		runTask(task);
	}

	@Override
	public String getValue()
	{
		return mValue;
	}

	@Override
	public String getText()
	{
		return mText;
	}

	private void runTask(GetMappedValueTask task)
	{
		if (mCurrentTask != null)
		{
			mCurrentTask.cancel();
			mCurrentTask = null;
		}

		mCurrentTask = task;
		TaskRunner.execute(task);
	}

	private class GetValueFromDescriptionTask extends GetMappedValueTask
	{
		public GetValueFromDescriptionTask(OnMappedAvailable onValueAvailable, String value)
		{
			super(mGetValueFromDescriptionDefinition, onValueAvailable, value);
		}

		@Override
		public void onPostExecute(MappedValue result)
		{
			mValue = result.value;
			super.onPostExecute(result);
		}
	}

	private class GetDescriptionFromValueTask extends GetMappedValueTask
	{
		public GetDescriptionFromValueTask(OnMappedAvailable onValueAvailable, String value)
		{
			super(mGetDescriptionFromValueDefinition, onValueAvailable, value);
		}

		@Override
		public void onPostExecute(MappedValue result)
		{
			mText = result.value;
			super.onPostExecute(result);
		}
	}

	private abstract class GetMappedValueTask extends TaskRunner.BaseTask<MappedValue>
	{
		private final ControlServiceDefinition mService;
		private final OnMappedAvailable mOnResultAvailable;
		private final String mValue;

		public GetMappedValueTask(ControlServiceDefinition service, OnMappedAvailable onResultAvailable, String value)
		{
			mService = service;
			mOnResultAvailable = onResultAvailable;
			mValue = value;
		}

		@Override
		public MappedValue doInBackground()
		{
			if (Strings.hasValue(mValue))
			{
				// Note: the first value is always the value to be mapped.
				LinkedHashMap<String, String> inputValues = new LinkedHashMap<>();
				for (int i = 0; i < mService.ServiceInput.size(); i++)
				{
					String inputName = mService.ServiceInput.get(i);
					String inputValue = (i == 0 ? mValue : mCoordinator.getStringValue(inputName));
					inputValues.put(inputName, inputValue);
				}

				return mServer.getMappedValue(mService.Service, inputValues);
			}
			else
				return MappedValue.exact(Strings.EMPTY);
		}

		@Override
		public void onPostExecute(MappedValue result)
		{
			if (mOnResultAvailable != null)
				mOnResultAvailable.run(result);
		}
	}

	private static class GetValueFromDescriptionService extends ControlServiceDefinition
	{
		public GetValueFromDescriptionService(LayoutItemDefinition itemDefinition)
		{
			super(itemDefinition, "_hc");

			// Error in the metadata, the first parameter expects the attribute name.
			if (ServiceInput.size() != 0)
				ServiceInput.set(0, itemDefinition.getControlInfo().optStringProperty("@ControlItemDescription"));
		}
	}

	private static class GetDescriptionFromValueService extends ControlServiceDefinition
	{
		public GetDescriptionFromValueService(LayoutItemDefinition itemDefinition)
		{
			super(itemDefinition, "_hc_rev");
		}
	}

	@Override
	public boolean getSupportsAutocorrection()
	{
		return false;
	}

	@Override
	public Integer getEditLength()
	{
		String itemDescriptions = mLayoutItem.getControlInfo().optStringProperty("@ControlItemDescription");
		if (Strings.hasValue(itemDescriptions))
		{
			DataTypeDefinition itemDescriptionsDefinition = Services.Application.getDefinition().getAttribute(itemDescriptions);
			if (itemDescriptionsDefinition != null)
			{
				int length = itemDescriptionsDefinition.getLength();
				if (length != 0)
					return length;
			}
		}

		return null;
	}

	@Override
	public IValuesFormatter getValuesFormatter()
	{
		return new ValuesFormatter();
	}

	private class ValuesFormatter implements IValuesFormatter
	{
		@Override
		public boolean needsAsync()
		{
			return true;
		}

		@Override
		public CharSequence format(String value)
		{
			// This is called from background, so calling doInBackground() is ok.
			return new GetDescriptionFromValueTask(null, value).doInBackground().value;
		}
	}
}
