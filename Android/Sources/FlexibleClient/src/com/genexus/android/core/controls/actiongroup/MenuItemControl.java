package com.genexus.android.core.controls.actiongroup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.genexus.android.core.base.metadata.expressions.Expression.Value;
import com.genexus.android.core.base.metadata.layout.ActionGroupItemDefinition;
import com.genexus.android.core.base.metadata.theme.LayoutBoxMeasures;
import com.genexus.android.core.base.metadata.theme.ThemeClassDefinition;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;
import com.genexus.android.core.utils.BadgeDrawable;
import com.genexus.android.core.utils.ThemeUtils;

public class MenuItemControl extends ActionGroupBaseItemControl<MenuItemControl>
{
	private MenuItem mMenuItem;
	private final int mMenuId;
	private OnRequestLayoutListener mOnRequestLayoutListener;

	public MenuItemControl(ActionGroupItemDefinition definition, int menuId)
	{
		super(definition);
		mMenuId = menuId;
	}

	public int getMenuId()
	{
		return mMenuId;
	}

	public void setMenuItem(MenuItem menuItem)
	{
		mMenuItem = menuItem;
		applyControlClass(getThemeClass(), getCaption());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
			setContentDescription(getName());
	}

	public interface OnRequestLayoutListener
	{
		void onRequestLayout(MenuItemControl item);
	}

	public void setOnRequestLayoutListener(OnRequestLayoutListener listener)
	{
		mOnRequestLayoutListener = listener;
	}

	@Override
	public void requestLayout()
	{
		super.requestLayout();
		if (mOnRequestLayoutListener != null)
			mOnRequestLayoutListener.onRequestLayout(this);
	}

	@Override
	public void setEnabled(boolean enabled)
	{
		super.setEnabled(enabled);
		if (mMenuItem != null)
			mMenuItem.setEnabled(enabled);
	}

	@Override
	public void setVisible(boolean visible)
	{
		super.setVisible(visible);
		if (mMenuItem != null)
			mMenuItem.setVisible(visible);
	}

	@Override
	public void setCaption(String caption)
	{
		super.setCaption(caption);
		applyControlClass(getThemeClass(), getCaption());
	}

	private void setTitle(String title) {
		if (mMenuItem != null)
			mMenuItem.setTitle(title);
	}

	private void setTitle(SpannableString spanString) {
		if (mMenuItem != null)
			mMenuItem.setTitle(spanString);
	}

	private static final String PROPERTY_BADGETEXT = "BadgeText";
	private static final String BADGE_ANIMATION_PROPERTY = "zoom";
	private static final float BADGE_ANIMATION_ZOOM = 1.5f;
	private static final float BADGE_ANIMATION_NO_ZOOM = 1f;
	private String mBadgeText;
	private String mOldBadgeText;
	private ThemeClassDefinition mBadgeThemeClass;

	public void setIcon(Context context, MenuItem menuItem, Drawable icon) {

		if (mBadgeText != null && mBadgeText.trim().length() > 0) {
			BadgeDrawable badge = new BadgeDrawable(context);
			badge.setText(mBadgeText);
			badge.setThemeClass(mBadgeThemeClass);

			if (mBadgeThemeClass != null && mBadgeThemeClass.isAnimated()) {
				ValueAnimator zoomOut = ObjectAnimator.ofFloat(badge, BADGE_ANIMATION_PROPERTY, BADGE_ANIMATION_ZOOM, BADGE_ANIMATION_NO_ZOOM);
				zoomOut.setDuration(mBadgeThemeClass.getAnimationDuration());
				zoomOut.addUpdateListener(updatedAnimation -> badge.invalidateSelf());
				zoomOut.start();
			}

			LayerDrawable iconDraw = new LayerDrawable(new Drawable[] { icon, badge });
			menuItem.setIcon(iconDraw);
		}
		else if (mOldBadgeText != null && mOldBadgeText.trim().length() > 0 && mBadgeThemeClass != null && mBadgeThemeClass.isAnimated()) {
			BadgeDrawable badge = new BadgeDrawable(context);
			badge.setText(mOldBadgeText);
			badge.setThemeClass(mBadgeThemeClass);

			ValueAnimator zoomOut = ObjectAnimator.ofFloat(badge, BADGE_ANIMATION_PROPERTY, BADGE_ANIMATION_NO_ZOOM, 0f);
			zoomOut.setDuration(mBadgeThemeClass.getAnimationDuration());
			zoomOut.addUpdateListener(updatedAnimation -> badge.invalidateSelf());
			zoomOut.addListener(new AnimatorListenerAdapter() {
				@Override
				public void onAnimationEnd(Animator animation) {
					badge.setVisible(false, false);
				}
			});
			zoomOut.start();

			LayerDrawable iconDraw = new LayerDrawable(new Drawable[] { icon, badge });
			menuItem.setIcon(iconDraw);
		}
		else {
			menuItem.setIcon(icon);
		}
		mOldBadgeText = mBadgeText;
	}

	@Override
	public void setPropertyValue(String name, Value value) {
		if (name.equalsIgnoreCase(PROPERTY_BADGETEXT)) {
			if (value == null)
				mBadgeText = null;
			else
				mBadgeText = value.coerceToString();
		}
		else {
			super.setPropertyValue(name, value);
		}
	}

	@Override
	public Value getPropertyValue(String name) {
		if (name.equalsIgnoreCase(PROPERTY_BADGETEXT)) {
			return Value.newString(mBadgeText);
		}
		return super.getPropertyValue(name);
	}

	@RequiresApi(api = Build.VERSION_CODES.O)
	private void setContentDescription(String controlName) {
		if (mMenuItem != null)
			mMenuItem.setContentDescription(controlName);
	}

	@Override
	public void setThemeClass(ThemeClassDefinition themeClass) {
		super.setThemeClass(themeClass);
		if (themeClass == null)
			mBadgeThemeClass = null;
		else {
			mBadgeThemeClass = Services.Themes.getThemeClass(themeClass.getName() + "_Badge");
			applyControlClass(themeClass, getCaption());
		}
	}

	private void applyControlClass(ThemeClassDefinition themeClass, String caption) {
		if (mMenuItem != null) {
			if (themeClass != null) {
				SpannableString spanString = getTitleSpan(caption, themeClass);
				if (spanString != null) {
					setTitle(spanString);
					return;
				}
			}

			setTitle(caption);
		}
	}

	private @Nullable SpannableString getTitleSpan(String caption, ThemeClassDefinition themeClass) {
		Integer foregroundColorId = ThemeUtils.getColorId(themeClass.getColor());
		Integer backgroundColorId = ThemeUtils.getColorId(themeClass.getBackgroundColor());
		if (foregroundColorId == null || backgroundColorId == null)
			return null;

		/* Space and span.length() -1 hack needed so backgroundSpan doesn't break from
			 	https://stackoverflow.com/questions/20069537/replacementspans-draw-method-isnt-called */
		SpannableString spanString = new SpannableString(caption + Strings.SPACE);

		if (foregroundColorId != null)
			spanString.setSpan(new ForegroundColorSpan(foregroundColorId), 0, spanString.length(), 0);

		if (backgroundColorId != null) {
			LayoutBoxMeasures padding = themeClass.getPadding();
			if (!padding.isEmpty())
				spanString.setSpan(new PaddingBackgroundColorSpan(backgroundColorId, foregroundColorId, padding), 0, spanString.length() - 1, 0);
			else
				spanString.setSpan(new BackgroundColorSpan(backgroundColorId), 0, spanString.length() - 1, 0);
		}

		return spanString;
	}
}
