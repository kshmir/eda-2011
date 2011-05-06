package front;

import java.awt.Color;
import java.awt.Graphics;

public abstract class BaseCell {
	protected static int size;
	protected static Color color;
	public abstract void paint(Graphics g);
	public abstract int x();
	public abstract int y();
}
