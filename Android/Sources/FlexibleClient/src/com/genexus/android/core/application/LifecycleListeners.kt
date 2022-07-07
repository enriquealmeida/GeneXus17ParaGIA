package com.genexus.android.core.application

import android.app.Activity
import com.genexus.android.core.base.services.IApplication
import com.genexus.android.core.fragments.IDataView

class LifecycleListeners {
    interface MetadataLoading {
        fun onMetadataLoadFinished(application: IApplication)
    }

    interface Component {
        fun onComponentInitialized(component: IDataView?)
    }

    interface Application {
        fun onApplicationCreated(application: IApplication)
    }

    interface MiniApp {
        fun onMiniAppStarted()
        fun onMiniAppStopped()
    }

    /**
     * Interface used by the Application service to signal changes in foreground/background state.
     */
    interface Foreground {
        /**
         * Called when the application is brought to the foreground.
         * @param justStarted The activity that was just started.
         */
        fun onBecameForeground(justStarted: Activity?)

        /**
         * Called when the application is dropped to the background.
         * @param justStopped The activity that was just stopped.
         */
        fun onBecameBackground(justStopped: Activity?)
    }

    interface Dialog {
        fun onDialogStarted(dialog: android.app.Dialog)
        fun onDialogStopped(dialog: android.app.Dialog)
    }
}
