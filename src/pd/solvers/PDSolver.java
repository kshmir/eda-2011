package pd.solvers;

import java.util.Stack;

import pd.PDMatrix;
import pd.utils.Cell;
import front.EventListener;

public class PDSolver {
	private static Stack<Cell> bestStack = new Stack<Cell>();
	private static int bestC = 0;
	private static int curC = 0;
	
	public static Stack<Cell> solve(PDMatrix mat, String method, Integer seconds, EventListener i)
	{
		if (method.equals("exact"))
			return PDExactSolver.solve(mat,i);
		else if (method.equals("approx"))
			return PDApproximateSolver.solve(mat, seconds,i);
		return null;
	}	
}
