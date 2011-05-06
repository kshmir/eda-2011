package front;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class EmptyCell extends BaseCell{
	private int x,y;
	private Rectangle2D.Double back;
	private Rectangle2D.Double vertical;
	private Rectangle2D.Double horizontal;

	public EmptyCell(int x, int y) {
		this.x=x;
		this.y=y;
		back = new Rectangle2D.Double(x * size, y * size, size, size);
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.white);
		g2d.fill(back);
		g2d.setColor(Color.black);
		g2d.draw(back);
	}

	@Override
	public int x() {
		// TODO Auto-generated method stub
		return x;
	}

	@Override
	public int y() {
		// TODO Auto-generated method stub
		return y;
	}
}
