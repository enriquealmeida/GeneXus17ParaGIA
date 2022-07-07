package com.genexus.android.core.common;

import com.genexus.android.core.base.services.IExceptions;
import com.genexus.android.core.base.services.Services;

class ExceptionManager implements IExceptions {

	@Override
	public void handle(Exception ex) {
		if (ex != null && ex.getMessage() != null) {
			Services.Log.error("ERROR:", ex.getMessage());
		}
		if (ex == null)
			Services.Log.error("ERROR:", " invalid exception was caught");
	}

	@Override
	public void printStackTrace(Exception ex) {
	}

}
