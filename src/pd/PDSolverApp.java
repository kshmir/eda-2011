package pd;

import java.io.IOException;
import java.util.Stack;

import pd.solvers.PDSolver;
import pd.utils.Cell;
import pd.utils.Movement;
import pd.utils.Point;
import front.ConsoleListener;
import front.EventListener;
import front.PrintAction;
import front.MultipleListener;
import front.PanelListener;

public class PDSolverApp {
	private static final int EXACT_PARAMS_SIZE = 2;
	private static final int APPROX_PARAMS_SIZE = 3;
	private static final int PROGRESS_EXACT_PARAMS_SIZE = 3;
	private static final int PROGRESS_APPROX_PARAMS_SIZE = 4;
	private static final int PARAM_INDEX = 2;
	private static final int ACTION_INDEX = 1;
	private static final int FILE_NAME_INDEX = 0;
	
								
	public static PrintAction action; 
	
	
	public static void main(String[] args)
	{
		try {
			validate(args);
			PDMatrix mat = PDParser.buildFromFile(args[FILE_NAME_INDEX]);
			action = getParamsActions(args);
			Integer approx_minutes = getApproxMinutes(args);
			EventListener itr = getPrintInterface(args, mat);
			Stack<Cell> cells = PDSolver.solve(mat, args[ACTION_INDEX], approx_minutes, itr);
			System.out.println("Tama–o: " + (cells.size()));
		}
		catch (NumberFormatException e) {
			System.out.println("Invalid Params!");
		}
		catch (InvalidParamsException e) {
			System.out.println("Invalid Params!");
		}
		catch (IOException e) {
			System.out.println("Couldn't read input file!");
		} catch (InvalidFileException e) {
			System.out.println("Invalid file detected!");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static Integer getApproxMinutes(String[] args) throws NumberFormatException {
		if (args[ACTION_INDEX].equals("approx")  && (args.length == APPROX_PARAMS_SIZE || args.length == PROGRESS_APPROX_PARAMS_SIZE))
			return Integer.parseInt(args[PARAM_INDEX]);
		return 0;
	}
	
	private static EventListener getPrintInterface(String[] args, PDMatrix mat) {
		EventListener el = null; 
		if ((args[args.length - 1]).equals("-c"))
			el = new ConsoleListener();
		else if ((args[args.length - 1]).equals("-cp"))
			el = new MultipleListener();
		else el = new PanelListener();
		el.initialize(mat);
		return el;
	}
	

	private static PrintAction getParamsActions(String[] args) throws InvalidParamsException {
		
		if ((args[args.length - 1]).equals("-c") ||
			(args[args.length - 1]).equals("-cp") 
				|| args.length == EXACT_PARAMS_SIZE 
				|| args[ACTION_INDEX].equals("approx") && args.length == APPROX_PARAMS_SIZE)
			return PrintAction.END_RESULT;
		
		
		if (args.length == APPROX_PARAMS_SIZE) {
			boolean gotNumber = false;
			try {
				Integer.parseInt(args[PARAM_INDEX]);
				gotNumber = true;
			}
			catch(Exception e) {}
			
			if (gotNumber)
				throw new InvalidParamsException();
		}
		
		String s = (args[ACTION_INDEX].equals("exact")) ? args[PROGRESS_EXACT_PARAMS_SIZE - 1] 
		                                         : args[PROGRESS_APPROX_PARAMS_SIZE - 1];
		
		if (s.equals("progress"))
			return PRINT_ACTIONS.PROGRESS;
		else if (s.equals("result"))
			return PRINT_ACTIONS.RESULT;
		else if (s.equals("bestsofar"))
			return PRINT_ACTIONS.BEST_SO_FAR;
		else if (s.equals("endresult"))
			return PRINT_ACTIONS.END_RESULT;
		else if (s.equals("progress-stepped"))
			return PRINT_ACTIONS.PROGRESS_STEPPED;
		else if (s.equals("result-stepped"))
			return PRINT_ACTIONS.RESULT_STEPPED;
		else if (s.equals("bestsofar-stepped"))
			return PRINT_ACTIONS.BEST_SO_FAR_STEPPED;
		else
			throw new InvalidParamsException();
			
	}

	
	public static void validate(String[] args) throws InvalidParamsException
	{
		if (args.length != APPROX_PARAMS_SIZE && args.length != EXACT_PARAMS_SIZE && 
			args.length != PROGRESS_APPROX_PARAMS_SIZE && args.length != PROGRESS_EXACT_PARAMS_SIZE ) {
			throw new InvalidParamsException();
		}
		boolean valid = true;
		if (args.length == APPROX_PARAMS_SIZE)	{
			try { 
				if (args[ACTION_INDEX].equals("approx"))
					Integer.parseInt(args[APPROX_PARAMS_SIZE - 1]); 
			} catch(Exception e) { 
				valid = false; 
			};
		}
		
		if (!(valid && (args[ACTION_INDEX].equals("exact") || args[ACTION_INDEX].equals("approx")))) {
			throw new InvalidParamsException();
		}
	}
}
