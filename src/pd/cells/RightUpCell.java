package pd.cells;

import pd.utils.Point;

public class RightUpCell extends Cell{

	public RightUpCell(Point p) {
		super(p);
	}

	@Override
	public Point nextPoint(Point p) {
		if(p.x == position.x+1 && p.y==position.y)
			return new Point(position.x,position.y-1);
		if(p.x == position.x && p.y==position.y-1)
			return new Point(position.x+1,position.y);
		return null;
	}

}
