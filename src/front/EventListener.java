package front;

import java.io.IOException;
import java.util.Stack;

import pd.PDMatrix;
import pd.utils.Cell;
import pd.utils.Movement;
import pd.utils.Point;

/**
 * @author MurciŽlagos Any class which implements this listener should be able
 *         to interact with the back end.
 */
public abstract class EventListener {
	
	public PrintAction	action;
	protected PDMatrix	mat;
	protected boolean	stepped			= false;
	private long		startTime;
	private long		endTime			= 0;
	protected long		totalWaitTime;
	private long		bestLength;
	private long		iterations;
	private long		sameLengthFound	= 0;
	public boolean		repeats			= false;
	
	protected EventListener(PrintAction act, PDMatrix p, boolean stepped, boolean repeats) {
		action = act;
		mat = p;
		this.repeats = repeats;
		this.stepped = stepped;
	}
	
	protected void printReport() {
		if (action != PrintAction.PROGRESS || endTime != 0) {
			
			if (endTime != 0)
				System.out.println("Final statistics:");
			else
				System.out.println("Current statistics:");
			
			// This times are completely approximate by the listeners, they are
			// not a real proof of speed, except when using ENDRESULT
			System.out.println("\tApprox. Total time:" + getTotalTime());
			System.out.println("\tApprox. Total wait time:" + getTotalWaitTime());
			System.out.println("\tApprox. Total computing time:" + getTotalComputeTime());
			
			System.out.println("\tBest length found:" + bestLength);
			if (repeats)
				System.out.println("\tNumber of times found:" + sameLengthFound);
			System.out.println("\tTotal iterations:" + getIterations());
			
		}
	}
	
	public void addIteration() {
		iterations++;
	}
	
	public long getIterations() {
		return iterations;
	}
	
	public void setStart() {
		startTime = System.currentTimeMillis();
	}
	
	public void setBestLength(int length) {
		bestLength = length;
	}
	
	public void setEnd() {
		endTime = System.currentTimeMillis();
	}
	
	public long getTotalTime() {
		return ((endTime != 0) ? endTime : System.currentTimeMillis()) - startTime;
	}
	
	public long getTotalWaitTime() {
		return totalWaitTime;
	}
	
	public long getTotalComputeTime() {
		return getTotalTime() - getTotalWaitTime();
	}
	
	protected void makeDelayOrAskStdin(PrintAction action) {
		if (action == this.action) {
			long l1 = System.currentTimeMillis();
			if (!stepped) {
				try {
					
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
			} else {
				try {
					System.in.read();
				} catch (IOException e) {
				}
			}
			totalWaitTime += System.currentTimeMillis() - l1;
		}
	}
	
	/**
	 * @param c
	 * @param x
	 * @param y
	 *            Adds the specified kind of cell to the position (x,y) MUST BE
	 *            OVERRIDEN
	 */
	public boolean addCell(Cell c, int x, int y, PrintAction action) {
		return addCell(c, x, y, action, false);
	}
	
	/**
	 * @param c
	 * @param x
	 * @param y
	 *            Adds the specified kind of cell to the position (x,y) MUST BE
	 *            OVERRIDEN
	 */
	public boolean addCell(Cell c, int x, int y, PrintAction action, boolean printAllOnOnce) {
		if (!printAllOnOnce)
			makeDelayOrAskStdin(action);
		return (action == this.action);
	}
	
	/**
	 * @param x
	 * @param y
	 *            Removes the cell and replaces it with a blank.
	 */
	public boolean removeCell(int x, int y, PrintAction action) {
		makeDelayOrAskStdin(action);
		return (action == this.action);
	}
	
	/**
	 * @param stack
	 * @param mat
	 *            rebuilds the matrix and adds every cell.
	 */
	@SuppressWarnings("unchecked")
	public void addAll(Stack<Cell> stack, PrintAction action) {
		if (action == this.action) {
			stack = (Stack<Cell>) stack.clone();
			stack.pop();
			Movement m = Cell.startDirection;
			Point p = m.applyTo(mat.getStartPoint());
			while (!stack.isEmpty()) {
				Cell c = stack.pop();
				addCell(c, p.x, p.y, action, true);
				m = c.NextDir(m);
				p = m.applyTo(p);
			}
			this.printReport();
		}
		makeDelayOrAskStdin(action);
	}
	
	public void setSameLengthFound(long sameLengthFound) {
		this.sameLengthFound = sameLengthFound;
	}
	
	public long getSameLengthFound() {
		return sameLengthFound;
	}
}
