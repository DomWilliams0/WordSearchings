package net.chunk64.wordsearch;

import java.awt.*;

public class Word
{
	private String word;
	private Direction direction;
	private Point startingPoint;

	public Word(String word, Direction direction, Point startingPoint)
	{
		this.word = word;
		this.direction = direction;
		this.startingPoint = startingPoint;
	}

	public Point[] getPoints(WordSearch wordSearch)
	{
		Point[] points = new Point[word.length()];
		Point last = new Point(startingPoint);

		points[0] = new Point(startingPoint);
		for (int i = 1; i < word.length(); i++)
		{
			if (!direction.update(last, wordSearch)) return null;

			points[i] = last;
			last = new Point(last);
		}
		return points;
	}

	@Override
	public String toString()
	{
		return "Word{" +
				"word='" + word + '\'' +
				", direction=" + direction +
				", startingPoint=" + startingPoint.x + "," + startingPoint.y +
				'}';
	}

	public String getWord()
	{
		return word;
	}

	public void setDirection(Direction direction)
	{
		this.direction = direction;
	}

	public Point getStartingPoint()
	{
		return startingPoint;
	}

	public void setStartingPoint(Point startingPoint)
	{
		this.startingPoint = startingPoint;
	}
}
