package com.genexus.android.reporting.queryviewer;

import android.content.Context;

import com.genexus.android.core.framework.GenexusModule;
import com.genexus.android.core.usercontrols.UcFactory;
import com.genexus.android.core.usercontrols.UserControlDefinition;

public class QueryViewerModule implements GenexusModule {

    @Override
    public void initialize(Context context) {

        UserControlDefinition basicUserControl = new UserControlDefinition(
                QueryViewer.NAME,
                QueryViewer.class
        );
        UcFactory.addControl(basicUserControl);
    }
}
