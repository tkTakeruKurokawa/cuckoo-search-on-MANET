package research;

import peersim.core.*;

import java.util.*;

public class DataRequest implements Control {
	private static Random random = new Random();
	private static ArrayList<Integer> succFlood = new ArrayList<Integer>();
	private static ArrayList<Integer> failFlood = new ArrayList<Integer>();
	private static ArrayList<Integer> succSet = new ArrayList<Integer>();
	private static ArrayList<Integer> failSet = new ArrayList<Integer>();
	private static ArrayList<Integer> accesses = new ArrayList<Integer>();
	private static ArrayList<Node> uploadedNode = new ArrayList<Node>();
	private static ArrayList<Node> rand;

	private static ArrayList<Data> requestList;
	private static int cycle = 0;

	private static int uploadID = 0;

	public DataRequest(String prefix) {
		for (int i = 0; i < 4; i++) {
			succFlood.add(i, 0);
			failFlood.add(i, 0);
			succSet.add(i, 0);
			failSet.add(i, 0);
		}
	}

	private static boolean check(Data data, Parameter parameter, Storage storage) {
		int capacity = parameter.getCapacity();
		int occupancy = data.getSize();
		int newCapacity = capacity - occupancy;

		if (!storage.contains(data) && (newCapacity >= 0)) {
			return true;
		}

		return false;
	}

	private static Node checkUL(String way, Node node, Data data) {
		int count = 0;
		if (Objects.equals(way, "owner")) {
			while (count < Network.size()) {
				Parameter parOwner = SharedResource.getNodeParameter("owner", node);
				Storage strOwner = SharedResource.getNodeStorage("owner", node);
				Parameter parRelate = SharedResource.getNodeParameter("relate", node);
				Storage strRelate = SharedResource.getNodeStorage("relate", node);
				Parameter parCuckoo = SharedResource.getNodeParameter("cuckoo", node);
				Storage strCuckoo = SharedResource.getNodeStorage("cuckoo", node);

				boolean sucOwner = check(data, parOwner, strOwner);
				boolean sucRelate = check(data, parRelate, strRelate);
				boolean sucCuckoo = check(data, parCuckoo, strCuckoo);

				if (sucOwner == true) {
					if (sucRelate == true) {
						if (sucCuckoo == true) {
							uploadedNode.add(node);
							return node;
						}
					}
				}

				node = rand.get(count);
				count++;
			}
		}

		if (Objects.equals(way, "path")) {
			while (count < Network.size()) {
				Parameter parameter = SharedResource.getNodeParameter("path", node);
				Storage storage = SharedResource.getNodeStorage("path", node);

				boolean success = check(data, parameter, storage);
				if (success) {
					return node;
				}

				node = rand.get(count);
				count++;
			}
		}

		return null;
	}

	private static void ownerReplication(Node node, Data data, Parameter parameter, Storage storage, int id) {
		boolean hit = Flooding.search(node, data, id, cycle);
		if (hit) {
			succFlood.set(id, succFlood.get(id) + 1);
			if (check(data, parameter, storage)) {
				storage.setData(node, data);
				succSet.set(id, succSet.get(id) + 1);
			} else {
				failSet.set(id, failSet.get(id) + 1);
			}
		} else {
			failFlood.set(id, failFlood.get(id) + 1);
		}
	}

	private static void pathReplication(Data data) {
		for (Node node : Flooding.getPath()) {
			Parameter parameter = SharedResource.getNodeParameter("path", node);
			Storage storage = SharedResource.getNodeStorage("path", node);
			// System.out.println("Path: " + node.getID());
			if (check(data, parameter, storage)) {
				storage.setData(node, data);
				succSet.set(1, succSet.get(1) + 1);
			} else {
				failSet.set(1, failSet.get(1) + 1);
			}
		}
	}

	public static void owner(Node node, Data data) {
		Parameter parameter = SharedResource.getNodeParameter("owner", node);
		Storage storage = SharedResource.getNodeStorage("owner", node);

		ownerReplication(node, data, parameter, storage, 0);
	}

	public static void path(Node node, Data data) {
		// Parameter parameter = SharedResource.getNodeParameter("path", node);
		// Storage storage = SharedResource.getNodeStorage("path", node);

		// if (check(parameter, storage)) {
		boolean hit = Flooding.search(node, data, 1, cycle);
		if (hit) {
			succFlood.set(1, succFlood.get(1) + 1);
			pathReplication(data);
		} else {
			failFlood.set(1, failFlood.get(1) + 1);
		}
		// }
	}

	public static void relate(Node node, Data data) {
		Parameter parameter = SharedResource.getNodeParameter("relate", node);
		Storage storage = SharedResource.getNodeStorage("relate", node);

		ownerReplication(node, data, parameter, storage, 2);
	}

	public static void cuckoo(Node node, Data data) {
		Parameter parameter = SharedResource.getNodeParameter("cuckoo", node);
		Storage storage = SharedResource.getNodeStorage("cuckoo", node);

		ownerReplication(node, data, parameter, storage, 3);
	}

	public boolean execute() {
		// データのアップロード
		if ((cycle % 5 == 0) && uploadID < Data.getMaxVariety()) {
			uploadedNode = new ArrayList<Node>();
			rand = new ArrayList<Node>();
			for (int i = 0; i < Network.size(); i++) {
				rand.add(Network.get(i));
			}
			Collections.shuffle(rand);

			Data data = Data.getData(uploadID);
			int num = Data.getUploads(uploadID);
			for (int i = 0; i < num; i++) {
				Node node = Network.get(random.nextInt(Network.size()));

				node = checkUL("owner", node, data);
				Storage ownerS = SharedResource.getNodeStorage("owner", node);
				ownerS.setData(node, data);
				Storage relateS = SharedResource.getNodeStorage("relate", node);
				relateS.setData(node, data);
				Storage cuckooS = SharedResource.getNodeStorage("cuckoo", node);
				cuckooS.setData(node, data);

				node = checkUL("path", node, data);
				Storage pathS = SharedResource.getNodeStorage("path", node);
				pathS.setData(node, data);
			}

			// アップロードしたノードをデータ要求するノードから外す
			Iterator<Node> it = rand.iterator();
			while (it.hasNext()) {
				Node nowNode = it.next();
				for (Node uploaded : uploadedNode) {
					if (nowNode.getIndex() == uploaded.getIndex()) {
						it.remove();
					}
				}
			}

			// データ要求するノードを選び，何サイクル後にデータ要求するか計算
			num = Data.getRequestedNum(uploadID);
			for (int i = 0; i < num; i++) {
				Node node = rand.get(i);
				NodeRequestCycle request = SharedResource.getNodeRequestCycle(node);
				request.setCycle(uploadID);
			}

			uploadID++;
		}

		accesses = new ArrayList<Integer>(); // データ要求数をカウントするリスト
		for (int dataID = 0; dataID < Data.getMaxVariety(); dataID++) {
			accesses.add(dataID, 0);
		}
		for (int nodeID = 0; nodeID < Network.size(); nodeID++) {
			// データ要求
			Node node = Network.get(nodeID);
			NodeRequestCycle request = SharedResource.getNodeRequestCycle(node);
			requestList = request.getRequestedData(); // データ要求されるデータが入る

			for (int dataID = 0; dataID < requestList.size(); dataID++) {
				Data data = requestList.get(dataID);
				accesses.set(data.getID(), accesses.get(data.getID()) + 1);

				owner(node, data);
				relate(node, data);
				cuckoo(node, data);
				path(node, data);
			}
		}
		SharedResource.setAccesses(accesses);

		// System.out.println("Owner Num: Success Flooding " + succFlood.get(0));
		// System.out.println("Owner Num: Fail Flooding " + failFlood.get(0));
		// System.out.println("Owner Num: Fail setData " + failSet.get(0));
		// System.out.println("Owner Num: Success setData " + succSet.get(0));
		// System.out.println();

		// System.out.println("Path Num: Success Flooding " + succFlood.get(1));
		// System.out.println("Path Num: Fail Flooding " + failFlood.get(1));
		// System.out.println("Path Num: Fail setData " + failSet.get(1));
		// System.out.println("Path Num: Success setData " + succSet.get(1));
		// System.out.println();

		// System.out.println("Relate Num: Success Flooding " + succFlood.get(2));
		// System.out.println("Relate Num: Fail Flooding " + failFlood.get(2));
		// System.out.println("Relate Num: Fail setData " + failSet.get(2));
		// System.out.println("Relate Num: Success setData " +
		// succSet.get(2));
		// System.out.println();

		// System.out.println("Cuckoo Num: Success Flooding " + succFlood.get(3));
		// System.out.println("Cuckoo Num: Fail Flooding " + failFlood.get(3));
		// System.out.println("Cuckoo Num: Fail setData " + failSet.get(3));
		// System.out.println("Cuckoo Num: Success setData " +
		// succSet.get(3));
		// System.out.println();

		cycle++;

		return false;
	}

}