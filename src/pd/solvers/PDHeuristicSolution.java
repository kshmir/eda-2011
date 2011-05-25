package pd.solvers;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.Stack;
import java.util.TreeMap;

import pd.CellCountMap;
import pd.PDMatrix;
import pd.utils.Cell;
import pd.utils.Movement;
import pd.utils.Point;
import front.EventListener;

/**
 * Finds and improves the solution in the given point.
 */
public class PDHeuristicSolution implements Comparable<PDHeuristicSolution> {
	
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
		while (stacks.size() - 1 < d)
			stacks.add(null);
		if (stacks.get(d) == null)
			stacks.set(d, new Stack<PDCellNode>());
		
		stacks.get(d).push(cell);
	}
	
	// Gets an element in the array/heap.
	private PDCellNode getElement(Integer d) {
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
		heap.clear();
		stacks.clear();
	}
	
	// Point from which we start (find a solution)
	private Point			startPoint;
	// Movement of the start point.
	private Movement		startMovement;
	// Point from which we start actually moving.
	public Point			solvePoint;
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
	public PDCellNode		endNode;
	
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
	
	public Solution randStar() {
		// Add the possible movements to the heap.
		for (int i : solveMovement.getCompatible()) {
			if (i != 6) {
				PDCellNode l = new PDCellNode(solvePoint, Cell.cells[i].NextDir(solveMovement), m.getCellCountMap()
						.clone(), Cell.cells[i]);
				l.countMap.decreasePiecesLeft(i);
				setElement(l.distTostart(), l);
				heap.add(l.distTostart());
			}
		}
		
		long startTime = listener.getTotalComputeTime();
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
			if (listener.getTotalComputeTime() - startTime > 5 || listener.getTotalComputeTime() > seconds * 1000)
				return null;
			
			// We move to the next if there's no movement to make or if we are
			// out of the map.
			if (currentMovement == Movement.NONE || !m.contains(nextPoint))
				continue;
			
			
			
			
			
			// Cut condition.
			if (nextPoint.equals(startPoint) && currentMovement.inverse().equals(startMovement)) {
				
				Solution sol = new Solution();
				sol.path = rebuildMovements(current, sol);
				sol.startNode = current;
				endNode = current;
				return sol;
				
			}
			
			// We check if we already have something here...
			Cell hist = current.historyPoints(nextPoint);
			if (m.get(nextPoint) == Cell.EMPTY && hist == null) {
				// If it's a valid position we find the matching cells.
				int[] moves = current.cell.getCompatibles(currentMovement);
				for (int j = 0; j < moves.length; j++) {
					int i = moves[j];
					// We check if we can put the cross or not.
					if (current.countMap.totalPiecesLeft(i) > 0 && i != 6) {
						PDCellNode l = PDCellNode.fromCell(current, Cell.cells[i]);
						int dist = r.nextInt(l.distTostart() * 10 + 1);
						setElement(dist, l);
						heap.add(dist);
						
					}
				}
				
			}
		}
		return null;
	}
	
	public int numberOfSiblings(Point p, Solution s) {
		int n = 0;
		if (s.cells.get(p.translate(Movement.UP.versor())) != null)
			n++;
		if (s.cells.get(p.translate(Movement.DOWN.versor())) != null)
			n++;
		if (s.cells.get(p.translate(Movement.LEFT.versor())) != null)
			n++;
		if (s.cells.get(p.translate(Movement.RIGHT.versor())) != null)
			n++;
		
		return n;
	}
	
	// Improves a given path with the given data.
	private PDCellNode[] improveMe(Point startPoint, Movement startMovement, Point solvePoint, Movement solveMovement,
			CellCountMap map, PDCellNode startNode, PDCellNode next, Solution sol) {
		rebuildHeap();
		int nsiblings = numberOfSiblings(startPoint, sol);
		int nsiblingsEnd = numberOfSiblings(solvePoint, sol);
		if (nsiblings == 4 && nsiblingsEnd == 4)
		{
			return null;
		}
		for (int i : startMovement.getCompatible()) {
			if (map.totalPiecesLeft(i) > 0 && (nsiblings < 4 || i == 6) ) {
				PDCellNode l = new PDCellNode(startPoint, Cell.cells[i].NextDir(startMovement), map.clone(),
						Cell.cells[i]);
				l.countMap.decreasePiecesLeft(i);
				setElement(l.distTostart(), l);
				heap.add(l.distTostart());
			}
		}
		
		// It empties if no path's found.
		while (!heap.isEmpty()) {
			listener.addIteration();
			PDCellNode current = getElement(heap.peek());
			
			if (current == null)
				continue;
			
			if (current.length > 6)
				continue;
			Movement currentMovement = current.movement;
			Point nextPoint = currentMovement.applyTo(current.location);
			
			// We cut if we take too long or if it's time to leave.
			if (listener.getTotalComputeTime() > seconds * 1000) {
				return null;
			}
			
			// We move to the next if there's no movement to make or if we are
			// out of the map.
			if (currentMovement == Movement.NONE || !m.contains(nextPoint))
				continue;
			
			// Cut condition.
			if (solvePoint.equals(current.location) && currentMovement.equals(solveMovement) && current.length > 2
					&& (nsiblingsEnd < 4 || current.cell.ordinal() == 6)) {
				// If we reach here we've got a solution.
				return new PDCellNode[] { current.topParent(), current };
			}
			
			// If we don't exceed on length.
			if (current.length <= 5) {
				// We check there's no step over here.
				Cell hist = null;
				if (!nextPoint.equals(startPoint) && !nextPoint.equals(solvePoint))
					hist = current.historyPoints(nextPoint, sol);
				else
					hist = current.historyPoints(nextPoint);
				if (hist == Cell.CROSS) {
					// If we have a cross then we step over it...
					PDCellNode l = PDCellNode.fromCell(current, Cell.CROSS);
					l.countMap.incrementPiecesLeft(Cell.CROSS);
					int dist = PDCellNode.distanceFromPoints(l.location, startPoint);
					setElement(dist, l);
					heap.add(dist);
				} else {
					if (m.get(nextPoint) == Cell.EMPTY && hist == null) {
						// If it's a valid position we find the matching cells.
						int[] moves = current.cell.getCompatibles(currentMovement);
						for (int j = 0; j < moves.length; j++) {
							int i = moves[j];
							// We check if we can put the piece or not.
							if (current.countMap.totalPiecesLeft(i) > 0) {
								PDCellNode l = PDCellNode.fromCell(current, Cell.cells[i]);
								int dist = PDCellNode.distanceFromPoints(l.location, startPoint);
								setElement(dist, l);
								heap.add(dist);
							}
						}
						
					}
				}
			}
			
		}
		return null;
	}
	
	// Solution class
	public class Solution {
		public Stack<Cell>		path;
		public PDCellNode		startNode;
		public Map<Point, Cell>	cells	= new TreeMap<Point, Cell>(new Comparator<Point>() {
											@Override
											public int compare(Point o1, Point o2) {
												return (o1.x - o2.x == 0) ? o1.y - o2.y : o1.x - o2.x;
											}
										});
	}
	
	// Explore inside the solution
	public Solution explore(int len, Solution current) {
		PDCellNode startNode = endNode.clone();
		CellCountMap map = startNode.countMap;
		
		// Nodes in the improvement....
		//
		// |-------|-------|-------|-------|
		// prev node end next
		//
		// We build a path between node and end with a better length.
		// By exploring with a minimum path.
		//
		PDCellNode prev = null;
		PDCellNode node = startNode;
		PDCellNode end = null;
		PDCellNode next = null;
		listener.addIteration();
		
		int i = 0;
		while (i < len) {
			prev = node;
			node = node.parent;
			i++;
		}
		
		end = node.parent;
		if (end != null)
			next = end.parent;
		else
			return null;
		
		PDCellNode[] ends = null;
		/*
		 * if (node.cell == Cell.CROSS && end.cell != Cell.UPDOWN && end.cell !=
		 * Cell.RIGHTDOWN) ends = improveMe(end.location, next.movement,
		 * node.location, node.movement, end.cell, end.cell, map, startNode,
		 * next, current);
		 */

		if (ends == null)
			// Improves the path.
			ends = improveMe(end.location, next.movement, node.location, node.movement, map, startNode, next, current);
		
		if (ends == null) {
			return null;
		}
		
		// If we have a path then we return the solution.
		prev.parent = ends[1];
		ends[0].parent = next;
		startNode.countMap = ends[1].countMap.clone();
		
		Solution sol = new Solution();
		sol.path = rebuildMovements(startNode, sol);
		sol.startNode = startNode;
		
		return sol;
	}
	
	// Builds a return stack.
	private Stack<Cell> rebuildMovements(PDCellNode mov, Solution s) {
		Stack<Cell> cell = new Stack<Cell>();
		PDCellNode p = mov;
		do {
			Cell c = p.cell;
			cell.add(c);
			if (s != null)
				s.cells.put(p.location, c);
			p = p.parent;
		} while (p != null);
		Stack<Cell> ret = new Stack<Cell>();
		while (!cell.isEmpty()) {
			ret.push(cell.pop());
		}
		ret.add(Cell.START);
		return ret;
	}
	
	@Override
	public int compareTo(PDHeuristicSolution o) {
		return (-1) * PDCellNode.distanceFromPoints(this.solvePoint, m.getStartPoint())
				- PDCellNode.distanceFromPoints(o.solvePoint, m.getStartPoint());
	}
	
}
