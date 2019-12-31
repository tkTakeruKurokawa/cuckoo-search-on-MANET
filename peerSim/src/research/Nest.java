package research;

import peersim.core.*;
import peersim.config.*;
import java.lang.Math;
import java.util.*;

public class Nest implements Control {
	private static final String PAR_DELTA = "delta";
	private static double delta;
	private static final String PAR_CAPACITY = "capacity";
	private static double capacity;

	private final double GAMMA = 1.329340388179137020474;
	private final double GAMMA2 = 0.9064024770554770779827;
	private final double PI = Math.PI;
	private final double BETA = 1.5;
	private final double NUME = GAMMA * Math.sin(PI * BETA / 2.0);
	private final double DENOM = GAMMA2 * BETA * Math.pow(2.0, (BETA - 1.0) / 2.0);
	private final double SIGMA = Math.pow(NUME / DENOM, 1.0 / BETA);

	private HashMap<Node, Double> pn = new HashMap<Node, Double>();
	private Random random = new Random();
	private Node node;
	public double[] egg;
	private double[] newEgg;
	private double value;
	private double distance;
	private int cost;

	public Nest(Node node) {
		this.node = node;
		egg = new double[3];
		newEgg = new double[3];

		NPCuckoo parameter = (NPCuckoo) SharedResource.getNodeParameter("cuckoo", node);
		egg[0] = parameter.getBattery();
		egg[1] = ((double) parameter.getCapacity());
		egg[2] = ((double) parameter.getUpTime());

		value = evaluate(egg);
	}

	public Nest(String prefix) {
		delta = Configuration.getDouble(prefix + "." + PAR_DELTA);
		capacity = Configuration.getDouble(prefix + "." + PAR_CAPACITY);
	}

	private Node minNode() {
		int j = 0;
		Node[] bestNode = new Node[pn.size()];
		double[] bestDouble = new double[pn.size()];
		for (Map.Entry<Node, Double> map : pn.entrySet()) {
			bestNode[j] = map.getKey();
			bestDouble[j] = map.getValue();
			// System.out.println("\t\tCandidate: (" + map.getKey().getIndex() + ", " +
			// map.getValue() + ")");
			j++;
		}

		double min = bestDouble[0];
		Node minNode = bestNode[0];
		for (int i = 1; i < j; i++) {
			if (min > bestDouble[i]) {
				min = bestDouble[i];
				minNode = bestNode[i];
			}
		}

		return minNode;
	}

	private boolean checkRange(double degree, double o) {
		double up = degree + delta;
		double bottom = degree - delta;
		// System.out.println("\t\t"+degree + ", " + up + ", " + bottom);
		if (bottom < o && o < up) {
			distance = Math.abs(degree - o);
			// System.out.println("\tdistance: " + distance);
			return true;
		}
		if (bottom < 0.0d) {
			bottom = 360.0d + bottom;
			// System.out.println("\tbottom: " + bottom);
			if (bottom < o && o < 360.0d) {
				distance = Math.abs((360.0d - o) + degree);
				// System.out.println("\tdistance: " + distance);
				return true;
			}
		}
		if (up > 360.0d) {
			up = up - 360.0d;
			// System.out.println("\tup: " + up);
			if (0.0d < o && o < up) {
				distance = Math.abs((360.0d - degree) + o);
				// System.out.println("\tdistance: " + distance);
				return true;
			}
		}

		return false;
	}

	private boolean isContain(NodeCoordinate srcCrd, NodeCoordinate dstCrd, double o) {
		double srcX = srcCrd.getX();
		double srcY = srcCrd.getY();
		double dstX = dstCrd.getX();
		double dstY = dstCrd.getY();

		double y = dstY - srcY;
		double x = dstX - srcX;

		double radian = Math.atan2(y, x);
		double degree = radian * 180.0d / PI;
		if (y < 0.0d)
			degree = 360.0d - Math.abs(degree);

		if (checkRange(degree, o)) {
			// System.out.println("\tCheck == TRUE");
			return true;
		}
		return false;
	}

	private Node levyWalk(Node base) {
		double r, o;
		int d;

		do {
			r = random.nextDouble();
		} while (r < 0.1d);

		d = (int) Math.round(Math.pow(r, -2.0));
		// d = (int) Math.round(s*2);
		// System.out.println("d: " + d);
		o = random.nextDouble() * 359.9999999999;

		// System.out.println(d);

		cost = 0;
		while (d > 0) {
			pn = new HashMap<Node, Double>();

			// System.out.println("d: " + d);
			// System.out.println("o: " + o);

			NodeCoordinate srcCrd = SharedResource.getCoordinate(base);

			// System.out.println("\tSRC Node: " + base.getIndex() + " (" + srcCrd.getX() +
			// ", " + srcCrd.getY() + ")");

			Link linkable = SharedResource.getLink(base);
			// System.out.println(linkable.degree());
			for (int i = 0; i < linkable.degree(); i++) {
				Node n = linkable.getNeighbor(i);

				NodeCoordinate dstCrd = SharedResource.getCoordinate(n);

				// System.out.println("\tTarget Node: " + n.getIndex() + " (" + dstCrd.getX() +
				// ", " + dstCrd.getY() + ")");

				if (isContain(srcCrd, dstCrd, o))
					pn.put(n, distance);
			}
			if (pn.isEmpty()) {
				base = null;
				return base;
			} else {
				base = minNode();
				// System.out.println("next Node:" + base.getIndex());
				d = d - 1;
				cost++;
			}
		}

		return base;
	}

	public boolean replace(Nest base, int cycle) {
		Node src = base.getNode();
		Node candidate;

		candidate = levyWalk(src);
		if (Objects.equals(candidate, null)) {
			return false;
		}

		ArrayList<Integer> costList = SharedResource.getCost(3);
		costList.set(cycle, costList.get(cycle) + cost);
		SharedResource.setCost(3, costList);

		NPCuckoo parameter = (NPCuckoo) SharedResource.getNodeParameter("cuckoo", candidate);
		newEgg[0] = parameter.getBattery();
		newEgg[1] = parameter.getCapacity();
		newEgg[2] = parameter.getUpTime();

		double newValue = evaluate(newEgg);
		// System.out.println("This Node: " + src.getIndex() + " value " + value + " ("
		// + egg[0] + ", " + egg[1] + ")");
		// System.out.println("Candidate Node: " + candidate.getIndex() + " value " +
		// newValue + " (" + newEgg[0] + ", " + newEgg[1] + ")");

		if (newValue > value) {
			this.node = candidate;
			// System.out.println("After change. Node: " + node.getIndex());
			double[] tmp = egg;
			egg = newEgg;
			newEgg = tmp;
			value = newValue;
		}

		return true;
	}

	public void abandon() {
		Node newNode = null;

		while (true) {
			newNode = levyWalk(node);
			if (!Objects.equals(newNode, null))
				break;
		}
		NPCuckoo parameter = (NPCuckoo) SharedResource.getNodeParameter("cuckoo", newNode);

		this.node = newNode;
		egg[0] = parameter.getBattery();
		egg[1] = parameter.getCapacity();
		egg[2] = parameter.getUpTime();
		value = evaluate(egg);
	}

	public void abandon(int nodeID) {
		Node newNode = Network.get(nodeID);
		NPCuckoo parameter = (NPCuckoo) SharedResource.getNodeParameter("cuckoo", newNode);
		newEgg[0] = parameter.getBattery();
		newEgg[1] = ((double) parameter.getCapacity());
		newEgg[2] = ((double) parameter.getUpTime());

		if (evaluate(newEgg) > evaluate(egg)) {
			this.node = newNode;
			egg[0] = newEgg[0];
			egg[1] = newEgg[1];
			egg[2] = newEgg[2];
			value = evaluate(newEgg);
		}
	}

	public double evaluate(double[] egg) {
		double battery = egg[0] / 100.0;
		double capacity = egg[1] / this.capacity;
		double upTime = egg[2] / 500.0;
		// double upTime = egg[2];

		// double value = 1.0 * battery + 1.0 * capacity;
		double value = 2.0 * battery + 1.5 * capacity + upTime;
		// double value = 1.0 * battery + 1.0 * capacity + Math.log10(upTime);
		if (egg[1] < 1.0)
			value = Double.NEGATIVE_INFINITY;
		return value;
	}

	public Node getNode() {
		return this.node;
	}

	public double getValue() {
		return value;
	}

	public boolean execute() {
		return false;
	}
}