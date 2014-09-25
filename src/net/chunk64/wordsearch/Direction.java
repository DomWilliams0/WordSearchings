package net.chunk64.wordsearch;

import net.chunk64.Main;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public enum Direction
{
	NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST;

	private static final Direction[] ALLOWED;

	static
	{
		// todo specify which directions are allowed in settings
		List<Direction> list = new ArrayList<>();
		list.add(NORTH_EAST);
		list.add(EAST);
		list.add(SOUTH_EAST);
		list.add(SOUTH);

		// weighted
		list.add(EAST);
		list.add(SOUTH);

		ALLOWED = list.toArray(new Direction[list.size()]);
	}


	/**
	 * @return True if new point is valid/not outside the grid
	 */
	public boolean update(Point point, WordSearch wordSearch)
	{
		switch (this)
		{
			case NORTH_EAST:
				point.y--;
				point.x++;
				break;
			case EAST:
				point.x++;
				break;
			case SOUTH_EAST:
				point.x++;
				point.y++;
				break;
			case SOUTH:
				point.y++;
				break;
			case SOUTH_WEST:
				point.y++;
				point.x--;
				break;
			case NORTH:
				point.y--;
				break;
			case WEST:
				point.x--;
				break;
			case NORTH_WEST:
				point.y--;
				point.x--;
				break;
		}

		final int preX = point.x;
		final int preY = point.y;

		point.y = Math.max(point.y, 0);
		point.y = Math.min(point.y, wordSearch.getSize() - 1);
		point.x = Math.max(point.x, 0);
		point.x = Math.min(point.x, wordSearch.getSize() - 1);

		return preX == point.x && preY == point.y;
	}


	public static Direction getRandomDirection()
	{
		return ALLOWED[Main.RANDOM.nextInt(ALLOWED.length)];
	}
}