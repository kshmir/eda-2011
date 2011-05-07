package front;

import pd.PDMatrix;
import pd.PDSolverApp;
import pd.utils.Cell;
import pd.utils.Point;

public class ConsoleListener extends EventListener {


	public ConsoleListener(PrintAction action, PDMatrix mat)
	{
		super(action,mat);
	}
	
	
	@Override
	public boolean addCell(Cell c, int x, int y, PrintAction action) {
		// Yes indeed, the cake is a lie
		if (super.addCell(c,x,y,action))
			printMat();
		else
			return false;
		return true;
	}

	@Override
	public boolean removeCell(int x, int y, PrintAction action) {
		// Yes indeed, the cake is a lie
		if (super.removeCell(x,y,action))
			printMat();
		else
			return false;
		return true;
	}
	
	private void printMat()
	{
		for (int i = 0; i < mat.getRows(); i++) {
			for (int j = 0; j < mat.getCols(); j++) {
				switch(mat.get(new Point(i,j))) {
					case CROSS:
						System.out.print("┼");
						break;
					case UPDOWN:
						System.out.print("│");
						break;
					case LEFTRIGHT:
						System.out.print("─");
						break;
					case LEFTUP:
						System.out.print("┘");
						break;
					case DOWNLEFT:
						System.out.print("┐");
						break;
					case RIGHTDOWN:
						System.out.print("┌");
						break;
					case UPRIGHT:
						System.out.print("└");
						break;
					case START:
						System.out.print("*");
						break;
					case WALL:
						System.out.print("█");
						break;
					case EMPTY:
						System.out.print(" ");
						break;
				}
			}
			System.out.println();
		}
	}
	

}
