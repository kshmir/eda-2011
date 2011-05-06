package pd;

import pd.cells.Cell;

public class CellCountMap {
	

	private Cell[] cells;
	private int[] counts;
	private int totalCount = 0;
	
	
	public CellCountMap()
	{
		counts = new int[7];
		cells = new Cell[7];
		
		// TODO: Initialize cells here.
	}
	public int totalPiecesLeft() {
		return totalCount;
	}
	public int totalPiecesLeft(Cell cell) {
		return totalPiecesLeft(cell.ordinal());
	}
	public int totalPiecesLeft(int index) {
		return counts[index];
	}
	public void setTotalPiecesLeft(Cell cell, int n) {
		setTotalPiecesLeft(cell.ordinal(), n);
	}
	public void setTotalPiecesLeft(int index, int n) {
		counts[index] = n;
	}
	public void decreasePiecesLeft(Cell t) {
		decreasePiecesLeft(t.ordinal());
	}
	public void decreasePiecesLeft(int index) {
		counts[index]--;
	}
	public void incrementPiecesLeft(Cell t) {
		incrementPiecesLeft(t.ordinal());
	}
	public void incrementPiecesLeft(int index) {
		counts[index]++;
	}

}
