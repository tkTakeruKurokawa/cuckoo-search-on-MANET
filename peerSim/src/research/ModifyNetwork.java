package research;

import peersim.config.*;
import peersim.core.*;
import java.math.BigDecimal;
import java.lang.Math;
import java.util.*;

public class ModifyNetwork implements Control {
	private static final String PAR_LEAVE_CAPACITY = "leave_capacity";
	private static int leaveCapacity;
	private static final String PAR_JOIN_CAPACITY = "join_capacity";
	private static int joinCapacity;
	private static final String PAR_LEAVE_LAMBDA = "leave_lambda";
	private static int leaveLambdaMax;
	private static final String PAR_JOIN_LAMBDA = "join_lambda";
	private static int joinLambdaMax;

	private static Queue<Node> queue = new ArrayDeque<Node>();
	private static ArrayList<Integer> rnd = new ArrayList<Integer>();
	private static ArrayList<Integer> hit = new ArrayList<Integer>();
	private static ArrayList<Double> joinLambda = new ArrayList<Double>();
	private static ArrayList<Double> leaveLambda = new ArrayList<Double>();
	private static ArrayList<Double> joinCycle = new ArrayList<Double>();
	private static ArrayList<Double> leaveCycle = new ArrayList<Double>();
	private static Random random = new Random();
	private static BigDecimal threshold = BigDecimal.valueOf(0.0d);

	private static int joinSum = 0;
	private static int leaveSum = 0;
	private static int count = 0;

	public ModifyNetwork(String prefix) {
		leaveCapacity = Configuration.getInt(prefix + "." + PAR_LEAVE_CAPACITY);
		joinCapacity = Configuration.getInt(prefix + "." + PAR_JOIN_CAPACITY);
		leaveLambdaMax = Configuration.getInt(prefix + "." + PAR_LEAVE_LAMBDA);
		joinLambdaMax = Configuration.getInt(prefix + "." + PAR_JOIN_LAMBDA);

		for (int i = 0; i < 100; i++) {
			rnd.add(i);
		}

		for (int i = 0; i < joinCapacity; i++) {
			joinLambda.add(1.0 / (random.nextDouble() * ((double) joinLambdaMax)));
			joinCycle.add(0.0);
		}
		for (int i = 0; i < leaveCapacity; i++) {
			leaveLambda.add(1.0 / (random.nextDouble() * ((double) leaveLambdaMax)));
			leaveCycle.add(0.0);
		}
	}

	public static boolean addLink(Link srcLink, Node addNode) {
		return srcLink.addNeighbor(addNode);
	}

	public static boolean addNode() {
		// System.out.println("*************** ADD NODE ***************");
		Node newNode = (Node) Network.prototype.clone();
		Network.add(newNode);
		// System.out.println("add Node ID: " + newNode.getIndex());

		NodeCoordinate newCrd = SharedResource.getCoordinate(newNode);
		newCrd.setCoordinate();

		BaseParameter parameter = SharedResource.getBaseParameter(newNode);
		parameter.setParameter();

		NPOwner npo = (NPOwner) SharedResource.getNodeParameter("owner", newNode);
		npo.setBattery(parameter.getBattery());
		npo.setCapacity(parameter.getCapacity());

		NPPath npp = (NPPath) SharedResource.getNodeParameter("path", newNode);
		npp.setBattery(parameter.getBattery());
		npp.setCapacity(parameter.getCapacity());

		NPRelate npr = (NPRelate) SharedResource.getNodeParameter("relate", newNode);
		npr.setBattery(parameter.getBattery());
		npr.setCapacity(parameter.getCapacity());
		npr.setContribution(random.nextDouble());

		NPCuckoo npc = (NPCuckoo) SharedResource.getNodeParameter("cuckoo", newNode);
		npc.setBattery(parameter.getBattery());
		npc.setCapacity(parameter.getCapacity());
		npc.setUpTime(1);

		Link newLink = SharedResource.getLink(newNode);
		// System.out.println("num of Nodes to add: " + num);

		while (true) {
			int dstID = 0;
			while (dstID < Network.size()) {
				if (dstID == newNode.getIndex()) {
					dstID++;
					continue;
				}

				Node dstNode = Network.get(dstID);
				NodeCoordinate dstCrd = SharedResource.getCoordinate(dstNode);
				Link dstLink = SharedResource.getLink(dstNode);

				if (RandomGeometricGraph.isConnect(newCrd, dstCrd)) {
					addLink(newLink, dstNode);
					addLink(dstLink, newNode);
				}

				dstID++;
			}

			if (newLink.degree() > 0) {
				return true;
			} else {
				newCrd.setCoordinate();
			}
		}
	}

	public static boolean removeLink(Node srcNode) {
		// System.out.println("*************** REMOVE NODE ***************");

		Link srcLinkSet = SharedResource.getLink(srcNode);

		// 削除するノードのリンクを取得し、隣接ノードをキューに入れる
		for (int i = 0; i < srcLinkSet.degree(); i++)
			queue.add(srcLinkSet.getNeighbor(i));

		// 隣接ノードに削除するノードを持つ,ノードから、該当ノードを削除する
		while (true) {
			Node dstNode = queue.poll();
			if (dstNode == null) {
				break;
			}
			Link dstLinkSet = SharedResource.getLink(dstNode);

			for (int i = 0; i < dstLinkSet.degree(); i++) {
				if (Objects.equals(dstLinkSet.getNeighbor(i), srcNode)) {
					// System.out.printf("Node %d's len ", n.getIndex());
					dstLinkSet.removeNeighbor(i);
				}
			}
		}

		srcLinkSet.onKill();
		return true;
	}

	public static boolean removeNode(Node node) {
		// Storage storage = SharedResource.getStorage(node);
		// storage.clear();

		Storage sOwner = SharedResource.getNodeStorage("owner", node);
		sOwner.clear();

		Storage sPath = SharedResource.getNodeStorage("path", node);
		sPath.clear();

		Storage sRelate = SharedResource.getNodeStorage("relate", node);
		sRelate.clear();

		Storage sCuckoo = SharedResource.getNodeStorage("cuckoo", node);
		sCuckoo.clear();

		removeLink(node);

		Network.remove(node.getIndex());
		// System.out.println("ALL Node: " + Network.size());
		// System.out.println();
		return true;
	}

	private static boolean probability(Double p) {
		hit = new ArrayList<Integer>();
		Collections.shuffle(rnd);

		int num = (int) Math.round(p * 100);
		if (num > 0)
			num = random.nextInt(num);
		for (int i = 0; i < num; i++)
			hit.add(rnd.get(i));

		int candidate = random.nextInt(100);
		if (hit.contains(candidate)) {
			return true;
		}

		return false;
	}

	public static double factorial(int src) {
		if (src == 0) {
			return 1.0;
		}
		double value = 1;
		for (int i = 1; i <= src; i++) {
			value *= i;
		}

		// System.out.println(value);
		return ((double) value);
	}

	private static boolean poisson(String type, int index, double lambda, double num) {
		double p = Math.exp(-1 * lambda * num) * (Math.pow(lambda * num, 1.0)) / factorial(1);
		if (Objects.equals(type, "join") && lambda * num > 1.0 && p <= 0.009) {
			joinLambda.set(index, 1.0 / (random.nextDouble() * ((double) joinLambdaMax)));
			joinCycle.set(index, 0.0);
		}
		if (Objects.equals(type, "leave") && lambda * num > 1.0 && p <= 0.009) {
			leaveLambda.set(index, 1.0 / (random.nextDouble() * ((double) leaveLambdaMax)));
			leaveCycle.set(index, 0.0);
		}
		return probability(p);
	}

	public static void joinCandidate() {
		int num = 0;
		for (int i = 0; i < joinLambda.size(); i++) {
			if (poisson("join", i, joinLambda.get(i), joinCycle.get(i))) {
				joinLambda.set(i, 1.0 / (random.nextDouble() * ((double) joinLambdaMax)));
				joinCycle.set(i, 0.0);
				addNode();
				num++;
				joinSum++;
			}
		}

		// System.out.println("join: " + num);
		// System.out.println("join Sum:" + joinSum);
		// System.out.println("Average join: " + (joinSum / ((double) count)));
	}

	public static void leaveCandidate() {
		int num = 0;
		for (int i = 0; i < leaveCapacity; i++) {
			if (poisson("leave", i, leaveLambda.get(i), leaveCycle.get(i))) {
				leaveLambda.set(i, 1.0 / (random.nextDouble() * ((double) leaveLambdaMax)));
				leaveCycle.set(i, 0.0);
				int nodeID = random.nextInt(Network.size());
				removeNode(Network.get(nodeID));
				num++;
				leaveSum++;
			}
		}

		// System.out.println("leave: " + num);
		// System.out.println("leave Sum:" + leaveSum);
		// System.out.println("Average leave: " + (leaveSum / ((double) count)));
	}

	public static BigDecimal reduceBattery(double battery) {
		BigDecimal a = BigDecimal.valueOf(battery);
		// BigDecimal b = BigDecimal.valueOf(0.15d);
		BigDecimal b = BigDecimal.valueOf(random.nextDouble() * 0.3);
		BigDecimal result;
		// if(random.nextBoolean())
		result = a.subtract(b);
		// else
		// result = a;
		return result;
	}

	public boolean execute() {

		for (int nodeID = 0; nodeID < Network.size(); nodeID++) {
			Node node = Network.get(nodeID);
			BaseParameter parameter = SharedResource.getBaseParameter(node);
			BigDecimal newValue = reduceBattery(parameter.getBattery());
			parameter.setBattery(newValue.doubleValue());

			NPOwner npo = (NPOwner) SharedResource.getNodeParameter("owner", node);
			npo.setBattery(parameter.getBattery());

			NPPath npp = (NPPath) SharedResource.getNodeParameter("path", node);
			npp.setBattery(parameter.getBattery());

			NPRelate npr = (NPRelate) SharedResource.getNodeParameter("relate", node);
			npr.setBattery(parameter.getBattery());

			NPCuckoo npc = (NPCuckoo) SharedResource.getNodeParameter("cuckoo", node);
			npc.setBattery(parameter.getBattery());
			npc.setUpTime(npc.getUpTime() + 1);

			if (newValue.compareTo(threshold) <= 0) {
				removeLink(node);
				removeNode(node);
				joinLambda.add(1.0 / (random.nextDouble() * ((double) joinLambdaMax)));
				joinCycle.add(0.0);
				// if (joinLambdaMax > 2) {
				// joinLambdaMax--;
				// }
			}
		}

		// ノード削除後，ノード間通信で全てのノードに到達できるか確認，再配置し，ノードを参加させる
		// ノード削除後に確認，再配置する事で確実にノード間通信で全てのノードに到達可能状態になっている
		// その状態でノードを追加
		leaveCandidate();
		if (!ConectionManagement.check()) {
			ConectionManagement.ajust();
		}
		ConectionManagement.check();

		joinCandidate();

		for (int i = 0; i < joinCapacity; i++) {
			joinCycle.set(i, joinCycle.get(i) + 1);
		}
		for (int i = 0; i < leaveCapacity; i++) {
			leaveCycle.set(i, leaveCycle.get(i) + 1);
		}

		System.out.println("Now Nodes: " + Network.size());

		count++;
		return false;
	}
}