package front;

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
	private BackGround cell;

	
	public ConsoleListener(PrintAction action, PDMatrix mat)
	{
		super(action,mat);
		cell = new BackGround(p.getCols(), p.getRows());
		frame = WindowUtilities.openInJFrame(cell, cell.getCols()
				* BackGround.size + 15, cell.getRows() * BackGround.size + 39,
				"Pipe Dream", frame);
		for (int i = 0; i < p.getCols(); i++)
			for (int j = 0; j < p.getRows(); j++) {
				addCell(p.get(new Point(i, j)), i, j);
			}
	}
	
	
	@Override
	public boolean addCell(Cell c, int i, int j, PrintAction action) {
		switch (c) {
		case CROSS:
			cell.crossCell(i, j);
			break;
		case DOWNLEFT:
			cell.downLeftCell(i, j);
			break;
		case EMPTY:
			cell.emptyCell(i, j);
			break;
		case LEFTRIGHT:
			cell.leftRightCell(i, j);
			break;
		case LEFTUP:
			cell.leftUpCell(i, j);
			break;
		case RIGHTDOWN:
			cell.rightDownCell(i, j);
			break;
		case START:
			cell.startCell(i, j, Cell.startDirection);
			break;
		case UPDOWN:
			cell.upDownCell(i, j);
			break;
		case UPRIGHT:
			cell.upRightCell(i, j);
			break;
		case WALL:
			cell.wallCell(i, j);
			break;
		}
	}

	@Override
	public void removeCell(int x, int y, ) {
		if (!hasStarted)
			return;
		cell.remove(x, y);
	}



}
