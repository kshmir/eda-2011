package front;

import java.util.Stack;

import pd.PDMatrix;
import pd.PDSolverApp;
import pd.utils.Cell;
import pd.utils.Movement;
import pd.utils.Point;

/**
 * @author Murciélagos Any class which implements this listener should be able
 *         to interact with the back end.
 */
public abstract class EventListener {
	
	protected PrintAction action;
	protected PDMatrix mat;
	
	private EventListener(PrintAction act, PDMatrix p)
	{
		action = act;
		mat = p;
	}
	
	/**
	 * @param c
	 * @param x
	 * @param y
	 * Adds the specified kind of cell to the position (x,y)
	 * MUST BE OVERRIDEN
	 */
	public boolean addCell(Cell c, int x, int y, PrintAction action)
	{
		return (action == this.action);
	}

	/**
	 * @param x
	 * @param y
	 * Removes the cell and replaces it with a blank.
	 */
	public boolean removeCell(int x, int y, PrintAction action)
	{
		return (action == this.action);
	}

	/**
	 * @param stack
	 * @param mat
	 * rebuilds the matrix and adds every cell.
	 */
	@SuppressWarnings("unchecked")
	public void addAll(Stack<Cell> stack, PDMatrix mat, PrintAction action) {
		if (action == this.action)
		{
			stack = (Stack<Cell>)stack.clone();
			stack.pop();
			Movement m = Cell.startDirection;
			Point p = m.applyTo(mat.getStartPoint());
			while (!stack.isEmpty()) {
				Cell c = stack.pop();
				addCell(c, p.x, p.y, action);
				m = c.NextDir(m);
				p = m.applyTo(p);
			}
		}
	}
}
