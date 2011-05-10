package front;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Stack;

import pd.PDMatrix;
import pd.utils.Cell;
import pd.utils.Movement;
import pd.utils.Point;

public class ConsoleListener extends EventListener {

	private PDMatrix start_mat;
	
	private PDMatrix original_mat;

	public ConsoleListener(PrintAction action, PDMatrix mat, boolean stepped)
	{
		super(action,mat,stepped);
		start_mat = mat.clone();
		this.mat = mat.clone();
		this.original_mat = mat;
		try {
			System.setOut(new PrintStream(System.out, true, "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	@Override
	public boolean addCell(Cell c, int x, int y, PrintAction action) {
		return addCell(c,x,y,action,false);
	}
	
	@Override
	public boolean addCell(Cell c, int x, int y, PrintAction action, boolean printAllOnOnce) {
		// Yes indeed, the cake is a lie
		if (action == this.action)
			printMat(original_mat);
		else
			return false;
		return super.addCell(c,x,y,action,printAllOnOnce);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void addAll(Stack<Cell> stack, PrintAction action) {
		if (action == this.action)
		{
			stack = (Stack<Cell>)stack.clone();
			stack.pop();
			Movement m = Cell.startDirection;
			Point p = m.applyTo(mat.getStartPoint());
			while (!stack.isEmpty()) {
				Cell c = stack.pop();
				mat.add(p, c);
				m = c.NextDir(m);
				p = m.applyTo(p);
			}
			printMat(mat);
			mat = start_mat.clone();
			this.printReport();
		}
		makeDelayOrAskStdin(action);
	};

	@Override
	public boolean removeCell(int x, int y, PrintAction action) {
		// Yes indeed, the cake is a lie
		if (action == this.action)
			printMat(original_mat);
		else
			return false;
		return super.removeCell(x,y,action);
	}
	
	private void printMat(PDMatrix mat)
	{
		long l1 = System.currentTimeMillis();
		for (int i = 0; i < mat.getRows(); i++) {
			for (int j = 0; j < mat.getCols(); j++) {
				switch(mat.get(new Point(j,i))) {
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
		totalWaitTime += System.currentTimeMillis() - l1;
	}
	

}
