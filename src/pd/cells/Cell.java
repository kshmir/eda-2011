package pd.cells;

import pd.utils.Point;

public abstract class Cell {
	protected Point position;
	public Cell(Point p) {
		position = p;
	}

	public static int getClassID()	{
		return -1;
	}
	
	public abstract Point nextPoint(Point p);
	
	public Point nextPoint(int x, int y){
		return nextPoint(new Point(x,y));
	}
	
}
