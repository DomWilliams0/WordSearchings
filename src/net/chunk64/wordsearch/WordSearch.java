package net.chunk64.wordsearch;

import net.chunk64.Main;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WordSearch
{
	private char[][] grid;
	private int size;

	public WordSearch(int size, int wordCount)
	{
		this.size = size;
		this.grid = new char[size][size];

		// load random words
		String[] words = grabWords(wordCount);
		if (words == null)
		{
			System.err.println("Failed to load dictionary words");
			System.exit(-1);
		}

		System.out.println("words = " + Arrays.toString(words));
		new WordArranger(words, this);

	}

	private WordSearch()
	{
	}

	public static WordSearch loadFromFile(File file)
	{
		WordSearch wordSearch = new WordSearch();
		BufferedReader reader;
		List<Character> chars = new ArrayList<>();

		try
		{
			reader = new BufferedReader(new FileReader(file));

			int character;
			while ((character = reader.read()) != -1)
			{
				char c = (char) character;
				if (Character.isLetter(c))
					chars.add(c);
			}
			reader.close();

		} catch (IOException e)
		{
			e.printStackTrace();
		}

		int size = (int) Math.sqrt(chars.size());

		char[][] grid = new char[size][size];
		int x = 0, y = 0;

		for (Character c : chars)
		{
			grid[x][y++] = c;
			if (y == size)
			{
				x++;
				y = 0;
			}
		}

		wordSearch.grid = grid;
		wordSearch.size = size;

		return wordSearch;
	}

	private String[] grabWords(int count)
	{
		int dictLength = countLines(readDictionary());
		Integer[] lineNumbers = new Integer[count];

		for (int i = 0; i < lineNumbers.length; i++)
			lineNumbers[i] = Main.RANDOM.nextInt(dictLength);

		return grabLines(lineNumbers, readDictionary());
	}

	private InputStreamReader readDictionary()
	{
		InputStream dictStream = WordSearch.class.getClassLoader().getResourceAsStream("dict.txt");
		return new InputStreamReader(dictStream);
	}

	private int countLines(InputStreamReader fileReader)
	{
		int wordCount = 0;
		LineNumberReader lineCounter = new LineNumberReader(fileReader);
		try
		{
			lineCounter.skip(Long.MAX_VALUE);
			wordCount = lineCounter.getLineNumber();
			lineCounter.close();
			fileReader.close();
		} catch (IOException e)
		{
			e.printStackTrace();
			System.exit(-1);
		}
		return wordCount;
	}

	private String[] grabLines(Integer[] lines, InputStreamReader reader)
	{
		BufferedReader br = new BufferedReader(reader);

		List<Integer> lineList = Arrays.asList(lines);
		Collections.sort(lineList);


		int currentLine = 1;
		String line;
		int selectIndex = 0;
		String[] selectedLines = new String[lines.length];

		while (selectIndex != selectedLines.length)
		{
			try
			{
				line = br.readLine();
				if (lineList.contains(currentLine++))
				{
					if (line.length() > size)
						lineList.set(selectIndex, currentLine);
					else
						selectedLines[selectIndex++] = line.toLowerCase();
				}

			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

		try
		{
			br.close();
			reader.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return selectedLines;
	}

	public void displayGrid()
	{
		display(System.out);
	}

	private void display(PrintStream stream)
	{
		for (char[] chars : grid)
		{
			for (char c : chars)
				stream.print(c + " ");
			stream.println();
		}
		stream.flush();
	}


	public void saveToFile(File file)
	{
		try
		{
			PrintStream stream = new PrintStream(file);
			display(stream);
			stream.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void changeCase(boolean upper)
	{
		for (int x = 0; x < grid.length; x++)
		{
			for (int y = 0; y < grid[x].length; y++)
			{
				String c = String.valueOf(grid[x][y]);
				c = upper ? c.toUpperCase() : c.toLowerCase();
				grid[x][y] = c.charAt(0);
			}
		}
	}

	public char[][] getGrid()
	{
		return grid;
	}

	public int getSize()
	{
		return size;
	}

}