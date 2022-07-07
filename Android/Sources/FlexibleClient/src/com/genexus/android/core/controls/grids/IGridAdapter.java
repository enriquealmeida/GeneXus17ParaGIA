package com.genexus.android.core.controls.grids;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.controllers.ViewData;

public interface IGridAdapter
{
	ViewData getData();
	Entity getEntity(int position);
}
