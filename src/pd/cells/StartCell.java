package pd.cells;

import pd.utils.Point;

public abstract class StartCell extends Cell{
	protected Point position;
	
	public StartCell(Point p) {
		position=p;
	}
	
}
