package $Main.AndroidPackageName$;

import com.genexus.android.core.providers.EntityDataProvider;

public class AppEntityDataProvider extends EntityDataProvider
{
	public AppEntityDataProvider()
	{
		EntityDataProvider.AUTHORITY = "$Main.AndroidPackageName$.appentityprovider";
		EntityDataProvider.URI_MATCHER = buildUriMatcher();
	}
}
