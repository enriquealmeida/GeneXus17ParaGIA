package com.genexus.android.core.base.application;

import java.util.ArrayList;
import java.util.EnumSet;

import com.genexus.android.core.base.utils.Strings;

class OutputMessages extends ArrayList<OutputMessage>
{
	private static final long serialVersionUID = 1L;

	public boolean hasErrors()
	{
		for (OutputMessage msg : this)
			if (msg.getLevel() == MessageLevel.ERROR)
				return true;

		return false;
	}

	public String getText()
	{
		return getText(EnumSet.allOf(MessageLevel.class));
	}

	public String getErrorText()
	{
		return getText(EnumSet.of(MessageLevel.ERROR));
	}

	public String getWarningText()
	{
		return getText(EnumSet.of(MessageLevel.WARNING));
	}

	private String getText(EnumSet<MessageLevel> levels)
	{
		StringBuilder sb = new StringBuilder();
		for (OutputMessage msg : this)
		{
			if (levels.contains(msg.getLevel()))
			{
				sb.append(msg.getText());
				sb.append(Strings.NEWLINE);
			}
		}

		return sb.toString();
	}
}
