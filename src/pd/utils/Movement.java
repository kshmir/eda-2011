package pd.utils;

public enum Movement {
	UP(0, -1), DOWN(0, 1), LEFT(-1, 0), RIGHT(1, 0), NONE(0, 0);
	
	private Point				p;
	
	private static Movement[]	moves		= { Movement.UP, Movement.DOWN, Movement.LEFT, Movement.RIGHT,
			Movement.NONE					};
	
	private static int[][]		compatibles	= {
			{ Cell.CROSS.ordinal(), Cell.DOWNLEFT.ordinal(), Cell.RIGHTDOWN.ordinal(), Cell.UPDOWN.ordinal() },
			{ Cell.CROSS.ordinal(), Cell.LEFTUP.ordinal(), Cell.UPDOWN.ordinal(), Cell.UPRIGHT.ordinal() },
			{ Cell.CROSS.ordinal(), Cell.RIGHTDOWN.ordinal(), Cell.UPRIGHT.ordinal(), Cell.LEFTRIGHT.ordinal() },
			{ Cell.CROSS.ordinal(), Cell.LEFTRIGHT.ordinal(), Cell.LEFTUP.ordinal(), Cell.DOWNLEFT.ordinal() }, {} };
	
	public int[] getCompatible() {
		return compatibles[this.ordinal()];
	}
	
	/**
	 * Builds a movement from it's internal constructor.
	 */
	Movement(int x, int y) {
		p = new Point(x, y);
	}
	
	/**
	 * Represents a Point which is equivalent to the Movement Enum given.
	 * 
	 * @return The versor.
	 */
	public Point versor() {
		return applyTo(new Point());
	}
	
	/**
	 * Makes a movement over a given Point.
	 * 
	 * @param The
	 *            Point which needs a movement to be applied on.
	 * @return The point with the movement applied on it
	 */
	public Point applyTo(Point origin) {
		return origin.translate(new Point(p.x, p.y));
	}
	
	/**
	 * Builds the enum from it's integer value.
	 * 
	 * @param value
	 *            Integer value.
	 * @return Movement to which the value belongs to, or Movement.NONE if it
	 *         doesn't correspond.
	 */
	public static Movement convertTo(int value) {
		try {
			return moves[value];
		} catch (Exception e) {
			return Movement.NONE;
		}
	}
	
	public Movement inverse() {
		switch (this) {
			case UP:
				return DOWN;
			case DOWN:
				return UP;
			case LEFT:
				return RIGHT;
			case RIGHT:
				return LEFT;
			default:
				return NONE;
		}
	}
	
}
