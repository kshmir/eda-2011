package pd;

import java.io.IOException;
import java.util.Stack;

import pd.utils.Cell;
import pd.utils.Movement;
import pd.utils.Point;

public class PDSolver {
	private static Stack<Cell> bestStack = new Stack<Cell>();
	private static int bestC = 0;
	private static int curC = 0;
	public static Stack<Cell> Solve(PDMatrix mat, String method)
	{
		long l1 = System.currentTimeMillis();
		if (method.equals("exact"))
			exactSolver(mat,
						mat.getStartPoint(),
						Cell.startDirection);
		
		System.out.println(System.currentTimeMillis() - l1);
		return bestStack;
	}	
	
	
	private static Stack<Cell> rebuildMovements(PDMatrix mat, Point p, Movement m)
	{
		Stack<Cell> cell = new Stack<Cell>();
		m = m.inverse();
		do{
			p = m.applyTo(p);
			Cell last = mat.get(p);
			cell.add(last);
			m = last.NextDir(m);
		}
		while(mat.get(p) != Cell.START);
		return cell;
	}
	
	@SuppressWarnings("unchecked")
	private static void exactSolver(PDMatrix mat, Point p, Movement currentMovement)
	{
		
		if (currentMovement == Movement.NONE)
			return;
		
		
		Point nextPoint = p;
		do
		{
			nextPoint = currentMovement.applyTo(nextPoint);

			if (!mat.contains(nextPoint)) {
				if (bestC < curC)
				{
					bestStack = rebuildMovements(mat, nextPoint, currentMovement);
					bestC = curC;
				}
				return;
			}
		} while(mat.get(nextPoint) == Cell.CROSS);
		
		
		
		
		
		if (mat.get(nextPoint) == Cell.EMPTY && mat.get(nextPoint) != Cell.START)
		{
			CellCountMap cc = mat.getCellCountMap();
			for (int i = 0; i < 7; i++) {
				if (cc.totalPiecesLeft(i) > 0)
				{
					cc.decreasePiecesLeft(i);
					curC++;
					Movement m = Cell.cells[i].NextDir(currentMovement);
					mat.add(nextPoint, Cell.cells[i]);
					exactSolver(mat, nextPoint, m);
					mat.add(nextPoint, Cell.EMPTY);
					curC--;
					cc.incrementPiecesLeft(i);
				}
			}
		}
	}
}
