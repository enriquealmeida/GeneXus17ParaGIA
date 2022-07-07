package com.genexus.android.core.resources;

import android.content.Context;
import android.content.res.TypedArray;

import com.genexus.android.R;
import com.genexus.android.core.base.metadata.ActionDefinition;
import com.genexus.android.core.base.metadata.enums.ActionTypes;

import java.util.HashMap;
import java.util.Map;

public class BuiltInResources {
    public static final int PLACE_ACTION_BAR = 1;
    public static final int PLACE_CONTENT = 2;

    private static final Map<String, BuiltInResource> BUILTIN_RESOURCES;

    private static final int THEME_DARK_VALUE = 0;
    private static final int THEME_LIGHT_VALUE = 1;
    private static final int THEME_LIGHT_WITH_DAB_VALUE = 2;

    private static Integer sThemeKind;

    static {
        BUILTIN_RESOURCES = new HashMap<>(22);

        // Standard actions
        addResource(new BuiltInResource(
                ActionDefinition.StandardAction.INSERT,
                R.drawable.gx_action_insert_dark,
                R.drawable.gx_action_insert_light,
                false)
        );
        addResource(new BuiltInResource(
                ActionDefinition.StandardAction.UPDATE,
                R.drawable.gx_action_update_dark,
                R.drawable.gx_action_update_light,
                false
        ));
        addResource(new BuiltInResource(
                ActionDefinition.StandardAction.EDIT,
                R.drawable.gx_action_update_dark,
                R.drawable.gx_action_update_light,
                false
        ));
        addResource(new BuiltInResource(
                ActionDefinition.StandardAction.DELETE,
                R.drawable.gx_action_delete_dark,
                R.drawable.gx_action_delete_light,
                false
        ));
        addResource(new BuiltInResource(
                ActionDefinition.StandardAction.SAVE,
                R.drawable.gx_action_save_dark,
                R.drawable.gx_action_save_light,
                false
        ));
        addResource(new BuiltInResource(
                ActionDefinition.StandardAction.CANCEL,
                R.drawable.gx_action_cancel_dark,
                R.drawable.gx_action_cancel_light,
                false
        ));
        addResource(new BuiltInResource(
                ActionDefinition.StandardAction.REFRESH,
                R.drawable.gx_action_refresh_dark,
                R.drawable.gx_action_refresh_light,
                false
        ));
        addResource(new BuiltInResource(
                ActionDefinition.StandardAction.SEARCH,
                R.drawable.gx_action_search_dark,
                R.drawable.gx_action_search_light,
                false
        ));
        addResource(new BuiltInResource(
                ActionDefinition.StandardAction.FILTER,
                R.drawable.gx_action_filter_dark,
                R.drawable.gx_action_filter_light,
                false
        ));
        addResource(new BuiltInResource(
                ActionDefinition.StandardAction.SHARE,
                R.drawable.gx_action_share_dark,
                R.drawable.gx_action_share_light,
                false
        ));

        // Contextual actions for semantic domains
        addResource(new BuiltInResource(
                ActionTypes.SEND_EMAIL,
                R.drawable.gx_domain_action_email_dark,
                R.drawable.gx_domain_action_email_light,
                false
        ));
        addResource(new BuiltInResource(
                ActionTypes.LOCATE_ADDRESS,
                R.drawable.gx_domain_action_locate_dark,
                R.drawable.gx_domain_action_locate_light,
                false
        ));
        addResource(new BuiltInResource(
                ActionTypes.LOCATE_GEO_LOCATION,
                R.drawable.gx_domain_action_locate_dark,
                R.drawable.gx_domain_action_locate_light,
                false
        ));
        addResource(new BuiltInResource(
                ActionTypes.CALL_NUMBER,
                R.drawable.gx_domain_action_call_dark,
                R.drawable.gx_domain_action_call_light,
                false
        ));
        addResource(new BuiltInResource(
                ActionTypes.VIEW_AUDIO,
                R.drawable.gx_domain_action_play_dark,
                R.drawable.gx_domain_action_play_light,
                false
        ));
        addResource(new BuiltInResource(
                ActionTypes.VIEW_VIDEO,
                R.drawable.gx_domain_action_play_dark,
                R.drawable.gx_domain_action_play_light,
                false
        ));
        addResource(new BuiltInResource(
                ActionTypes.VIEW_URL,
                R.drawable.gx_domain_action_link_dark,
                R.drawable.gx_domain_action_link_light,
                false
        ));

        // Autolinks & prompts
        addResource(new BuiltInResource(
                ActionTypes.LINK,
                R.drawable.gx_field_link_dark,
                R.drawable.gx_field_link_light,
                true
        ));
        addResource(new BuiltInResource(
                ActionTypes.PROMPT,
                R.drawable.gx_field_prompt_dark,
                R.drawable.gx_field_prompt_light,
                false
        ));

        // Multimedia selection icons
        addResource(new BuiltInResource(
                MediaTypes.IMAGE_STUB,
                R.drawable.ic_image_white_48dp,
                R.drawable.ic_image_black_48dp,
                false
        ));
        addResource(new BuiltInResource(
                MediaTypes.AUDIO_STUB,
                R.drawable.ic_mic_white_48dp,
                R.drawable.ic_mic_black_48dp,
                false
        ));
        addResource(new BuiltInResource(
                MediaTypes.VIDEO_STUB,
                R.drawable.ic_movie_white_48dp,
                R.drawable.ic_movie_black_48dp,
                false
        ));
    }

    private static void addResource(BuiltInResource resource) {
        BUILTIN_RESOURCES.put(resource.getName(), resource);
    }

    /**
     * Chooses one of the three given resources depending on whether the current theme is based off
     * the dark, light or light with dark actionbar base theme.
     */
    public static <T> T getResource(Context context, T darkRes, T lightRes, T lightWithDarkRes) {
        if (sThemeKind == null) {
            TypedArray a = context.obtainStyledAttributes(new int[]{R.attr.gx_base_theme});
            if (!a.hasValue(0)) {
                throw new IllegalStateException("The theme must inherit from one of the followings: " +
                        "Theme.Genexus.Dark, Theme.Genexus.Light, Theme.Genexus.Light.DarkActionBar");
            }

            sThemeKind = a.getInt(0, 0);
            a.recycle();
        }

        switch (sThemeKind) {
            case THEME_DARK_VALUE:
                return darkRes;
            case THEME_LIGHT_VALUE:
                return lightRes;
            case THEME_LIGHT_WITH_DAB_VALUE:
                return lightWithDarkRes;
            default:
                return darkRes;
        }
    }

    /**
     * Gets the resourceId associated to a standard action (e.g. Insert, Refresh, Share).
     *
     * @param action Action name
     * @param place  Where the drawable will be used (i.e. action bar or content)
     */
    public static int getResId(Context context, String action, int place) {
        BuiltInResource resource = BUILTIN_RESOURCES.get(action);
        if (resource == null) {
            return 0;
        }

        // In action bar, LightWithDarkActionBar = Dark.
        // In content, LightWithDarkActionBar = Light.
        int lightWithDarkRes = place == PLACE_ACTION_BAR ? resource.getDarkResource() : resource.getLightResource();

        return getResource(context, resource.getDarkResource(), resource.getLightResource(), lightWithDarkRes);
    }

    public static boolean shouldAutoMirror(String action) {
        BuiltInResource resource = BUILTIN_RESOURCES.get(action);
        if (resource == null) {
            return false;
        } else {
            return resource.shouldAutomirror();
        }
    }
}
