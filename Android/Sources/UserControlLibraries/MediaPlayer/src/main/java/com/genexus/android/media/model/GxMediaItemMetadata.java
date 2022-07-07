package com.genexus.android.media.model;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import android.support.v4.media.MediaMetadataCompat;

import com.genexus.android.core.base.model.Entity;
import com.genexus.android.core.base.model.EntityFactory;
import com.genexus.android.core.base.model.EntityList;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.base.utils.Strings;

/**
 * Extended metadata for a Media Item
 */
@SuppressWarnings("SpellCheckingInspection")
class GxMediaItemMetadata
{
	private static final HashMap<String, MappedKey> MAPPING;

	private enum Type { STRING, LONG }

	private static class MappedKey
	{
		public final String name;
		public final Type type;

		public MappedKey(String name, Type type)
		{
			this.name = name;
			this.type = type;
		}
	}

	static
	{
		MAPPING = new HashMap<>();
		MAPPING.put("gx.media.metadata.artist", new MappedKey(MediaMetadataCompat.METADATA_KEY_ARTIST, Type.STRING));
		MAPPING.put("gx.media.metadata.album", new MappedKey(MediaMetadataCompat.METADATA_KEY_ALBUM, Type.STRING));
		MAPPING.put("gx.media.metadata.author", new MappedKey(MediaMetadataCompat.METADATA_KEY_AUTHOR, Type.STRING));
		MAPPING.put("gx.media.metadata.writer", new MappedKey(MediaMetadataCompat.METADATA_KEY_WRITER, Type.STRING));
		MAPPING.put("gx.media.metadata.composer", new MappedKey(MediaMetadataCompat.METADATA_KEY_COMPOSER, Type.STRING));
		MAPPING.put("gx.media.metadata.compilation", new MappedKey(MediaMetadataCompat.METADATA_KEY_COMPILATION, Type.STRING));
		MAPPING.put("gx.media.metadata.date", new MappedKey(MediaMetadataCompat.METADATA_KEY_DATE, Type.STRING));
		MAPPING.put("gx.media.metadata.year", new MappedKey(MediaMetadataCompat.METADATA_KEY_YEAR, Type.LONG));
		MAPPING.put("gx.media.metadata.genre", new MappedKey(MediaMetadataCompat.METADATA_KEY_GENRE, Type.STRING));
		MAPPING.put("gx.media.metadata.tracknumber", new MappedKey(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, Type.LONG));
		MAPPING.put("gx.media.metadata.numberoftracks", new MappedKey(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, Type.LONG));
		MAPPING.put("gx.media.metadata.discnumber", new MappedKey(MediaMetadataCompat.METADATA_KEY_DISC_NUMBER, Type.LONG));
		MAPPING.put("gx.media.metadata.albumartist", new MappedKey(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, Type.STRING));
	}

	// Real collection of data. LinkedHashMap to keep the original order.
	private final LinkedHashMap<String, String> mProperties;

	public GxMediaItemMetadata()
	{
		mProperties = new LinkedHashMap<>();
	}

	public GxMediaItemMetadata(EntityList sdtMetadata)
	{
		mProperties = new LinkedHashMap<>();

		if (sdtMetadata != null)
		{
			for (Entity sdtProperty : sdtMetadata)
			{
				String key = sdtProperty.optStringProperty("Key");
				String value = sdtProperty.optStringProperty("Value");
				mProperties.put(key, value);
			}
		}
	}

	public EntityList toSdt()
	{
		EntityList sdtMetadata = new EntityList();
		for (Map.Entry<String, String> property : mProperties.entrySet()) {
			Entity sdtProperty = EntityFactory.newSdt("GeneXus.SD.Media.MediaItem", "Metadata");
			sdtProperty.setProperty("Key", property.getKey());
			sdtProperty.setProperty("Value", property.getValue());
			sdtMetadata.add(sdtProperty);
		}

		return sdtMetadata;
	}

	public void toMediaMetadata(MediaMetadataCompat.Builder builder)
	{
		// Map the "known" GX properties to their Android equivalents.
		for (Map.Entry<String, String> property : mProperties.entrySet())
		{
			MappedKey mappedKey = MAPPING.get(property.getKey());
			if (mappedKey != null)
			{
				switch (mappedKey.type)
				{
					case STRING :
						String strValue = property.getValue();
						if (Strings.hasValue(strValue))
							builder.putString(mappedKey.name, strValue);
						break;

					case LONG:
						Long longValue = Services.Strings.tryParseLong(property.getValue());
						if (longValue != null)
							builder.putLong(mappedKey.name, longValue);
						break;
				}
			}
		}
	}
}
