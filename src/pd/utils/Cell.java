package pd.utils;

import pd.utils.Movement;

public enum Cell {
	LEFTUP(new Movement[] { Movement.LEFT, Movement.UP }), UPRIGHT(
			new Movement[] { Movement.UP, Movement.RIGHT }), RIGHTDOWN(
			new Movement[] { Movement.RIGHT, Movement.DOWN }), DOWNLEFT(
			new Movement[] { Movement.LEFT, Movement.DOWN }), UPDOWN(
			new Movement[] { Movement.DOWN, Movement.UP }), LEFTRIGHT(
			new Movement[] { Movement.LEFT, Movement.RIGHT }), CROSS(
			new Movement[] { Movement.LEFT, Movement.RIGHT, Movement.DOWN,
					Movement.UP }), WALL(new Movement[] { Movement.NONE }), EMPTY(new Movement[] { Movement.NONE }), START(
			new Movement[] { Movement.NONE });

	private Movement[] direction;
	public static Cell[] cells = { Cell.LEFTUP, Cell.UPRIGHT, Cell.RIGHTDOWN,
			Cell.DOWNLEFT, Cell.UPDOWN, Cell.LEFTRIGHT, Cell.CROSS, Cell.WALL,
			Cell.EMPTY, Cell.START };

	Cell(Movement[] m) {
		direction = m;
	}
	
	public static Cell convertTo(int value){
		try
		{
			return cells[value];
		}
		catch (Exception e)
		{
			return Cell.EMPTY;
		}
	}
	public Movement NextDir(Movement e){
		if (direction.length<=1){
			return direction[0]; 
		}
		
		return null;
	}
	
}