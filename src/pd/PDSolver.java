package pd;

import java.io.IOException;
import java.util.Stack;

import pd.utils.Cell;
import pd.utils.Movement;
import pd.utils.Point;

public class PDSolver {
	private static Stack<Point> bestStack = new Stack<Point>();
	private static Stack<Point> solutionStack = new Stack<Point>();
	public static Stack<Point> Solve(PDMatrix mat, String method)
	{
		if (method.equals("exact"))
			exactSolver(mat,
						mat.getStartPoint(),
						Cell.startDirection);
		
		return bestStack;
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
				if (bestStack.size() < solutionStack.size())
					bestStack = (Stack<Point>) solutionStack.clone();
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
					solutionStack.push(nextPoint);
					Movement m = Cell.cells[i].NextDir(currentMovement);
					mat.add(nextPoint, Cell.cells[i]);
					
					
					exactSolver(mat, nextPoint, m);
					mat.add(nextPoint, Cell.EMPTY);
					solutionStack.pop();
					cc.incrementPiecesLeft(i);
				}
			}
		}
	}
}
