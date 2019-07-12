package research;

import peersim.config.*;
import peersim.core.*;

import java.io.PrintWriter;
import java.util.*;
import java.io.*;

public class IncreaseReplications implements Control {
	private static Random random = new Random();
	private static ArrayList<Integer> succFlood = new ArrayList<Integer>();
	private static ArrayList<Integer> failFlood = new ArrayList<Integer>();
	private static ArrayList<Integer> succSet = new ArrayList<Integer>();
	private static ArrayList<Integer> failSet = new ArrayList<Integer>();

	private static ArrayList<Boolean> upLoaded;
	private static ArrayList<Data> requestList;
	private static Node node;
	private static Data data;
	private static int cycle = 0;

	private static PrintWriter links;
	private static int allLinks;
	private static double totalLinks = 0.0;

	public IncreaseReplications(String prefix) {
		for (int i = 0; i < 4; i++) {
			succFlood.add(i, 0);
			failFlood.add(i, 0);
			succSet.add(i, 0);
			failSet.add(i, 0);
		}
		try {
			String way = new File(".").getAbsoluteFile().getParent() + "/result/averageLinks.tsv";
			File file = new File(way);
			links = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
		} catch (IOException e) {
			System.out.println(e);
		}
		links.println("Cycle\tAvarage Links");
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

	private static void firstUL(String way) {
		if (Objects.equals(way, "owner")) {
			while (true) {
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

				node = Network.get(random.nextInt(Network.size()));
			}

		}

		if (Objects.equals(way, "path")) {
			while (true) {
				Parameter parameter = SharedResource.getNPPath(node);
				Storage storage = SharedResource.getSPath(node);

				boolean success = check(parameter, storage);
				if (success) {
					break;
				}

				node = Network.get(random.nextInt(Network.size()));
			}
		}
	}

	private static void ownerReplication(Parameter parameter, Storage storage, int id) {
		boolean hit = Flooding.search(node, data, id);
		if (hit) {
			succFlood.set(id, succFlood.get(id) + 1);
			if (check(parameter, storage)) {
				storage.setData(node, data);
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
				storage.setData(node, data);
				succSet.set(1, succSet.get(1) + 1);
			} else {
				failSet.set(1, failSet.get(1) + 1);
			}
		}
	}

	public static void owner() {
		Parameter parameter = SharedResource.getNPOwner(node);
		Storage storage = SharedResource.getSOwner(node);

		ownerReplication(parameter, storage, 0);
	}

	public static void path() {
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

	public static void relate() {
		Parameter parameter = SharedResource.getNPRelate(node);
		Storage storage = SharedResource.getSRelate(node);

		ownerReplication(parameter, storage, 2);
	}

	public static void cuckoo() {
		Parameter parameter = SharedResource.getNPCuckoo(node);
		Storage storage = SharedResource.getSCuckoo(node);

		ownerReplication(parameter, storage, 3);
	}

	public boolean execute() {
		upLoaded = SharedResource.getUpLoaded();
		ArrayList<Data> cyclesRequestList = SharedResource.getCyclesRequestList();

		allLinks = 0;
		for (int nodeID = 0; nodeID < Network.size(); nodeID++) {
			SharedResource.nextRand();

			node = Network.get(nodeID);
			Link linkable = SharedResource.getLink(node);
			allLinks += linkable.degree();
			Node pathNode = node;

			RequestProbability request = SharedResource.getRequestProbability(node);
			requestList = request.dataRequests();

			for (int dataID = 0; dataID < requestList.size(); dataID++) {
				data = requestList.get(dataID);

				if (!upLoaded.get(data.getID())) {
					firstUL("owner");
					Storage ownerS = SharedResource.getSOwner(node);
					ownerS.setData(node, data);
					Storage relateS = SharedResource.getSRelate(node);
					relateS.setData(node, data);
					Storage cuckooS = SharedResource.getSCuckoo(node);
					cuckooS.setData(node, data);

					firstUL("path");
					Storage pathS = SharedResource.getSPath(pathNode);
					pathS.setData(node, data);

				} else {
					owner();
					relate();
					cuckoo();
					path();
				}

				if (!cyclesRequestList.contains(data)) {
					cyclesRequestList.set(data.getID(), data);
				}
			}
		}

		for (int i = 0; i < Data.getNowVariety(); i++) {
			if (cyclesRequestList.get(i) != null) {
				upLoaded.set(i, true);
			}
		}

		System.out.println("Owner Num: Success Flooding " + succFlood.get(0));
		System.out.println("Owner Num: Fail Flooding " + failFlood.get(0));
		System.out.println("Owner Num: Fail setData " + failSet.get(0));
		System.out.println("Owner Num: Success setData " + succSet.get(0));
		System.out.println();

		System.out.println("Path Num: Success Flooding " + succFlood.get(1));
		System.out.println("Path Num: Fail Flooding " + failFlood.get(1));
		System.out.println("Path Num: Fail setData " + failSet.get(1));
		System.out.println("Path Num: Success setData " + succSet.get(1));
		System.out.println();

		System.out.println("Relate Num: Success Flooding " + succFlood.get(2));
		System.out.println("Relate Num: Fail Flooding " + failFlood.get(2));
		System.out.println("Relate Num: Fail setData " + failSet.get(2));
		System.out.println("Relate Num: Success setData " + succSet.get(2));
		System.out.println();

		System.out.println("Cuckoo Num: Success Flooding " + succFlood.get(3));
		System.out.println("Cuckoo Num: Fail Flooding " + failFlood.get(3));
		System.out.println("Cuckoo Num: Fail setData " + failSet.get(3));
		System.out.println("Cuckoo Num: Success setData " + succSet.get(3));
		System.out.println();

		SharedResource.setUpLoaded(upLoaded);
		SharedResource.setCyclesRequestList(cyclesRequestList);

		totalLinks += ((double) allLinks) / Network.size();
		links.println(cycle + "\t" + ((double) allLinks) / Network.size());

		cycle++;
		if (cycle == 500) {
			links.println("Average Links for 500 cycles:\t" + totalLinks / 500.0);
			links.close();
		}

		return false;
	}

}