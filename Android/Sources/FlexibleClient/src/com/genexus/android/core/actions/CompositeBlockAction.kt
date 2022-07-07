package com.genexus.android.core.actions

import com.genexus.android.core.base.metadata.ActionDefinition

class CompositeBlockAction(context: UIContext?, definition: ActionDefinition?, parameters: ActionParameters?) : Action(context, definition, parameters) {
    private val actionBlock = ActionFactory.getInnerActionChildren(context, definition, parameters, true)

    override fun Do(): Boolean {
        ActionExecution(actionBlock).executeAction()
        return true
    }

    override fun getErrorMessage(): String {
        return "" // this is never used
    }

    override fun catchOnActivityResult(): Boolean {
        return true
    }

    companion object {
        @JvmStatic
        fun isAction(definition: ActionDefinition): Boolean {
            return "composite" == definition.optStringProperty(ActionHelper.STATEMENT_NAME)
        }
    }
}
