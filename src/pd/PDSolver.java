package pd;

import pd.cells.Cell;

public class PDSolver {
	public static void Solve(PDMatrix mat, String method)
	{
		if (method.equals("exact"))
			exactSolver(mat,mat.getStartPoint(), mat.getStartCell());
	}	
	
	private static void exactSolver(PDMatrix mat, Point p, Cell currentCell)
	{
	}
}
