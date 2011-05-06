package pd;

import pd.cells.Cell;
import pd.cells.DownStartCell;
import pd.cells.LeftStartCell;
import pd.cells.RightStartCell;
import pd.cells.UpStartCell;
import pd.cells.WallCell;
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
		
		return  cells[p.x][p.y];
	}

	public void remove (Point p){
		if (p!=null && p.x<cols && p.y<rows)
			cells[p.x][p.y]=null;
	}
	
	public boolean add (Point p, Cell cell){
		if (cell==null || p==null)
			return false;
		if (get(p)!=null){
			return false;
		}
		cells[p.x][p.y]=cell;
		return true;
	}

	public void putWall(int i, int j) {
		if (i<cols && j<rows)
			cells[i][j]=new WallCell(new Point(i,j));
	}

	
	public Point getStartPoint()
	{
		return startPoint;
	}
	
	public Cell getStartCell()
	{
		return get(get(startPoint).nextPoint(startPoint));
	}
	
	
	
	public void setStart(int i, int j, char ch) {
		if (i<cols && j<rows){
			Movement e = Movement.convertTo(new Integer(ch));
			Cell c;
			if(e==Movement.UP)
				c = new UpStartCell(new Point(i,j));
			if(e==Movement.DOWN)
				c = new DownStartCell(new Point(i,j));
			if(e==Movement.LEFT)
				c = new LeftStartCell(new Point(i,j));
			if(e==Movement.RIGHT)
				c = new RightStartCell(new Point(i,j));
			else
				c=null;
			startPoint = new Point(i,j);
			cells[i][j]=c;  
		}
	}
	
}
