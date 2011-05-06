package pd;

import pd.cells.Cell;
import pd.utils.Movement;
import pd.utils.Point;

public class PDSolver {
	public static void Solve(PDMatrix mat, String method)
	{
		if (method.equals("exact"))
			exactSolver(mat,mat.getStartPoint(), mat.getStartCell());
	}	
	
	private static void exactSolver(PDMatrix mat, Point p, Cell currentCell, Movement d)
	{
		for (int i = 0; i < 7; i++) {
			CellCountMap cc = mat.getCellCountMap();
			if (cc.totalPiecesLeft(i) > 0)
			{
				cc.decreasePiecesLeft(i);
				
				
				
				
				
				
				
				
				
				cc.incrementPiecesLeft(i);
			}
		}
	}
}
