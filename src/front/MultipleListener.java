package front;

import pd.PDMatrix;
import pd.utils.Cell;

public class MultipleListener extends EventListener {

	private EventListener console;
	private EventListener panel;
	
	@Override
	public void addCell(Cell c, int x, int y) {
		console.addCell(c, x, y);
		panel.addCell(c, x, y);
	}

	@Override
	public void removeCell(int x, int y) {
		console.removeCell(x, y);
		panel.removeCell(x, y);
	}

	@Override
	public void initialize(PDMatrix p) {
		console = new ConsoleListener();
		panel = new PanelListener();
		console.initialize(p);
		panel.initialize(p);
	}

}
