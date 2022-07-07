package com.genexus.android;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.genexus.android.layout.LayoutTag;
import com.genexus.android.core.base.metadata.layout.LayoutItemDefinition;
import com.genexus.android.core.utils.Cast;

public class ViewHierarchyVisitor
{
	/**
	 * Gets all the Views of type viewType present in the hierarchy starting at root.
	 * Hierarchy traversal is preorder so parents will always appear before children in result.
	 * @param viewType Type of views to locate.
	 * @param root View hierarchy.
	 */
	public static <ViewT> List<ViewT> getViews(Class<ViewT> viewType, View root)
	{
		List<ViewT> list = new ArrayList<>();

		if (viewType != null && root != null)
			visit(viewType, root, list);

		return list;
	}

	private static <ViewT> void visit(Class<ViewT> viewType, View view, List<ViewT> accum)
	{
		if (viewType.isInstance(view))
			accum.add(viewType.cast(view));

		for (View child : getViewWithChildren(view).getCustomViewChildren())
			visit(viewType, child, accum);
	}

	/**
	 * Gets the first View in the parent hierarchy that is of the desired type.
	 * @param viewType Type of view to locate.
	 * @param view Child to get parent from.
	 * @return The first view of the specified type that contains the supplied view directly or indirectly, otherwise null.
	 */
	public static <ViewT> ViewT getParent(Class<ViewT> viewType, View view)
	{
		if (view == null)
			return null;

		if (viewType.isInstance(view))
			return viewType.cast(view);

		View parent = Cast.as(View.class, view.getParent());
		return getParent(viewType, parent);
	}

	public static View findGenexusControl(@NonNull View rootView, @NonNull String controlName) {
		LayoutItemDefinition definition = (LayoutItemDefinition) rootView.getTag(LayoutTag.CONTROL_DEFINITION);

		if (definition != null && controlName.equalsIgnoreCase(definition.getName())) {
			return rootView;
		}

		for (View childView : getViewWithChildren(rootView).getCustomViewChildren()) {
			View result = findGenexusControl(childView, controlName);
			if (result != null) {
				return result;
			}
		}

		return null;
	}

	private static ICustomViewChildrenProvider getViewWithChildren(View view)
	{
		if (view instanceof ICustomViewChildrenProvider)
			return (ICustomViewChildrenProvider)view;

		return new BaseViewChildrenProvider(view);
	}

	public interface ICustomViewChildrenProvider
	{
		Collection<View> getCustomViewChildren();
	}

	private static class BaseViewChildrenProvider implements ICustomViewChildrenProvider
	{
		private final View mView;

		BaseViewChildrenProvider(View view)
		{
			mView = view;
		}

		@Override
		public Collection<View> getCustomViewChildren()
		{
			if (mView instanceof ViewGroup)
			{
				ViewGroup parent = (ViewGroup)mView;
				List<View> children = new ArrayList<>();
				for (int i = 0; i < parent.getChildCount(); i++)
					children.add(parent.getChildAt(i));

				return Collections.unmodifiableCollection(children);
			}
			else
				return Collections.emptyList();
		}
	}
}
