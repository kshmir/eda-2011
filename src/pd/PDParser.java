package pd;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import pd.cells.Cell;
import pd.cells.CrossCell;
import pd.cells.LeftDownCell;
import pd.cells.LeftRightCell;
import pd.cells.LeftUpCell;
import pd.cells.RightDownCell;
import pd.cells.RightUpCell;
import pd.cells.UpDownCell;

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
		
		@SuppressWarnings("unchecked")
		Class<? extends Cell>[] cells = (Class<? extends Cell>[]) new Object[] { 
			LeftUpCell.class, RightUpCell.class, RightDownCell.class,
			LeftDownCell.class, UpDownCell.class, LeftRightCell.class, CrossCell.class
		};
		
		Map<Class<? extends Cell>, Integer> cellmap = matriz.getAvailableBlocks();
		for (Class<? extends Cell> celltype : cells) {
			try
			{
				int n = Integer.parseInt(reader.readLine());
				cellmap.put(celltype, n);
			}
			catch (Exception e)
			{
				throw new InvalidFileException();
			}
		}
		return matriz;
	}
}
