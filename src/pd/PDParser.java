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
		
		if ((line = reader.readLine()).split(",").length == 2)  {
			try {
				rows = Integer.parseInt(line.split(",")[0].trim());
				cols = Integer.parseInt(line.split(",")[1].trim());
						
				if (rows <= 0 || cols <= 0)
					throw new NumberFormatException();
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
			line = reader.readLine().trim();
			
			if (line.length() > cols)
				throw new InvalidFileException();
			for (int j = 0; j < line.length(); j++) {
				char c = line.charAt(j);
				
				if (startSet && (c == 'N' || c == 'S' || c == 'E' || c == 'W'))
					throw new InvalidFileException();
				else if (c == '#')
					matriz.putWall(i,j);
				else if (c == 'N' || c == 'S' || c == 'E' || c == 'W') 	{
					matriz.setStart(i,j,c);
					startSet = true;
				}
			}
		}
		
		CellCountMap map = matriz.getCellCountMap();
		for (int i = 0; i < 7; i++) {
			try	{
				map.setTotalPiecesLeft(i, Integer.parseInt(reader.readLine().trim()));
			}
			catch (Exception e)	{
				throw new InvalidFileException();
			}
		}
		if (!startSet)
			throw new InvalidFileException();
		
		
		return matriz;
	}
}
