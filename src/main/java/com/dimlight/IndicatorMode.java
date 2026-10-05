package com.dimlight;

public enum IndicatorMode
{
	OFF("Off", false, false),
	ON_SCREEN("On screen", true, false),
	CHAT("Chat", false, true),
	BOTH("Chat + on screen", true, true);

	private final String label;
	private final boolean onScreen;
	private final boolean chat;

	IndicatorMode(String label, boolean onScreen, boolean chat)
	{
		this.label = label;
		this.onScreen = onScreen;
		this.chat = chat;
	}

	public boolean showsOnScreen()
	{
		return onScreen;
	}

	public boolean showsInChat()
	{
		return chat;
	}

	@Override
	public String toString()
	{
		return label;
	}
}
