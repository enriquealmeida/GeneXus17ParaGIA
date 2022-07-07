package com.genexus.android.core.base.application;

public class OutputMessage
{
	private MessageLevel mLevel;
	private String mText;

	public OutputMessage(MessageLevel level, String text)
	{
		mLevel = level;
		mText = text;
	}

	public MessageLevel getLevel() { return mLevel; }
	public String getText() { return mText; }
}
