package front;

import javax.swing.JFrame;

import pd.utils.Movement;

public class Main{

	public static void main(String[] args) {
		JFrame frame = new JFrame("Pipe Dream");
			BackGround cell = new BackGround(15, 15);
		frame = WindowUtilities.openInJFrame(cell, cell.getCols()
				* BackGround.size + 15, cell.getRows() * BackGround.size + 39,
				"Pipe Dream", frame);
		cell.crossRectangle(0, 2);
		cell.crossRectangle(2, 2);
		cell.downLeftRectangle(1, 1);
		cell.leftRightCell(3, 3);
		cell.leftUpCell(3, 3);
		cell.emptyCell(2, 2);
		cell.upDownCell(2, 5);
		cell.upRightCell(2, 6);
		cell.downLeftCell(6, 6);
		cell.leftRightCell(5, 6);
		cell.wallCell(7, 6);
		cell.startCell(8, 3, Movement.RIGHT);

		frame.setContentPane(cell);
		frame.repaint();
		cell.crossRectangle(3, 8);
	}
}
