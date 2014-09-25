package net.chunk64.gui.wordsearch;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class WordFrame extends JFrame
{
	private static final int DESIRED_HEIGHT = 600;
	private static final Font FONT = Font.decode("Consolas");

	private Map<String, WordLabel> labels;
	private JFrame frame;

	public WordFrame(JFrame frame)
	{
		this.frame = frame;
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		setLayout(new GridLayout(0, 1));
		setFocusable(false);

	}

	public void setWords(String[] words)
	{
		labels = new HashMap<>();

		Font font = FONT.deriveFont(Font.PLAIN, 15f);

		int height = DESIRED_HEIGHT / words.length;
		height = Math.max(20, height);
		height = Math.min(40, height);

		int maxWidth = words[0].length();
		for (int i = 1; i < words.length; i++)
			maxWidth = Math.max(words[i].length(), maxWidth);

		for (String word : words)
		{
			WordLabel label = new WordLabel(word.toLowerCase(), new Dimension(maxWidth, height), font);

			add(label);
			labels.put(word, label);
		}

		setPreferredSize(new Dimension(maxWidth * 10, height * words.length));
		setLocation(frame.getX() - frame.getWidth() / 2, frame.getY());

		pack();
		setVisible(true);
		frame.setVisible(true);
	}

	public WordLabel getWord(String word)
	{
		return labels.get(word.toLowerCase());
	}

	public boolean isComplete()
	{
		for (WordLabel label : labels.values())
			if (!label.isFound()) return false;
		return true;
	}
}

class WordLabel extends JLabel
{
	private boolean found;

	WordLabel(String text, Dimension size, Font font)
	{
		super(text, CENTER);
		setFound(false);
		setFont(font);
		setPreferredSize(size);
	}

	public boolean isFound()
	{
		return found;
	}

	public void setFound(boolean found)
	{
		this.found = found;
		setForeground(found ? Color.RED : Color.BLACK);
		repaint();
	}

	@Override
	public String toString()
	{
		return "WordLabel{" +
				"word=" + getText() +
				'}';
	}
}
