package pd;

public class PDSolverApp {
	private static final int EXACT_PARAMS_SIZE = 5;
	private static final int APPROX_PARAMS_SIZE = 5;
	private static final int ACTION_INDEX = 4; 
	
	public static void main(String[] args)
	{
		try {
			validate(args);
			PDMatrix mat = PDParser.buildFromFile(fileName);
		}
		catch (InvalidParamsException e)
		{
			
		}
	}
	
	public static void validate(String[] args)
	{
		if (args.length != APPROX_PARAMS_SIZE && args.length != EXACT_PARAMS_SIZE)
			throw new InvalidParamsException();
		boolean valid = true;
		if (args.length == APPROX_PARAMS_SIZE)	{
			try { Integer.parseInt(args[APPROX_PARAMS_SIZE - 1]); } catch(Exception e) { valid = false; };
		}
		
		if (!(valid && (args[ACTION_INDEX] == "exact" || args[ACTION_INDEX] == "approx")))
			throw new InvalidParamsException();
	}
}
