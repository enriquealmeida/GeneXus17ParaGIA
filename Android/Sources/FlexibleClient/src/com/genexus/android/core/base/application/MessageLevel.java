package com.genexus.android.core.base.application;

public enum MessageLevel {
	WARNING, ERROR, INFORMATION;

	public int getValue(){
		switch(this) {
			case WARNING:
				return 0;
			case ERROR:
				return 1;
			case INFORMATION:
				return 2;
			default:
				return -1;
		}
	}
}
