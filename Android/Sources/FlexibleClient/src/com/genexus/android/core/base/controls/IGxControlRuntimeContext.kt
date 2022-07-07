package com.genexus.android.core.base.controls

import com.genexus.android.core.common.ExecutionContext

/**
 * Interface for user controls that support runtime properties, methods, and events,
 * and need an execution context for each one.
 */
interface IGxControlRuntimeContext : IGxControlRuntime {
    fun setExecutionContext(context: ExecutionContext)
}
