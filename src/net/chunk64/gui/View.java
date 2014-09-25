package net.chunk64.gui;

public enum View
{
	WORDSEARCH("ws"), SELECTION("s");
	private String viewName;

	View(String s)
	{
		viewName = s;
	}

	public String getName()
	{
		return viewName;
	}
}