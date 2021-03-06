package pd.utils;


/**
 * @author MurciÈlagos
 * This enumerator is used for a fast recognition of the direction received
 */
public enum Cell {
	LEFTUP, UPRIGHT, RIGHTDOWN, DOWNLEFT, UPDOWN, LEFTRIGHT, CROSS, WALL, EMPTY, START;

	public static Movement startDirection;

	public static Cell[] cells = { Cell.LEFTUP, Cell.UPRIGHT, Cell.RIGHTDOWN,
			Cell.DOWNLEFT, Cell.UPDOWN, Cell.LEFTRIGHT, Cell.CROSS, Cell.WALL,
			Cell.EMPTY, Cell.START };

	/**
	 * @author MurciÈlagos
	 * This method is used to convert an ordinal into its corresponing Cell enumerator.
	 */
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
	
	/**
	 * @author MurciÈlagos
	 * This method return Which Cells Entities are compatibles with each one.
	 * This method was created to improve the DFS efficiency.
	 */
	public int[] getCompatibles(Movement e)
	{
		switch (this) {
		case LEFTUP:
			if (e == Movement.UP)
				return Movement.UP.getCompatible();
			else if (e == Movement.LEFT)
				return Movement.LEFT.getCompatible();
			break;
		case UPRIGHT:
			if (e == Movement.RIGHT)
				return Movement.RIGHT.getCompatible();
			else if (e == Movement.UP)
				return Movement.UP.getCompatible();
			break;
		case RIGHTDOWN:
			if (e == Movement.DOWN)
				return Movement.DOWN.getCompatible();
			else if (e == Movement.RIGHT)
				return Movement.RIGHT.getCompatible();
			break;
		case DOWNLEFT:
			if (e == Movement.LEFT)
				return Movement.LEFT.getCompatible();
			else if (e == Movement.DOWN)
				return Movement.DOWN.getCompatible();
			break;

		case LEFTRIGHT:
			if (e != Movement.RIGHT && e != Movement.LEFT)
				return Movement.NONE.getCompatible();
			else
				return e.getCompatible();
		case UPDOWN:
			if (e != Movement.UP && e != Movement.DOWN)
				return Movement.NONE.getCompatible();
			else
				return e.getCompatible();
		case START:
			return startDirection.getCompatible();
		case CROSS:
			return e.getCompatible();
		}
		return Movement.NONE.getCompatible();
	}

	/**
	 * @author MurciÈlagos
	 * This method simulates de behaiviour of a Cell.
	 */
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

	@Override
	public String toString() {
		switch(this) {
		case CROSS:
			return "┼";
		case UPDOWN:
			return "│";
		case LEFTRIGHT:
			return "─";
		case LEFTUP:
			return "┘";
		case DOWNLEFT:
			return "┐";
		case RIGHTDOWN:
			return "┌";
		case UPRIGHT:
			return "└";
		case START:
			return "*";
		case WALL:
			return "█";
		case EMPTY:
			return " ";
		}
		return "█";
	}
}
