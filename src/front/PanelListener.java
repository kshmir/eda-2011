package front;

import java.util.Stack;

import javax.swing.JFrame;

import pd.PDMatrix;
import pd.utils.Cell;
import pd.utils.Point;

/**
 * @author Murciélagos This implementation of the {@link EventListener} will be
 *         the default one, it's very simple to visualize the algorithm with it.
 */
public class PanelListener extends EventListener {

	private JFrame frame = new JFrame("Pipe Dream");
	private BackGround bg;
	public boolean printOnConsole = true;
	
	@Override
	protected void printReport() {
		if (printOnConsole)
			super.printReport();
	}
	
	public PanelListener(PrintAction action, PDMatrix p, boolean kb, boolean repeats)
	{
		super(action,p,kb,repeats);
		bg = new BackGround(p.getRows(),p.getCols());
		frame = WindowUtilities.openInJFrame(bg, bg.getCols()
				* BackGround.size + 15, bg.getRows() * BackGround.size + 39,
				"Pipe Dream", frame);
		for (int i = 0; i < p.getCols(); i++)
			for (int j = 0; j < p.getRows(); j++) {
				addCell(p.get(new Point(i, j)), i, j, action, true);
			}
		bg.repaint();
	}
	
	@Override
	public boolean addCell(Cell c, int x, int y, PrintAction action) {
		return addCell(c,x,y,action,false);
	};
	
	@Override
	public boolean addCell(Cell c, int i, int j, PrintAction action, boolean printAllOnOnce) {
		
		if (action == this.action)
		{
			long l1 = System.currentTimeMillis();
			switch (c) {
			case CROSS:
				bg.crossCell(i, j);
				break;
			case DOWNLEFT:
				bg.downLeftCell(i, j);
				break;
			case EMPTY:
				bg.emptyCell(i, j);
				break;
			case LEFTRIGHT:
				bg.leftRightCell(i, j);
				break;
			case LEFTUP:
				bg.leftUpCell(i, j);
				break;
			case RIGHTDOWN:
				bg.rightDownCell(i, j);
				break;
			case START:
				bg.startCell(i, j, Cell.startDirection);
				break;
			case UPDOWN:
				bg.upDownCell(i, j);
				break;
			case UPRIGHT:
				bg.upRightCell(i, j);
				break;
			case WALL:
				bg.wallCell(i, j);
				break;
			}
			bg.repaint();
			totalWaitTime += System.currentTimeMillis() - l1;
		}
		
		return super.addCell(c, i, j, action, printAllOnOnce);
	}
	
	@Override
	public void addAll(Stack<Cell> stack, PrintAction action) {
		if (this.action == action) clearPanel();
		super.addAll(stack,action);
	};
	
	private void clearPanel()
	{
		long l1 = System.currentTimeMillis();
		for (int i = 0; i < mat.getCols(); i++)
			for (int j = 0; j < mat.getRows(); j++) {
				Cell c = mat.get(new Point(i, j)); 
				if (c != Cell.WALL && c != Cell.START)
					bg.remove(i, j);
			}
		bg.repaint();
		totalWaitTime += System.currentTimeMillis() - l1;
	}
	
	
	

	@Override
	public boolean removeCell(int x, int y, PrintAction action) {
		long l1 = System.currentTimeMillis();
		if (action == this.action)
		{
			bg.remove(x, y);
			bg.repaint();
		}
		totalWaitTime += System.currentTimeMillis() - l1;
		return true;
	}



}
