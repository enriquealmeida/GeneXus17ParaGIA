package com.genexus.android.layout;

import java.util.ArrayList;
import java.util.List;

import com.genexus.android.core.base.metadata.Properties;
import com.genexus.android.core.base.metadata.SectionDefinition;
import com.genexus.android.core.base.metadata.enums.LayoutItemsTypes;
import com.genexus.android.core.base.metadata.layout.ContentDefinition;
import com.genexus.android.core.base.metadata.layout.ILayoutVisitor;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.base.metadata.layout.TabItemDefinition;
import com.genexus.android.core.utils.Cast;

public class SectionsLayoutVisitor implements ILayoutVisitor
{
	private ArrayList<LayoutSection> mSections = new ArrayList<>();
	private boolean mHasAllSection = false;

	public SectionsLayoutVisitor() { }

	@Override
	public void enterVisitor(LayoutItemDefinition visitable) { }

	@Override
	public void visit(LayoutItemDefinition visitable)
	{
		// Collect all contents (presumably sections) in layout.
		if (visitable.getType().equalsIgnoreCase(LayoutItemsTypes.ONE_CONTENT))
		{
			ContentDefinition content = (ContentDefinition)visitable;
			if (content.getObject() instanceof SectionDefinition)
			{
				LayoutSection layoutSection = new LayoutSection(content);
				if (layoutSection != null)
					mSections.add(layoutSection);
			}
		}
		if (visitable.getType().equalsIgnoreCase(LayoutItemsTypes.ALL_CONTENT))
		{
			mHasAllSection = true;
		}
	}

	public List<LayoutSection> getInlineSections()
	{
		return getSectionsByDisplayType(Properties.ContentDisplayType.INLINE_CONTENT);
	}

	private List<LayoutSection> getSectionsByDisplayType(String displayType)
	{
		List<LayoutSection> sections = new ArrayList<>();

		for (LayoutSection section : mSections)
			if (displayType.equals(section.getDisplayType()))
				sections.add(section);

		return sections;
	}

	public boolean hasSections()
	{
		return (!mSections.isEmpty());
	}

	@Override
	public void leaveVisitor(LayoutItemDefinition visitable) { }

	// TODO: Remove this class when layout is drawn properly.
	public static class LayoutSection
	{
		private ContentDefinition mContent;
		private SectionDefinition mSection;
		private TabItemDefinition mInTab;

		public static List<LayoutSection> all(List<SectionDefinition> sections)
		{
			ArrayList<LayoutSection> result = new ArrayList<>();
			for (SectionDefinition section : sections)
				result.add(new LayoutSection(section));

			return result;
		}

		public LayoutSection(ContentDefinition content)
		{
			mContent = content;
			mSection = Cast.as(SectionDefinition.class, content.getObject());

			if (mSection == null)
				throw new IllegalArgumentException("Content is not associated to a section.");

			// See if the section is placed in a tab, to load appearance properties from there.
			TabItemDefinition parentTabItem = content.findParentOfType(TabItemDefinition.class);
			if (parentTabItem != null)
				mInTab = parentTabItem;
		}

		private LayoutSection(SectionDefinition section)
		{
			mSection = section;
		}

		public SectionDefinition getSection() { return mSection; }

		public String getDisplayType()
		{
			return (mContent != null ? mContent.getDisplayType() : Properties.ContentDisplayType.INLINE_CONTENT);
		}

		public String getCaption()
		{
			return (isInlineInTab() ? mInTab.getCaption() : mSection.getCaption());
		}

		public String getImage()
		{
			return (isInlineInTab() ? mInTab.getImage() : mSection.getImage());
		}

		public String getImageUnSelected()
		{
			return (isInlineInTab() ? mInTab.getImageUnselected() : mSection.getImageUnSelected());
		}
		
		private boolean isInlineInTab()
		{
			return (mInTab != null &&
					getDisplayType().equals(Properties.ContentDisplayType.INLINE_CONTENT));
		}
	}
}
