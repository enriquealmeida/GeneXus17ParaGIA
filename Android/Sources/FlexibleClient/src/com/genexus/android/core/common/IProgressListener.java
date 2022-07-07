package com.genexus.android.core.common;

public interface IProgressListener {

	void setCount(int totalLength) ;
	
	void step(int totalProgress) ;

}
