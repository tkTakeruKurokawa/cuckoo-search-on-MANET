package research;

import peersim.config.*;
import peersim.core.*;

import java.util.*;

public class Flooding implements Control {
	private static int ttl;

	// private Node node;
	private static ArrayList<Node> addedQueueList;
	private static HashMap<Node, Integer> nodeTTL;
	private static Queue<Node> queue;
	private static ArrayList<Node> path;
	private static HashMap<Node, Node> parent;

	private static Storage storage;
	private static Node source;
	private static Node targetNode = null;
	private static Data target;
	private static int id;
	private static boolean hit;
	private static int cost;

	public Flooding(String prefix) {
	}

	public static ArrayList<Node> getPath() {
		return path;
	}

	private static boolean nextSearch(Node node, int ttl) {
		queue.add(node); // キューに引数のノードを入れる
		nodeTTL.put(node, ttl); // ノードとttlを関連付け
		addedQueueList.add(node); // キューに追加したノードを保持
		parent.put(source, null);

		// キューが空でない場合探し続ける
		// ターゲットトデータを持っているノードを発見したらtrueを返す
		while (Objects.nonNull(queue.peek())) {
			node = queue.poll(); // キューからノードを取り出す
			ttl = nodeTTL.get(node); // 取り出したノードに関連付けられたTTLを取り出す
			cost++;

			// データを探すのか==NULL，ノードを探すのか==nonNULL
			if (Objects.isNull(targetNode)) {
				// 取り出したノードがターゲットデータを持っているかチェック
				if (contains(node)) {
					hit = true;
					while (Objects.nonNull(node)) {
						path.add(node);
						node = parent.get(node);
					}
					return true;
				}

				// 取り出したノードがターゲットノードであるかチェック
			} else {
				if (targetNode.getIndex() == node.getIndex()) {
					hit = true;
					while (Objects.nonNull(node)) {
						path.add(node);
						node = parent.get(node);
					}
					return true;
				}
			}
			// 取り出したノードのTTLが0であった場合
			if (ttl <= 0) {
				if (Objects.isNull(queue.peek())) {
					return false;
				}
				continue;
			}

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

			int newTTL = ttl - 1;
			Link linkable = SharedResource.getLink(node); // 繋がっているノードを取得
			for (int nodeID = 0; nodeID < linkable.degree(); nodeID++) {
				Node neighbor = linkable.getNeighbor(nodeID);

				if (!addedQueueList.contains(neighbor)) {
					nodeTTL.put(neighbor, newTTL); // 隣接ノードとttlを関連付け
					queue.add(neighbor);
					addedQueueList.add(neighbor); // キューに追加したノードを保持
					parent.put(neighbor, node);
				}
			}
		}

		return false;
	}

	private static boolean contains(Node node) {
		if (Objects.equals(id, 0)) {
			storage = SharedResource.getNodeStorage("owner", node);
			return storage.contains(target);
		}

		if (Objects.equals(id, 1)) {
			storage = SharedResource.getNodeStorage("path", node);
			return storage.contains(target);
		}

		if (Objects.equals(id, 2)) {
			storage = SharedResource.getNodeStorage("relate", node);
			return storage.contains(target);
		}

		if (Objects.equals(id, 3)) {
			storage = SharedResource.getNodeStorage("cuckoo", node);
			return storage.contains(target);
		}

		return false;
	}

	public static void calculateNetworkCost(int id, int cycle) {
		ArrayList<Integer> searchCostList = SharedResource.getSearchCost(id);
		ArrayList<Integer> replicationCostList = SharedResource.getReplicationCost(id);
		int searchCost = cost;
		int hops = getPath().size() - 1;
		if (id == 1) {
			int total = 0;
			for (int num = 1; num <= hops; num++) {
				total += num;
			}
			searchCostList.set(cycle, searchCostList.get(cycle) + searchCost);
			replicationCostList.set(cycle, replicationCostList.get(cycle) + total);
			SharedResource.setSearchCost(id, searchCostList);
			SharedResource.setReplicationCost(id, replicationCostList);

		} else {
			searchCostList.set(cycle, searchCostList.get(cycle) + searchCost);
			replicationCostList.set(cycle, replicationCostList.get(cycle) + hops);
			SharedResource.setSearchCost(id, searchCostList);
			SharedResource.setReplicationCost(id, replicationCostList);
		}

		for (int i = 0; i < 4; i++) {
			ArrayList<Integer> count = SharedResource.getReplicationCount(i);
			count.set(cycle, count.get(cycle) + 1);
			SharedResource.setReplicationCount(i, count);
		}
	}

	public static boolean search(Node node, Data data, int num, int cycle) {
		addedQueueList = new ArrayList<Node>();
		nodeTTL = new HashMap<Node, Integer>();
		queue = new ArrayDeque<Node>();
		path = new ArrayList<Node>();
		parent = new HashMap<Node, Node>();

		ttl = Network.size();
		source = node;
		target = data;
		id = num;
		hit = false;
		cost = -1; // 検索はルートノードから始まるため初期値は-1

		nextSearch(node, ttl);

		calculateNetworkCost(id, cycle);

		if (!hit)
			return false;

		return true;
	}

	public static Integer hops(Node node, Data data, int num) {
		addedQueueList = new ArrayList<Node>();
		nodeTTL = new HashMap<Node, Integer>();
		queue = new ArrayDeque<Node>();
		path = new ArrayList<Node>();
		parent = new HashMap<Node, Node>();

		ttl = Network.size();
		source = node;
		target = data;
		id = num;
		hit = false;

		nextSearch(node, ttl);
		if (hit) {
			return getPath().size() - 1;
		}

		return null;
	}

	// カッコウ探索で低需要データを割り当てる際のホップ数
	public static Integer hops(Node src, Node dst) {
		addedQueueList = new ArrayList<Node>();
		nodeTTL = new HashMap<Node, Integer>();
		queue = new ArrayDeque<Node>();
		path = new ArrayList<Node>();
		parent = new HashMap<Node, Node>();

		ttl = Network.size();
		source = src;
		targetNode = dst;
		hit = false;

		nextSearch(src, ttl);

		for (Node node : path) {
			// System.out.println(node.getIndex());
		}
		targetNode = null;

		if (hit) {
			return getPath().size() - 1;
		}

		return null;
	}

	public boolean execute() {
		return false;
	}

}