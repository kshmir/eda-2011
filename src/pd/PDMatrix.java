package pd;

import pd.cells.Cell;
import pd.cells.StartCell;
import pd.utils.Point;
import pd.utils.Movement;

public class PDMatrix {

	private Cell[][] cells;
	private int rows;
	private int cols;
	
	public PDMatrix (int x, int y){
		rows=y;
		cols=x;
		cells = new Cell[rows][cols];
	}
	
	public Cell[] siblings (Point p){
		Cell[] sibs = new Cell[4];
					
			
			
		return null;
	}

	public Cell get (Point p){
		return  cells[p.getX()][p.getY()];
	}

	public void remove (Point p){
		cells[p.getX()][p.getY()]=null;
	}
	
	public boolean add (Point p, Cell cell){
		if (get(p)!=null){
			return false;
		}
		cells[p.getX()][p.getY()]=cell;
		return true;
	}

	public void putWall(int i, int j) {
		
		
	}

	public void setStart(int i, int j) {
		//cells[i][j]=new StartCell() 
		
	}

}
