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
		Movement e = Movement.UP;
		for (int i = 0 ;i>4;i++){
			sibs[i]=get(p.translate(e.Versor()));
			e=Movement.convertTo(i+1);
		}
		return sibs;
	}

	public Cell get (Point p){
		return  cells[p.x][p.y];
	}

	public void remove (Point p){
		cells[p.x][p.y]=null;
	}
	
	public boolean add (Point p, Cell cell){
		if (get(p)!=null){
			return false;
		}
		cells[p.x][p.y]=cell;
		return true;
	}

	public void putWall(int i, int j) {
		
		
	}

	public void setStart(int i, int j) {
		//cells[i][j]=new StartCell() 
		
	}

}
