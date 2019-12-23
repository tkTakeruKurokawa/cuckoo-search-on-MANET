package research;

import peersim.core.*;

import java.util.*;

public class DataRequest implements Control {
	private static Random random = new Random();
	private static ArrayList<Integer> succFlood = new ArrayList<Integer>();
	private static ArrayList<Integer> failFlood = new ArrayList<Integer>();
	private static ArrayList<Integer> succSet = new ArrayList<Integer>();
	private static ArrayList<Integer> failSet = new ArrayList<Integer>();
	private static ArrayList<Node> uploadedNode = new ArrayList<Node>();
	private static ArrayList<Node> rand;

	private static ArrayList<Data> requestList;
	private static Data data;
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

	private static boolean check(Parameter parameter, Storage storage) {
		int capacity = parameter.getCapacity();
		int occupancy = data.getSize();
		int newCapacity = capacity - occupancy;

		if (!storage.contains(data) && (newCapacity >= 0)) {
			return true;
		}

		return false;
	}

	private static void checkUL(String way, Node node) {
		int count = 0;
		if (Objects.equals(way, "owner")) {
			while (count < Network.size()) {
				Parameter parOwner = SharedResource.getNPOwner(node);
				Storage strOwner = SharedResource.getSOwner(node);
				Parameter parRelate = SharedResource.getNPRelate(node);
				Storage strRelate = SharedResource.getSRelate(node);
				Parameter parCuckoo = SharedResource.getNPCuckoo(node);
				Storage strCuckoo = SharedResource.getSCuckoo(node);

				boolean sucOwner = check(parOwner, strOwner);
				boolean sucRelate = check(parRelate, strRelate);
				boolean sucCuckoo = check(parCuckoo, strCuckoo);

				if (sucOwner == true) {
					if (sucRelate == true) {
						if (sucCuckoo == true) {
							uploadedNode.add(node);
							return;
						}
					}
				}

				node = rand.get(count);
				count++;
			}
		}

		if (Objects.equals(way, "path")) {
			while (count < Network.size()) {
				Parameter parameter = SharedResource.getNPPath(node);
				Storage storage = SharedResource.getSPath(node);

				boolean success = check(parameter, storage);
				if (success) {
					return;
				}

				node = rand.get(count);
				count++;
			}
		}
	}

	private static void ownerReplication(Node node, Parameter parameter, Storage storage, int id) {
		boolean hit = Flooding.search(node, data, id);
		if (hit) {
			succFlood.set(id, succFlood.get(id) + 1);
			if (check(parameter, storage)) {
				storage.setHighDemandData(node, data);
				succSet.set(id, succSet.get(id) + 1);
			} else {
				failSet.set(id, failSet.get(id) + 1);
			}
		} else {
			failFlood.set(id, failFlood.get(id) + 1);
		}
	}

	private static void pathReplication() {
		for (Node node : Flooding.getPath()) {
			Parameter parameter = SharedResource.getNPPath(node);
			Storage storage = SharedResource.getSPath(node);
			// System.out.println("Path: " + node.getID());
			if (check(parameter, storage)) {
				storage.setHighDemandData(node, data);
				succSet.set(1, succSet.get(1) + 1);
			} else {
				failSet.set(1, failSet.get(1) + 1);
			}
		}
	}

	public static void owner(Node node) {
		Parameter parameter = SharedResource.getNPOwner(node);
		Storage storage = SharedResource.getSOwner(node);

		ownerReplication(node, parameter, storage, 0);
	}

	public static void path(Node node) {
		// Parameter parameter = SharedResource.getNPPath(node);
		// Storage storage = SharedResource.getSPath(node);

		// if (check(parameter, storage)) {
		boolean hit = Flooding.search(node, data, 1);
		if (hit) {
			succFlood.set(1, succFlood.get(1) + 1);
			pathReplication();
		} else {
			failFlood.set(1, failFlood.get(1) + 1);
		}
		// }
	}

	public static void relate(Node node) {
		Parameter parameter = SharedResource.getNPRelate(node);
		Storage storage = SharedResource.getSRelate(node);

		ownerReplication(node, parameter, storage, 2);
	}

	public static void cuckoo(Node node) {
		Parameter parameter = SharedResource.getNPCuckoo(node);
		Storage storage = SharedResource.getSCuckoo(node);

		ownerReplication(node, parameter, storage, 3);
	}

	public boolean execute() {
		// データのアップロード
		if ((cycle % 5 == 0) && uploadID < Data.getMaxVariety()) {
			rand = new ArrayList<Node>();
			for (int i = 0; i < Network.size(); i++) {
				rand.add(Network.get(i));
			}
			Collections.shuffle(rand);

			int num = Data.getUploads(uploadID);
			for (int i = 0; i < num; i++) {
				Node node = Network.get(random.nextInt(Network.size()));

				checkUL("owner", node);
				Storage ownerS = SharedResource.getSOwner(node);
				ownerS.setHighDemandData(node, data);
				Storage relateS = SharedResource.getSRelate(node);
				relateS.setHighDemandData(node, data);
				Storage cuckooS = SharedResource.getSCuckoo(node);
				cuckooS.setHighDemandData(node, data);

				checkUL("path", node);
				Storage pathS = SharedResource.getSPath(node);
				pathS.setHighDemandData(node, data);
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

		// データ要求
		for (int nodeID = 0; nodeID < Network.size(); nodeID++) {
			// データ要求
			Node node = Network.get(nodeID);
			NodeRequestCycle request = SharedResource.getNodeRequestCycle(node);
			// requestList = request.dataRequests("high"); // データ要求するデータが入る
			requestList = request.getRequestedDatas(); // データ要求されるデータが入る

			for (int dataID = 0; dataID < requestList.size(); dataID++) {
				data = requestList.get(dataID);

				owner(node);
				relate(node);
				cuckoo(node);
				path(node);
			}
		}

		// System.out.println("Owner Num: Success Flooding " + succFlood.get(0));
		// System.out.println("Owner Num: Fail Flooding " + failFlood.get(0));
		// System.out.println("Owner Num: Fail setHighDemandData " + failSet.get(0));
		// System.out.println("Owner Num: Success setHighDemandData " + succSet.get(0));
		// System.out.println();

		// System.out.println("Path Num: Success Flooding " + succFlood.get(1));
		// System.out.println("Path Num: Fail Flooding " + failFlood.get(1));
		// System.out.println("Path Num: Fail setHighDemandData " + failSet.get(1));
		// System.out.println("Path Num: Success setHighDemandData " + succSet.get(1));
		// System.out.println();

		// System.out.println("Relate Num: Success Flooding " + succFlood.get(2));
		// System.out.println("Relate Num: Fail Flooding " + failFlood.get(2));
		// System.out.println("Relate Num: Fail setHighDemandData " + failSet.get(2));
		// System.out.println("Relate Num: Success setHighDemandData " +
		// succSet.get(2));
		// System.out.println();

		// System.out.println("Cuckoo Num: Success Flooding " + succFlood.get(3));
		// System.out.println("Cuckoo Num: Fail Flooding " + failFlood.get(3));
		// System.out.println("Cuckoo Num: Fail setHighDemandData " + failSet.get(3));
		// System.out.println("Cuckoo Num: Success setHighDemandData " +
		// succSet.get(3));
		// System.out.println();

		cycle++;

		return false;
	}

}