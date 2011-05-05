package pd.cells;

import pd.utils.Point;

public class CrossCell extends Cell{
	public CrossCell(Point p) {
		super(p);
	}

	@Override
	public Point nextPoint(Point p) {
		if (p.x == position.x - 1 && p.y == position.y)
			return new Point(position.x + 1, position.y);
		if (p.x == position.x + 1 && p.y == position.y)
			return new Point(position.x - 1, position.y);
		if (p.x == position.x - 1 && p.y == position.y)
			return new Point(position.x + 1, position.y);
		if (p.x == position.x + 1 && p.y == position.y)
			return new Point(position.x - 1, position.y);
		return null;
	}
}
