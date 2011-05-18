package pd.utils;

import java.util.ArrayList;
import java.util.List;


public class Node<T> {
	private int id = -1;
	public T value;
	public ArrayList<Node<T>> pointsTo;
	public Object tag;
	
	public Node()
	{
		
	}
	
	public Node(T value)
	{
		this.value = value;
		this.pointsTo = new ArrayList<Node<T>>();
	}
	
	/**
	 * Asks for an ID for the Node
	 * Built on a generic way with the memory address.
	 * @return
	 */
	public String getID()
	{
		return String.valueOf((id != -1) ? id : (id = this.hashCode()));
	}

	public void setValue(T value) {
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Node<T> other = (Node<T>) obj;
		if (id != other.id)
			return false;
		if (pointsTo == null) {
			if (other.pointsTo != null)
				return false;
		} else if (!pointsTo.equals(other.pointsTo))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
	
	
}
