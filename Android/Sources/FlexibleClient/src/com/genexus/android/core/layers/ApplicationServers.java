package com.genexus.android.core.layers;

import com.genexus.android.core.base.metadata.enums.Connectivity;
import com.genexus.android.core.base.providers.IApplicationServer;

public class ApplicationServers
{
	private final RemoteApplicationServer mRemoteServer = new RemoteApplicationServer();
	private final LocalApplicationServer mLocalServer = new LocalApplicationServer();

	public IApplicationServer getApplicationServer(Connectivity connectivity)
	{
		if (connectivity == Connectivity.Online)
			return getRemoteServer();
		else if (connectivity == Connectivity.Offline)
			return getLocalServer();
		else
			throw new IllegalStateException("Invalid 'Connectivity' value. Should be online or offline at this point");
	}

	private IApplicationServer getRemoteServer()
	{
		return mRemoteServer;
	}

	private IApplicationServer getLocalServer()
	{
		return mLocalServer;
	}
}
