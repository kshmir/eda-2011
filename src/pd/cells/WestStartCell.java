package pd.cells;

import pd.utils.Point;

public class WestStartCell extends StartCell {
	public WestStartCell(Point p) {
		super(p);
	}

	@Override
	public Point nextPoint(Point p) {
		if (p.equals(super.position))
			return new Point(super.position.getX() + 1, super.position.getY());
		return null;
	}

}
