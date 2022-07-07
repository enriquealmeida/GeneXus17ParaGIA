package com.genexus.android.core.base.metadata.layout;

import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.serialization.INodeObject;

public class RowDefinition extends LayoutItemDefinition
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("checkstyle:MemberName")
	public final List<CellDefinition> Cells = new ArrayList<>();
	// The parent table of this Row, rows elements are allowed only on Tables.
	private final TableDefinition mTableDefinition;

	public RowDefinition(LayoutDefinition layout, LayoutItemDefinition itemParent)
	{
		super(layout, itemParent);
		mTableDefinition = (TableDefinition) itemParent;
	}
	
	public int getIndex() {
		return mTableDefinition.Rows.indexOf(this);
	}

	@Override
	public TableDefinition getParent()
	{
		return (TableDefinition)super.getParent();
	}

	@Override
	public void readData(INodeObject rowNode)
	{
		super.readData(rowNode);
		String height = rowNode.optString("@rowHeight");
		mTableDefinition.Rows.add(this);
	}

	public int getEndY()
	{
		int y = 0;
		for (CellDefinition cell : Cells)
		{
			y = cell.getAbsoluteY() + cell.getAbsoluteHeight();
			if (y != 0)
				break;
		}

		return y;
	}
}
