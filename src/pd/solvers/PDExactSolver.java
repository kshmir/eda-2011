package pd.solvers;

import java.util.Stack;

import pd.CellCountMap;
import pd.PDMatrix;
import pd.utils.Cell;
import pd.utils.Movement;
import pd.utils.Point;
import front.EventListener;
import front.PrintAction;

/**
 * Solves the problem with the given matrix, finding the best solution so far.
 * @author Murcielagos
 */
public class PDExactSolver {
	// Tells wether the algorithm was solved or not.
	private boolean solved = false;
	// Stores the best stack used.
	private Stack<Cell> bestStack = new Stack<Cell>();
	// Length of the best path found so far.
	private int bestC = 1;
	// Length of the current path we're looking for.
	private int curC = 1;
	// Used to make callbacks to the frontend.
	private EventListener listener;
	// Object which stores the amount of elements in the matrix
	private CellCountMap cellCount;
	
	public PDExactSolver(EventListener itr)
	{
		listener = itr;
	}
	
	public Stack<Cell> solve(PDMatrix mat)
	{
		listener.setStart();
		
		cellCount = mat.getCellCountMap();
		
		//Calls the main Deep First Search function.
		solve(mat, mat.getStartPoint(), Cell.startDirection);
		
		listener.setEnd();
		return bestStack;
	}	
	
	// Searches all the solutions in the tree of solutions.
	// By marking and unmarking the matrix with the point and it's movements.
	private void solve(PDMatrix mat, Point p, Movement currentMovement)
	{
		listener.addIteration();
		
		// Cut condition: Having solved the problem, or not having a movement to make.
		if (currentMovement == Movement.NONE || solved)
			return;
		
		// Next point were moving to.
		Point nextPoint = p;
		
		// Gets the next point were moving to. 
		nextPoint = currentMovement.applyTo(nextPoint);
		
		// If we are outside the matrix then it's a full path.
		if (!mat.contains(nextPoint)) {
			
			// If we're required to print each result then we send it.
			// We compare here in order to avoid making a rebuildMovements on each result.
			if (listener.action == PrintAction.RESULT)	
				listener.addAll(rebuildMovements(mat, nextPoint, currentMovement), PrintAction.RESULT);	
			
			if (bestC < curC)
			{
				listener.setSameLengthFound(0);
				
				// We've got a better result to store and show.
				bestStack = rebuildMovements(mat, nextPoint, currentMovement);
				listener.setBestLength(bestC = curC);
				listener.addAll(bestStack, PrintAction.BEST_SO_FAR);
								
				if (mat.availablePoints == 0 || curC - 1 == mat.maxPathLen)
				{
					// If there are no more crosses and points to add, then it's a cut.
					// If there are no more pieces then it's a cut.
					solved = true;
					return;
				}
			}
			else if (bestC == curC && listener.repeats)
			{
				listener.setSameLengthFound(listener.getSameLengthFound() + 1);
				listener.addAll(rebuildMovements(mat, nextPoint, currentMovement), PrintAction.BEST_SO_FAR);
			}
			return;
		}

		// We get the cell we're stepped into.
		Cell currentCell = mat.get(nextPoint);
		
		// If it's a cross then we go deeply and move on with the same direction
		// (We make a hop over the cross)
		if (currentCell == Cell.CROSS)
		{
			curC++;
			cellCount.decreasePiecesLeft(Cell.CROSS);
			solve(mat, nextPoint, currentMovement);
			cellCount.incrementPiecesLeft(Cell.CROSS);
			curC--;
		}
		else
		if (currentCell == Cell.EMPTY && currentCell != Cell.START)
		{
			// We get the pieces compatibles with the current cell at the point 
			// we're in.
			// This reduces the for length from 4 to 7. And makes a reduction 
			// in the amount of calls and makes sure that each piece can get in. 
			// This gives us a boost of around 20% speed in most cases.
			int[] compatibles = mat.get(p).getCompatibles(currentMovement);
			
			for (int j = 0; j < compatibles.length; j++) {
				int i = compatibles[j];
				
				/* The first condition asks to have pieces available of that kind.
				 * The second makes sure of having no repeated cases with the crosses.
				 * Since there are some cases in which having too many crosses can
				 * make an exponencial increment on time.
				 * Since a solution can work with a pattern like:
				 * -------- or ----+---+ or ---+---+-- etc.
				 * The normal DFS would pass through all the permutations of it.
				 * We only take the first case (------++) by only using crosses 
				 * when there are no more -'s or |'s.
				 * But making that cut alone would lose the cases in which the
				 * cross is used before the end.
				 * So we check whether the position to put the cross is valid or
				 * not by checking the siblings of the cross.
				 */
				if ( cellCount.totalPiecesLeft(i) > 0 
					&& ( i != 6 ||  

							 (
									 (cellCount.totalPiecesLeft(Cell.UPDOWN.ordinal()) == 0 
									 && (currentMovement == Movement.UP || currentMovement == Movement.DOWN)) 
							 ||
							 		 (cellCount.totalPiecesLeft(Cell.LEFTRIGHT.ordinal()) == 0 
									 && (currentMovement == Movement.LEFT || currentMovement == Movement.RIGHT))
							 ||
							 		 (valid(mat.siblings(nextPoint)))
							 )
					)
				)
				{
					// If we decide to put the cell into the map, we've got
					// to change the amounts.
					cellCount.decreasePiecesLeft(i);
					curC++ ;
					// We calculate the next movement given by the cell
					// we're calculating and the direction we have.
					Movement m = Cell.cells[i].NextDir(currentMovement);
					// We set the matrix with that cell and tell the listener about it.
					// Who will print it if it's printing by progress.
					mat.add(nextPoint, Cell.cells[i]);
					listener.addCell(Cell.cells[i], nextPoint.x, nextPoint.y, PrintAction.PROGRESS);
					
					// Recursion step.
					solve(mat, nextPoint, m);
					
					// Rollback of everything.
					listener.removeCell(nextPoint.x, nextPoint.y, PrintAction.PROGRESS);
					mat.add(nextPoint, Cell.EMPTY);
					curC--;
					cellCount.incrementPiecesLeft(i);
				}
			}
		}
	}

	/**
	 * Builds a stack from the movements we have so far.
	 * @return A stack of cells.
	 */
	private Stack<Cell> rebuildMovements(PDMatrix mat, Point p, Movement m)
	{
		Stack<Cell> cell = new Stack<Cell>();
		m = m.inverse();
		do {
			p = m.applyTo(p);
			Cell last = mat.get(p);
			cell.add(last);
			m = last.NextDir(m);
		}
		while(mat.get(p) != Cell.START);
		return cell;
	}
	
	/**
	 * Tells whether a sibling vector is valid or not for a cross cell.
	 */
	private boolean valid(Cell[] valid)
	{
		int c = 0;
		for (int i = 0; i < valid.length; i++) {
			if (valid[i] == Cell.EMPTY)
				c++;
		}
		return c == 3;
	}
	
}

