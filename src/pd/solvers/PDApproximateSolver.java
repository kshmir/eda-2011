package pd.solvers;

import java.util.ArrayList;
import java.util.Stack;

import pd.PDMatrix;
import pd.utils.Cell;
import pd.utils.DiGraphImpl;
import pd.utils.HeapTree;
import pd.utils.Movement;
import pd.utils.Point;
import front.EventListener;

public class PDApproximateSolver {
	private static Stack<Cell> bestSolution = new Stack<Cell>();
	private static ArrayList<HeuristicSolvePath> solvePaths = new ArrayList<HeuristicSolvePath>();
	private static long startMilis;
	protected PDMatrix m;

	private ArrayList<HeuristicSolvePath> getSolvePaths()
	{
		for (int i = 0; i < m.getCols(); i++) {
			for (int j = 0; j < m.getRows(); j++) {
				Point p = new Point(i,j);
				if (m.get(p) == Cell.EMPTY)
				{
					if (i == 0) {
						if (j == 0)
						{
							solvePaths.add(new HeuristicSolvePath(m.getStartPoint(),p,Cell.startDirection,Movement.DOWN));
							solvePaths.add(new HeuristicSolvePath(m.getStartPoint(),p,Cell.startDirection,Movement.RIGHT));
						}
						else if (j == m.getRows() - 1)
						{
							
						}
							
					}
				}
			}
		}
		return null;
	}

	private static int hf(PDMatrix m, Point p) {
		return (int) (Integer.MAX_VALUE - (Math
				.sqrt((m.getStartPoint().x - p.x) * (m.getStartPoint().x - p.x)
						+ (m.getStartPoint().y - p.y)
						* (m.getStartPoint().y - p.y))));
	}

	public static Stack<Cell> solve(PDMatrix m, Integer seconds, EventListener i) {
		m = m.clone();
		return bestSolution;
	}

	protected static class CellLocation implements Comparable<CellLocation> {
		public CellLocation parent;
		public Cell node;
		public Point location;
		public Movement movement;
		public int length;

		@Override
		public int compareTo(CellLocation arg0) {
			return 0;
		}

		private CellLocation() {
		}

		public CellLocation(Point start, Movement startDirection) {
			location = start;
			movement = startDirection;
			length = 1;
		}

		public CellLocation fromCell(CellLocation parent, Cell node) {
			CellLocation n = new CellLocation();
			n.parent = parent;
			n.node = node;
			n.length = parent.length++;
			n.movement = node.NextDir(parent.movement);
			return n;
		}
	}

	public class HeuristicSolvePath extends DiGraphImpl<CellLocation> {
		private HeapTree<CellLocation> heap = new HeapTree<CellLocation>();
		private boolean greedyDone = false;
		private Point startPoint;
		private Movement startMovement;
		private Point solvePoint;
		private Movement solveMovement;

		public HeuristicSolvePath(Point startPoint,
				Point solvePoint, Movement startMov, Movement solveMov) {
			this.startPoint = startPoint;
			this.startMovement = startMov;
			this.solvePoint = solvePoint;
			this.solveMovement = solveMov;
		}

		private void greedySolve() {

		}

	}
}
