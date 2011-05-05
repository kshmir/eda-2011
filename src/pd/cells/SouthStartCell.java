package pd.cells;

import pd.utils.Point;

public class SouthStartCell extends StartCell {
	public SouthStartCell(Point p) {
		super(p);
	}

	@Override
	public Point nextPoint(Point p) {
		if (p.equals(super.position))
			return new Point(super.position.getX(), super.position.getY() + 1);
		return null;
	}

}
