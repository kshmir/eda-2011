package pd.solvers;

import pd.CellCountMap;
import pd.utils.Cell;
import pd.utils.Movement;
import pd.utils.Point;

public class PDCellNode implements Comparable<PDCellNode> {
	public PDCellNode	parent;
	public Cell			cell;
	public Point		location;
	public Movement		movement;
	public CellCountMap	countMap;
	public char			length;
	
	@Override
	public int compareTo(PDCellNode p) {
		return distanceFromPoints(this.location,
				PDApproximateSolver.m.getStartPoint().translate(Cell.startDirection.versor()))
				- distanceFromPoints(p.location,
						PDApproximateSolver.m.getStartPoint().translate(Cell.startDirection.versor()));
	}
	
	public int distTostart() {
		return distanceFromPoints(this.location,
				PDApproximateSolver.m.getStartPoint().translate(Cell.startDirection.versor()));
	}
	
	public static int distanceFromPoints(Point p, Point p2) {
		return (int) Math.sqrt((p.x - p2.x) * (p.x - p2.x) + (p.y - p2.y) * (p.y - p2.y));
	}
	
	public Cell historyPoints(Point p) {
		PDCellNode c = this;
		while (c != null) {
			if (c.location.equals(p))
				return c.cell;
			c = c.parent;
		}
		return null;
	}
	
	private PDCellNode() {
	}
	
	public PDCellNode(Point start, Movement startDirection, CellCountMap map, Cell node) {
		location = start;
		movement = startDirection;
		length = 1;
		this.cell = node;
		this.countMap = map.clone();
	}
	
	public static PDCellNode fromCell(PDCellNode parent, Cell node) {
		PDCellNode n = new PDCellNode();
		n.parent = parent;
		n.cell = node;
		n.length = (char) (parent.length + 1);
		n.location = parent.location.translate(parent.movement.versor());
		n.movement = node.NextDir(parent.movement);
		n.countMap = parent.countMap.clone();
		
		n.countMap.decreasePiecesLeft(node);
		
		return n;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cell == null) ? 0 : cell.hashCode());
		result = prime * result + ((countMap == null) ? 0 : countMap.hashCode());
		result = prime * result + length;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((movement == null) ? 0 : movement.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PDCellNode other = (PDCellNode) obj;
		if (cell != other.cell)
			return false;
		if (countMap == null) {
			if (other.countMap != null)
				return false;
		} else if (!countMap.equals(other.countMap))
			return false;
		if (length != other.length)
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (movement != other.movement)
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
	}
	
}