package pd.utils;

public class Direction implements Comparable<Direction> {
	public Point p;
	public Movement m;
	public Cell c;
	
	private int heuristicValue = 0;
	
	public Direction(Point _p, Movement _m, Cell _c, int hValue)
	{
		p = _p;
		m = _m;
		c = _c;
		heuristicValue = hValue;
	}

	@Override
	public int compareTo(Direction o) {
		return heuristicValue - o.heuristicValue;
	}
}
