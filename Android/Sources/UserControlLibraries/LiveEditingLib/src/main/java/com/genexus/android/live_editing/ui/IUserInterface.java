package com.genexus.android.live_editing.ui;

import com.genexus.android.live_editing.support.Endpoint;

public interface IUserInterface {
	void displayConnectionAttempt(Endpoint targetEndpoint);
	void displayConnectionFailed();
	void displayConnectionEstablished(Endpoint endpoint);
	void displayConnectionDropped();
	void stopDisplayingInformation();
}
