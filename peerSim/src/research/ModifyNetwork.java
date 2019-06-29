package research;

import peersim.config.*;
import peersim.core.*;
import java.math.BigDecimal;
import java.lang.Math;
import java.util.*;

public class ModifyNetwork implements Control {
	private static final String PAR_CAPACITY = "capacity";
	private static int capacity;

	private static Queue<Node> queue = new ArrayDeque<Node>();
	private static ArrayList<Integer> rnd = new ArrayList<Integer>();
	private static ArrayList<Integer> hit = new ArrayList<Integer>();
	private static Random random = new Random();
	private static BigDecimal threshold = BigDecimal.valueOf(0.0d);

	public ModifyNetwork(String prefix) {
		capacity = Configuration.getInt(prefix + "." + PAR_CAPACITY);

		for (int i = 0; i < 100; i++) {
			rnd.add(i);
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

		ArrayList<Double> relateOccu = SharedResource.getRelateOccu();
		relateOccu.add(newNode.getIndex(), 0.0);
		SharedResource.setRelateOccu(relateOccu);

		ArrayList<Double> cuckooOccu = SharedResource.getCuckooOccu();
		cuckooOccu.add(newNode.getIndex(), 0.0);
		SharedResource.setCuckooOccu(cuckooOccu);

		NodeCoordinate newCrd = SharedResource.getCoordinate(newNode);
		newCrd.setCoordinate();

		NodeParameter parameter = SharedResource.getParameter(newNode);
		parameter.setParameter();

		NPOwner npo = SharedResource.getNPOwner(newNode);
		npo.setBattery(parameter.getBattery());
		npo.setCapacity(parameter.getCapacity());

		NPPath npp = SharedResource.getNPPath(newNode);
		npp.setBattery(parameter.getBattery());
		npp.setCapacity(parameter.getCapacity());

		NPRelate npr = SharedResource.getNPRelate(newNode);
		npr.setBattery(parameter.getBattery());
		npr.setCapacity(parameter.getCapacity());

		NPCuckoo npc = SharedResource.getNPCuckoo(newNode);
		npc.setBattery(parameter.getBattery());
		npc.setCapacity(parameter.getCapacity());

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

				if (RandomGeometricGraph.isConnect(newCrd, dstCrd)) {
					Link dstLink = SharedResource.getLink(dstNode);
					addLink(newLink, dstNode);
					addLink(dstLink, newNode);
				}

				dstID++;
			}

			if (!Objects.equals(newLink.getNeighbor(0), null))
				break;

			newCrd.setCoordinate();
		}

		return true;
	}

	public static boolean removeLink(Node node) {
		// System.out.println("*************** REMOVE NODE ***************");

		Link nodesLink = SharedResource.getLink(node);

		// 削除するノードのリンクを取得し、隣接ノードをキューに入れる
		for (int i = 0; i < nodesLink.degree(); i++)
			queue.add(nodesLink.getNeighbor(i));

		// 隣接ノードに削除するノードを持つ,ノードから、該当ノードを削除する
		while (true) {
			Node n = queue.poll();
			if (n == null)
				break;
			Link neighborsLink = SharedResource.getLink(n);

			for (int i = 0; i < neighborsLink.degree(); i++) {
				if (Objects.equals(neighborsLink.getNeighbor(i), node)) {
					// System.out.printf("Node %d's len ", n.getIndex());
					neighborsLink.removeNeighbor(i);
				}
			}
		}
		return true;

	}

	public static boolean removeNode(Node node) {
		// Storage storage = SharedResource.getStorage(node);
		// storage.clear();

		int nodeID = node.getIndex();
		ArrayList<Double> relateOccu = SharedResource.getRelateOccu();
		Double value = relateOccu.get(Network.size() - 1);
		relateOccu.set(nodeID, value);
		relateOccu.remove(Network.size() - 1);
		SharedResource.setRelateOccu(relateOccu);

		ArrayList<Double> cuckooOccu = SharedResource.getCuckooOccu();
		value = cuckooOccu.get(Network.size() - 1);
		cuckooOccu.set(nodeID, value);
		cuckooOccu.remove(Network.size() - 1);
		SharedResource.setCuckooOccu(cuckooOccu);

		StorageOwner sOwner = SharedResource.getSOwner(node);
		sOwner.clear();

		StoragePath sPath = SharedResource.getSPath(node);
		sPath.clear();

		StorageRelate sRelate = SharedResource.getSRelate(node);
		sRelate.clear();

		StorageCuckoo sCuckoo = SharedResource.getSCuckoo(node);
		sCuckoo.clear();

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

	private static boolean poisson() {
		double lambda = 1.0 / 50.0;

		double p = Math.exp(-1 * lambda) * (Math.pow(lambda, 1.0)) / 1.0;
		return probability(p);
	}

	public static int joinCandidate() {
		int num = 0;
		for (int i = 0; i < capacity; i++) {
			if (poisson())
				num++;
		}

		return num;
	}

	public static int leaveCandidate() {
		int num = 0;
		for (int i = 0; i < capacity; i++) {
			if (poisson())
				num++;
		}

		return num;
	}

	public static BigDecimal reduceBattery(double battery) {
		BigDecimal a = BigDecimal.valueOf(battery);
		// BigDecimal b = BigDecimal.valueOf(0.15d);
		BigDecimal b = BigDecimal.valueOf(random.nextDouble() * 0.2);
		BigDecimal result;
		// if(random.nextBoolean())
		result = a.subtract(b);
		// else
		// result = a;
		return result;
	}

	public boolean execute() {

		int addNum = joinCandidate();
		for (int i = 0; i < addNum; i++)
			addNode();

		for (int nodeID = 0; nodeID < Network.size(); nodeID++) {
			Node node = Network.get(nodeID);
			NodeParameter parameter = SharedResource.getParameter(node);
			BigDecimal newValue = reduceBattery(parameter.getBattery());
			parameter.setBattery(newValue.doubleValue());

			NPOwner npo = SharedResource.getNPOwner(node);
			npo.setBattery(parameter.getBattery());

			NPPath npp = SharedResource.getNPPath(node);
			npp.setBattery(parameter.getBattery());

			NPRelate npr = SharedResource.getNPRelate(node);
			npr.setBattery(parameter.getBattery());

			NPCuckoo npc = SharedResource.getNPCuckoo(node);
			npc.setBattery(parameter.getBattery());

			if (newValue.compareTo(threshold) <= 0) {
				removeLink(node);
				removeNode(node);
			}
		}

		int removeNum = leaveCandidate();
		for (int i = 0; i < removeNum; i++) {
			int nodeID = random.nextInt(Network.size());
			removeLink(Network.get(nodeID));
			removeNode(Network.get(nodeID));
		}

		return false;
	}
}