package pd.solvers;

import java.io.IOException;
import java.util.Stack;

import pd.CellCountMap;
import pd.PDMatrix;
import pd.utils.Cell;
import pd.utils.Movement;
import pd.utils.Point;

public class PDSolver {
	private static Stack<Cell> bestStack = new Stack<Cell>();
	private static int bestC = 0;
	private static int curC = 0;
	
	public static Stack<Cell> Solve(PDMatrix mat, String method, Integer seconds)
	{
		if (method.equals("exact"))
			return PDExactSolver.Solve(mat);
		else if (method.equals("approx"))
			return PDApproximateSolver.solve(mat, seconds);
		return null;
	}	
}
