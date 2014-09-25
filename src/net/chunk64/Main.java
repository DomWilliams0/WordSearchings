package net.chunk64;

import net.chunk64.wordsearch.WordSearch;

import java.util.Random;

public class Main
{
	public static final Random RANDOM = new Random();
	public static boolean SHOW_SOLUTIONS = false;

//	public static void main(String[] args)
//	{
//		WordSearch wordSearch = WordSearch.loadFromFile(new File("wordsearch.txt"));
//		wordSearch.changeCase(true);
//
//		PanelController controller = new GUI().getController();
//		controller.setWordSearch(wordSearch);
//		controller.changeView(View.WORDSEARCH);
//	}

	public static void main(String[] args)
	{
		try
		{
			if (args.length != 1 && args.length != 2)
				throw new IllegalArgumentException();

			String input = args[0];

			int size, wordcount;
			if (input.equals("small"))
			{
				size = 10;
				wordcount = 4;
			}
			else if (input.equals("medium"))
			{
				size = 20;
				wordcount = 10;
			}
			else if (input.equals("large"))
			{
				size = 50;
				wordcount = 30;
			}
			else
				throw new IllegalArgumentException();

			SHOW_SOLUTIONS = args.length == 2 && args[1].equals("cheat");
			new WordSearch(size, wordcount).displayGrid();

		} catch (IllegalArgumentException e)
		{
			e.printStackTrace();
			System.err.println("Usage: <small | medium | large>");
			System.exit(0);
		}

	}
}