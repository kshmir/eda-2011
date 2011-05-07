package front;

import java.util.Stack;

import pd.PDMatrix;
import pd.utils.Cell;
import pd.utils.Movement;
import pd.utils.Point;

public abstract class EventListener {
	public abstract void addCell(Cell c, int x, int y);

	public abstract void removeCell(int x, int y);

	public void addAll(Stack<Cell> stack, PDMatrix mat){
		stack.pop();
		Movement m = Cell.startDirection;
		Point p = m.applyTo(mat.getStartPoint());
		while(!stack.isEmpty())
		{
			Cell c = stack.pop();
			addCell(c, p.x, p.y);
			m = c.NextDir(m);
			p = m.applyTo(p);
		}
	}
	
	public abstract void initialize(PDMatrix p);
}
