package pd;


import pd.utils.Cell;
import pd.utils.Movement;
import pd.utils.Point;


public class PDMatrix {
 
	private Cell[][] cells;
	private int rows;
	private int cols;

	
	private CellCountMap map;
	private Point startPoint;
	
	public PDMatrix (int _rows, int _cols){
		rows=_rows;
		cols=_cols;
		cells = new Cell[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				cells[i][j] = Cell.EMPTY;
			}
		}
		map = new CellCountMap();
	}
	
	
	public CellCountMap getCellCountMap()
	{
		return map;
	}
	
	public Cell[] siblings (Point p){
		if (p == null || p.x >= cols || p.y >= rows)
			return null;
		
		Cell[] sibs = new Cell[4];
		Movement e = Movement.UP;
		for (int i = 0 ;i>4;i++){
			sibs[i]=get(p.translate(e.versor()));
			e=Movement.convertTo(i+1);
		}
		return sibs;

	}

	public Cell get (Point p){
		if (p==null || p.x>=cols || p.y>=rows)
			return null;
		
		return  cells[p.y][p.x];
	}

	public void remove (Point p){
		if (p!=null && p.x<cols && p.y<rows)
			cells[p.y][p.x]=null;
	}
	
	public boolean add (Point p, Cell cell){
		if (cell==null || p==null)
			return false;
		if (get(p)!=null){
			return false;
		}
		cells[p.y][p.x]=cell;
		return true;
	}

	public void putWall(int x, int y) {
		if (x<cols && y<rows)
			cells[y][x]= Cell.WALL;
	}

	
	public Point getStartPoint()
	{
		return startPoint;
	}
	
	
	
	
	
	public void setStart(int i, int j, char ch) {
		switch(ch)
		{
			case 'N':
				cells[i][j]	
				break;
			case 'S':
				
				break;
			case 'E':
				
				break;
			case 'W':
				
				break;
		}
	}
	
	public void print()
	{
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				System.out.print(cells[cols -j - 1][i].ordinal());
			}
			System.out.println();
		}
	}
	
}
