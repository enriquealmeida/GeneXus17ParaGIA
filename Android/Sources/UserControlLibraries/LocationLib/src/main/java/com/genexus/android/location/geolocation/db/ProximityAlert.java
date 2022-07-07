package com.genexus.android.location.geolocation.db;

public class ProximityAlert {
	
	private Integer id;
	private String name;
	private String description;
	private String geolocation;
	private Integer radius;
	private String expirationDateTime;
	private String actionName;
	
	public ProximityAlert() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGeolocation() {
		return geolocation;
	}

	public void setGeolocation(String geolocation) {
		this.geolocation = geolocation;
	}

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	public String getExpirationDateTime() {
		return expirationDateTime;
	}

	public void setExpirationDateTime(String expirationDateTime) {
		this.expirationDateTime = expirationDateTime;
	}

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
	
	
}
