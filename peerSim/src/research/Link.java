package research;

import peersim.config.*;
import peersim.core.*;

public class Link implements Protocol, Linkable {

	private static final int DEFAULT_INITIAL_CAPACITY = 100;
	private static final String PAR_INITCAP = "capacity";

	protected Node[] neighbors;
	protected int len;

	public Link(String s) {
		neighbors = new Node[Configuration.getInt(s + "." + PAR_INITCAP, DEFAULT_INITIAL_CAPACITY)];
		len = 0;
	}

	public Object clone() {
		Link link = null;
		try {
			link = (Link) super.clone();
		} catch (CloneNotSupportedException e) {
		} // never happens
		link.neighbors = new Node[neighbors.length];
		System.arraycopy(neighbors, 0, link.neighbors, 0, len);
		link.len = len;
		return link;
	}

	public boolean contains(Node n) {
		for (int i = 0; i < len; i++) {
			if (neighbors[i] == n)
				return true;
		}
		return false;
	}

	public boolean addNeighbor(Node n) {
		for (int i = 0; i < len; i++) {
			if (neighbors[i] == n) {
				// System.out.println(len);
				return false;
			}
		}
		if (len == neighbors.length) {
			Node[] temp = new Node[3 * neighbors.length / 2];
			System.arraycopy(neighbors, 0, temp, 0, neighbors.length);
			neighbors = temp;
		}
		neighbors[len] = n;
		len++;
		// System.out.println(len);
		return true;
	}

	public boolean removeNeighbor(int i) {
		neighbors[i] = neighbors[len - 1];
		neighbors[len - 1] = null;
		len--;
		// System.out.println(len);
		return true;
	}

	public Node getNeighbor(int i) {
		return neighbors[i];
	}

	public int degree() {
		return len;
	}

	public void pack() {
		if (len == neighbors.length)
			return;
		Node[] temp = new Node[len];
		System.arraycopy(neighbors, 0, temp, 0, len);
		neighbors = temp;
	}

	public String toString() {
		if (neighbors == null)
			return "DEAD!";
		StringBuffer buffer = new StringBuffer();
		buffer.append("len=" + len + " maxlen=" + neighbors.length + " [");
		for (int i = 0; i < len; ++i) {
			buffer.append(neighbors[i].getIndex() + " ");
		}
		return buffer.append("]").toString();
	}

	public void onKill() {
		neighbors = new Node[DEFAULT_INITIAL_CAPACITY];
		len = 0;
	}

}
