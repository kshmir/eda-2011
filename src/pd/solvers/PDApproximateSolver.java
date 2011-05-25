package pd.solvers;

import java.util.Stack;
import java.util.TreeSet;

import pd.PDMatrix;
import pd.solvers.PDHeuristicSolution.Solution;
import pd.utils.Cell;
import pd.utils.Movement;
import pd.utils.Point;
import front.EventListener;
import front.PrintAction;

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
	private TreeSet<PDHeuristicSolution>	solvePaths		= new TreeSet<PDHeuristicSolution>();
	/*
	 * Global instance of the matrix, this doesn't avoid Threading but helps out
	 * to reduce some memory.
	 */
	public static PDMatrix					m;
	/*
	 * Helps cutting the solver.
	 */
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
	
	// Hill Climbing solution.
	public Stack<Cell> solve() {
		// Starts up the listener and finds solvable points.
		listener.setStart();
		// Builds instances of solutions
		buildSolvePaths();
		
		
		while (true) {
			// We explore for each point a new fast random path.
			for (PDHeuristicSolution solvePath : solvePaths) {
				Solution sol = null;
				// Fast random explore
				sol = solvePath.randStar();
				// Rebuild heap
				solvePath.rebuildHeap();
				// If there's a solution we try to expand it.
				if (sol != null) {
					if (sol.path.size() > solSize) {
						solSize = sol.path.size();
						bestSolution = sol.path;
						listener.setBestLength(solSize);
						// Listeners
						listener.addAll(sol.path, PrintAction.PROGRESS);
						listener.addAll(sol.path, PrintAction.RESULT);
					}
					int len = sol.path.size();
					Solution max = sol;
					do {
						sol = max;
						max = null;
						for (int j = 1; j < len - 3; j++) {
							// We explore a better solution.
							Solution s = solvePath.explore(j, sol);
							if (s != null) {
								// Listeners
								listener.addAll(s.path, PrintAction.PROGRESS);
								if (max == null || s.path.size() > max.path.size()) {
									if (s.path.size() > solSize) {
										solSize = s.path.size();
										bestSolution = s.path;
										listener.setBestLength(solSize);
										// Listeners
										listener.addAll(s.path, PrintAction.BEST_SO_FAR);
									}
									else if (s.path.size() == solSize)
									{
										if (listener.repeats)
											listener.addAll(s.path, PrintAction.BEST_SO_FAR);
									}
									// Listeners
									listener.addAll(s.path, PrintAction.RESULT);
									max = s;
								}
							}
						}
						if (max != null) {
							solvePath.endNode = max.startNode;
							len = max.path.size();
						}
						// The cut is a local maximum.
					} while (max != null);
					
				}
				
			}
			
			// The algorithm ends when solved
			if (listener.getTotalComputeTime() > seconds * 1000)
				break;
		}
		
		listener.setEnd();
		listener.setBestLength(solSize);
		return bestSolution;
	}
}
