package com.genexus.android.core.controls.maps.common;

import java.io.InputStream;

public interface IKmlDeserializer {
	MapLayer deserialize(IMapsProvider mapsProvider, InputStream is);
}
