package pd.utils;

import java.util.Random;

public enum Cell {
	LEFTUP, UPRIGHT, RIGHTDOWN, DOWNLEFT, UPDOWN, LEFTRIGHT, CROSS, WALL, EMPTY, START;

	public static Movement startDirection;

	public static Cell[] cells = { Cell.LEFTUP, Cell.UPRIGHT, Cell.RIGHTDOWN,
			Cell.DOWNLEFT, Cell.UPDOWN, Cell.LEFTRIGHT, Cell.CROSS, Cell.WALL,
			Cell.EMPTY, Cell.START };

	public static Cell convertTo(int value) {
		try {
			
			return cells[value];
		} catch (Exception e) {
			return Cell.EMPTY;
		}
	}
	
	Cell()	{}

	public static void setStart(Movement e) {
		
		startDirection = e;
	}

	public Movement NextDir(Movement e) {

		switch (this) {
		case LEFTUP:
			if (e == Movement.RIGHT)
				return Movement.UP;
			else if (e == Movement.DOWN)
				return Movement.LEFT;
			break;
		case UPRIGHT:
			if (e == Movement.DOWN)
				return Movement.RIGHT;
			else if (e == Movement.LEFT)
				return Movement.UP;
			break;
		case RIGHTDOWN:
			if (e == Movement.LEFT)
				return Movement.DOWN;
			else if (e == Movement.UP)
				return Movement.RIGHT;
			break;
		case DOWNLEFT:
			if (e == Movement.UP)
				return Movement.LEFT;
			else if (e == Movement.RIGHT)
				return Movement.DOWN;
			break;

		case LEFTRIGHT:
			if (e != Movement.RIGHT && e != Movement.LEFT)
				return Movement.NONE;
			else
				return e;
		case UPDOWN:
			if (e != Movement.UP && e != Movement.DOWN)
				return Movement.NONE;
			else
				return e;

		case CROSS:
			return e;
		}
		return Movement.NONE;
	}

}
