package pd;

import java.io.IOException;

public class PDSolverApp {
	private static final int EXACT_PARAMS_SIZE = 5;
	private static final int APPROX_PARAMS_SIZE = 5;
	private static final int ACTION_INDEX = 4;
	private static final int FILE_NAME_INDEX = 4;
	
	
	public static void main(String[] args)
	{
		try {
			validate(args);
				PDMatrix mat = PDParser.buildFromFile(args[FILE_NAME_INDEX]);
			
		}
		catch (InvalidParamsException e) {
			// TODO Auto-generated catch block
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
		} catch (InvalidFileException e) {
			// TODO Auto-generated catch block
		}
	}
	
	public static void validate(String[] args) throws InvalidParamsException
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
