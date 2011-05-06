package pd.solvers;

import java.io.IOException;
import java.util.Stack;

import pd.CellCountMap;
import pd.PDMatrix;
import pd.utils.Cell;
import pd.utils.Movement;
import pd.utils.Point;

public class PDExactSolver {
	private static boolean solved = false;
	private static Stack<Cell> bestStack = new Stack<Cell>();
	private static int bestC = 0;
	private static int curC = 0;
	
	public static Stack<Cell> Solve(PDMatrix mat)
	{
		long l1 = System.currentTimeMillis();
		
		solve(mat, mat.getStartPoint(), Cell.startDirection);
		
		System.out.println("Solve time: " + (System.currentTimeMillis() - l1));
		return bestStack;
	}	
	
	
	private static Stack<Cell> rebuildMovements(PDMatrix mat, Point p, Movement m)
	{
		Stack<Cell> cell = new Stack<Cell>();
		m = m.inverse();
		do {
			p = m.applyTo(p);
			Cell last = mat.get(p);
			cell.add(last);
			m = last.NextDir(m);
		}
		while(mat.get(p) != Cell.START);
		return cell;
	}
	
	private static boolean valid(Cell[] valid)
	{
		int c = 0;
		for (int i = 0; i < valid.length; i++) {
			if (valid[i] == Cell.EMPTY)
				c++;
		}
		return c == 3;
	}
	
	@SuppressWarnings("unchecked")
	private static void solve(PDMatrix mat, Point p, Movement currentMovement)
	{
		if (currentMovement == Movement.NONE)
			return;
		CellCountMap cc = mat.getCellCountMap();
		Point nextPoint = p;
		
		nextPoint = currentMovement.applyTo(nextPoint);

		if (!mat.contains(nextPoint)) {
			if (bestC < curC)
			{
				bestStack = rebuildMovements(mat, nextPoint, currentMovement);
				bestC = curC;
				if ((mat.availablePoints == 0 || cc.totalPiecesLeft() == 0) 
						&& cc.totalPiecesLeft(Cell.CROSS) == 0)
				{
					solved = true;
					return;
				}
			}
			
			return;
		}

		
		
		if (mat.get(nextPoint) == Cell.CROSS)
		{
			curC++;
			solve(mat, nextPoint, currentMovement);
			curC--;
		}
	
		if (mat.get(nextPoint) == Cell.EMPTY && mat.get(nextPoint) != Cell.START)
		{
			int[] compatibles = mat.get(p).getCompatibles(currentMovement);
			
			for (int j = 0; j < compatibles.length; j++) {
				int i = compatibles[j];
				if ( cc.totalPiecesLeft(i) > 0 
						&& ( i != 6 || 
					 (i == 6 && 

							 (
									 (cc.totalPiecesLeft(Cell.UPDOWN.ordinal()) == 0 
									 && (currentMovement == Movement.UP || currentMovement == Movement.DOWN)) 
							 ||
							 		 (cc.totalPiecesLeft(Cell.LEFTRIGHT.ordinal()) == 0 
									 && (currentMovement == Movement.LEFT || currentMovement == Movement.RIGHT))
							 ||
							 		 (valid(mat.siblings(nextPoint)))
							 )
					 ))
				)
				{
					cc.decreasePiecesLeft(i);
					curC++ ;
					Movement m = Cell.cells[i].NextDir(currentMovement);
					mat.add(nextPoint, Cell.cells[i]);
					solve(mat, nextPoint, m);
					mat.add(nextPoint, Cell.EMPTY);
					curC--;
					cc.incrementPiecesLeft(i);
				}
			}
		}
	}
}

