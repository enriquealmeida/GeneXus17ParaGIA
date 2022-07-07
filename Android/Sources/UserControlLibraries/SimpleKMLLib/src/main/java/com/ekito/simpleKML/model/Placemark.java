/**
 * Copyright 2012 Ekito - http://www.ekito.fr/
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.ekito.simpleKML.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementUnion;

/**
 * A Placemark is a {@link Feature} with associated {@link Geometry}. In Google
 * Earth, a Placemark appears as a list item in the Places panel. A Placemark
 * with a Point has an icon associated with it that marks a point on the Earth
 * in the 3D viewer. (In the Google Earth 3D viewer, a Point Placemark is the
 * only object you can click or roll over. Other Geometry objects do not have an
 * icon in the 3D viewer. To give the user something to click in the 3D viewer,
 * you would need to create a MultiGeometry object that contains both a Point
 * and the other Geometry object.)
 */
public class Placemark extends Feature {

	/** The geometry list. */
	@ElementUnion({
			@Element(name = "Point", type = Point.class, required = false),
			@Element(name = "LineString", type = LineString.class, required = false),
			@Element(name = "LinearRing", type = LinearRing.class, required = false),
			@Element(name = "Polygon", type = Polygon.class, required = false),
			@Element(name = "MultiGeometry", type = MultiGeometry.class, required = false),
			@Element(name = "MultiTrack", type = MultiTrack.class, required = false),
			@Element(name = "Model", type = Model.class, required = false),
			@Element(name = "Track", type = Track.class, required = false) })
	private Geometry geometry;//Converted to Geometry as KML allows only 0 or 1 Geometry inside placemark 

	/**
	 * Gets the geometry.
	 * 
	 * @return the geometry
	 */
	public Geometry getGeometry() {
		return geometry;
	}

	/**
	 * Sets the geometry.
	 * 
	 * @param geometryList
	 *            the new geometry
	 */
	public void setGeometry(Geometry geometryList) {
		this.geometry = geometryList;
	}
}
