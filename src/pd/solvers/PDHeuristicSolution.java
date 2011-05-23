package pd.solvers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Stack;

import pd.PDMatrix;
import pd.utils.Cell;
import pd.utils.Movement;
import pd.utils.Point;
import front.EventListener;
import front.PrintAction;

/**
 * Finds and improves the solution in the given point.
 */
public class PDHeuristicSolution {
	
	// Heap storing the distances (or the length of the path) for each stack of
	// nodes.
	public PriorityQueue<Integer>		heap	= new PriorityQueue<Integer>(11, new Comparator<Integer>() {
													@Override
													public int compare(Integer o1, Integer o2) {
														return (int) ((o1 - o2));
													}
												});
	// We use an arraylist of stacks to store the elements, since there might be
	// lots of repeated elements.
	// By doing this we achieved a nice boost of about 30% speed. Since the
	// access to the heap was a lot faster!
	public ArrayList<Stack<PDCellNode>>	stacks	= new ArrayList<Stack<PDCellNode>>(100);
	
	// Sets an element in the array/heap.
	private void setElement(Integer d, PDCellNode cell) {
		if (d < 0)
			d *= -1;
		while (stacks.size() - 1 < d)
			stacks.add(null);
		if (stacks.get(d) == null)
			stacks.set(d, new Stack<PDCellNode>());
		
		stacks.get(d).push(cell);
	}
	
	// Gets an element in the array/heap.
	private PDCellNode getElement(Integer d) {
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
	
	// Rebuilds the "heap".
	public void rebuildHeap() {
		PriorityQueue<Integer> new_heap = new PriorityQueue<Integer>(11, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return (int) ((o2 - o1));
			}
		});
		ArrayList<Stack<PDCellNode>> new_stacks = new ArrayList<Stack<PDCellNode>>();
		while (!stacks.isEmpty()) {
			Stack<PDCellNode> cells = stacks.remove(stacks.size() - 1);
			if (cells != null) {
				for (PDCellNode cellLocation : cells) {
					while (new_stacks.size() - 1 < cellLocation.length)
						new_stacks.add(null);
					if (new_stacks.get(cellLocation.length) == null)
						new_stacks.set(cellLocation.length, new Stack<PDCellNode>());
					new_stacks.get(cellLocation.length).push(cellLocation);
					new_heap.add((int) -cellLocation.length);
				}
			}
		}
		stacks = new_stacks;
		heap = new_heap;
	}
	
	// Point from which we start (find a solution)
	private Point			startPoint;
	// Movement of the start point.
	private Movement		startMovement;
	// Point from which we start actually moving.
	private Point			solvePoint;
	// Movement with with we start.
	private Movement		solveMovement;
	// Solution
	public Stack<Cell>		bestStack;
	// Solution length
	public int				bestC;
	
	// Needed in the algorithms.
	private PDMatrix		m;
	private EventListener	listener;
	private int				seconds;
	
	public PDHeuristicSolution(Point startPoint, Point solvePoint, Movement startMov, Movement solveMov, PDMatrix m,
			EventListener listener, int seconds) {
		this.startPoint = startPoint;
		this.startMovement = startMov;
		this.solvePoint = solvePoint;
		this.solveMovement = solveMov;
		this.m = m;
		this.listener = listener;
		this.seconds = seconds;
	}
	
	public Stack<Cell> randStar() {
		long startTime = System.currentTimeMillis();
		// Add the possible movements to the heap.
		for (int i : solveMovement.getCompatible()) {
			PDCellNode l = new PDCellNode(solvePoint, Cell.cells[i].NextDir(solveMovement),
					m.getCellCountMap().clone(), Cell.cells[i]);
			l.countMap.decreasePiecesLeft(i);
			setElement(l.distTostart(), l);
			heap.add(l.distTostart());
		}
		
		Random r = new Random();
		// It empties if no path's found.
		while (!heap.isEmpty()) {
			listener.addIteration();
			PDCellNode current = getElement(heap.peek());
			if (current == null)
				continue;
			Movement currentMovement = current.movement;
			Point nextPoint = currentMovement.applyTo(current.location);
			
			// We cut if we take too long or if it's time to leave.
			if (System.currentTimeMillis() - startTime > 10 || listener.getTotalComputeTime() > seconds * 1000)
				return null;
			
			// We move to the next if there's no movement to make or if we are
			// out of the map.
			if (currentMovement == Movement.NONE || !m.contains(nextPoint))
				continue;
			
			// Cut condition.
			if (nextPoint.equals(startPoint) && currentMovement.inverse().equals(startMovement)) {
				bestStack = rebuildMovements(current);
				listener.addAll(bestStack, listener.action);
				listener.addAll(bestStack, PrintAction.BEST_SO_FAR);
				bestC = current.length;
				return bestStack;
			}
			
			// We check if we already have something here...
			Cell hist = current.historyPoints(nextPoint);
			if (hist == Cell.CROSS) {
				// If we have a cross then we step over it...
				PDCellNode l = PDCellNode.fromCell(current, Cell.CROSS);
				l.countMap.incrementPiecesLeft(Cell.CROSS);
				int dist = r.nextInt((l.distTostart() * 10 + 1));
				setElement(dist, l);
				heap.add(dist);
			} else {
				if (m.get(nextPoint) == Cell.EMPTY && hist == null) {
					// If it's a valid position we find the matching cells.
					int[] moves = current.cell.getCompatibles(currentMovement);
					for (int j = 0; j < moves.length; j++) {
						int i = moves[j];
						// We check if we can put the cross or not.
						if (current.countMap.totalPiecesLeft(i) > 0
								&& (i != 6 || ((current.countMap.totalPiecesLeft(Cell.UPDOWN.ordinal()) == 0 && (currentMovement == Movement.UP || currentMovement == Movement.DOWN))
										|| (current.countMap.totalPiecesLeft(Cell.LEFTRIGHT.ordinal()) == 0 && (currentMovement == Movement.LEFT || currentMovement == Movement.RIGHT)) || valid(
										nextPoint, current)))) {
							PDCellNode l = PDCellNode.fromCell(current, Cell.cells[i]);
							int dist = r.nextInt(l.distTostart() * 10 + 1);
							setElement(dist, l);
							heap.add(dist);
						}
					}
					
				}
			}
		}
		return bestStack;
	}
	
	public Stack<Cell> explore(long miliseconds) {
		long start = System.currentTimeMillis();
		
		// Same as aStar
		while (!heap.isEmpty()) {
			listener.addIteration();
			PDCellNode current = getElement(heap.peek());
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
				if (listener.action == PrintAction.PROGRESS)
					listener.addAll(rebuildMovements(current), listener.action);
				
				if (current.length > bestC) {
					// If we have a better solution then we save it. It's a
					// local solution!
					bestStack = rebuildMovements(current);
					listener.addAll(bestStack, PrintAction.BEST_SO_FAR);
					bestC = current.length;
					if (current.length == m.maxPathLen) {
						// Cut condition.
						PDApproximateSolver.solved = true;
						return bestStack;
					}
				}
			}
			
			// Same as aStar
			Cell hist = current.historyPoints(nextPoint);
			if (hist == Cell.CROSS) {
				PDCellNode l = PDCellNode.fromCell(current, Cell.CROSS);
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
							PDCellNode l = PDCellNode.fromCell(current, Cell.cells[i]);
							setElement((int) l.length, l);
							heap.add((int) l.length);
						}
					}
				}
			}
		}
		return bestStack;
	}
	
	// Builds a return stack.
	private Stack<Cell> rebuildMovements(PDCellNode mov) {
		Stack<Cell> cell = new Stack<Cell>();
		PDMatrix mat = m.clone();
		PDCellNode p = mov;
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
	
	/**
	 * Tells whether a sibling vector is valid or not for a cross cell.
	 */
	private boolean valid(Point p, PDCellNode location) {
		if (p == null)
			return true;
		
		// Here we do the same as the exact solver, but harder
		// Since we don't have a current matrix of anything, we have to explore
		// Our current path given by the cellnode.
		int cnt = 4;
		PDCellNode c = location;
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
