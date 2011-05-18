package pd.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class DiGraphImpl<T> {

	private HashMap<T, Node<T>> nodeMap = new HashMap<T, Node<T>>();
	private ArrayList<Node<T>> nodeList = new ArrayList<Node<T>>();

	public void removeNode(T k) {
		Node<T> node = nodeMap.get(k);
		nodeList.remove(node);
		nodeMap.remove(k);
		for (int i = 0; i < nodeList.size(); i++) {
			Node<T> n = nodeList.get(i);
			for (int j = 0; j < n.pointsTo.size(); j++) {
				Node<T> nod = n.pointsTo.get(i);
				if (n.equals(nod)) {
					n.pointsTo.remove(j);
					j--;
				}
			}
		}
	}

	public int nodeCount() {
		return nodeList.size();
	}

	public void addArc(T from, T to) {
		nodeMap.get(from).pointsTo.add(nodeMap.get(to));
	}

	public void removeArc(T from, T to) {
		ArrayList<Node<T>> arcs = nodeMap.get(from).pointsTo;
		for (int i = 0; i < arcs.size(); i++) {
			Node<T> el = arcs.get(i);
			if (el.equals(to)) {
				arcs.remove(i);
				i--;
			}
		}
	}

	public boolean isArc(T from, T val) {
		ArrayList<Node<T>> arcs = nodeMap.get(from).pointsTo;
		for (int i = 0; i < arcs.size(); i++) {
			Node<T> el = arcs.get(i);
			if (el.value.equals(val)) {
				return true;
			}
		}
		return false;
	}

	public int arcCount() {
		int counter = 0;
		for (int i = 0; i < nodeList.size(); i++) {
			Node<T> el = nodeList.get(i);
			counter += el.pointsTo.size();
		}
		return counter;
	}

	public int inDegree(T from) {
		Node<T> node = nodeMap.get(from);
		int degree = 0;
		for (int i = 0; i < nodeList.size(); i++) {
			Node<T> n = nodeList.get(i);
			for (int j = 0; j < n.pointsTo.size(); j++) {
				Node<T> nod = n.pointsTo.get(i);
				if (n.equals(nod)) {
					degree++;
				}
			}
		}
		return degree;
	}

	public int outDegree(T from) {
		return nodeMap.get(from).pointsTo.size();
	}

	public void addNode(T k) {
		Node<T> node = new Node<T>(k);
		nodeList.add(node);
		nodeMap.put(k, node);
	}

	public Node<T> getStartNode() {
		return nodeList.get(0);
	}

}
