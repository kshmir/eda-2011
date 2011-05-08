package front;

import java.util.Stack;

import pd.PDMatrix;
import pd.utils.Cell;

public class MultipleListener extends EventListener {

	public MultipleListener(PrintAction act, PDMatrix p, boolean kb) {
		super(act, p, kb);
		console = new ConsoleListener(action,p,kb);
		panel = new PanelListener(action,p,kb);
		panel.printOnConsole = false;
	}

	private ConsoleListener console;
	private PanelListener panel;
	
	@Override
	public boolean addCell(Cell c, int x, int y,PrintAction action) {
		console.addCell(c, x, y,action);
		panel.addCell(c, x, y,action);
		return true;
	}
	
	@Override
	public boolean addCell(Cell c, int x, int y,PrintAction action, boolean printAllOnOnce) {
		console.addCell(c, x, y,action,printAllOnOnce);
		panel.addCell(c, x, y,action, printAllOnOnce);
		return true;
	}
	
	
	@Override
	public void addAll(Stack<Cell> stack, PrintAction action) {
		console.addAll(stack, action);
		panel.addAll(stack, action);
	};
	

	@Override
	public boolean removeCell(int x, int y,PrintAction action) {
		console.removeCell(x, y,action);
		panel.removeCell(x, y,action);
		
		return true;
	}


}
