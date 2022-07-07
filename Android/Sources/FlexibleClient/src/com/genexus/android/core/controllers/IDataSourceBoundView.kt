package com.genexus.android.core.controllers

import com.genexus.android.core.base.metadata.IDataSourceDefinition

interface IDataSourceBoundView {
    val dataSourceId: String
    val dataSource: IDataSourceDefinition?
    val dataSourceMember: String?
    val dataSourceRowsPerPage: Int
    fun setController(controller: IDataSourceController)
    val isActive: Boolean
    fun update(data: ViewData)
    fun onBeforeRefresh(params: RefreshParameters)
    fun needsMoreData(): Boolean
}
