package front;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class LeftRightCell extends BaseCell{
	private int x,y;
	private Rectangle2D.Double back;
	private Rectangle2D.Double horizontal;

	public LeftRightCell(int x, int y) {
		this.x=x;
		this.y=y;
		back = new Rectangle2D.Double(x * size, y * size, size, size);
		horizontal = new Rectangle2D.Double(x * size, y * size + size / 3,
				size, size / 3);
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.black);
		g2d.draw(back);
		g2d.setColor(color);
		g2d.fill(horizontal);
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
