package research;

import peersim.core.*;

import java.util.*;

public class IncreaseLowDemandData implements Control {
	private static Random random = new Random();
	private static ArrayList<Integer> succFlood = new ArrayList<Integer>();
	private static ArrayList<Integer> failFlood = new ArrayList<Integer>();
	private static ArrayList<Integer> succSet = new ArrayList<Integer>();
	private static ArrayList<Integer> failSet = new ArrayList<Integer>();
	private static ArrayList<Double> relateSum = new ArrayList<Double>();
	private static ArrayList<Double> cuckooSum = new ArrayList<Double>();
	private static ArrayList<Integer> relateCount = new ArrayList<Integer>();
	private static ArrayList<Integer> cuckooCount = new ArrayList<Integer>();
	private static ArrayList<Integer> passedCycle = new ArrayList<Integer>();
	private static ArrayList<Node> rand;

	private static ArrayList<Data> requestList;
	private static Data data;
	private static int cycle = 0;

	private static int uploadID = 0;

	public IncreaseLowDemandData(String prefix) {
		for (int i = 0; i < 4; i++) {
			succFlood.add(i, 0);
			failFlood.add(i, 0);
			succSet.add(i, 0);
			failSet.add(i, 0);
		}
		for (int i = 0; i < Data.getMaxVariety(); i++) {
			relateSum.add(i, 0.0);
			cuckooSum.add(i, 0.0);
			relateCount.add(i, 0);
			cuckooCount.add(i, 0);
			passedCycle.add(i, 0);
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

	private static void checkUL(Node node, String way) {
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

	public static int replicaNum(String type, Data data) {
		int num;
		int nowReplications;
		int dataID = data.getID();

		if (Objects.equals(type, "relate")) {
			double beta = 1.0;
			nowReplications = SharedResource.getRelateLowCounter().get(dataID);
			relateCount.set(dataID, relateCount.get(dataID) + 1);
			relateSum.set(dataID, relateSum.get(dataID) + nowReplications);

			num = ((int) Math.round(beta * nowReplications
					* (relateSum.get(dataID) / relateCount.get(dataID).doubleValue()) / nowReplications));

		} else {
			double beta = 1.0;
			nowReplications = SharedResource.getCuckooLowCounter().get(dataID);
			cuckooCount.set(dataID, cuckooCount.get(dataID) + 1);
			cuckooSum.set(dataID, cuckooSum.get(dataID) + nowReplications);
			num = ((int) Math.round(beta * nowReplications
					* (cuckooSum.get(dataID) / cuckooCount.get(dataID).doubleValue()) / nowReplications));
		}

		System.out.println(data.getID() + ": " + type + " Num: " + num + " Now: " + nowReplications);
		return num - nowReplications;
	}

	public static void relatedResearch(int max) {
		int addNum = 0;
		Node node;
		while (addNum < max) {
			node = Network.get(random.nextInt(Network.size()));
			node = RelatedResearch.getBestNode(node, data);

			if (node != null) {
				succFlood.set(2, succFlood.get(2) + 1);
				Storage storage = SharedResource.getSRelate(node);
				boolean success = storage.setLowDemandData(node, data);
				if (success) {
					succSet.set(2, succSet.get(2) + 1);
				} else {
					failSet.set(2, failSet.get(2) + 1);
				}
			} else {
				failFlood.set(2, failFlood.get(2) + 1);
			}

			addNum++;
		}
	}

	public static void cuckooSearch(int max) {
		int addNum = 0;
		Node node;
		// System.out.println("Add Num: " + diff);
		while (addNum < max) {
			node = CuckooSearch.search(data);
			// System.out.println("CS Node: " + node);

			if (node != null) {
				succFlood.set(3, succFlood.get(3) + 1);
				Storage storage = SharedResource.getSCuckoo(node);
				boolean success = storage.setLowDemandData(node, data);
				if (success) {
					succSet.set(3, succSet.get(3) + 1);
				} else {
					failSet.set(3, failSet.get(3) + 1);
				}
			} else {
				failFlood.set(3, failFlood.get(3) + 1);
			}

			addNum++;
		}
	}

	public static void owner(Node node) {
		Parameter parameter = SharedResource.getNPOwner(node);
		Storage storage = SharedResource.getSOwner(node);

		boolean hit = Flooding.search(node, data, 0);
		if (hit) {
			succFlood.set(0, succFlood.get(0) + 1);
			if (check(parameter, storage)) {
				storage.setLowDemandData(node, data);
				succSet.set(0, succSet.get(0) + 1);
			} else {
				failSet.set(0, failSet.get(0) + 1);
			}
		} else {
			failFlood.set(0, failFlood.get(0) + 1);
		}
	}

	public static void path(Node node) {
		// Parameter parameter = SharedResource.getNPPath(node);
		// Storage storage = SharedResource.getSPath(node);

		// if (check(parameter, storage)) {
		boolean hit = Flooding.search(node, data, 1);
		if (hit) {
			succFlood.set(1, succFlood.get(1) + 1);
			for (Node n : Flooding.getPath()) {
				Parameter parameter = SharedResource.getNPPath(n);
				Storage storage = SharedResource.getSPath(n);
				// System.out.println("Path: " + n.getID());
				if (check(parameter, storage)) {
					storage.setLowDemandData(n, data);
					succSet.set(1, succSet.get(1) + 1);
				} else {
					failSet.set(1, failSet.get(1) + 1);
				}
			}
		} else {
			failFlood.set(1, failFlood.get(1) + 1);
		}
		// }
	}

	public static void relate(int num) {
		relatedResearch(num);
	}

	public static void cuckoo(int num) {
		cuckooSearch(num);
	}

	public boolean execute() {
		Node node;

		// データのアップロード
		if ((cycle % 5 == 0) && uploadID < Data.getMaxVariety()) {
			rand = new ArrayList<Node>();
			for (int i = 0; i < Network.size(); i++) {
				rand.add(Network.get(i));
			}
			Collections.shuffle(rand);

			data = Data.getData(uploadID);
			if (Objects.equals(data.getType(), "low")) {
				int num = random.nextInt(6) + 5;
				for (int i = 0; i < num; i++) {
					node = Network.get(random.nextInt(Network.size()));

					checkUL(node, "owner");
					Storage ownerS = SharedResource.getSOwner(node);
					ownerS.setLowDemandData(node, data);
					Storage relateS = SharedResource.getSRelate(node);
					relateS.setLowDemandData(node, data);
					Storage cuckooS = SharedResource.getSCuckoo(node);
					cuckooS.setLowDemandData(node, data);

					checkUL(node, "path");
					Storage pathS = SharedResource.getSPath(node);
					pathS.setLowDemandData(node, data);
				}
			}
			uploadID++;
		}

		// データ要求がある場合
		for (int nodeID = 0; nodeID < Network.size(); nodeID++) {
			node = Network.get(nodeID);
			// データ要求
			RequestProbability request = SharedResource.getRequestProbability(node);
			requestList = request.dataRequests("low"); // データ要求するデータが入る

			for (int dataID = 0; dataID < requestList.size(); dataID++) {
				data = requestList.get(dataID);

				owner(node);
				path(node);
				relate(1);
				cuckoo(1);

				passedCycle.set(dataID, 0);
			}
		}

		// データ要求がない場合
		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			data = Data.getData(dataID);

			ArrayList<Boolean> dataRequest = SharedResource.getDataRequest();

			if (Objects.equals(data.getType(), "low") && dataRequest.get(dataID) == true) {
				replicaNum("relate", data);
				replicaNum("cuckoo", data);
			}

			if (Objects.equals(data.getType(), "low") && dataRequest.get(dataID) == false) {
				int num = replicaNum("relate", Data.getData(dataID));
				relate(num);

				num = replicaNum("cuckoo", Data.getData(dataID));
				if (passedCycle.get(dataID) < 200) {
					cuckoo(num);
				}
				System.out.println();

				passedCycle.set(dataID, passedCycle.get(dataID) + 1);
			}

			dataRequest.set(dataID, false);
			SharedResource.setDataRequest(dataRequest);
		}

		// System.out.println("Owner Num: Success Flooding:\t" + succFlood.get(0));
		// System.out.println("Owner Num: Fail Flooding:\t" + failFlood.get(0));
		// System.out.println("Owner Num: Fail setLowDemandData:\t" + failSet.get(0));
		// System.out.println("Owner Num: Success setLowDemandData:\t" +
		// succSet.get(0));
		// System.out.println();

		// System.out.println("Path Num: Success Flooding:\t" + succFlood.get(1));
		// System.out.println("Path Num: Fail Flooding:\t" + failFlood.get(1));
		// System.out.println("Path Num: Fail setLowDemandData:\t" + failSet.get(1));
		// System.out.println("Path Num: Success setLowDemandData:\t" + succSet.get(1));
		// System.out.println();

		// System.out.println("Relate Num: Success Search:\t" + succFlood.get(2));
		// System.out.println("Relate Num: Fail Search:\t" + failFlood.get(2));
		// System.out.println("Relate Num: Fail setLowDemandData:\t" + failSet.get(2));
		// System.out.println("Relate Num: Success setLowDemandData:\t" +
		// succSet.get(2));
		// System.out.println();

		// System.out.println("Cuckoo Num: Success Search:\t" + succFlood.get(3));
		// System.out.println("Cuckoo Num: Fail Search:\t" + failFlood.get(3));
		// System.out.println("Cuckoo Num: Fail setLowDemandData:\t" + failSet.get(3));
		// System.out.println("Cuckoo Num: Success setLowDemandData:\t" +
		// succSet.get(3));
		// System.out.println();

		cycle++;

		return false;
	}

}