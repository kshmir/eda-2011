package pd.cells;

import pd.utils.Point;

public abstract class Cell {
	protected Point position;
	public Cell(Point p) {
		position=p;
	}
	// Las coordenadas crecen hacia abajo y hacia la izquierda
	public abstract Point nextPoint(Point p);
}
