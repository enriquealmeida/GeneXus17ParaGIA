package com.genexus.android.core.base.application;

import java.util.List;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityList;

public interface IBusinessComponent
{
	void initialize(Entity entity);
	boolean success();
	EntityList getMessages();
	OutputResult load(Entity entity, List<String> key);
	OutputResult save(Entity entity);
	OutputResult delete();
	OutputResult insert(Entity entity);
	OutputResult insertOrUpdate(Entity entity);
	OutputResult update(Entity entity);
	String getName();
}
