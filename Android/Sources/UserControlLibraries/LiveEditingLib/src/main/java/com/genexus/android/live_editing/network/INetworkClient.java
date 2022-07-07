package com.genexus.android.live_editing.network;

import com.genexus.android.live_editing.commands.IClientCommand;

public interface INetworkClient {
	void connect();
	void disconnect() throws IllegalStateException;
	void send(IClientCommand command) throws IllegalStateException;
}
