package pd.solvers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Stack;
import java.util.TreeSet;

import pd.PDMatrix;
import pd.solvers.PDHeuristicSolution.Solution;
import pd.utils.Cell;
import pd.utils.Movement;
import pd.utils.Point;
import front.EventListener;

/**
 * Class which handles the approximate solutions of the problem.
 * 
 * @author cristian
 */
public class PDApproximateSolver {
	/**
	 * Contains the best solution found so far.
	 */
	private Stack<Cell>						bestSolution	= new Stack<Cell>();
	/*
	 * Contains the real length of the solution. Since crosses can change the
	 * counts.
	 */
	private int								solSize;
	/*
	 * Contains all the possible paths to explore.
	 */
	private ArrayList<PDHeuristicSolution>	solvePaths		= new ArrayList<PDHeuristicSolution>();
	public static PDMatrix					m;
	public static boolean					solved			= false;
	/*
	 * Contains the callbacks to the frontend.
	 */
	private EventListener					listener;
	/*
	 * Seconds given by the user before a cut is done.
	 */
	private int								seconds;
	
	/*
	 * Builds the points from which the solver should start the paths.
	 */
	private void buildSolvePaths() {
		/* Builds a new Solution instance from each valid place */
		for (int i = 0; i < m.getCols(); i++) {
			for (int j = 0; j < m.getRows(); j++) {
				Point p = new Point(i, j);
				if (m.get(p) == Cell.EMPTY) {
					if (i == 0) {
						solvePaths.add(new PDHeuristicSolution(m.getStartPoint(), p, Cell.startDirection,
								Movement.RIGHT, m, listener, seconds));
					} else if (i == m.getCols() - 1) {
						solvePaths.add(new PDHeuristicSolution(m.getStartPoint(), p, Cell.startDirection,
								Movement.LEFT, m, listener, seconds));
					}
					
					if (j == 0) {
						solvePaths.add(new PDHeuristicSolution(m.getStartPoint(), p, Cell.startDirection,
								Movement.DOWN, m, listener, seconds));
					} else if (j == m.getRows() - 1) {
						solvePaths.add(new PDHeuristicSolution(m.getStartPoint(), p, Cell.startDirection, Movement.UP,
								m, listener, seconds));
					}
				}
			}
		}
		
	}
	
	public PDApproximateSolver(PDMatrix m, Integer seconds, EventListener i) {
		PDApproximateSolver.m = m;
		this.seconds = seconds;
		this.listener = i;
	}
	
	public Stack<Cell> solve() {
		// Starts up the listener and finds solvable points.
		listener.setStart();
		// Builds instances of solutions
		buildSolvePaths();
		// Used to validate the paths to use.
		int validPaths = 0;
		
		// SecondaryPaths stores the solutions being discarted for the moment.
		// And is randomly checked in order to get out of local maximums.
		TreeSet<PDHeuristicSolution> secondaryPaths = new TreeSet<PDHeuristicSolution>(
				new Comparator<PDHeuristicSolution>() {
					@Override
					public int compare(PDHeuristicSolution o1, PDHeuristicSolution o2) {
						return ((o2.bestC - o1.bestC) == 0) ? 1 : (o2.bestC - o1.bestC);
					}
				});
		// Paths stores the paths currently being explored, it starts with all
		// the paths
		// but end ups with just 2 paths being explored constantly.
		TreeSet<PDHeuristicSolution> paths = new TreeSet<PDHeuristicSolution>(new Comparator<PDHeuristicSolution>() {
			@Override
			public int compare(PDHeuristicSolution o1, PDHeuristicSolution o2) {
				return ((o2.bestC - o1.bestC) == 0) ? 1 : (o2.bestC - o1.bestC);
			}
		});
		
		while (true) {
			for (PDHeuristicSolution solvePath : solvePaths) {
				Stack<Cell> stck = null;
				stck = solvePath.randStar();
				solvePath.bestStack = stck;
				solvePath.rebuildHeap();
				if (stck != null) {
					int len = stck.size();
					Solution max = null;
					
					do {
						max = null;
						for (int j = 1; j < len - 3; j++) {
							Solution s = solvePath.explore(j);
							if (s != null) {
								if (max == null || s.path.size() > max.path.size()) {
									if (s.path.size() > solSize) {
										solSize = s.path.size();
										bestSolution = s.path;
									}
									solvePath.bestStack = s.path;
									max = s;
									listener.setBestLength(solSize + 1);
									// listener.addAll(s.path, listener.action);
								}
							}
						}
						if (max != null)
							solvePath.endNode = max.startNode;
					} while (max != null);
					
				}
				
			}
			
			if (listener.getTotalComputeTime() > seconds * 1000 || solved)
				break;
		}
		
		listener.setEnd();
		listener.setBestLength(solSize + 1);
		return bestSolution;
	}
}
