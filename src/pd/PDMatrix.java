package pd;

import java.util.Comparator;
import java.util.TreeMap;

import pd.cells.Cell;
import pd.cells.StartCell;
import pd.utils.Movement;
import pd.utils.Point;

public class PDMatrix {

	private TreeMap<Class<? extends Cell>,Integer> availableBlocks; 
	private Cell[][] cells;
	private int rows;
	private int cols;
	
	public PDMatrix (int _rows, int _cols){
		rows=_rows;
		cols=_cols;
		cells = new Cell[rows][cols];
		availableBlocks = new TreeMap<Class<? extends Cell>, Integer>(new Comparator<Class<? extends Cell>>() {
			@Override
			public int compare(Class<? extends Cell> o1,
					Class<? extends Cell> o2) {
				return o1.hashCode() - o2.hashCode();
			}
		});
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
