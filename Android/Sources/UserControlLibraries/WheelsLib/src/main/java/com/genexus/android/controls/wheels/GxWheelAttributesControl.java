package com.genexus.android.controls.wheels;

import android.util.Pair;

import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.metadata.layout.ControlInfo;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.common.DynamicValueItems;
import com.genexus.android.core.controls.common.ValueItem;
import com.genexus.android.core.controls.common.ValueItems;
import com.genexus.android.core.ui.Coordinator;
import com.genexus.android.core.utils.TaskRunner;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

public class GxWheelAttributesControl extends GxWheelEnumControl {
    private String mServiceName;
    private String mServiceInputs;
    private Coordinator mCoordinator;
    private Boolean mLoading = false;

    public GxWheelAttributesControl(Coordinator coordinator, ControlInfo info) {
        mServiceName = info.optStringProperty("@service");
        mServiceInputs = info.optStringProperty("@serviceInputs");
        mCoordinator = coordinator;
        // do the loadWheelItems() in onFirstSetGxValue() else the parameters to dataproviders aren't available
    }

    private class LoadDataTask extends TaskRunner.BaseTask<ValueItems<ValueItem>> {
        @Override
        public ValueItems<ValueItem> doInBackground() {
            Connectivity connectivity = mCoordinator.getUIContext().getConnectivitySupport();
            LinkedHashMap<String, String> conditionValues = null;
            if (mServiceInputs != null && mServiceInputs.length() > 0) {
                conditionValues = new LinkedHashMap<>();
                for (String inputMember : mServiceInputs.split(",", -1))
                    conditionValues.put(inputMember, mCoordinator.getStringValue(inputMember));
            }
            List<Pair<String, String>> items = Services.Application.getApplicationServer(connectivity).getDynamicComboValues(mServiceName, conditionValues);
            mLoading = false; // If an update is required from here on do it
            return new DynamicValueItems(items);
        }

        @Override
        public void onPostExecute(ValueItems<ValueItem> result) {
            load(result);
        }
    }

    private void loadWheelItems() {
        if (!mLoading) {
            mLoading = true;
            TaskRunner.execute(new LoadDataTask());
        }
    }

    @Override
    public void onFirstSetGxValue() {
        loadWheelItems();
    }

    @Override
    public List<String> getDependencies() {
        if (mServiceInputs != null && mServiceInputs.length() > 0) {
            return Arrays.asList(mServiceInputs.split(","));
        }
        else {
            return super.getDependencies();
        }
    }

    @Override
    public void onDependencyValueChanged(String name, Object value) {
        loadWheelItems();
    }

    @Override
    public void onRefresh() {
        loadWheelItems();
    }
}
