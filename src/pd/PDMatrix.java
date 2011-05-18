package pd;


import pd.utils.Cell;
import pd.utils.Movement;
import pd.utils.Point;


public class PDMatrix implements Cloneable {
 
	private Cell[][] cells;
	private int rows;
	private int cols;
	
	public int maxPathLen = 0;

	@Override
	public PDMatrix clone() 
	{
		PDMatrix n = new PDMatrix(rows,cols);
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				n.add(new Point(i,j),this.get(new Point(i,j)));
			}
		}
		n.startPoint = this.startPoint;
		return n;
	}
	
	public int availablePoints = 0;
	
	private CellCountMap map;
	private Point startPoint;
	
	public PDMatrix (int _rows, int _cols){
		rows=_rows;
		cols=_cols;
		cells = new Cell[cols][rows];
		for (int i = 0; i < cols; i++) {
			for (int j = 0; j < rows; j++) {
				cells[i][j] = Cell.EMPTY;
			}
		}
		availablePoints = rows * cols;
		
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
		for (int i = 0 ;i<4;i++){
			sibs[i]=get(p.translate(e.versor()));
			e=Movement.convertTo(i+1);
		}
		return sibs;
	}

	public Cell get (Point p){
		if (p==null || p.x>=cols || p.y>=rows || p.x < 0 || p.y < 0)
			return null;
		
		return  cells[p.x][p.y];
	}

	public void remove (Point p){
		if (p!=null && p.x<cols && p.y<rows)
		{
			cells[p.x][p.y]=Cell.EMPTY;
			availablePoints++;
		}
		
	}
	
	public boolean add (Point p, Cell cell){
		cells[p.x][p.y]=cell;
		availablePoints += (cell == Cell.EMPTY) ? 1 : -1;
		return true;
	}

	public void putWall(int x, int y) {
		if (x<cols && y<rows)
		{
			cells[x][y]= Cell.WALL;
			availablePoints--;
		}
	}

	
	public Point getStartPoint()
	{
		return startPoint;
	}

	
	public void setStart(int x, int y, char ch) {
		switch(ch)
		{
			case 'N':
				Cell.setStart(Movement.UP);
				break;
			case 'S':
				Cell.setStart(Movement.DOWN);
				break;
			case 'W':
				Cell.setStart(Movement.LEFT);
				break;
			case 'E':
				Cell.setStart(Movement.RIGHT);
				break;
		}
		startPoint =new Point(x,y);
		cells[x][y]	= Cell.START;
		availablePoints--;
	}
	
	


	public boolean contains(Point nextPoint) {
		return (nextPoint.x >= 0 && nextPoint.y >= 0 && nextPoint.y < rows && nextPoint.x < cols);
	}
	public int getRows(){
		return rows;
	}
	public int getCols(){
		return cols;
	}
	
}
