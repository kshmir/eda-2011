package pd;

import pd.utils.Cell;
import pd.utils.Point;

public class PDMatrixTest {

	public static void main(String[] args) {
		PDMatrix matrix = new PDMatrix(3,4);
		
		Point p =  new Point(1,3);
		matrix.siblings (p);
		Cell c = matrix.get(p);
		matrix.remove (p);
		Cell cell = null;
		boolean b = matrix.add(p,cell);
		int i = 2;
		int j = 3;
		matrix.putWall(i,j);
		char ch = 0;
		matrix.setStart(i, j, ch);
		
		

	}

}
