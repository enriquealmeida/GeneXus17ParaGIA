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

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;

/**
 * Creates an untyped name/value pair. The name can have two versions: name and displayName.
 * The name attribute is used to identify the data pair within the KML file.
 * The displayName element is used when a properly formatted name, with spaces and HTML formatting,
 * is displayed in Google Earth. In the {@link BalloonStyle#text}, the notation $[name/displayName]
 * is replaced with {@link Data#displayName}. If you substitute the value of the name attribute of the {@link Data} element
 * in this format (for example, $[holeYardage], the attribute value is replaced with {@link Data#value}.
 * By default, the Placemark's balloon displays the name/value pairs associated with it.
 */
public class Data {

	/** The name. */
	@Attribute(required=false)
	private String name;
	
	/** The display name. */
	@Element(required=false)
	private String displayName;
	
	/** The value. */
	@Element(required=false)
	private String value;

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the display name.
	 *
	 * @return the display name
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Sets the display name.
	 *
	 * @param displayName the new display name
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
