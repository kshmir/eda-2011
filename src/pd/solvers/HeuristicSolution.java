package pd.solvers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Stack;

import pd.PDMatrix;
import pd.utils.Cell;
import pd.utils.Movement;
import pd.utils.Point;
import front.EventListener;
import front.PrintAction;

public class HeuristicSolution {
	public PriorityQueue<Integer>		heap	= new PriorityQueue<Integer>(11, new Comparator<Integer>() {
													@Override
													public int compare(Integer o1, Integer o2) {
														return (int) ((o1 - o2));
													}
												});
	public ArrayList<Stack<CellNode>>	stacks	= new ArrayList<Stack<CellNode>>(100);
	
	private void setElement(Integer d, CellNode cell) {
		if (d < 0)
			d *= -1;
		while (stacks.size() - 1 < d)
			stacks.add(null);
		if (stacks.get(d) == null)
			stacks.set(d, new Stack<CellNode>());
		
		stacks.get(d).push(cell);
	}
	
	private CellNode getElement(Integer d) {
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
	
	public void rebuildHeap() {
		PriorityQueue<Integer> new_heap = new PriorityQueue<Integer>(11, new Comparator<Integer>() {
			@Override
			public int compare(Integer o1, Integer o2) {
				return (int) ((o2 - o1));
			}
		});
		ArrayList<Stack<CellNode>> new_stacks = new ArrayList<Stack<CellNode>>();
		while (!stacks.isEmpty()) {
			Stack<CellNode> cells = stacks.remove(stacks.size() - 1);
			if (cells != null) {
				for (CellNode cellLocation : cells) {
					while (new_stacks.size() - 1 < cellLocation.length)
						new_stacks.add(null);
					if (new_stacks.get(cellLocation.length) == null)
						new_stacks.set(cellLocation.length, new Stack<CellNode>());
					new_stacks.get(cellLocation.length).push(cellLocation);
					new_heap.add((int) -cellLocation.length);
				}
			}
		}
		stacks = new_stacks;
		heap = new_heap;
	}
	
	private Point			startPoint;
	private Movement		startMovement;
	private Point			solvePoint;
	private Movement		solveMovement;
	public Stack<Cell>		bestStack;
	public int				bestC;
	private PDMatrix		m;
	private EventListener	listener;
	private int				seconds;
	
	public HeuristicSolution(Point startPoint, Point solvePoint, Movement startMov, Movement solveMov, PDMatrix m,
			EventListener listener, int seconds) {
		this.startPoint = startPoint;
		this.startMovement = startMov;
		this.solvePoint = solvePoint;
		this.solveMovement = solveMov;
		this.m = m;
		this.listener = listener;
		this.seconds = seconds;
	}
	
	int	c			= 0;
	int	bestDist	= 100000;
	
	public Stack<Cell> aStar() {
		long startTime = System.currentTimeMillis();
		for (int i : solveMovement.getCompatible()) {
			CellNode l = new CellNode(solvePoint, Cell.cells[i].NextDir(solveMovement), m.getCellCountMap().clone(),
					Cell.cells[i]);
			l.countMap.decreasePiecesLeft(i);
			setElement(l.distTostart(), l);
			heap.add(l.distTostart());
		}
		
		while (!heap.isEmpty()) {
			listener.addIteration();
			CellNode current = getElement(heap.peek());
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
				CellNode l = CellNode.fromCell(current, Cell.CROSS);
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
							CellNode l = CellNode.fromCell(current, Cell.cells[i]);
							setElement(l.distTostart(), l);
							heap.add(l.distTostart());
						}
					}
					
				}
			}
		}
		return bestStack;
	}
	
	public Stack<Cell> explore(long miliseconds) {
		long start = System.currentTimeMillis();
		
		while (!heap.isEmpty()) {
			listener.addIteration();
			
			CellNode current = getElement(heap.peek());
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
				CellNode l = CellNode.fromCell(current, Cell.CROSS);
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
							CellNode l = CellNode.fromCell(current, Cell.cells[i]);
							setElement((int) l.length, l);
							heap.add((int) l.length);
						}
					}
				}
			}
		}
		return bestStack;
	}
	
	private Stack<Cell> rebuildMovements(CellNode mov) {
		Stack<Cell> cell = new Stack<Cell>();
		PDMatrix mat = m.clone();
		CellNode p = mov;
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
	private boolean valid(Point p, CellNode location) {
		if (p == null)
			return true;
		
		int cnt = 4;
		CellNode c = location;
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
