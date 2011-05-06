package front;


import javax.swing.JFrame;

import pd.PDMatrix;
import pd.utils.Cell;
import pd.utils.Point;

public class Listener extends EventListener {

	private JFrame frame = new JFrame("Pipe Dream");
	private BackGround cell;
	private boolean hasStarted=false;
	@Override
	public void addCell(Cell c, int i, int j) {
		if(!hasStarted)
			return;
		switch(c){
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
	public void removeCell(int x, int y) {
		if(!hasStarted)
			return;
		cell.remove(x, y);
	}


	@Override
	public void initialize(PDMatrix p) {
		hasStarted=true;
		cell = new BackGround(p.getCols(), p.getRows());
		frame = WindowUtilities.openInJFrame(cell, cell.getCols()
				* BackGround.size + 15, cell.getRows() * BackGround.size + 39,
				"Pipe Dream", frame);
		for(int i=0;i<p.getCols();i++)
			for(int j=0;j<p.getRows();j++){
				addCell(p.get(new Point(i,j)),i,j);
				
			}

//		frame.setContentPane(cell);
//		frame.repaint();

	}

}
