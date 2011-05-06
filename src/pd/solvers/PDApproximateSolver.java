package pd.solvers;

import java.io.IOException;
import java.util.Stack;

import pd.CellCountMap;
import pd.PDMatrix;
import pd.utils.Cell;
import pd.utils.Direction;
import pd.utils.HeapTree;
import pd.utils.Movement;
import pd.utils.Point;

public class PDApproximateSolver {
	
	private static Stack<Cell> bestStack = new Stack<Cell>();
	private static int bestC = 0;
	private static int curC = 0;
	
	private static int hf(PDMatrix m, Point p) {
		return (int) (Integer.MAX_VALUE - (Math.sqrt(	(m.getStartPoint().x - p.x) * (m.getStartPoint().x - p.x) + 
				(m.getStartPoint().y - p.y) * (m.getStartPoint().y - p.y))));
	}
	
	public static Stack<Cell> solve(PDMatrix m, Integer seconds)
	{
		HeapTree<Direction> directions = new HeapTree<Direction>();
		
		return bestStack;
	}
}
