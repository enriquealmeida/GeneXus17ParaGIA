package com.genexus.android.core.fragments

import android.content.Context
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.genexus.android.R
import com.genexus.android.ViewHierarchyVisitor
import com.genexus.android.animations.Transformations
import com.genexus.android.core.actions.ActionDefinitionWithHandlers
import com.genexus.android.core.actions.ActionExecution
import com.genexus.android.core.actions.ActionFactory
import com.genexus.android.core.actions.UIContext
import com.genexus.android.core.activities.SearchResultsActivity
import com.genexus.android.core.adapters.AdaptersHelper
import com.genexus.android.core.base.controls.IGxControlRuntime
import com.genexus.android.core.base.controls.IGxControlRuntimeContext
import com.genexus.android.core.base.metadata.ActionDefinition
import com.genexus.android.core.base.metadata.IDataSourceDefinition
import com.genexus.android.core.base.metadata.expressions.Expression
import com.genexus.android.core.base.metadata.layout.GridDefinition
import com.genexus.android.core.base.metadata.layout.Size
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition
import com.genexus.android.core.base.model.Entity
import com.genexus.android.core.base.model.EntityList
import com.genexus.android.core.base.services.Services
import com.genexus.android.core.base.utils.Strings
import com.genexus.android.core.common.ExecutionContext
import com.genexus.android.core.common.SecurityHelper
import com.genexus.android.core.controllers.IDataSourceBoundView
import com.genexus.android.core.controllers.IDataSourceController
import com.genexus.android.core.controllers.IDataViewController
import com.genexus.android.core.controllers.RefreshParameters
import com.genexus.android.core.controllers.ViewData
import com.genexus.android.core.controls.GxControlViewWrapper
import com.genexus.android.core.controls.GxImageViewStatic
import com.genexus.android.core.controls.GxListView
import com.genexus.android.core.controls.GxTextBlockTextView
import com.genexus.android.core.controls.IDataViewHosted
import com.genexus.android.core.controls.IGridView
import com.genexus.android.core.controls.IGridView.GridEventsListener
import com.genexus.android.core.controls.IGxGridControl
import com.genexus.android.core.controls.IGxThemeable
import com.genexus.android.core.controls.LoadingIndicatorView
import com.genexus.android.core.controls.grids.GridHelper
import com.genexus.android.core.controls.grids.IGridSite
import com.genexus.android.core.controls.grids.ISupportsEditableControls
import com.genexus.android.core.controls.grids.ISupportsMultipleSelection
import com.genexus.android.core.ui.Coordinator
import com.genexus.android.core.usercontrols.UcFactory
import com.genexus.android.core.utils.Cast
import com.genexus.android.core.utils.TaskRunner
import com.genexus.android.core.utils.ThemeUtils
import com.genexus.android.layout.GxLayoutInTab
import com.genexus.android.layout.GxTheme
import com.genexus.android.layout.LayoutTag

/**
 * Container for all controls that implement a Grid interface.
 * Handles paging and necessary plumbing so that IGridViews only need to have an update([data]) method.
 */
open class GridContainer(context: Context, private val coordinator: Coordinator, override val definition: GridDefinition) :
    FrameLayout(context),
    IGxGridControl,
    IDataViewHosted,
    IDataSourceBoundView,
    IGxThemeable,
    OnRefreshListener {

    companion object {
        private const val METHOD_REFRESH = "Refresh"
        private const val PULL_RELEASE_EVENT = "PullRelease"
    }

    private var _host: IDataView? = null
    private var controller: IDataSourceController? = null
    private val grid: IGridView?
    val gridView: View
    private val emptyDataSetImage: GxImageViewStatic
    private val emptyDataSetText: GxTextBlockTextView
    private val statusText: TextView
    private val loadingIndicator: LoadingIndicatorView
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var currentData: ViewData? = null
    private var needsMoreData = false
    private val securityToken: SecurityHelper.Token
    private var _themeClass: ThemeClassDefinition? = null
    private var selectAfterLoadParameters: List<Expression.Value>? = null
    private val controlWrapper: GxControlViewWrapper
    private var moveToTop = false

    override val name: String = definition.name
    override val dataSource: IDataSourceDefinition? = definition.dataSource
    override val dataSourceId: String = dataSource?.name ?: definition.name

    override val dataSourceMember: String get() {
        val member = definition.dataSourceMember
        if (member.isNotEmpty()) return member // SDT collection, for now.
        return if (!isFirstLevelGrid) name else ""
    }

    private val isFirstLevelGrid: Boolean
        get() {
            var definition = definition.parent
            while (definition != null) {
                if (definition is GridDefinition) return false
                definition = definition.parent
            }
            return true
        }

    override val dataSourceRowsPerPage = definition.rowsPerPage

    override fun setController(controller: IDataSourceController) {
        this.controller = controller
        needsMoreData = true
    }

    override val isActive: Boolean get() {
        if (!isShown) return false
        val parentTab = ViewHierarchyVisitor.getParent(GxLayoutInTab::class.java, this)
        return parentTab?.isActiveTab ?: true
    }

    fun setAbsoluteSize(size: Size?) {
        // setBounds() is currently done by LayoutFragmentAdapter. Override setLayoutParams if you want to keep size properties.
        // AdaptersHelper.setBounds(mDefinition.getItemTable(), size.getWidth(), size.getHeight(), getContext());
        if (Cast.`as`(IGridSite::class.java, grid) != null) (grid as IGridSite?)!!.setAbsoluteSize(size)
    }

    override fun onBeforeRefresh(params: RefreshParameters) {
        if (params.isSearchOrFilter || showLoadingInSearchPatternResults(params)) {
            // Replace the current data with the loading indicator.
            gridVisibility = GONE

            // restore loading indicator
            loadingIndicator.restoreLoadingView()
            // show loading indicator
            loadingIndicator.visibility = VISIBLE

            // Also remove the previous "searched for X" text, if any. It's no longer current.
            statusText.visibility = GONE
        }
        // if is a full refresh go to top
        if (!params.keepPosition)
            moveToTop = true
    }

    private fun showLoadingInSearchPatternResults(params: RefreshParameters): Boolean {
        if (params.reason == RefreshParameters.Reason.IMPLICIT) return false
        controller?.let { controller ->
            val activityController = controller.parent.parent
            if (activityController.activity is SearchResultsActivity) return true
        }
        return false
    }

    override fun update(data: ViewData) {
        swipeRefreshLayout?.let { it.isRefreshing = false }
        if (data.dataUnchanged) {
            // This is the same data that we already had. However, the visibility of inner views
            // might not be up to date, so we check that.
            currentData?.let { updateComponentsVisibility(it) }
            return
        }

        if (SecurityHelper.handleSecurityError(
                coordinator.uiContext,
                data.statusCode,
                data.statusMessage,
                securityToken
            ) != SecurityHelper.Handled.NOT_HANDLED
        ) return

        if (definition.showSelector != GridDefinition.ShowSelector.None)
            prepareForSelection(data)

        currentData = data
        needsMoreData = false // Reset before update(), since Grid may request extra data immediately after updating.
        data.moveToTop = moveToTop
        grid?.update(data)
        moveToTop = false

        // Update ListView with autogrow (does not support WRAP_CONTENT).
        if (definition.hasAutoGrow() && grid is GxListView && data.entities.size != 0) {
            val p = layoutParams
            p.height = grid.calculateAutoHeight()
            layoutParams = p
        }

        updateComponentsVisibility(data)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        if (gridVisibility == VISIBLE && selectAfterLoadParameters != null) {
            (grid as IGxControlRuntime).callMethod(GridHelper.METHOD_SELECT, selectAfterLoadParameters)
            selectAfterLoadParameters = null
        }
    }

    /**
     * Updates the visibility of each of the inner views (e.g. the grid itself, status text,
     * empty data set text, &c) depending on the data being displayed.
     */
    private fun updateComponentsVisibility(data: ViewData) {
        if (data.entities.size != 0) {
            // Make grid control visible if it wasn't.
            gridVisibility = VISIBLE
            emptyDataSetImage.visibility = GONE
            emptyDataSetText.visibility = GONE
        } else {
            // Make empty data set text (or image) visible (if applicable).
            var gridVisibility = VISIBLE

            if (definition.emptyDataSetText.isNotEmpty()) {
                gridVisibility = GONE
                emptyDataSetText.text = Services.Strings.attemptFromHtml(definition.emptyDataSetText)
                GxTheme.applyStyle(emptyDataSetText, definition.emptyDataSetTextClass)
                emptyDataSetText.visibility = VISIBLE
            }

            if (definition.emptyDataSetImage.isNotEmpty()) {
                gridVisibility = GONE
                Services.Images.displayImage(emptyDataSetImage, definition.emptyDataSetImage)
                GxTheme.applyStyle(emptyDataSetImage, definition.emptyDataSetImageClass)
                emptyDataSetImage.visibility = VISIBLE
            }

            this.gridVisibility = gridVisibility
        }

        // Update the "searched for, filtered by" status text.
        updateStatus(data)

        // Always remove loading indicator. Will be replaced either by Grid or empty view.
        // hide loading indicator.
        loadingIndicator.visibility = GONE
        // release loading View
        loadingIndicator.releaseLoadingView()
    }

    private var gridVisibility
        get() = gridView.visibility
        set(visibility) {
            gridView.visibility = visibility
            swipeRefreshLayout?.visibility = visibility
        }

    private fun updateStatus(data: ViewData) {
        val messages = ArrayList<String?>()

        // Update the "searched/filtered" indicator.
        val dataUri = data.uri
        if (dataUri != null) {
            // Search
            if (!dataUri.searchText.isNullOrEmpty()) messages.add(
                Services.Strings.getResource(R.string.GXM_DataSearched, dataUri.searchText)
            )

            // Filters
            if (dataUri.hasFilterValues()) {
                val filteredBy = ArrayList<String?>()
                for (filterAttribute in dataUri.dataSource.filter.attributes) {
                    if (dataUri.getFilter(filterAttribute) != null) filteredBy.add(filterAttribute.description)
                }
                messages.add(Services.Strings.getResource(R.string.GXM_DataFiltered, filteredBy.joinToString(", ")))
            }
        }
        statusText.text = messages.joinToString(Strings.SPACE)
        statusText.visibility = if (messages.size != 0) VISIBLE else GONE
    }

    override fun getData(): EntityList {
        return currentData?.entities ?: EntityList()
    }

    fun saveEditValues() {
        (grid as? ISupportsEditableControls)?.saveEditValues()
    }

    override var host: IDataView?
        get() {
            if (_host == null) {
                // This is ugly as hell.
                // Ideally the GridContainer should receive the host as a constructor parameter,
                // but that is very difficult when creating nested grids. So we either use the provided
                // one or try to get it from the view hierarchy.
                var parent = parent
                while (parent != null) {
                    if (parent is IDataViewHosted) {
                        _host = (parent as IDataViewHosted).host
                        break // Do not continue upwards, if previous hosted didn't have a host, no one else does either.
                    }
                    parent = parent.parent
                }
            }
            return _host
        }
        set(value) {
            _host = value
        }

    private val requestDataListener: GridEventsListener = object : GridEventsListener {
        override fun requestMoreData() {
            if (controller == null) return

            // Ignore new request if a previous one is pending.
            if (needsMoreData) return

            // Ignore requests for more data if last request caused a network error.
            if (currentData?.hasErrors() == true) return
            needsMoreData = true
            controller?.onRequestMoreData()
        }

        override fun runDefaultAction(context: UIContext, entity: Entity, postAction: Runnable?): Boolean {
            val hostController = hostController ?: return false
            if (hostController.handleSelection(entity)) {
                postAction?.run()
                return true
            }

            // Execute the default action for the Grid.
            if (definition.defaultAction == null) return false
            val action: ActionDefinition = ActionDefinitionWithHandlers(definition.defaultAction, null, postAction)
            return if (Thread.currentThread() !== Looper.getMainLooper().thread) {
                waitAndRunAction(context, action, entity)
            } else {
                TaskRunner.execute { waitAndRunAction(context, action, entity) }
                true
            }
        }

        private fun waitAndRunAction(context: UIContext, action: ActionDefinition, entity: Entity): Boolean {
            // wait for selection changed to process
            while (ActionExecution.isEventRunning(definition.name + ".SelectionChanged")) {
                try {
                    Thread.sleep(100)
                } catch (ignored: InterruptedException) {
                }
            }
            return runAction(context, action, entity)
        }

        @Suppress("NAME_SHADOWING")
        override fun runAction(uiContext: UIContext?, action: ActionDefinition, entity: Entity?): Boolean {
            val hostController = hostController ?: return false
            if (currentData == null) return false

            // Set current entity to evaluate expressions like '&Collection.CurrentItem.X'
            if (entity != null) currentData?.entities?.currentItem = entity

            var uiContext = uiContext
            var entity = entity
            if (_host != null) {
                // Ask host to move values to its entity (needed if variables from form are used in action).
                val hostEntity = host?.contextEntity

                // Context and entity may be null if executing an action that is NOT associated to a grid item.
                if (uiContext == null) uiContext = host?.uiContext
                if (entity == null) entity = hostEntity
            }
            hostController.runAction(uiContext, action, entity)
            return true
        }

        private val hostController: IDataViewController?
            get() {
                if (controller != null) return controller!!.parent
                val host: IDataView? = host
                if (host != null && host.controller != null) return host.controller
                Services.Log.warning("GridContainer has neither a specific controller nor an associated host with a controller.")
                return null
            }

        override fun getHostUIContext(): UIContext {
            return this@GridContainer.coordinator.uiContext
        }
    }

    override fun needsMoreData() = needsMoreData

    override fun getInterface(c: Class<*>): Any? {
        return if (c.isInstance(this)) c.cast(this) else null
    }

    override var themeClass: ThemeClassDefinition?
        get() = _themeClass
        set(themeClass) {
            _themeClass = themeClass
            applyClass(themeClass)
        }

    private fun prepareForSelection(data: ViewData) {
        // Notify items if a particular expression should be used to evaluate selection.
        val selectionExpression = definition.selectionExpression
        for (item in data.entities) {
            if (selectionExpression != null) item.selectionExpression = selectionExpression
        }
    }

    override fun setSelectionMode(enabled: Boolean, forAction: ActionDefinition?) {
        if (!enabled) currentData?.let {
            for (entity in it.entities)
                entity.setIsSelected(false)
        }

        if (grid is ISupportsMultipleSelection)
            grid.setSelectionMode(enabled, forAction)
        else
            Services.Log.warning("'${grid?.javaClass?.name}' does not support multiple selection.")

        // Selection cannot be fully disabled if working on "always on" selection mode.
        // So enabled=false clears selection, and we re-enable it afterwards.
        if (!enabled && definition.showSelector == GridDefinition.ShowSelector.Always)
            setSelectionMode(true, null)
    }

    override fun setFocus(showKeyboard: Boolean) {
        controlWrapper.setFocus(showKeyboard)
    }

    override var isVisible
        get() = controlWrapper.isVisible
        set(value) { controlWrapper.isVisible = value }

    override var caption
        get() = controlWrapper.caption
        set(value) { controlWrapper.caption = value }

    override fun setExecutionContext(context: ExecutionContext) {
        // Pass properties to the custom control if it supports them.
        if (grid is IGxControlRuntimeContext)
            grid.setExecutionContext(context)
    }

    override fun getPropertyValue(name: String): Expression.Value? {
        // Retrieve properties from custom control if it supports them.
        return if (grid is IGxControlRuntime) grid.getPropertyValue(name) else null
    }

    override fun setPropertyValue(name: String, value: Expression.Value?) {
        // Pass properties to the custom control if it supports them.
        if (grid is IGxControlRuntime) grid.setPropertyValue(name, value)
    }

    override fun callMethod(name: String, parameters: List<Expression.Value>): Expression.Value? {
        // Grid.Refresh() is a standard method, handle it here instead of passing it on to concrete grids.
        if (METHOD_REFRESH.equals(name, ignoreCase = true)) {
            refreshData(false)
        } else if (grid is IGxControlRuntime) {
            if (currentData == null && GridHelper.METHOD_SELECT.equals(name, ignoreCase = true)) {
                // Select call before the data has loaded, save it for later
                selectAfterLoadParameters = parameters
            } else {
                // Pass on the method to the custom grid.
                return grid.callMethod(name, parameters)
            }
        }
        return null
    }

    private fun refreshData(keepPosition: Boolean) {
        if (controller != null) {
            controller!!.parent.parent.onRefresh(controller, RefreshParameters(RefreshParameters.Reason.MANUAL, keepPosition))
        } else {
            AdaptersHelper.loadGrid(this, null, _host?.contextEntity, false)
        }
    }

    override fun applyClass(themeClass: ThemeClassDefinition?) {
        if (grid is IGxThemeable) {
            grid.themeClass = themeClass
            setTag(LayoutTag.CONTROL_THEME_CLASS, themeClass)
        }
        Transformations.apply(grid as View, themeClass)

        // initialize loading indicator.
        _themeClass?.let { loadingIndicator.setThemeClass(it.themeAnimationClass) }
    }

    // SwipeRefreshLayout.OnRefreshListener.onRefresh()
    override fun onRefresh() {
        val pullReleaseHasRefresh = pullReleaseHasRefresh()
        // Services.Log.debug("event has refresh " + pullReleaseHasRefresh);
        // if grid event PullRelease exits, fire this event
        val executeCustomEvent = if (pullReleaseHasRefresh) {
            // run pull release method
            coordinator.runControlEvent(this, PULL_RELEASE_EVENT)
        } else {
            // run method, and remove pull release indicator at the end.
            coordinator.runControlEvent(this, PULL_RELEASE_EVENT, null) { // remove swipe refresh at event end
                swipeRefreshLayout?.let {
                    Services.Device.runOnUiThread { it.isRefreshing = false }
                }
            }
        }
        // Services.Log.debug("event execute custom " + executeCustomEvent);
        if (!executeCustomEvent) {
            // if not run event, do only the default, refresh grid.
            refreshData(true)
        }
    }

    private fun pullReleaseHasRefresh(): Boolean {
        val action = coordinator.getControlEventHandler(this, PULL_RELEASE_EVENT)
        if (action != null) {
            val myAction = ActionFactory.getAction(coordinator.uiContext, action, null, action.isComposite)
            if (myAction.hasRefresh() || myAction.hasControlAction(name, "Refresh")) return true
        }
        return false
    }

    // implement enabled = false design property
    @Suppress("INAPPLICABLE_JVM_NAME")
    @get:JvmName("get-isEnabled")
    @set:JvmName("set-isEnabled")
    override var isEnabled
        get() = gridView.isEnabled
        set(value) { gridView.isEnabled = value }

    override val view get() = gridView

    init {
        setWillNotDraw(true)
        controlWrapper = GxControlViewWrapper(this)
        securityToken = SecurityHelper.Token()

        // Place for a generic message, such as filtering/search/error notifications.
        statusText = TextView(getContext())
        val statusTextLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT, Services.Device.dipsToPixels(48))
        statusTextLayoutParams.setMargins(Services.Device.dipsToPixels(8), 0, 0, 0)
        statusText.layoutParams = statusTextLayoutParams
        statusText.gravity = Gravity.CENTER_VERTICAL
        statusText.visibility = GONE
        addView(statusText)

        // Use factory to create underlying control (e.g. ListView, ImageGallery...)
        // NOTE: Coordinator is passed to grid control so that it can call events, but the grid is not registered
        // as a view for the Coordinator. Otherwise the gesture listener will mess up the default touch events.
        grid = UcFactory.createGrid(context, coordinator, definition)
        gridView = grid as View
        grid.addListener(requestDataListener)
        if (definition.hasAutoGrow() && definition.gridUserControlSupportAutoGrow()) {
            // set layout params for autogrow in android
            gridView.layoutParams =
                LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        } else if (definition.hasAutoGrow() || definition.dataSource == null || !definition.dataSource.orders.hasAnyWithAlphaIndexer()) {
            // Use MATCH_PARENT unless we have alpha indexer, that doesn't seem to work in that case.
            gridView.layoutParams =
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        }

        // if pull to refresh , add a SwipeRefreshLayout wrapper to grid and add this view to the layout.
        if (definition.hasPullToRefresh() && !definition.hasInverseLoad()) {
            swipeRefreshLayout = SwipeRefreshLayout(context).apply {
                addView(gridView)
                setOnRefreshListener(this@GridContainer)

                val refreshIndicatorColor = ThemeUtils.getAndroidThemeColorId(context, R.attr.colorAccent)
                if (refreshIndicatorColor != null)
                    setColorSchemeColors(refreshIndicatorColor)
            }
            addView(swipeRefreshLayout)
        } else {
            swipeRefreshLayout = null
            addView(gridView)
        }

        // Add indicator for "empty data set".
        emptyDataSetImage = GxImageViewStatic(context, null, null)
        emptyDataSetImage.visibility = GONE
        emptyDataSetImage.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(emptyDataSetImage)

        // Add indicator for "empty data set" as text.
        emptyDataSetText = GxTextBlockTextView(context)
        emptyDataSetText.visibility = GONE
        emptyDataSetText.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        emptyDataSetText.gravity = Gravity.CENTER
        addView(emptyDataSetText)

        // Add "loading" indicator.
        loadingIndicator = LoadingIndicatorView(getContext())
        loadingIndicator.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        addView(loadingIndicator)
        gridVisibility = GONE
        if (definition.showSelector == GridDefinition.ShowSelector.Always)
            setSelectionMode(true, null)
    }
}
