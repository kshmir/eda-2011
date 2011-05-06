package pd;

import java.io.IOException;
import java.util.Stack;

import pd.utils.Cell;
import pd.utils.Movement;
import pd.utils.Point;

public class PDSolverApp {
	private static final int EXACT_PARAMS_SIZE = 2;
	private static final int APPROX_PARAMS_SIZE = 3;
	private static final int ACTION_INDEX = 1;
	private static final int FILE_NAME_INDEX = 0;
	
	
	public static void main(String[] args)
	{
		try {
			validate(args);
			PDMatrix mat = PDParser.buildFromFile(args[FILE_NAME_INDEX]);
			Stack<Cell> cells = PDSolver.Solve(mat, "exact");
			System.out.println("Tama–o: " + (cells.size() - 1));
			print(cells,mat);
		}
		catch (InvalidParamsException e) {
			System.out.println("Invalid Params!");
		}
		catch (IOException e) {
			System.out.println("Couldn't read input file!");
		} catch (InvalidFileException e) {
			System.out.println("Invalid file detected!");
		}
	}
	
	public static void print(Stack<Cell> stack, PDMatrix mat)
	{
		stack.pop();
		Movement m = Cell.startDirection;
		Point p = m.applyTo(mat.getStartPoint());
		while(!stack.isEmpty())
		{
			Cell c = stack.pop();
			mat.add(p, c);
			m = c.NextDir(m);
			p = m.applyTo(p);
		}
		mat.print();
	}
	
	public static void validate(String[] args) throws InvalidParamsException
	{
		if (args.length != APPROX_PARAMS_SIZE && args.length != EXACT_PARAMS_SIZE)
		{
			
			throw new InvalidParamsException();
		}
		boolean valid = true;
		if (args.length == APPROX_PARAMS_SIZE)	{
			try { Integer.parseInt(args[APPROX_PARAMS_SIZE - 1]); } catch(Exception e) { valid = false; };
		}
		
		if (!(valid && (args[ACTION_INDEX].equals("exact") || args[ACTION_INDEX].equals("approx"))))
		{
			throw new InvalidParamsException();
		}
	}
}
