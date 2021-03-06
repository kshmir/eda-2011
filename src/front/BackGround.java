/**
 * @author Murciélagos
 * 
 *
 */
package front;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import pd.utils.Movement;

public class BackGround extends JPanel {
	public static int size = 42;
	private int rows, cols;
	public static Color color = Color.blue;
	private static final long serialVersionUID = 1L;
	private BaseCell[][] cells;

	public BackGround(int r, int c) {
		rows = r;
		cols = c;
		BaseCell.color = color;
		BaseCell.size = size;
		cells = new BaseCell[cols][rows];
		for (int i = 0; i < cols; i++)
			for (int j = 0; j < rows; j++)
				emptyCell(i, j);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponents(g);
		Graphics2D g2d = (Graphics2D) g;
		for (int i = 0; i < cols; i++)
			for (int j = 0; j < rows; j++)
				cells[i][j].paint(g2d);

	}

	public void remove(int x, int y) {
		for (int i = 0; i < cols; i++)
			for (int j = 0; j < rows; j++)
				emptyCell(x, y);
	}

	public void leftRightCell(int x, int y) {
		if (x >= 0 && x < cols && y >= 0 && y < rows)
		{
			cells[x][y] = new LeftRightCell(x, y);
		}
	}

	public void upRightCell(int x, int y) {
		if (x >= 0 && x < cols && y >= 0 && y < rows)
		{
			cells[x][y] = new UpRightCell(x, y);
		}
	}

	public void upDownCell(int x, int y) {
		if (x >= 0 && x < cols && y >= 0 && y < rows)
		{
			cells[x][y] = new UpDownCell(x, y);
		}
	}

	public void leftUpCell(int x, int y) {
		if (x >= 0 && x < cols && y >= 0 && y < rows)
		{
			cells[x][y] = new LeftUpCell(x, y);
		}
	}

	public void downLeftCell(int x, int y) {
		if (x >= 0 && x < cols && y >= 0 && y < rows)
		{
			cells[x][y] = new DownLeftCell(x, y);
		}
	}

	public void emptyCell(int x, int y) {
		if (x >= 0 && x < cols && y >= 0 && y < rows)
		{
			cells[x][y] = new EmptyCell(x, y);
		}
	}

	public void crossCell(int x, int y) {
		if (x >= 0 && x < cols && y >= 0 && y < rows)
		{
			cells[x][y] = new CrossCell(x, y);
		}
	}

	public void wallCell(int x, int y) {
		if (x >= 0 && x < cols && y >= 0 && y < rows)
		{
			cells[x][y] = new WallCell(x, y);
		}
	}

	public void startCell(int x, int y, Movement movement) {
		if (x >= 0 && x < cols && y >= 0 && y < rows)
		{
			cells[x][y] = new StartCell(x, y, movement);
		}
	}
	
	public void rightDownCell(int x, int y) {
		if (x >= 0 && x < cols && y >= 0 && y < rows)
		{
			cells[x][y] = new RightDownCell(x, y);
		}
	}
	
	public int getCols() {
		return cols;
	}

	public int getRows() {
		return rows;
	}

}
