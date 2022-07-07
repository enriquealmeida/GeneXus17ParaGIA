package com.genexus.android.testing;

import com.genexus.android.ContextImpl;
import com.genexus.android.core.application.MyApplication;
import com.genexus.android.core.base.metadata.GenexusApplication;
import com.artech.base.services.AndroidContext;
import com.genexus.android.core.providers.EntityDataProvider;
import com.genexus.android.core.services.EntityService;

public class MyTestApplication extends MyApplication {

    @Override
    public final void onCreate() {
        GenexusApplication app = new GenexusApplication();
        app.setName("MyTestApplication");
        app.setAppEntry("MyTestEntry");
        setApp(app);
        AndroidContext.ApplicationContext = new ContextImpl(getApplicationContext());
        super.onCreate();
    }

    @Override
    public Class<? extends EntityService> getEntityServiceClass() {
        return null;
    }

    @Override
    public EntityDataProvider getProvider() {
        return null;
    }
}
