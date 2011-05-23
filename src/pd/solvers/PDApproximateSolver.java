package pd.solvers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.Stack;
import java.util.TreeSet;

import pd.PDMatrix;
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
	public static boolean solved = false;
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
		
		// We first make a fast aStar algorithm to find greedy solutions.
		for (PDHeuristicSolution solvePath : solvePaths) {
			Stack<Cell> stck = solvePath.randStar();
			if (stck != null) {
				if (solvePath.bestC > solSize) {
					solSize = solvePath.bestC;
					bestSolution = stck;
					listener.setBestLength(solSize + 1);
				}
				// Paths is now being populated with each valid solvePath.
				// It avoids having impossible solutions like unreachable paths.
				paths.add(solvePath);
				validPaths++;
			}
		}
		
		// After the aStar algorithm is done, we need to rebuild the heap of
		// each solution
		// Since aStar sorts it with the minimum distance to the start point.
		// But now we need the longest distance.
		for (PDHeuristicSolution solvePath : paths) {
			if (solvePath.bestStack != null) {
				solvePath.rebuildHeap();
			}
		}
		
		// We enter on the loop where the hillclimbing begins.
		Random r = new Random();
		while (true) {
			// We explore each posible solution.
			for (PDHeuristicSolution solvePath : paths) {
				if (solvePath.bestStack != null) {
					Stack<Cell> stck = solvePath.explore((seconds * 100) / validPaths + 10);
					if (stck != null) {
						if (solvePath.bestC > solSize) {
							solSize = solvePath.bestC;
							bestSolution = stck;
							listener.setBestLength(solSize + 1);
						}
					}
				}
			}
			
			// Here we make a random exploration with some of the discarded
			// solutions.
			// We found out that by doing this, we might skip local maximums and
			// have better results on long term.
			int i = 0, target = r.nextInt(secondaryPaths.size() + 1);
			PDHeuristicSolution toReplace = null;
			for (PDHeuristicSolution solvePath : secondaryPaths) {
				if (target == i) {
					if (solvePath.bestStack != null) {
						Stack<Cell> stck = solvePath.explore((seconds * 100) / validPaths + 10);
						if (stck != null) {
							if (solvePath.bestC >= solSize) {
								solSize = solvePath.bestC;
								bestSolution = stck;
								listener.setBestLength(solSize + 1);
								toReplace = paths.pollLast();
								paths.add(solvePath);
							}
						}
						
					}
				}
				i++;
			}
			if (toReplace != null)
				secondaryPaths.add(toReplace);
			
			// If we're done we quit.
			if (listener.getTotalComputeTime() > seconds * 1000 || solved)
				break;
			
			// We rebuild the set, since the best length of each solution might
			// have changed.
			TreeSet<PDHeuristicSolution> pathsRebuilder = new TreeSet<PDHeuristicSolution>(
					new Comparator<PDHeuristicSolution>() {
						@Override
						public int compare(PDHeuristicSolution o1, PDHeuristicSolution o2) {
							return ((o2.bestC - o1.bestC) == 0) ? 1 : (o2.bestC - o1.bestC);
						}
					});
			

			// We move all the paths we consider no longer importance since they
			// have a short position.
			while (paths.size() > validPaths && validPaths >= 2) {
				secondaryPaths.add(paths.pollLast());
			}
			
			// We rebuild the paths.
			// If any solution is completely explored, then we take an old from
			// the secondaryPaths.
			while (paths.size() > 0) {
				PDHeuristicSolution p = paths.pollFirst();
				if (p.heap.size() > 0)
					pathsRebuilder.add(p);
				else if (secondaryPaths.size() > 0)
					pathsRebuilder.add(secondaryPaths.pollFirst());
			}
			
			
			// If the new paths no longer have elements, then it's a cut.
			if (pathsRebuilder.size() == 0)
				break;
			
			paths = pathsRebuilder;
			
			// We keep reducing the amount of valid paths until we get 2 or 3.
			if (validPaths >= 2)
				validPaths /= 2;
			
		}
		
		listener.setEnd();
		listener.setBestLength(solSize + 1);
		return bestSolution;
	}
	
}
