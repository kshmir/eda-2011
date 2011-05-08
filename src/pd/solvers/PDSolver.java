package pd.solvers;

import java.util.Stack;

import pd.PDMatrix;
import pd.utils.Cell;
import front.EventListener;

/**
 * Solves the problem with the given matrix, cutting at the given seconds and returns the best stack found.
 * @author Murcielagos
 */
public class PDSolver {
	public static Stack<Cell> solve(PDMatrix mat, String method, Integer seconds, EventListener i)
	{
		if (method.equals("exact"))
			return new PDExactSolver(i).solve(mat);
		else if (method.equals("approx"))
			return PDApproximateSolver.solve(mat, seconds,i);
		return null;
	}	
}
