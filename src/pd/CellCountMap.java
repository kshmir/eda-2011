package pd;

import pd.cells.Cell;

public abstract class CellCountMap {
	public abstract int totalPiecesLeft();
	public abstract int totalPiecesLeft(Class<? extends Cell> t);
	public abstract int totalPiecesLeft(int index);
	public abstract int setTotalPiecesLeft(Class<? extends Cell> t);
}
