package research;

import peersim.config.*;
import peersim.core.*;

import java.util.*;

public class RelatedResearch implements Control {
	private static final String PAR_TTL = "ttl";
	private static int ttl;
	private static final String PAR_CAPACITY = "capacity";
	private static double maxCapacity;

	private static ArrayList<Node> addedQueueList = new ArrayList<Node>();
	private static HashMap<Node, Integer> nodeTTL = new HashMap<Node, Integer>();
	private static Queue<Node> queue = new ArrayDeque<Node>();
	private static HashMap<Node, Node> parent = new HashMap<Node, Node>();

	private static Node bestNode;
	private static double bestValue;
	private static Data target;
	private static int searchCost;

	public RelatedResearch(String prefix) {
		ttl = Configuration.getInt(prefix + "." + PAR_TTL);
		maxCapacity = Configuration.getDouble(prefix + "." + PAR_CAPACITY);
	}

	public static int calculateNetworkCost() {
		int cost;
		Node node = bestNode;
		ArrayList<Node> path = new ArrayList<Node>();
		while (Objects.nonNull(node)) {
			path.add(node);
			node = parent.get(node);
		}

		cost = path.size() - 1;
		return cost;
	}

	public static void objectiveFunction(Node node) {
		StorageRelate storage = (StorageRelate) SharedResource.getNodeStorage("relate", node);
		NPRelate parameter = (NPRelate) SharedResource.getNodeParameter("relate", node);
		double battery = parameter.getBattery();
		int capacity = parameter.getCapacity();
		int occupancy = target.getSize();
		int newCapacity = capacity - occupancy;
		double contribution = parameter.getContribution();

		if (!storage.contains(target) && newCapacity >= 0) {
			double b = battery / 100.0;
			double c = contribution;
			double value = 1.0 * b + 1.0 * c;
			// value = Math.log(value);
			if (value > bestValue) {
				bestNode = node;
				bestValue = value;
			}
		}
	}

	private static void nextSearch(Node node) {
		queue.add(node); // キューに引数のノードを入れる
		nodeTTL.put(node, ttl); // ノードとttlを関連付け
		addedQueueList.add(node); // キューに追加したノードを保持
		parent.put(node, null);

		// キューが空でない場合探し続ける
		// ターゲートデータを持っているノードを発見したらtrueを返す
		while (queue.peek() != null) {
			node = queue.poll(); // キューからノードを取り出す
			int nowTTL = nodeTTL.get(node); // 取り出したノードに関連付けられたTTLを取り出す
			searchCost++;

			// 取り出したノードのTTLが0であった場合
			if (nowTTL <= 0) {
				if (queue.peek() == null) {
					return;
				}
				continue;
			}
			objectiveFunction(node);

			int newTTL = nowTTL - 1;
			Link linkable = SharedResource.getLink(node); // 繋がっているノードを取得
			for (int nodeID = 0; nodeID < linkable.degree(); nodeID++) {
				Node neighbor = linkable.getNeighbor(nodeID);

				if (!addedQueueList.contains(neighbor)) {
					if (newTTL > 0) {
						nodeTTL.put(neighbor, newTTL); // 隣接ノードとttlを関連付け
						queue.add(neighbor);
						addedQueueList.add(neighbor); // キューに追加したノードを保持
						parent.put(neighbor, node);
					}
				}
			}
		}

		return;
	}

	public static Node getBestNode(Node node, Data data, int cycle) {
		addedQueueList = new ArrayList<Node>();
		nodeTTL = new HashMap<Node, Integer>();
		queue = new ArrayDeque<Node>();
		parent = new HashMap<Node, Node>();

		target = data;
		bestNode = null;
		bestValue = Double.NEGATIVE_INFINITY;
		searchCost = -1; // 検索はルートノードから始まるため初期値は-1

		nextSearch(node);

		int replicationCost = calculateNetworkCost();

		ArrayList<Integer> costList = SharedResource.getReplicationCost(2);
		costList.set(cycle, costList.get(cycle) + searchCost + replicationCost);
		SharedResource.setReplicationCost(2, costList);
		return bestNode;
	}

	public boolean execute() {
		return false;
	}

}