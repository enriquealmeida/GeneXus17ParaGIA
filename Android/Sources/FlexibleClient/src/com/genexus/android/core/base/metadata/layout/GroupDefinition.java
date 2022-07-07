package com.genexus.android.core.base.metadata.layout;

public class GroupDefinition extends LayoutItemDefinition implements ILayoutContainer
{
	private static final long serialVersionUID = 1L;

	public GroupDefinition(LayoutDefinition layout, LayoutItemDefinition itemParent)
	{
		super(layout, itemParent);
	}

	@Override
	public TableDefinition getContent()
	{
		return (TableDefinition) getChildItems().get(0);
	}

	@Override
	public void calculateBounds(float absoulteWidth, float absoluteHeight)
	{
		getContent().calculateBounds(absoulteWidth, absoluteHeight);
	}
}
