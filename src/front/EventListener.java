package front;

import java.util.Stack;

import pd.PDMatrix;
import pd.utils.*;

public interface EventListener {
	public void addCell(Cell c, int x, int y);

	public void removeCell(int x, int y);

	public void addAll(Stack s, PDMatrix p);
}
