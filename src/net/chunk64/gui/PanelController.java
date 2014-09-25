package net.chunk64.gui;

import net.chunk64.gui.wordsearch.SelectionOverlay;
import net.chunk64.gui.wordsearch.WordFrame;
import net.chunk64.gui.wordsearch.WorldSearchPanel;
import net.chunk64.wordsearch.WordSearch;

import javax.swing.*;
import java.awt.*;

public class PanelController extends JLayeredPane
{

	private JFrame frame;
	private WordFrame wordFrame;
	private CardLayout cardLayout;

	private SelectionOverlay overlay;

	public PanelController(JFrame frame)
	{
		this.frame = frame;
		this.wordFrame = new WordFrame(frame);
		this.overlay = new SelectionOverlay(wordFrame);

		add(overlay, new Integer(10));

		cardLayout = new CardLayout();
		setLayout(cardLayout);

	}

	public void setWordSearch(WordSearch wordSearch)
	{
		WorldSearchPanel wsPanel = new WorldSearchPanel(wordSearch);
		add(wsPanel, View.WORDSEARCH.getName(), 0);
		wordFrame.setWords(wordSearch.getWords());
		overlay.setCellSize(wsPanel.getCellSize());
		overlay.setWordSearch(wordSearch);
	}

	public void changeView(View view)
	{
		cardLayout.show(this, view.getName());
		frame.pack();
	}


}

