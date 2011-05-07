package front;

import java.util.Stack;

import pd.PDMatrix;
import pd.utils.Cell;
import pd.utils.Movement;
import pd.utils.Point;

/**
 * @author Murciélagos Any class which implements this listener should be able
 *         to interact with the back end.
 */
public abstract class EventListener {
	/**
	 * @param c
	 * @param x
	 * @param y
	 *            Adds the specified kind of cell to the position (x,y)
	 */
	public abstract void addCell(Cell c, int x, int y);

	/**
	 * @param x
	 * @param y
	 *            Removes the cell and replaces it with a blank.
	 */
	public abstract void removeCell(int x, int y);

	/**
	 * @param stack
	 * @param mat
	 * rebuilds the matrix and adds every cell.
	 */
	public void addAll(Stack<Cell> stack, PDMatrix mat) {
		stack.pop();
		Movement m = Cell.startDirection;
		Point p = m.applyTo(mat.getStartPoint());
		while (!stack.isEmpty()) {
			Cell c = stack.pop();
			// mat.add(p, c);
			addCell(c, p.x, p.y);
			m = c.NextDir(m);
			p = m.applyTo(p);
		}
	}

	public abstract void initialize(PDMatrix p);
}
