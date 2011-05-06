package pd;

import pd.cells.Cell;
import pd.utils.Point;

public class PDSolver {
	public static void Solve(PDMatrix mat, String method)
	{
		if (method.equals("exact"))
			exactSolver(mat,mat.getStartPoint(), mat.getStartCell());
	}	
	
	private static void exactSolver(PDMatrix mat, Point p, Cell currentCell)
	{
		Class<? extends Cell>[] classes = mat.getCellTypes();
		for (int i = 0; i < classes.length; i++) {
			Class<? extends Cell> cl = classes[i];
			CellCountMap cc = mat.getCellCount();
			if (cc.totalPiecesLeft(i) > 0)
			{
				cc.decreasePiecesLeft(i);
				
				
				
				
				
				
				
				
				
				cc.incrementPiecesLeft(i);
			}
		}
	}
}
