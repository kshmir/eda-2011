package front;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public class StartCell extends BaseCell {

	private int x,y;
	private Rectangle2D.Double back;
	private Rectangle2D.Double rec;
	
	public StartCell(int x, int y, int m) {
		this.x=x;
		this.y=y;
		back = new Rectangle2D.Double(x * size, y * size, size, size);
		switch (m) {
		case 0:// up
			rec = new Rectangle2D.Double(x * size + size / 3, y * size,
					size / 3, size * 2 / 3);
			break;
		case 1:// down
			rec = new Rectangle2D.Double(x * size + size / 3, y * size + size
					/ 3, size / 3, size * 2 / 3);
			break;
		case 2:// left
			rec = new Rectangle2D.Double(x * size, y * size + size / 3,
					size * 2 / 3, size / 3);
			break;
		case 3:// right
			rec = new Rectangle2D.Double(x * size + size / 3, y * size + size
					/ 3, size * 2 / 3, size / 3);
			break;
		}
	}
	
	@Override
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.lightGray);
		g2d.fill(back);
		g2d.setColor(Color.black);
		g2d.draw(back);
		g2d.setColor(color);
		g2d.fill(rec);
		
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
