package com.genexus.android.core.controls

import com.genexus.android.core.base.metadata.ActionDefinition
import com.genexus.android.core.base.metadata.layout.GridDefinition
import com.genexus.android.core.base.model.EntityList

interface IGxGridControl : IGxControl {
    /**
     * Gets the definition of the grid.
     */
    override val definition: GridDefinition

    /**
     * Gets the list of entities currently displayed in the grid.
     */
    fun getData(): EntityList

    /**
     * Starts or ends selection mode for the grid.
     * When this mode is ended, selection is cleared.
     * @param forAction Starts mode for a specific action (can be null).
     */
    fun setSelectionMode(enabled: Boolean, forAction: ActionDefinition?)
}
