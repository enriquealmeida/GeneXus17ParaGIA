package com.genexus.android.maps.kml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import com.genexus.android.controls.maps.common.kml.KmlDeserializerImpl;
import com.genexus.android.core.base.services.Services;
import com.genexus.android.core.controls.maps.common.IKmlDeserializer;
import com.genexus.android.core.controls.maps.common.IMapsProvider;
import com.genexus.android.core.controls.maps.common.MapLayer;
import com.genexus.android.core.utils.TaskRunner;

public class KmlReaderTask extends TaskRunner.BaseTask<MapLayer> {
	private final IMapsProvider mMapsProvider;
	private final String mKmlString;
	private final boolean mIsFile;

	public KmlReaderTask(IMapsProvider mapsProvider, String kmlString, boolean isFile) {
		mMapsProvider = mapsProvider;
		mKmlString = kmlString;
		mIsFile = isFile;
	}

	@Override
	public MapLayer doInBackground() {
		IKmlDeserializer deserializer = new KmlDeserializerImpl();
		try (InputStream is = mIsFile ? new FileInputStream(mKmlString) : IOUtils.toInputStream(mKmlString)) {
			return deserializer.deserialize(mMapsProvider, is);
		} catch (IOException ex) {
			Services.Log.warning("KmlDeserializer", ex.getMessage());
			return null;
		}
	}
}
