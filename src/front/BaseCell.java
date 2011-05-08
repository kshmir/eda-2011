package front;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * @author Murciélagos This class was made to model a cell with blocks. Each
 *         subclass knows how to draw itself and where to do it (the position
 *         must be received in the builder of each subclass).
 */
public abstract class BaseCell {
	/**
	 * size and color are fixed to change them at once.
	 */
	protected static int size;
	protected static Color color;
	protected int x,y;
	protected Rectangle2D.Double back;
	

	public BaseCell(int x, int y) {
		this.x=x;
		this.y=y;
		back = new Rectangle2D.Double(x * size, y * size, size, size);
	}

	/**
	 * @param g
	 *            receives where to write and each subclass draws itself in a
	 *            different way.
	 */
	public void paint(Graphics g)
	{
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.white);
		g2d.fill(back);
		g2d.setColor(Color.black);
		g2d.draw(back);
	}

	public abstract int x();

	public abstract int y();

	public static void setSize(int s) {
		size = s;
	}

	public static void setColor(Color c) {
		color = c;
	}
}
