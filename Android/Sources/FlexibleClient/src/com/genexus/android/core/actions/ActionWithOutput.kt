package com.genexus.android.core.actions

import android.app.Activity
import com.genexus.android.core.base.application.OutputResult
import com.genexus.android.core.base.metadata.ActionDefinition

abstract class ActionWithOutput(context: UIContext, definition: ActionDefinition, parameters: ActionParameters) :
    Action(context, definition, parameters), IActionWithOutput {

    private var outputResult: OutputResult? = null

    override fun getActivity(): Activity? = super.getActivity() // IActionWithOutput needs it to be public

    override fun getOutput(): OutputResult? = outputResult

    protected fun setOutput(outputResult: OutputResult?) {
        this.outputResult = outputResult
    }

    override fun getErrorMessage(): String {
        return outputResult?.errorText ?: ""
    }
}
