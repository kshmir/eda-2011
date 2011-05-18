package pd.utils;

public class HeapTree<T extends Comparable<? super T>>{

	private Node<T> root;
	
	public void clear()
	{
		root = null;
	}
	
	public boolean isEmpty()
	{
		return root == null;
	}
	
	private static class Node<T> 
	{
		Node<T> left;
		Node<T> right;
		T value;
		int descendants = 0;
		public Node(T val)
		{
			value = val;
		}
	}

	public void add(T value)
	{
		root = add((Node<T>)root, value);
	}
	
	private Node<T> add(Node<T> node, T value)
	{
		if (node == null)
		{
			node = new Node<T>(value);
			node.descendants = 0;
			return node;
		}
		
		
		
		if (value.compareTo(node.value) == 0)
			return node;
		
		if (value.compareTo(node.value) > 0)
		{
			T aux = node.value;
			node.value = value;
			value = aux;
		}
		if (node.left != null && node.right != null)
		{
			if (((Node<T>)node.left).descendants < ((Node<T>)node.right).descendants)
				node.left = add((Node<T>)node.left, value);
			else
				node.right = add((Node<T>)node.right, value);
		}
		else if (node.right != null)
		{
			node.left = add((Node<T>)node.left, value);
		}
		else
		{
			node.right = add((Node<T>)node.right, value);
		}
		
	
	
		if (node.right != null)
			node.descendants += 1 + ((Node<T>)node.right).descendants;
		if (node.left != null)
			node.descendants += 1 + ((Node<T>)node.left).descendants;
		
		
		return node;
	}
	
	public void delete()
	{
		remove(root.value);
	}
	
	public void remove(T value)
	{
		root = remove(value,root);
		
	}
	
	private Node<T> remove(T value, Node<T> node)
	{
		if (node == null)
			return null;
		
		if (value.compareTo(node.value) == 0)
		{
			if (node.left != null && node.right != null)
			{
				if (node.left.value.compareTo(node.right.value) > 0)
				{
					node.value = node.left.value;
					value = node.value;
					node.left = remove(value, node.left);
				}
				else
				{
					node.value = node.right.value;
					value = node.value;
					node.right = remove(value, node.right);
				}
			}
			else
			{
				if (node.left != null)
				{
					node.value = node.left.value;
					value = node.value;
					node.left = remove(value, node.left);
				}
				else if (node.right != null) 
				{
					node.value = node.right.value;
					value = node.value;
					node.right = remove(value, node.right);
				}
				else
				{
					node = remove(value,null);
				}
			}
		}
		else
		{
			if (node.left != null && node.left.value.compareTo(value) >= 0)
				node.left = remove(value, node.left);
			if (node.right != null && node.right.value.compareTo(value) >= 0)
				node.right = remove(value, node.right);
		}		
		return node;
	}

	public T getRoot()
	{
		T val = root.value;
		root = remove(root.value,root);
		return val;
	}
	
}
