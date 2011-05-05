package pd.cells;

import pd.utils.Point;

public class RightStartCell extends StartCell {
	public RightStartCell(Point p) {
		super(p);
	}

	@Override
	public Point nextPoint(Point p) {
		if (p.equals(super.position))
			return new Point(super.position.x + 1, super.position.y);
		return null;
	}

}
