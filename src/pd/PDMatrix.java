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
		
		return  cells[p.x][p.y];
	}

	public void remove (Point p){
		if (p!=null && p.x<cols && p.y<rows)
			cells[p.x][p.y]=Cell.EMPTY;
	}
	
	public boolean add (Point p, Cell cell){
		cells[p.x][p.y]=cell;
		return true;
	}

	public void putWall(int x, int y) {
		if (x<cols && y<rows)
			cells[x][y]= Cell.WALL;
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
	}
	
	public void print()
	{
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				switch(cells[j][i]) {
					case CROSS:
						System.out.print("â”¼");
						break;
					case UPDOWN:
						System.out.print("â”‚");
						break;
					case LEFTRIGHT:
						System.out.print("â”€");
						break;
					case LEFTUP:
						System.out.print("â”˜");
						break;
					case DOWNLEFT:
						System.out.print("â”�");
						break;
					case RIGHTDOWN:
						System.out.print("â”Œ");
						break;
					case UPRIGHT:
						System.out.print("â””");
						break;
					case START:
						System.out.print("*");
						break;
					case WALL:
						System.out.print("â–ˆ");
						break;
					case EMPTY:
						System.out.print(" ");
						break;
				}
				
				
				
				
			}
			System.out.println();
		}
	}


	public boolean contains(Point nextPoint) {
		return (nextPoint.x >= 0 && nextPoint.y >= 0 && nextPoint.y < rows && nextPoint.x < cols);
	}
	
}
