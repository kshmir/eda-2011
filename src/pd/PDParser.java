package pd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class PDParser {
	public static PDMatrix buildFromFile(String string) throws IOException, InvalidFileException
	{
		File file = new File(string);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		int rows = 0, cols = 0;
		
		String line = null;
		if ((line = reader.readLine()).matches("[0-9]+,[0-9]+"))  {
			try {
				rows = Integer.parseInt(line.split(",")[0]);
				cols = Integer.parseInt(line.split(",")[1]);
			}
			catch(NumberFormatException e) {
				throw new InvalidFileException();
			}
		}
		else
			throw new InvalidFileException();
		
		PDMatrix matriz = new PDMatrix(rows, cols);
		boolean startSet = false;
		
		for (int i = 0; i < rows; i++) {
			line = reader.readLine();
			if (line.length() >= cols)
				throw new InvalidFileException();
			for (int j = 0; j < line.length(); j++) {
				char c = line.charAt(j);
				if (c == '#')
					matriz.putWall(i,j);
				else if (c == 'N') 	{
					matriz.setStart(i,j);
				}
			}
		}
		
		
		
		return null;
	}
}
