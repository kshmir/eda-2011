package front;

import java.util.Stack;

import javax.swing.JFrame;

import pd.PDMatrix;
import pd.utils.Cell;
import pd.utils.Point;

public class Listener implements EventListener {

	private JFrame frame = new JFrame("Pipe Dream");
	private BackGround cell;

	@Override
	public void addCell(Cell c, int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeCell(int x, int y) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addAll(Stack s, PDMatrix p) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initialize(PDMatrix p) {
		// TODO Auto-generated method stub
		cell = new BackGround(p.getCols(), p.getRows());
		frame = WindowUtilities.openInJFrame(cell, cell.getCols()
				* BackGround.size + 15, cell.getRows() * BackGround.size + 39,
				"Pipe Dream", frame);
		for(int i=0;i<p.getCols();i++)
			for(int j=0;j<p.getRows();j++){
				switch(p.get(new Point(i,j))){
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
				}
			}
		//cell.crossRectangle(2, 2);
		//cell.downLeftRectangle(1, 1);
		cell.leftRightCell(3, 3);
		cell.leftUpCell(3, 3);
		cell.emptyCell(2, 2);
		cell.upDownCell(2, 5);
		cell.upRightCell(2, 6);
		cell.downLeftCell(6, 6);
		cell.leftRightCell(5, 6);
		cell.wallCell(7, 6);
		//cell.startCell(8, 3, 3);

		frame.setContentPane(cell);
		frame.repaint();
		//cell.crossRectangle(3, 8);

	}

}
