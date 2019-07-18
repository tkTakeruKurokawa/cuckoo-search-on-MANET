package research;

import peersim.config.*;
import peersim.core.*;

import java.util.*;
import java.lang.Math;

public class RelatedResearch implements Control {
	private static final String PAR_TTL = "ttl";
	private static int ttl;
	private static int newTTL;
	private static final String PAR_CAPACITY = "capacity";
	private static double maxCapacity;

	private static ArrayList<Node> addedQueueList = new ArrayList<Node>();
	private static HashMap<Node, Integer> nodeTTL = new HashMap<Node, Integer>();
	private static Queue<Node> queue = new ArrayDeque<Node>();

	private static Node bestNode;
	private static double bestValue;
	private static Data target;

	public RelatedResearch(String prefix) {
		ttl = Configuration.getInt(prefix + "." + PAR_TTL);
		maxCapacity = Configuration.getDouble(prefix + "." + PAR_CAPACITY);
	}

	public static void objectiveFunction(Node node) {
		StorageRelate storage = SharedResource.getSRelate(node);
		NPRelate parameter = SharedResource.getNPRelate(node);
		double battery = parameter.getBattery();
		int capacity = parameter.getCapacity();
		int occupancy = target.getSize();
		int newCapacity = capacity - occupancy;
		double contribution = parameter.getContribution();

		if (!storage.contains(target) && newCapacity >= 0) {
			double b = battery / 100.0;
			double c = contribution;
			double value = 2.0 * b + 1.0 * c;
			value = Math.log(value);
			if (value > bestValue) {
				bestNode = node;
				bestValue = value;
			}
		}
	}

	private static void nextSearch(Node node) {
		queue.add(node); // キューに引数のノードを入れる
		nodeTTL.put(node, newTTL); // ノードとttlを関連付け
		addedQueueList.add(node); // キューに追加したノードを保持

		// キューが空でない場合探し続ける
		// ターゲートデータを持っているノードを発見したらtrueを返す
		while (queue.peek() != null) {
			node = queue.poll(); // キューからノードを取り出す
			int nowTTL = nodeTTL.get(node); // 取り出したノードに関連付けられたTTLを取り出す

			// System.out.println("TTL: " + nowTTL);
			// 取り出したノードのTTLが0であった場合
			if (nowTTL <= 0) {
				if (queue.peek() == null) {
					return;
				}
				continue;
			}
			objectiveFunction(node);

			// if (id == 1) {
			// System.out.println("TTL" + ttl);
			// System.out.printf("NodeIndex: %d\n Neighbors Index:", node.getIndex());
			// Link link = SharedResource.getLink(node);
			// for (int nodeID = 0; nodeID < link.degree(); nodeID++) {
			// Node n = link.getNeighbor(nodeID);
			// System.out.printf(" %d", n.getIndex());
			// }
			// System.out.println();
			// }

			Link linkable = SharedResource.getLink(node); // 繋がっているノードを取得
			for (int nodeID = 0; nodeID < linkable.degree(); nodeID++) {
				Node neighbor = linkable.getNeighbor(nodeID);

				if (!addedQueueList.contains(neighbor)) {
					newTTL--;
					if (newTTL > 0) {
						nodeTTL.put(neighbor, newTTL); // 隣接ノードとttlを関連付け
						queue.add(neighbor);
						addedQueueList.add(neighbor); // キューに追加したノードを保持
					}
				}
			}
		}

		return;
	}

	public static Node getBestNode(Node node, Data data) {
		addedQueueList = new ArrayList<Node>();
		nodeTTL = new HashMap<Node, Integer>();
		queue = new ArrayDeque<Node>();

		target = data;
		newTTL = ttl;
		bestNode = null;
		bestValue = Double.NEGATIVE_INFINITY;

		nextSearch(node);

		return bestNode;
	}

	public boolean execute() {
		return false;
	}

}