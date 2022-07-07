package com.artech.controls.maps.common.kml;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.apache.commons.io.IOUtils;

import android.os.AsyncTask;

import com.artech.base.services.Services;
import com.artech.controls.maps.common.IMapsProvider;
import com.artech.controls.maps.common.MapLayer;

public class KmlReaderAsyncTask extends AsyncTask<String, Void, MapLayer>
{
	private final IMapsProvider mMapsProvider;
	
	public KmlReaderAsyncTask(IMapsProvider mapsProvider)
	{
		mMapsProvider = mapsProvider;
	}
	
	@Override
	protected MapLayer doInBackground(String... params)
	{
		if (params == null || params.length == 0)
			return null;

		IKmlDeserializer deserializer = null;
		try
		{
			Class<?> klass = Class.forName("com.artech.controls.maps.common.kml.KmlDeserializerImpl");
			if (klass != null)
				deserializer = (IKmlDeserializer) klass.getConstructor().newInstance();
		} catch (InstantiationException | InvocationTargetException | NoSuchMethodException
			| IllegalAccessException | ClassNotFoundException e)
		{
			Services.Log.warning("Error instantiating KmlDeserializerImpl", e);
			return null;
		}

		if (deserializer != null)
		{
			String kmlString = params[0];
			// InputStream is = new FileInputStream(filename); To read as a file. For now the KML is passed as text.

			try (InputStream is = IOUtils.toInputStream(kmlString))
			{
				return deserializer.deserialize(mMapsProvider, is);
			} catch (IOException ex)
			{
				Services.Log.warning("KmlDeserializer", ex.getMessage());
				return null;
			}
		} else
			return null;
	}
}
