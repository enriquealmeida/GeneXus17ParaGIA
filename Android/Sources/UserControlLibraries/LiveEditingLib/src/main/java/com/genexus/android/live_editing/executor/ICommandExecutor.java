package com.genexus.android.live_editing.executor;

import com.genexus.android.live_editing.commands.IServerCommand;

public interface ICommandExecutor {
	void enqueue(IServerCommand command);
}
