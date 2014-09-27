package net.chunk64.gui.wordsearch;

import net.chunk64.wordsearch.Direction;
import net.chunk64.wordsearch.Solver;
import net.chunk64.wordsearch.Word;
import net.chunk64.wordsearch.WordSearch;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SelectionOverlay extends JPanel
{
	private static final Color CURRENT_COLOR = new Color(255, 68, 77, 170);
	private static final Color FIXED_COLOR = new Color(31, 135, 255, 170);
	private static final Color SOLUTIONS_COLOR = new Color(17, 255, 66, 170);

	private List<Selection> selections;
	private Selection currentSelection;
	private int cellSize;
	private WordSearch wordSearch;

	private List<Selection> solutions;
	private boolean showingSolutions;


	public SelectionOverlay(final WordFrame wordFrame)
	{
		selections = new ArrayList<>();
		solutions = new ArrayList<>();

		addKeyListener(new KeyAdapter()
		{
			@Override
			public void keyPressed(KeyEvent e)
			{
				super.keyPressed(e);
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
					System.exit(0);
				if (e.getKeyCode() == KeyEvent.VK_C)
				{
					showingSolutions = !showingSolutions;
					repaint();
				}
				if (e.getKeyCode() == KeyEvent.VK_V)
				{
					gameOver();
				}

			}


		});


		MouseAdapter clickListener = new MouseAdapter()
		{
			@Override
			public void mouseDragged(MouseEvent e)
			{
//				if (++count < 1)
//					return;
//				count = 0;

				if (currentSelection != null)
				{
					currentSelection.dragTo(e.getX(), e.getY(), wordSearch);
					repaint();
				}
			}

			@Override
			public void mousePressed(MouseEvent e)
			{
				if (currentSelection == null)
					currentSelection = new Selection(e.getX(), e.getY(), cellSize);
			}

			@Override
			public void mouseReleased(MouseEvent e)
			{
				if (currentSelection != null)
				{
					String word = currentSelection.getWord(wordSearch);
					WordLabel label = wordFrame.getWord(word);

					boolean correctWord = label != null;

					if (correctWord && !label.isFound())
					{
						selections.add(currentSelection);
						label.setFound(true);
					}


					if (wordFrame.isComplete())
					{
						gameOver();
					}


					currentSelection = null;
					repaint();

				}
			}
		};

		addMouseListener(clickListener);
		addMouseMotionListener(clickListener);

		setFocusable(true);
		requestFocus();

		setOpaque(false);

	}

	protected void gameOver()
	{
		for (KeyListener listener : getKeyListeners())
			removeKeyListener(listener);
		for (MouseListener listener : getMouseListeners())
			removeMouseListener(listener);
		new GameOverMouseListener(this);

		JLabel label = new JLabel("<html>You finished in # minutes!<br>Do you want to try another?</html>", SwingConstants.CENTER);
		int option = JOptionPane.showOptionDialog(this, label, "Well done!", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
				new String[]{"Restart", "Quit"}, null);

		if (option == 0)
			System.out.println("RESTARTING"); //todo show selection
		else if (option == 1)
			System.exit(0);
	}


	@Override
	protected void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		if (currentSelection != null)
		{
			g2d.setColor(CURRENT_COLOR);
			currentSelection.draw(g2d);
		}

		g2d.setColor(showingSolutions ? SOLUTIONS_COLOR : FIXED_COLOR);
		for (Selection s : showingSolutions ? solutions : selections)
			s.draw(g2d);

	}


	public void setCellSize(int cellSize)
	{
		this.cellSize = cellSize;
	}

	public void setWordSearch(WordSearch wordSearch)
	{
		this.wordSearch = wordSearch;

		Set<Word> wordSolutions = new Solver(wordSearch).getSolutions();
		for (Word solution : wordSolutions)
		{
			Selection s = new Selection(solution.getStartingPoint().x, solution.getStartingPoint().y, cellSize);
			Point[] absPoints = solution.getPoints(wordSearch);
			for (Point point : absPoints)
			{
				point.x *= cellSize;
				point.y *= cellSize;
			}
			s.addCells(absPoints);
			solutions.add(s);
		}
	}
}

class GameOverMouseListener extends MouseAdapter
{
	private SelectionOverlay overlay;

	GameOverMouseListener(SelectionOverlay overlay)
	{
		this.overlay = overlay;
		overlay.addMouseListener(this);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		overlay.gameOver();
	}
}

class Selection
{
	private Point start;
	private Point end;
	private Point lastEnd;
	private int cellSize;

	private List<Point> cells;


	public Selection(int x, int y, int cellSize)
	{
		this.cellSize = cellSize;
		start = new Point(x, y);
		end = new Point(start);
		lastEnd = new Point(end);

		cells = new ArrayList<>();
	}

	public void dragTo(int x, int y, WordSearch wordSearch)
	{
		lastEnd.x = end.x;
		lastEnd.y = end.y;
		end.x = x;
		end.y = y;
		findSelectedCells(wordSearch);
	}

	public void draw(Graphics2D g2d)
	{
		for (Point point : cells)
			g2d.fillRect(point.x + 1, point.y + 1, cellSize - 1, cellSize - 1);

	}

	private Point roundToCell(Point point)
	{
		return new Point((int) (Math.ceil(point.x / cellSize) * cellSize), (int) (Math.ceil(point.y / cellSize) * cellSize));
	}

	public String getWord(WordSearch wordSearch)
	{
		StringBuilder sb = new StringBuilder();
		for (Point cell : cells)
			sb.append(wordSearch.getGrid()[cell.y / cellSize][cell.x / cellSize]);
		return sb.toString();
	}

	public void findSelectedCells(WordSearch wordSearch)
	{
		double angle = Math.toDegrees(Math.atan2(end.y - start.y, end.x - start.x) + Math.PI);
		angle = Math.round(angle / 45f) * 45; // nearest multiple of 45
		Direction direction = Direction.fromAngle(angle);

		cells.clear();
		Point next = roundToCell(start);
		Point startCell = new Point(next);
		Point endCell = roundToCell(end);

		int count = 0;
		while (true)
		{
			if (++count > wordSearch.getSize()) break;

			if (endCell.equals(startCell))
			{
				cells.add(startCell);
				break;
			}

			// outside
			if (next.x >= wordSearch.getGrid().length * cellSize || next.x < 0) break;
			if (next.y >= wordSearch.getGrid()[0].length * cellSize || next.y < 0) break;

			// top
			if (endCell.y < start.y - cellSize)
			{
				if (next.y <= end.y) break;
			}
			// bottom
			else
			{
				if (end.y <= next.y) break;
			}

			// left
			if (endCell.x < start.x - cellSize)
			{
				if (next.x <= end.x) break;
			}
			// right
			else
			{
				if (end.x <= next.x) break;
			}

			cells.add(next);

			Point p = new Point(next);
			direction.update(p, null, cellSize);
			next = roundToCell(p);

		}

	}

	public void addCells(Point... cells)
	{
		Collections.addAll(this.cells, cells);
	}

}

