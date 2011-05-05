package pd.cells;

import pd.utils.Point;

public class UpDownCell extends Cell {
	public UpDownCell(Point p) {
		super(p);
	}

	@Override
	public Point nextPoint(Point p) {
		if (p.x == position.x && p.y == position.y - 1)
			return new Point(position.x, position.y + 1);
		if (p.x == position.x && p.y == position.y + 1)
			return new Point(position.x, position.y - 1);
		return null;
	}
}
