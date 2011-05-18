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
			line = reader.readLine();
			
			int len = (line.length() > cols) ? cols : line.length();
			for (int j = 0; j < len; j++) {
				char c = line.charAt(j);
				if (startSet && (c == 'N' || c == 'S' || c == 'E' || c == 'W'))
					throw new InvalidFileException();
				else if (c == '#')
					matriz.putWall(j,i);
				else if (c == 'N' || c == 'S' || c == 'E' || c == 'W') 	{
					matriz.setStart(j,i,c);
					startSet = true;
				}
			}
		}
		
		CellCountMap map = matriz.getCellCountMap();
		for (int i = 0; i < 7; i++) {
			try	{
				map.setTotalPiecesLeft(i, Integer.parseInt(reader.readLine().trim()));
				matriz.maxPathLen += map.totalPiecesLeft(i);
				if (i == 6)
					matriz.maxPathLen += map.totalPiecesLeft(i);
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
