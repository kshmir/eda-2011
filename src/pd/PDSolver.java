package pd;

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
			exactSolver(mat,mat.getStartPoint(),mat.get(mat.getStartPoint()).NextDir(Cell.startDirection));
		
		return bestStack;
	}	
	
	@SuppressWarnings("unchecked")
	private static void exactSolver(PDMatrix mat, Point p, Movement currentMovement)
	{
		if (currentMovement == Movement.NONE)
			return;
		
		Point nextPoint = p.translate(currentMovement);
		
		if (!mat.contains(nextPoint) && bestStack.size() < solutionStack.size())
				bestStack = (Stack<Point>) solutionStack.clone();
		
		if (mat.get(nextPoint) != Cell.EMPTY && mat.get(nextPoint) != Cell.START)
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
					solutionStack.pop();
					cc.incrementPiecesLeft(i);
				}
			}
		}
	}
}
