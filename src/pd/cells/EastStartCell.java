package pd.cells;

import pd.utils.Point;

public class EastStartCell extends StartCell{
	public EastStartCell(Point p) {
		super(p);
	}

//Las coordenadas crecen hacia abajo y hacia la izquierda
	@Override
	public Point nextPoint(Point p) {
		return new Point(super.position.getX()-1,super.position.getY());
	}

}
