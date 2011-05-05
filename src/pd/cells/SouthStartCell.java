package pd.cells;

import pd.utils.Point;

public class SouthStartCell extends StartCell{
	public SouthStartCell(Point p) {
		super(p);
	}
//Las coordenadas crecen hacia abajo y hacia la izquierda
	@Override
	public Point nextPoint(Point p) {
		return new Point(super.position.getX(),super.position.getY()+1);
	}

}
