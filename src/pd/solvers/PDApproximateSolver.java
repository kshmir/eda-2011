package pd.solvers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Stack;
import java.util.TreeSet;

import pd.CellCountMap;
import pd.PDMatrix;
import pd.utils.Cell;
import pd.utils.Movement;
import pd.utils.Point;
import front.EventListener;
import front.PrintAction;

public class PDApproximateSolver {
	private Stack<Cell>						bestSolution	= new Stack<Cell>();
	private int								solSize;
	private ArrayList<HeuristicSolvePath>	solvePaths		= new ArrayList<HeuristicSolvePath>();
	private long							startMilis;
	protected static PDMatrix				m;
	private EventListener					listener;
	private int								seconds;
	private boolean							solved;
	
	private void getSolvePaths() {
		for (int i = 0; i < m.getCols(); i++) {
			for (int j = 0; j < m.getRows(); j++) {
				Point p = new Point(i, j);
				if (m.get(p) == Cell.EMPTY) {
					if (i == 0) {
						solvePaths
								.add(new HeuristicSolvePath(m.getStartPoint(), p, Cell.startDirection, Movement.RIGHT));
					} else if (i == m.getCols() - 1) {
						solvePaths
								.add(new HeuristicSolvePath(m.getStartPoint(), p, Cell.startDirection, Movement.LEFT));
					}
					
					if (j == 0) {
						solvePaths
								.add(new HeuristicSolvePath(m.getStartPoint(), p, Cell.startDirection, Movement.DOWN));
					} else if (j == m.getRows() - 1) {
						solvePaths.add(new HeuristicSolvePath(m.getStartPoint(), p, Cell.startDirection, Movement.UP));
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
		startMilis = System.currentTimeMillis();
		getSolvePaths();
		int validPaths = 0;
		
		TreeSet<HeuristicSolvePath> secondaryPaths = new TreeSet<HeuristicSolvePath>(
				new Comparator<HeuristicSolvePath>() {
					@Override
					public int compare(HeuristicSolvePath o1, HeuristicSolvePath o2) {
						return ((o2.bestC - o1.bestC) == 0) ? 1 : (o2.bestC - o1.bestC);
					}
				});
		
		TreeSet<HeuristicSolvePath> paths = new TreeSet<HeuristicSolvePath>(new Comparator<HeuristicSolvePath>() {
			@Override
			public int compare(HeuristicSolvePath o1, HeuristicSolvePath o2) {
				return ((o2.bestC - o1.bestC) == 0) ? 1 : (o2.bestC - o1.bestC);
			}
		});
		for (HeuristicSolvePath solvePath : solvePaths) {
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
		for (HeuristicSolvePath solvePath : paths) {
			Stack<Cell> stck = solvePath.aStar();
			if (stck != null) {
				solvePath.rebuildHeap();
			}
		}
		while (true) {
			for (HeuristicSolvePath solvePath : paths) {
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
			
			TreeSet<HeuristicSolvePath> pathsRebuilder = new TreeSet<HeuristicSolvePath>(
					new Comparator<HeuristicSolvePath>() {
						@Override
						public int compare(HeuristicSolvePath o1, HeuristicSolvePath o2) {
							return ((o2.bestC - o1.bestC) == 0) ? 1 : (o2.bestC - o1.bestC);
						}
					});
			
			if (listener.getTotalComputeTime() > seconds * 1000)
				break;
			
			while (paths.size() > validPaths && validPaths >= 2) {
				secondaryPaths.add(paths.pollLast());
			}
			
			while (paths.size() > 0) {
				HeuristicSolvePath p = paths.pollFirst();
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
	
	protected static class CellLocation implements Comparable<CellLocation> {
		public CellLocation	parent;
		public Cell			cell;
		public Point		location;
		public Movement		movement;
		public CellCountMap	countMap;
		public char			length;
		
		@Override
		public int compareTo(CellLocation p) {
			return distanceFromPoints(this.location, m.getStartPoint().translate(Cell.startDirection.versor()))
					- distanceFromPoints(p.location, m.getStartPoint().translate(Cell.startDirection.versor()));
		}
		
		public int distTostart() {
			return distanceFromPoints(this.location, m.getStartPoint().translate(Cell.startDirection.versor()));
		}
		
		public static int distanceFromPoints(Point p, Point p2) {
			return (int) Math.sqrt((p.x - p2.x) * (p.x - p2.x) + (p.y - p2.y) * (p.y - p2.y));
		}
		
		public Cell historyPoints(Point p) {
			CellLocation c = this;
			while (c != null) {
				if (c.location.equals(p))
					return c.cell;
				c = c.parent;
			}
			return null;
		}
		
		private CellLocation() {
		}
		
		public CellLocation(Point start, Movement startDirection, CellCountMap map, Cell node) {
			location = start;
			movement = startDirection;
			length = 1;
			this.cell = node;
			this.countMap = map.clone();
		}
		
		public static CellLocation fromCell(CellLocation parent, Cell node) {
			CellLocation n = new CellLocation();
			n.parent = parent;
			n.cell = node;
			n.length = (char) (parent.length + 1);
			n.location = parent.location.translate(parent.movement.versor());
			n.movement = node.NextDir(parent.movement);
			n.countMap = parent.countMap.clone();
			
			n.countMap.decreasePiecesLeft(node);
			
			return n;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((cell == null) ? 0 : cell.hashCode());
			result = prime * result + ((countMap == null) ? 0 : countMap.hashCode());
			result = prime * result + length;
			result = prime * result + ((location == null) ? 0 : location.hashCode());
			result = prime * result + ((movement == null) ? 0 : movement.hashCode());
			result = prime * result + ((parent == null) ? 0 : parent.hashCode());
			return result;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			CellLocation other = (CellLocation) obj;
			if (cell != other.cell)
				return false;
			if (countMap == null) {
				if (other.countMap != null)
					return false;
			} else if (!countMap.equals(other.countMap))
				return false;
			if (length != other.length)
				return false;
			if (location == null) {
				if (other.location != null)
					return false;
			} else if (!location.equals(other.location))
				return false;
			if (movement != other.movement)
				return false;
			if (parent == null) {
				if (other.parent != null)
					return false;
			} else if (!parent.equals(other.parent))
				return false;
			return true;
		}
		
	}
	
	public class HeuristicSolvePath {
		private PriorityQueue<Integer>			heap	= new PriorityQueue<Integer>(11, new Comparator<Integer>() {
															@Override
															public int compare(Integer o1, Integer o2) {
																return (int) ((o1 - o2));
															}
														});
		private ArrayList<Stack<CellLocation>>	stacks	= new ArrayList<Stack<CellLocation>>(100);
		
		private void setElement(Integer d, CellLocation cell) {
			if (d < 0)
				d *= -1;
			while (stacks.size() - 1 < d)
				stacks.add(null);
			if (stacks.get(d) == null)
				stacks.set(d, new Stack<CellLocation>());
			
			stacks.get(d).push(cell);
		}
		
		private CellLocation getElement(Integer d) {
			if (d < 0)
				d *= -1;
			if (stacks.get(d) != null && !stacks.get(d).isEmpty()) {
				if (stacks.size() == 1)
					heap.poll();
				return stacks.get(d).pop();
			} else {
				heap.poll();
				return null;
			}
		}
		
		private void rebuildHeap() {
			PriorityQueue<Integer> new_heap = new PriorityQueue<Integer>(11, new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					return (int) ((o2 - o1));
				}
			});
			ArrayList<Stack<CellLocation>> new_stacks = new ArrayList<Stack<CellLocation>>();
			while (!stacks.isEmpty()) {
				Stack<CellLocation> cells = stacks.remove(stacks.size() - 1);
				if (cells != null) {
					for (CellLocation cellLocation : cells) {
						while (new_stacks.size() - 1 < cellLocation.length)
							new_stacks.add(null);
						if (new_stacks.get(cellLocation.length) == null)
							new_stacks.set(cellLocation.length, new Stack<CellLocation>());
						new_stacks.get(cellLocation.length).push(cellLocation);
						new_heap.add((int) -cellLocation.length);
					}
				}
			}
			stacks = new_stacks;
			heap = new_heap;
		}
		
		private Point		startPoint;
		private Movement	startMovement;
		private Point		solvePoint;
		private Movement	solveMovement;
		public Stack<Cell>	bestStack;
		private int			bestC;
		
		public HeuristicSolvePath(Point startPoint, Point solvePoint, Movement startMov, Movement solveMov) {
			this.startPoint = startPoint;
			this.startMovement = startMov;
			this.solvePoint = solvePoint;
			this.solveMovement = solveMov;
		}
		
		int	c			= 0;
		int	bestDist	= 100000;
		
		private Stack<Cell> aStar() {
			long startTime = System.currentTimeMillis();
			for (int i : solveMovement.getCompatible()) {
				CellLocation l = new CellLocation(solvePoint, Cell.cells[i].NextDir(solveMovement), m.getCellCountMap()
						.clone(), Cell.cells[i]);
				l.countMap.decreasePiecesLeft(i);
				setElement(l.distTostart(), l);
				heap.add(l.distTostart());
			}
			
			while (!heap.isEmpty()) {
				listener.addIteration();
				CellLocation current = getElement(heap.peek());
				if (current == null) {
					
					continue;
				}
				Movement currentMovement = current.movement;
				Point nextPoint = currentMovement.applyTo(current.location);
				
				if (System.currentTimeMillis() - startTime > 10 || listener.getTotalComputeTime() > seconds * 1000)
					return null;
				
				if (currentMovement == Movement.NONE || !m.contains(nextPoint))
					continue;
				
				if (nextPoint.equals(startPoint) && currentMovement.inverse().equals(startMovement)) {
					
					// If we're required to print each result then we send it.
					// We compare here in order to avoid making a
					// rebuildMovements on each result.
					
					bestStack = rebuildMovements(current);
					if (listener.action == PrintAction.RESULT)
						listener.addAll(bestStack, listener.action);
					
					listener.addAll(bestStack, PrintAction.BEST_SO_FAR);
					bestC = current.length;
					return bestStack;
				}
				
				Cell hist = current.historyPoints(nextPoint);
				if (hist == Cell.CROSS) {
					CellLocation l = CellLocation.fromCell(current, Cell.CROSS);
					l.countMap.incrementPiecesLeft(Cell.CROSS);
					setElement(l.distTostart(), l);
					heap.add(l.distTostart());
				} else {
					if (m.get(nextPoint) == Cell.EMPTY && hist == null) {
						int[] moves = current.cell.getCompatibles(currentMovement);
						for (int j = 0; j < moves.length; j++) {
							int i = moves[j];
							if (current.countMap.totalPiecesLeft(i) > 0
									&& (i != 6 || ((current.countMap.totalPiecesLeft(Cell.UPDOWN.ordinal()) == 0 && (currentMovement == Movement.UP || currentMovement == Movement.DOWN))
											|| (current.countMap.totalPiecesLeft(Cell.LEFTRIGHT.ordinal()) == 0 && (currentMovement == Movement.LEFT || currentMovement == Movement.RIGHT)) || valid(
											nextPoint, current)))) {
								CellLocation l = CellLocation.fromCell(current, Cell.cells[i]);
								setElement(l.distTostart(), l);
								heap.add(l.distTostart());
							}
						}
						
					}
				}
			}
			return bestStack;
		}
		
		private Stack<Cell> explore(long miliseconds) {
			long start = System.currentTimeMillis();
			
			while (!heap.isEmpty()) {
				listener.addIteration();
				
				CellLocation current = getElement(heap.peek());
				if (current == null)
					continue;
				Movement currentMovement = current.movement;
				Point nextPoint = currentMovement.applyTo(current.location);
				
				if (System.currentTimeMillis() - start > miliseconds || listener.getTotalComputeTime() > seconds * 1000) {
					break;
				}
				
				if (currentMovement == Movement.NONE || !m.contains(nextPoint))
					continue;
				
				if (nextPoint.equals(startPoint) && currentMovement.inverse().equals(startMovement)) {
					
					// If we're required to print each result then we send it.
					// We compare here in order to avoid making a
					// rebuildMovements on each result.
					if (listener.action == PrintAction.PROGRESS)
						listener.addAll(rebuildMovements(current), listener.action);
					
					if (current.length > bestC) {
						
						bestStack = rebuildMovements(current);
						listener.addAll(bestStack, PrintAction.BEST_SO_FAR);
						bestC = current.length;
					}
				}
				
				Cell hist = current.historyPoints(nextPoint);
				if (hist == Cell.CROSS) {
					CellLocation l = CellLocation.fromCell(current, Cell.CROSS);
					l.countMap.incrementPiecesLeft(Cell.CROSS);
					setElement((int) l.length, l);
					heap.add((int) l.length);
				} else {
					if (m.get(nextPoint) == Cell.EMPTY && hist == null) {
						int[] moves = current.cell.getCompatibles(currentMovement);
						for (int j = 0; j < moves.length; j++) {
							int i = moves[j];
							if (current.countMap.totalPiecesLeft(i) > 0
									&& (i != 6 || ((current.countMap.totalPiecesLeft(Cell.UPDOWN.ordinal()) == 0 && (currentMovement == Movement.UP || currentMovement == Movement.DOWN))
											|| (current.countMap.totalPiecesLeft(Cell.LEFTRIGHT.ordinal()) == 0 && (currentMovement == Movement.LEFT || currentMovement == Movement.RIGHT)) || valid(
											nextPoint, current)))) {
								CellLocation l = CellLocation.fromCell(current, Cell.cells[i]);
								setElement((int) l.length, l);
								heap.add((int) l.length);
							}
						}
					}
				}
			}
			return bestStack;
		}
		
		private Stack<Cell> rebuildMovements(CellLocation mov) {
			Stack<Cell> cell = new Stack<Cell>();
			PDMatrix mat = m.clone();
			CellLocation p = mov;
			do {
				Cell c = p.cell;
				mat.add(p.location, c);
				cell.add(c);
				p = p.parent;
			} while (p != null);
			Stack<Cell> ret = new Stack<Cell>();
			while (!cell.isEmpty()) {
				ret.push(cell.pop());
			}
			ret.add(Cell.START);
			return ret;
		}
	}
	
	/**
	 * Tells whether a sibling vector is valid or not for a cross cell.
	 */
	private boolean valid(Point p, CellLocation location) {
		if (p == null)
			return true;
		
		int cnt = 4;
		CellLocation c = location;
		Stack<Point> explored = new Stack<Point>();
		while (c != null) {
			if (c.location.equals(p.translate(Movement.UP.versor()))
					|| c.location.equals(p.translate(Movement.DOWN.versor()))
					|| c.location.equals(p.translate(Movement.LEFT.versor()))
					|| c.location.equals(p.translate(Movement.RIGHT.versor()))) {
				if (!explored.contains(c.location) && c.cell != Cell.CROSS) {
					explored.push(c.location);
					cnt--;
				}
			}
			c = c.parent;
		}
		
		Movement[] movs = new Movement[] { Movement.UP, Movement.DOWN, Movement.LEFT, Movement.RIGHT };
		
		for (int i = 0; i < movs.length; i++) {
			if (m.get(p.translate(movs[i].versor())) != Cell.EMPTY && m.get(p.translate(movs[i].versor())) != null
					&& m.get(p.translate(movs[i].versor())) != Cell.START)
				return false;
		}
		
		return cnt == 3;
	}
}
