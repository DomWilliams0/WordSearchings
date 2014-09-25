package net.chunk64.wordsearch;

import net.chunk64.Main;

import java.awt.*;
import java.util.*;
import java.util.List;

public class WordArranger
{
	private List<Word> wordPositions;
	private WordSearch wordSearch;

	public WordArranger(String[] words, WordSearch wordSearch)
	{
		this.wordSearch = wordSearch;

		wordPositions = new ArrayList<>();
		for (String word : words)
		{
			Direction direction = Direction.getRandomDirection();
			Point point = getRandomPoint();
			wordPositions.add(new Word(word, direction, point));
		}

		List<Word> invalidWords = new ArrayList<>();
		int attempts = 0;
		do
		{
			findInvalidWords(invalidWords, wordPositions);

			switch (++attempts)
			{
				case 50000:
					System.out.println("This is taking a while...");
					break;
				case 150000:
					System.out.println("These words weren't designed to be put into a wordsearch...");
					break;
				case 500000:
					System.out.println("Ahem...");
					break;
				case 1000000:
					System.out.println("Could not produce a wordsearch with these words and this size, please try again!");
					System.exit(1);
					break;
			}

		} while (!invalidWords.isEmpty());

//		System.out.println("number of generation attempts: " + attempts);
		updateGrid();

	}

	private void updateGrid()
	{
		char[][] grid = wordSearch.getGrid();

		for (Word word : wordPositions)
		{
			Point[] points = word.getPoints(wordSearch);
			if (points == null) continue;

			for (int i = 0; i < points.length; i++)
			{
				Point point = points[i];
				grid[point.x][point.y] = word.getWord().charAt(i);
			}
		}

		for (int x = 0; x < wordSearch.getSize(); x++)
		{
			for (int y = 0; y < wordSearch.getSize(); y++)
			{
				if (grid[x][y] == 0)
					grid[x][y] = Main.SHOW_SOLUTIONS ? '.' : (char) (Main.RANDOM.nextInt(26) + 'a');
			}
		}


	}

	private void findInvalidWords(List<Word> invalidWords, List<Word> words)
	{
		// replace invalid words
		for (Word word : invalidWords)
		{
			word.setStartingPoint(getRandomPoint());
			word.setDirection(Direction.getRandomDirection());
		}
		invalidWords.clear();


		// gather all points
		Map<Word, Point[]> allPoints = new HashMap<>();
		for (Word word : words)
		{
			Point[] points = word.getPoints(wordSearch);
			if (points == null)
			{
				// word sticks out of grid
				invalidWords.add(word);
				continue;
			}

			allPoints.put(word, points);
		}

		// find all intersections
		nextParent:
		for (Map.Entry<Word, Point[]> parent : allPoints.entrySet())
		{
			nextChild:
			for (Map.Entry<Word, Point[]> child : allPoints.entrySet())
			{
				if (child.getKey() == parent.getKey()) continue;

				for (Word word : invalidWords)
				{
					if (word == child.getKey())
						continue nextChild;
					if (word == parent.getKey())
						continue nextParent;
				}

				if (intersect(parent.getValue(), child.getValue()))
				{
					invalidWords.add(parent.getKey());
					invalidWords.add(child.getKey());
				}
			}

		}


	}

	private boolean intersect(Point[] a, Point[] b)
	{
		List<Point> bList = Arrays.asList(b);
		for (Point point : a)
			if (bList.contains(point))
				return true;
		return false;

	}


	private Point getRandomPoint()
	{
		return new Point(Main.RANDOM.nextInt(wordSearch.getSize()), Main.RANDOM.nextInt(wordSearch.getSize()));
	}

}

