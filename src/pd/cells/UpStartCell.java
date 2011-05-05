package pd.cells;

import pd.utils.Point;

public class UpStartCell extends StartCell {
	public UpStartCell(Point p) {
		super(p);
	}

	@Override
	public Point nextPoint(Point p) {
		if (p.equals(super.position))
			return new Point(super.position.x, super.position.y - 1);
		return null;
	}

}
