package com.genexus.android.animations;

import com.genexus.android.R;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.NameMap;

public class Transitions
{
	private static NameMap<Transition> sTransitions;

	static
	{
		sTransitions = new NameMap<>();

		// These transitions are symmetrical: the "enter" and "close" parts are the same.
		add(new Transition("gx_none", R.anim.gx_none, R.anim.gx_none));
		add(new Transition("gx_push_up", R.anim.gx_push_up_out, R.anim.gx_push_up_in));
		add(new Transition("gx_push_down", R.anim.gx_push_down_out, R.anim.gx_push_down_in));
		add(new Transition("gx_push_left", R.anim.gx_push_left_out, R.anim.gx_push_left_in));
		add(new Transition("gx_push_right", R.anim.gx_push_right_out, R.anim.gx_push_right_in));
		add(new Transition("gx_fade", R.anim.gx_fade_out, R.anim.gx_fade_in));

		// These transitions are asymmetrical. The animation used when restoring the previous activity
		// is not exactly the same as the animation used to call up another activity.
		// Also, they are tweaked for fragments, because fragment animations are not respecting z-order.
		add(new Transition("gx_slide_up", R.anim.gx_slide_caller_activity_out, R.anim.gx_slide_caller_fragment_out, R.anim.gx_slide_up_in, R.anim.gx_slide_up_out, R.anim.gx_slide_caller_activity_in));
		add(new Transition("gx_slide_down", R.anim.gx_slide_caller_activity_out, R.anim.gx_slide_caller_fragment_out, R.anim.gx_slide_down_in, R.anim.gx_slide_down_out, R.anim.gx_slide_caller_activity_in));
		add(new Transition("gx_slide_left", R.anim.gx_slide_caller_activity_out, R.anim.gx_slide_caller_fragment_out, R.anim.gx_slide_left_in, R.anim.gx_slide_left_out, R.anim.gx_slide_caller_activity_in));
		add(new Transition("gx_slide_right", R.anim.gx_slide_caller_activity_out, R.anim.gx_slide_caller_fragment_out, R.anim.gx_slide_right_in, R.anim.gx_slide_right_out, R.anim.gx_slide_caller_activity_in));
	}

	public static Transition get(String name)
	{
		if (!Services.Strings.hasValue(name))
			return null;

		return sTransitions.get(name);
	}

	@SuppressWarnings("WeakerAccess")
	public static void add(Transition transition)
	{
		sTransitions.put(transition.getName(), transition);
	}
}
