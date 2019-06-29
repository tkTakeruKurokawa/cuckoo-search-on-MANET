package research;

import peersim.config.*;
import peersim.core.*;

import java.io.PrintWriter;
import java.util.*;

public class Flooding implements Control {
	private static final String PAR_TTL = "ttl";
	private static int ttl;

	// private Node node;
	private static ArrayList<Node> addedQueueList = new ArrayList<Node>();
	private static HashMap<Node, Integer> nodeTTL = new HashMap<Node, Integer>();
	private static Queue<Node> queue = new ArrayDeque<Node>();
	private static ArrayList<Node> path = new ArrayList<Node>();
	private static HashMap<Node, Node> parent = new HashMap<Node, Node>();

	private static Storage storage;
	private static Node source;
	private static Data target;
	private static int id;
	private static boolean hit;
	private static int hop;

	public Flooding(String prefix) {
		ttl = Configuration.getInt(prefix + "." + PAR_TTL);
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
		// ターゲートデータを持っているノードを発見したらtrueを返す
		while (queue.peek() != null) {
			node = queue.poll(); // キューからノードを取り出す
			ttl = nodeTTL.get(node); // 取り出したノードに関連付けられたTTLを取り出す

			// 取り出したノードがターゲットデータを持っているかチェック
			if (contains(node)) {
				hit = true;
				// System.out.println("Node " + node.getID() + "Having");
				while (node != null) {
					path.add(node);
					node = parent.get(node);
				}
				return true;
			}
			// 取り出したノードのTTLが0であった場合
			if (ttl <= 0) {
				if (queue.peek() == null) {
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
			storage = SharedResource.getSOwner(node);
			return storage.contains(target);
		}

		if (Objects.equals(id, 1)) {
			storage = SharedResource.getSPath(node);
			return storage.contains(target);
		}

		if (Objects.equals(id, 2)) {
			storage = SharedResource.getSRelate(node);
			return storage.contains(target);
		}

		if (Objects.equals(id, 3)) {
			storage = SharedResource.getSCuckoo(node);
			return storage.contains(target);
		}

		return false;
	}

	public static boolean search(Node node, Data data, int num) {
		addedQueueList = new ArrayList<Node>();
		nodeTTL = new HashMap<Node, Integer>();
		queue = new ArrayDeque<Node>();
		path = new ArrayList<Node>();
		parent = new HashMap<Node, Node>();

		source = node;
		target = data;
		id = num;
		hit = false;

		nextSearch(node, ttl);
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

	public boolean execute() {
		return false;
	}

}