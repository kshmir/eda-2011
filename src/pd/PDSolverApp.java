package pd;

import java.io.IOException;
import java.util.Stack;

import pd.solvers.PDSolver;
import pd.utils.Cell;
import front.ConsoleListener;
import front.EventListener;
import front.MultipleListener;
import front.PanelListener;
import front.PrintAction;

public class PDSolverApp {
	public static void main(String[] args) {
		try {
			validate(args);
			PDMatrix mat = PDParser.buildFromFile(args[FILE_NAME_INDEX]);
			action = getParamsActions(args);
			Integer approx_minutes = getApproxMinutes(args);
			EventListener itr = getPrintInterface(args, mat, action);
			Stack<Cell> cells = PDSolver.solve(mat, args[ACTION_INDEX], approx_minutes, itr);
			itr.addAll(cells, action);
		} catch (NumberFormatException e) {
			System.out.println(invalidParamsString);
			e.printStackTrace();
		} catch (InvalidParamsException e) {
			System.out.println(invalidParamsString);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Couldn't read input file!");
		} catch (InvalidFileException e) {
			System.out.println("Invalid file detected!");
			System.out.println("Make sure the amount of values given is valid");
			System.out.println("And also make sure that the height of the map is valid");
		} catch (StackOverflowError e) {
			System.out.println("Looks like we ran out of stack space... what a shame!");
			System.out.println("If you see this message, then please don't make us recourse!");
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			System.out.println("Looks like we ran out of heap space... what a shame!");
			System.out.println("If you see this message, then please don't make us recourse!");
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("If you see this message, then please don't make us recourse");
			e.printStackTrace();
		}
	}
	
	private static Integer getApproxMinutes(String[] args) throws NumberFormatException {
		if (args[ACTION_INDEX].equals("approx"))
			return Integer.parseInt(args[PARAM_INDEX]);
		return 0;
	}
	
	private static EventListener getPrintInterface(String[] args, PDMatrix mat, PrintAction action) {
		EventListener el = null;
		if (args[args.length - 1].startsWith("-") && args[args.length - 1].contains("p")
				&& args[args.length - 1].contains("c"))
			el = new MultipleListener(action, mat, args[args.length - 1].contains("k"),
					args[args.length - 1].contains("r") && args[args.length - 1].startsWith("-"));
		else if (args[args.length - 1].startsWith("-") && args[args.length - 1].contains("c"))
			el = new ConsoleListener(action, mat, args[args.length - 1].contains("k"),
					args[args.length - 1].contains("r") && args[args.length - 1].startsWith("-"));
		else
			el = new PanelListener(action, mat, args[args.length - 1].contains("k"),
					args[args.length - 1].contains("r") && args[args.length - 1].startsWith("-"));
		return el;
	}
	
	private static PrintAction getParamsActions(String[] args) throws InvalidParamsException {
		
		if (args.length == EXACT_PARAMS_SIZE || args[ACTION_INDEX].equals("approx")
				&& args.length == APPROX_PARAMS_SIZE)
			return PrintAction.END_RESULT;
		
		if (args.length == APPROX_PARAMS_SIZE) {
			boolean gotNumber = false;
			try {
				Integer.parseInt(args[PARAM_INDEX]);
				gotNumber = true;
			} catch (Exception e) {
			}
			
			if (gotNumber)
				throw new InvalidParamsException();
		}
		
		String s = (args[ACTION_INDEX].equals("exact")) ? args[PROGRESS_EXACT_PARAMS_SIZE - 1]
				: args[PROGRESS_APPROX_PARAMS_SIZE - 1];
		
		if (s.equals("progress"))
			return PrintAction.PROGRESS;
		else if (s.equals("eachresult"))
			return PrintAction.RESULT;
		else if (s.equals("betterresult"))
			return PrintAction.BEST_SO_FAR;
		else if (s.equals("bestresult"))
			return PrintAction.END_RESULT;
		else if (args[args.length - 1].contains("-"))
			return PrintAction.END_RESULT;
		else
			throw new InvalidParamsException();
	}
	
	public static void validate(String[] args) throws InvalidParamsException {
		if (args.length != APPROX_PARAMS_SIZE && args.length != EXACT_PARAMS_SIZE
				&& args.length != PROGRESS_APPROX_PARAMS_SIZE && args.length != PROGRESS_EXACT_PARAMS_SIZE
				&& args.length != PROGRESS_APPROX_PARAMS_SIZE + 1) {
			throw new InvalidParamsException();
		}
		boolean valid = true;
		if (args.length == APPROX_PARAMS_SIZE) {
			try {
				if (args[ACTION_INDEX].equals("approx"))
					Integer.parseInt(args[APPROX_PARAMS_SIZE - 1]);
			} catch (Exception e) {
				valid = false;
			}
			;
		}
		
		if (!(valid && (args[ACTION_INDEX].equals("exact") || args[ACTION_INDEX].equals("approx")))) {
			throw new InvalidParamsException();
		}
	}
	
	private static final int	EXACT_PARAMS_SIZE			= 2;
	private static final int	APPROX_PARAMS_SIZE			= 3;
	private static final int	PROGRESS_EXACT_PARAMS_SIZE	= 3;
	private static final int	PROGRESS_APPROX_PARAMS_SIZE	= 4;
	private static final int	PARAM_INDEX					= 2;
	private static final int	ACTION_INDEX				= 1;
	private static final int	FILE_NAME_INDEX				= 0;
	
	private static final String	invalidParamsString			= "You've put invalid params, the correct formats are:\n"
																	+ "(filename) (exact|approx) (minutes for approx) (printmethod) -(c?p?x?){1}\n"
																	+ "For example:\n\t"
																	+ "map exact progress -cpk\n\t\t"
																	+ "Shows each iteration on the panel and the console, stepping by keyboard.\n\t"
																	+ "map exact progress -c\n\t\t"
																	+ "Shows each iteration on the console, stepping by 100ms step.\n\t"
																	+ "map exact eachresult\n\t\t"
																	+ "Shows each result found on the panel.\n\t"
																	+ "map exact betterresult\n\t\t"
																	+ "Shows each best result found so far on the panel.\n\t"
																	+ "map exact bestresult\n\t\t"
																	+ "Shows THE best result found on the panel.\n\t";
	
	public static PrintAction	action;
}
