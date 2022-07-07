package com.genexus.android.live_editing.network;

import androidx.annotation.NonNull;

import com.genexus.android.live_editing.commands.IServerCommand;
import com.genexus.android.live_editing.support.Endpoint;

public interface INetworkEventsListener {
	void onConnectionAttempt(@NonNull Endpoint targetEndpoint);
	void onMaxAttemptsReached();
	void onConnectionEstablished(@NonNull Endpoint endpoint);
	void onConnectionDropped();
	void onCommandReceived(IServerCommand command);
}
