package research;

import peersim.core.*;
import peersim.config.*;
import java.lang.Math;
import java.util.*;

public class Nest implements Control {
	private static final String PAR_DELTA = "delta";
	private static double delta;
	private static final String PAR_MAX_Battery = "maxBattery";
	private static double maxBattery;
	private static final String PAR_MAX_CAPACITY = "maxCapacity";
	private static double maxCapacity;
	private static final String PAR_STEP_LIMIT = "stepLimit";
	private static int stepLimit;

	private Node node;
	public double[] eggs;
	private double value;

	private static Random random = new Random();
	private static HashMap<Node, Double> candidates = new HashMap<Node, Double>();
	private static final double PI = Math.PI;
	private static Data targetData;
	private static Node lastPosition = null;
	private static double distance;
	private static int steps;

	public Nest(Node node) {
		this.node = node;
		eggs = new double[3];

		NPCuckoo parameter = (NPCuckoo) SharedResource.getNodeParameter("cuckoo", node);
		eggs[0] = parameter.getBattery();
		eggs[1] = ((double) parameter.getCapacity());
		eggs[2] = ((double) parameter.getUpTime());

		value = evaluate(eggs);
	}

	public Nest(String prefix) {
		delta = Configuration.getDouble(prefix + "." + PAR_DELTA);
		maxBattery = Configuration.getDouble(prefix + "." + PAR_MAX_Battery);
		maxCapacity = Configuration.getDouble(prefix + "." + PAR_MAX_CAPACITY);
		stepLimit = Configuration.getInt(prefix + "." + PAR_STEP_LIMIT);
	}

	public static ArrayList<Nest> runLevyWalk(ArrayList<Nest> nests, Node base, Data targeData, int cycle,
			int generation) {
		initializeProperty(targeData);

		Node startNode = base;
		if (generation != 0) {
			startNode = lastPosition;
		}
		while (steps < stepLimit) {
			// System.out.println("Start Node: " + startNode.getIndex());
			startNode = levyWalk(nests, startNode);
			// System.out.println("steps: " + steps);
		}
		lastPosition = startNode;
		// System.out.println("Last Node: " + lastPosition.getIndex());

		ArrayList<Integer> costList = SharedResource.getReplicationCost(3);
		costList.set(cycle, costList.get(cycle) + steps);
		SharedResource.setReplicationCost(3, costList);

		return nests;
	}

	private static void initializeProperty(Data data) {
		distance = 0;
		steps = 0;
		targetData = data;
	}

	private static Node levyWalk(ArrayList<Nest> nests, Node base) {
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

		while (d > 0) {
			candidates = new HashMap<Node, Double>();

			// System.out.println("d: " + d + "\tstart node: " + base.getIndex());
			// System.out.println("o: " + o);

			NodeCoordinate srcCrd = SharedResource.getCoordinate(base);

			// System.out.println("\tSRC Node: " + base.getIndex() + " (" + srcCrd.getX() +
			// ", " + srcCrd.getY() + ")");

			Link linkable = SharedResource.getLink(base);
			if (linkable.degree() == 0) {
				System.out.println("No Neighbor");
				System.out.println(base.toString());
				System.exit(10000);
			}
			for (int i = 0; i < linkable.degree(); i++) {
				Node n = linkable.getNeighbor(i);

				NodeCoordinate dstCrd = SharedResource.getCoordinate(n);

				// System.out
				// .println("\tTarget Node: " + n.getIndex() + " (" + dstCrd.getX() + ", " +
				// dstCrd.getY() + ")");

				if (isContain(srcCrd, dstCrd, o))
					candidates.put(n, distance);
			}
			if (candidates.isEmpty()) {
				return base;
			} else {
				base = minNode();
				// System.out.println("next Node:" + base.getIndex());
				compareAndReplace(nests, base);
				d = d - 1;
				steps++;
			}
		}

		return base;
	}

	private static boolean isContain(NodeCoordinate srcCrd, NodeCoordinate dstCrd, double o) {
		double srcX = srcCrd.getX();
		double srcY = srcCrd.getY();
		double dstX = dstCrd.getX();
		double dstY = dstCrd.getY();

		double y = dstY - srcY;
		double x = dstX - srcX;

		double radian = Math.atan2(y, x);
		double degree = radian * 180.0d / PI;
		if (y < 0.0d) {
			degree = 360.0d - Math.abs(degree);
		}

		if (checkRange(degree, o)) {
			// System.out.println("\tCheck == TRUE");
			return true;
		}
		return false;
	}

	/**
	 * o: 進む方向（角度） degree: ベースノードとターゲットノードの角度 up: ＋方向の許容範囲 bottom: ー方向の許容範囲
	 **/
	private static boolean checkRange(double degree, double o) {
		double up = degree + delta;
		double bottom = degree - delta;
		// System.out.println("\t\t" + o + ", " + degree + ", " + up + ", " + bottom);
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

	private static Node minNode() {
		double minValue = Double.MAX_VALUE;
		Node minNode = null;

		for (Map.Entry<Node, Double> entry : candidates.entrySet()) {
			if (entry.getValue() < minValue) {
				minValue = entry.getValue();
				minNode = entry.getKey();
			}
		}

		return minNode;
	}

	// 巣集合に低需要データを持っているノードが入るバグがある
	// 巣集合に入っているノードが探索で見つかった場合にもう一度入るバグがある
	private static void compareAndReplace(ArrayList<Nest> nests, Node newNode) {
		if (!checkNode(nests, newNode)) {
			return;
		}

		NPCuckoo parameter = (NPCuckoo) SharedResource.getNodeParameter("cuckoo", newNode);
		double newValue = evaluate(parameter.getBattery(), parameter.getCapacity(), parameter.getUpTime());

		for (Nest nest : nests) {
			double nowValue = nest.evaluate(nest.getEggs());

			if (newValue > nowValue) {
				nest.renewNest(newNode, parameter, newValue);
				sort(nests, 0, nests.size() - 1);
				return;
			}
		}
	}

	private static boolean checkNode(ArrayList<Nest> nests, Node node) {
		// Levy walkで見つけたノードが低需要データを持っている場合
		Storage storage = SharedResource.getNodeStorage("cuckoo", node);
		if (storage.contains(targetData)) {
			// System.out.println("Node having Data: " + targetData.getID() + " storage: " +
			// storage.toString());
			return false;
		}

		// Levy walkで見つけたノードが既に巣集合に入っている場合
		for (Nest nest : nests) {
			if (nest.getNode().getIndex() == node.getIndex()) {
				// System.out.println("Nest having Node: " + nest.getNode().getIndex() + "
				// node:" + node.getIndex());
				return false;
			}
		}

		return true;
	}

	public static double evaluate(double battery, double capacity, double upTime) {
		battery = battery / maxBattery;
		capacity = capacity / maxCapacity;
		upTime = upTime / 500.0;

		double value = 2.0 * battery + 1.5 * capacity + upTime;

		if (capacity < 1.0) {
			value = Double.MIN_VALUE;
		}

		return value;
	}

	public static void sort(ArrayList<Nest> nests, int lb, int ub) {
		int i, j, k;
		double pivot;
		Nest tmpNest;

		if (lb < ub) {
			k = (lb + ub) / 2;
			pivot = nests.get(k).getValue();
			i = lb;
			j = ub;

			do {
				while (nests.get(i).getValue() < pivot) {
					i++;
				}
				while (nests.get(j).getValue() > pivot) {
					j--;
				}
				if (i <= j) {
					tmpNest = nests.get(i);

					nests.set(i, nests.get(j));

					nests.set(j, tmpNest);
					i++;
					j--;
				}
			} while (i <= j);
			sort(nests, lb, j);
			sort(nests, i, ub);
		}
	}

	// public void abandon() {
	// Node newNode = null;

	// while (true) {
	// newNode = levyWalk(node);
	// if (!Objects.equals(newNode, null))
	// break;
	// }
	// NPCuckoo parameter = (NPCuckoo) SharedResource.getNodeParameter("cuckoo",
	// newNode);

	// this.node = newNode;
	// eggs[0] = parameter.getBattery();
	// eggs[1] = parameter.getCapacity();
	// eggs[2] = parameter.getUpTime();
	// value = evaluate(eggs);
	// }

	public void abandon(int nodeID) {
		Node newNode = Network.get(nodeID);
		NPCuckoo parameter = (NPCuckoo) SharedResource.getNodeParameter("cuckoo", newNode);
		double[] newEggs = new double[3];
		newEggs[0] = parameter.getBattery();
		newEggs[1] = ((double) parameter.getCapacity());
		newEggs[2] = ((double) parameter.getUpTime());
		double newValue = evaluate(newEggs);

		if (newValue > evaluate(eggs)) {
			renewNest(newNode, parameter, newValue);
		}
	}

	public double evaluate(double[] eggs) {
		double battery = eggs[0] / maxBattery;
		double capacity = eggs[1] / maxCapacity;
		double upTime = eggs[2] / 500.0;
		// double upTime = eggs[2];

		// double value = 1.0 * battery + 1.0 * capacity;
		double value = 2.0 * battery + 1.5 * capacity + upTime;
		// double value = 1.0 * battery + 1.0 * capacity + Math.log10(upTime);
		if (eggs[1] < 1.0) {
			value = Double.MIN_VALUE;
		}

		return value;
	}

	public double[] getEggs() {
		return eggs;
	}

	public Node getNode() {
		return this.node;
	}

	public double getValue() {
		return this.value;
	}

	private void renewNest(Node newNode, NPCuckoo parameter, double newValue) {
		this.node = newNode;
		this.eggs[0] = parameter.getBattery();
		this.eggs[1] = parameter.getCapacity();
		this.eggs[2] = parameter.getUpTime();
		this.value = newValue;
	}

	public boolean execute() {
		return false;
	}

}