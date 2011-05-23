package pd.solvers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Stack;
import java.util.TreeSet;

import pd.PDMatrix;
import pd.utils.Cell;
import pd.utils.Movement;
import pd.utils.Point;
import front.EventListener;

public class PDApproximateSolver {
	private Stack<Cell>						bestSolution	= new Stack<Cell>();
	private int								solSize;
	private ArrayList<HeuristicSolution>	solvePaths		= new ArrayList<HeuristicSolution>();
	public static PDMatrix					m;
	private EventListener					listener;
	private int								seconds;
	
	private void getSolvePaths() {
		for (int i = 0; i < m.getCols(); i++) {
			for (int j = 0; j < m.getRows(); j++) {
				Point p = new Point(i, j);
				if (m.get(p) == Cell.EMPTY) {
					if (i == 0) {
						solvePaths.add(new HeuristicSolution(m.getStartPoint(), p, Cell.startDirection, Movement.RIGHT,
								m, listener, seconds));
					} else if (i == m.getCols() - 1) {
						solvePaths.add(new HeuristicSolution(m.getStartPoint(), p, Cell.startDirection, Movement.LEFT,
								m, listener, seconds));
					}
					
					if (j == 0) {
						solvePaths.add(new HeuristicSolution(m.getStartPoint(), p, Cell.startDirection, Movement.DOWN,
								m, listener, seconds));
					} else if (j == m.getRows() - 1) {
						solvePaths.add(new HeuristicSolution(m.getStartPoint(), p, Cell.startDirection, Movement.UP, m,
								listener, seconds));
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
		listener.setStart();
		getSolvePaths();
		int validPaths = 0;
		
		TreeSet<HeuristicSolution> secondaryPaths = new TreeSet<HeuristicSolution>(new Comparator<HeuristicSolution>() {
			@Override
			public int compare(HeuristicSolution o1, HeuristicSolution o2) {
				return ((o2.bestC - o1.bestC) == 0) ? 1 : (o2.bestC - o1.bestC);
			}
		});
		
		TreeSet<HeuristicSolution> paths = new TreeSet<HeuristicSolution>(new Comparator<HeuristicSolution>() {
			@Override
			public int compare(HeuristicSolution o1, HeuristicSolution o2) {
				return ((o2.bestC - o1.bestC) == 0) ? 1 : (o2.bestC - o1.bestC);
			}
		});
		for (HeuristicSolution solvePath : solvePaths) {
			Stack<Cell> stck = solvePath.aStar();
			if (stck != null) {
				if (solvePath.bestC > solSize) {
					solSize = solvePath.bestC;
					bestSolution = stck;
					listener.setBestLength(solSize + 1);
				}
				paths.add(solvePath);
				validPaths++;
			}
		}
		for (HeuristicSolution solvePath : paths) {
			Stack<Cell> stck = solvePath.aStar();
			if (stck != null) {
				solvePath.rebuildHeap();
			}
		}
		while (true) {
			for (HeuristicSolution solvePath : paths) {
				if (solvePath.bestStack != null) {
					Stack<Cell> stck = solvePath.explore((seconds * 50) / validPaths);
					if (stck != null) {
						if (solvePath.bestC > solSize) {
							solSize = solvePath.bestC;
							bestSolution = stck;
							listener.setBestLength(solSize + 1);
						}
					}
				}
			}
			
			TreeSet<HeuristicSolution> pathsRebuilder = new TreeSet<HeuristicSolution>(
					new Comparator<HeuristicSolution>() {
						@Override
						public int compare(HeuristicSolution o1, HeuristicSolution o2) {
							return ((o2.bestC - o1.bestC) == 0) ? 1 : (o2.bestC - o1.bestC);
						}
					});
			
			if (listener.getTotalComputeTime() > seconds * 1000)
				break;
			
			while (paths.size() > validPaths && validPaths >= 2) {
				secondaryPaths.add(paths.pollLast());
			}
			
			while (paths.size() > 0) {
				HeuristicSolution p = paths.pollFirst();
				if (p.heap.size() > 0)
					pathsRebuilder.add(p);
				else if (secondaryPaths.size() > 0)
					pathsRebuilder.add(secondaryPaths.pollFirst());
			}
			
			if (pathsRebuilder.size() == 0)
				break;
			
			paths = pathsRebuilder;
			
			System.out.println(secondaryPaths.size() + " " + pathsRebuilder.size());
			
			if (validPaths >= 2)
				validPaths /= 2;
			
		}
		
		listener.setEnd();
		listener.setBestLength(solSize + 1);
		return bestSolution;
	}
	
}
