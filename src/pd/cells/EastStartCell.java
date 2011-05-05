package pd.cells;

import pd.utils.Point;

public class EastStartCell extends StartCell {
	public EastStartCell(Point p) {
		super(p);
	}

	@Override
	public Point nextPoint(Point p) {
		if (p.equals(super.position))
			return new Point(super.position.getX() - 1, super.position.getY());
		return null;
	}

}
