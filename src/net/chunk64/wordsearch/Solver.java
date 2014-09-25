package net.chunk64.wordsearch;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Solver
{
	private Set<Word> solutions;

	public Solver(WordSearch wordSearch)
	{
		this.solutions = new HashSet<>();

		char[][] grid = wordSearch.getGrid();
		String[] words = wordSearch.getWords();

		next:
		for (String word : words)
		{
			for (int x = 0; x < grid.length; x++)
			{
				for (int y = 0; y < grid[x].length; y++)
				{
					for (Direction d : Direction.values())
					{
						char[] letters = getLettersFromGrid(d, x, y, word.length(), wordSearch);
						if (letters == null) continue;
						if (isEqual(letters, word))
						{
							solutions.add(new Word(word, d, new Point(x, y)));
							continue next;
						}
					}

				}
			}


		}
	}

	public Set<Word> getSolutions()
	{
		return solutions;
	}

	private boolean isEqual(char[] c, String s)
	{
		if (c.length != s.length()) return false;
		for (int i = 0; i < c.length; i++)
			if (Character.toLowerCase(s.charAt(i)) != Character.toLowerCase(c[i])) return false;
		return true;
	}

	private char[] getLettersFromGrid(Direction direction, int x, int y, int length, WordSearch wordSearch)
	{
		char[] chars = new char[length];
		Point point = new Point(x, y);

		for (int i = 0; i < chars.length; i++)
		{
			chars[i] = wordSearch.getGrid()[point.y][point.x];
			if (!direction.update(point, wordSearch))
				return null;
		}

		return chars;
	}

}
