package com.genexus.android.core.base.metadata.loader;

import android.content.Context;

import com.genexus.android.core.base.metadata.languages.Language;
import com.genexus.android.core.base.metadata.languages.LanguageCatalog;
import com.genexus.android.core.base.serialization.INodeObject;

public class LanguagesMetadataLoader
{
	public static LanguageCatalog loadFrom(Context context, INodeObject jsonLanguages)
	{
		LanguageCatalog catalog = new LanguageCatalog();
		catalog.setDefault(jsonLanguages.optString("DefaultLanguage"));

		for (INodeObject jsonLanguage : jsonLanguages.optCollection("Languages"))
		{
			String name = jsonLanguage.getString("Name");
			String languageCode = jsonLanguage.optString("LanguageCode");
			String countryCode = jsonLanguage.optString("CountryCode");

			// TODO: Download all languages, but only deserialize the current one.
			// (Others will be deserialized later, if needed).
			INodeObject jsonLanguageFile = MetadataLoader.getDefinition(context, name + ".language");
			if (jsonLanguageFile != null)
			{
				Language language = new Language(name, languageCode, countryCode);
				loadLanguage(language, jsonLanguageFile, jsonLanguage.getNode("properties"));
				catalog.add(language);
			}
		}

		return catalog;
	}

	private static void loadLanguage(Language language, INodeObject jsonLanguage, INodeObject jsonProperties)
	{
		if (jsonProperties != null)
		{
			for (String propKey : jsonProperties.names())
			{
				String propValue = jsonProperties.getString(propKey);
				language.getProperties().put(propKey, propValue);
			}
		}

		for (INodeObject jsonTranslation : jsonLanguage.optCollection("Translations"))
		{
			String message = jsonTranslation.getString("M");
			String translation = jsonTranslation.getString("T");
			language.add(message, translation);
		}
	}
}
