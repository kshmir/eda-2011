package front;

import java.awt.Color;
import java.awt.Graphics;

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

	/**
	 * @param g
	 *            receives where to write and each subclass draws itself in a
	 *            different way.
	 */
	public abstract void paint(Graphics g);

	public abstract int x();

	public abstract int y();

	public static void setSize(int s) {
		size = s;
	}

	public static void setColor(Color c) {
		color = c;
	}
}
