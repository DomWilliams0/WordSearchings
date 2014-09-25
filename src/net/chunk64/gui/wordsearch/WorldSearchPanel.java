package net.chunk64.gui.wordsearch;

import net.chunk64.gui.GUI;
import net.chunk64.wordsearch.WordSearch;

import javax.swing.*;
import java.awt.*;

public class WorldSearchPanel extends JPanel
{
	private WordSearch wordSearch;
	private final int cellSize;

	private static Font FONT = Font.decode("Consolas"); //todo add tff as resource
	private Font font;


	public WorldSearchPanel(WordSearch wordSearch)
	{
		this.wordSearch = wordSearch;
		this.cellSize = (int) Math.floor((float) GUI.WINDOW_SIZE.width / wordSearch.getSize());

		font = FONT.deriveFont(Font.PLAIN, cellSize);

		int preferredSize = cellSize * wordSearch.getSize();
		setPreferredSize(new Dimension(preferredSize, preferredSize));
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);
		g2d.setFont(font);
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		int width = g.getFontMetrics().charWidth('a');

		for (int x = 0; x < wordSearch.getSize(); x++)
		{
			for (int y = 0; y < wordSearch.getSize(); y++)
			{
				g2d.drawRect(cellSize * x, cellSize * y, cellSize, cellSize);
				g2d.drawString(String.valueOf(wordSearch.getGrid()[y][x]), (x * cellSize) + (width * 0.4f), (y * cellSize) + (g2d.getFontMetrics().getHeight() * 0.75f));
			}
		}
	}

	public int getCellSize()
	{
		return cellSize;
	}
}

