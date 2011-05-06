package pd.utils;

import pd.utils.Movement;

public enum Cell {
	LEFTUP, UPRIGHT, RIGHTDOWN, DOWNLEFT, UPDOWN, LEFTRIGHT, CROSS, WALL, EMPTY, START;

	private Movement[] moves;
	private static Cell[] cells = { Cell.LEFTUP, Cell.UPRIGHT, Cell.RIGHTDOWN,
			Cell.DOWNLEFT, Cell.UPDOWN, Cell.LEFTRIGHT, Cell.CROSS, Cell.WALL,
			Cell.EMPTY, Cell.START };

}
