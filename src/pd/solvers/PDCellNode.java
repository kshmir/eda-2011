package pd.solvers;

import pd.CellCountMap;
import pd.utils.Cell;
import pd.utils.Movement;
import pd.utils.Point;

/**
 * Our heuristic solution differs a lot from the exact solution in the way 
 * it calculates the path.
 * PDCellNode is a key class in the heuristic solution.
 * A valid path should always start with a CellNode which is in the startPoint.
 * CellNodes form an inverse tree in which the root is the point from which the Heuristic starts.
 */
public class PDCellNode implements Comparable<PDCellNode> {
	// Parent of the CellNode, if it's null, then the CellNode is a start point.
	public PDCellNode	parent;
	// Cell (piece) contained in the path.
	public Cell			cell;
	// Location in the map.
	public Point		location;
	// Movement it contains.
	public Movement		movement;
	// Amount of cells available for the given cell.
	public CellCountMap	countMap;
	// Length of the current path.
	public int			length;
	
	/**
	 * Default comparator, 
	 * Compares the distance to the start point with another node.
	 * Used by the aStar algorithm at the beggining.
	 */
	@Override
	public int compareTo(PDCellNode p) {
		return distanceFromPoints(this.location,
				PDApproximateSolver.m.getStartPoint().translate(Cell.startDirection.versor()))
				- distanceFromPoints(p.location,
						PDApproximateSolver.m.getStartPoint().translate(Cell.startDirection.versor()));
	}
	
	/*
	 * The same.
	 */
	public int distTostart() {
		return distanceFromPoints(this.location,
				PDApproximateSolver.m.getStartPoint().translate(Cell.startDirection.versor()));
	}
	
	public static int distanceFromPoints(Point p, Point p2) {
		return (int) Math.sqrt((p.x - p2.x) * (p.x - p2.x) + (p.y - p2.y) * (p.y - p2.y));
	}
	
	/*
	 * Returns, if exists, the cell in the path on the point given.
	 * We'd wish we could make this faster, it would improve things a bit.
	 * It's O(m) where m is the length of the path, it might not be good...
	 */
	public Cell historyPoints(Point p) {
		PDCellNode c = this;
		while (c != null) {
			if (c.location.equals(p))
				return c.cell;
			c = c.parent;
		}
		return null;
	}
	
	public Cell historyPoints(Point p, PDCellNode startNode, PDCellNode next, Point startPoint) {
		PDCellNode c = this;
		while (c != null) {
			if (c.location.equals(p))
				return c.cell;
			c = c.parent;
		}
		c = next;
		while (c != null) {
			if (c.location.equals(p))
				return c.cell;
			c = c.parent;
		}
		c = startNode;
		while (c != null && c.location != startPoint) {
			if (c.location.equals(p))
				return c.cell;
			c = c.parent;
		}
		
		
		return null;
	}
	
	public PDCellNode topParent() {
		PDCellNode c = this;
		while (c != null) {
			if (c.parent == null)
				return c;
			c = c.parent;
		}
		return null;
	}
	
	private PDCellNode() {
	}
	
	// For the start.
	public PDCellNode(Point start, Movement startDirection, CellCountMap map, Cell node) {
		location = start;
		movement = startDirection;
		length = 1;
		this.cell = node;
		this.countMap = map.clone();
	}
	
	// Builds a new cell by just giving it a node. It makes algoritms simplier.
	public static PDCellNode fromCell(PDCellNode parent, Cell node) {
		PDCellNode n = new PDCellNode();
		n.parent = parent;
		n.cell = node;
		n.length = (parent.length + 1);
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
	
	public PDCellNode clone()
	{
		PDCellNode n = new PDCellNode();
		if (this.parent != null)
			n.parent = this.parent.clone();
		n.cell = this.cell;
		n.length = this.length;
		n.location = new Point(this.location.x, this.location.y);
		n.movement = this.movement;
		n.countMap = this.countMap.clone();
		return n;
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
	
	@Override
	public String toString() {
		return cell.toString();
	}
	
}