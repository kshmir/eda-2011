package pd.cells;

import pd.utils.Point;

public class WallCell extends Cell{

	public WallCell(Point p) {
		super(p);
	}

	@Override
	public Point nextPoint(Point p) {
		return null;
	}

}
