package research;

import peersim.config.Configuration;
import peersim.core.*;
import java.util.*;
import java.lang.Math;

public class LowDemandDataReplication implements Control {
	private static final String PAR_ALFA = "alfa";
	private static double alfa;
	private static final String PAR_COEFFICIENT = "coefficient";
	private static double coefficient;
	private static final String PAR_SAFE_NUM = "safe_num";
	private static int safe;

	private static Random random = new Random();
	private static ArrayList<Integer> passedCycle = new ArrayList<Integer>();
	private static ArrayList<Double> relateNext = new ArrayList<Double>();
	private static ArrayList<Double> cuckooNext = new ArrayList<Double>();
	private static ArrayList<Integer> relateThreshold = new ArrayList<Integer>();
	private static ArrayList<Integer> cuckooThreshold = new ArrayList<Integer>();
	private static ArrayList<Boolean> lowDemand = new ArrayList<Boolean>();

	private static int cycle = 0;
	private static ArrayList<Boolean> replicated;

	public LowDemandDataReplication(String prefix) {
		alfa = Configuration.getDouble(prefix + "." + PAR_ALFA);
		coefficient = Configuration.getDouble(prefix + "." + PAR_COEFFICIENT);
		safe = Configuration.getInt(prefix + "." + PAR_SAFE_NUM);

		for (int i = 0; i < Data.getMaxVariety(); i++) {
			passedCycle.add(i, 0);
			relateNext.add(i, -2.0);
			cuckooNext.add(i, -2.0);
			relateThreshold.add(i, -1);
			cuckooThreshold.add(i, -1);
			lowDemand.add(i, false);
		}
	}

	public static Node searchHavingNode(Data data) {
		Node node;
		while (true) {
			int nodeID = random.nextInt(Network.size());
			node = Network.get(nodeID);
			Storage storage = SharedResource.getNodeStorage("cuckoo", node);
			if (storage.contains(data)) {
				break;
			}
		}

		return node;
	}

	public static void calculateNetworkCost(int id, Node src, Node dest, int cycle) {
		ArrayList<Integer> costList = SharedResource.getCost(id);
		Integer hops = Flooding.hops(src, dest);
		if (Objects.isNull(hops)) {
			for (int i = 0; i < Network.size(); i++) {
				NodeCoordinate node = SharedResource.getCoordinate(Network.get(i));
				System.out.println(node.getX() + "\t" + node.getY());
			}
			System.exit(0);

		}
		costList.set(cycle, costList.get(cycle) + hops);
		SharedResource.setCost(id, costList);
	}

	public static void relatedResearch(Data data, int max) {
		int addNum = 0;
		Node node;
		while (addNum < max) {
			node = Network.get(random.nextInt(Network.size()));
			node = RelatedResearch.getBestNode(node, data, cycle);

			if (Objects.nonNull(node)) {
				Storage storage = SharedResource.getNodeStorage("relate", node);
				boolean success = storage.setData(node, data);
				if (success) {
					Parameter parameter = (Parameter) node.getProtocol(5);
					if (replicated.get(data.getID()).equals(true)) {
						OutPut.writeCompare("relate", parameter);
					}
				} else {
				}
			} else {
			}

			addNum++;
		}
	}

	public static void cuckooSearch(Data data, int max) {
		int addNum = 0;
		Node node;
		Node base = searchHavingNode(data);
		// System.out.println("Add Num: " + diff);
		while (addNum < max) {
			node = CuckooSearch.search(base, data, cycle);
			// System.out.println("CS Node: " + node);

			if (Objects.nonNull(node)) {
				Storage storage = SharedResource.getNodeStorage("cuckoo", node);
				boolean success = storage.setData(node, data);
				if (success) {
					Parameter parameter = (Parameter) node.getProtocol(6);
					OutPut.writeCompare("cuckoo", parameter);
					calculateNetworkCost(3, base, node, cycle);
				} else {
				}
			} else {
			}

			addNum++;
		}
	}

	public static void replicate(String type, ArrayList<Integer> counter, ArrayList<Integer> threshold,
			ArrayList<Double> next) {
		threshold = defineThreshold(type, counter, threshold);
		next = exponentialSmoothing(counter, threshold, next);
		calculateReplications(type, counter, threshold, next);

		if (type.equals("relate")) {
			relateThreshold = threshold;
			relateNext = next;
		} else {
			cuckooThreshold = threshold;
			cuckooNext = next;
		}
	}

	public static ArrayList<Integer> defineThreshold(String type, ArrayList<Integer> counter,
			ArrayList<Integer> threshold) {
		Double total = 0.0;
		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			total += (double) counter.get(dataID);
			lowDemand.set(dataID, false);
			if (type.equals("cuckoo")) {
				replicated.add(dataID, false);
			}

		}

		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			if (threshold.get(dataID) < 0) {
				if (((double) counter.get(dataID)) <= (total * 0.05d)) {
					threshold.set(dataID, counter.get(dataID));
					lowDemand.set(dataID, true);
				} else if (((double) counter.get(dataID)) <= (((double) Network.size()) * 0.05d)) {
					threshold.set(dataID, counter.get(dataID));
					lowDemand.set(dataID, true);
				}
			}
		}

		return threshold;
	}

	public static ArrayList<Double> exponentialSmoothing(ArrayList<Integer> counter, ArrayList<Integer> threshold,
			ArrayList<Double> next) {
		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			Double md = (double) counter.get(dataID);

			if (next.get(dataID) < -1.0d) {
				next.set(dataID, md);
			} else {
				double value = alfa * md + (1 - alfa) * next.get(dataID);
				if (((int) Math.round(value)) <= threshold.get(dataID)) {
					lowDemand.set(dataID, true);
				}
				next.set(dataID, value);
			}
		}

		return next;
	}

	public static void calculateReplications(String type, ArrayList<Integer> counter, ArrayList<Integer> threshold,
			ArrayList<Double> next) {
		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			if (lowDemand.get(dataID).equals(true)) {
				Double x = (double) threshold.get(dataID) / next.get(dataID);
				if (x.equals(0.0) || Double.isInfinite(x) || Double.isNaN(x)) {
					x = 1.0;
				}
				int dataNum = counter.get(dataID);
				int safeNum = (int) Math.round(coefficient * (double) threshold.get(dataID));

				if (dataNum < safeNum && dataNum > 0) {
					Data data = Data.getData(dataID);
					int replications = safeNum - dataNum;

					if (type.equals("relate")) {
						relatedResearch(data, replications);
					} else {
						if (passedCycle.get(dataID) < 100) {
							cuckooSearch(data, replications);
							replicated.set(dataID, true);
						}
					}
				}
			}
		}
	}

	public static void showLowDemandData() {
		ArrayList<Integer> relateCounter = SharedResource.getCounter("relate");
		ArrayList<Integer> cuckooCounter = SharedResource.getCounter("cuckoo");
		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			System.out.println("-----------------------------------");
			System.out.println("relate ID " + dataID + ", num: " + relateCounter.get(dataID) + ", Threshold: "
					+ relateThreshold.get(dataID));

			System.out.println("cuckoo ID: " + dataID + ", num: " + cuckooCounter.get(dataID) + ", Threshold: "
					+ cuckooThreshold.get(dataID) + ", passedCycle: " + passedCycle.get(dataID));
		}
		System.out.println();
	}

	public boolean execute() {
		replicated = new ArrayList<Boolean>();

		ArrayList<Integer> relateCounter = SharedResource.getCounter("relate");
		ArrayList<Integer> cuckooCounter = SharedResource.getCounter("cuckoo");
		replicate("cuckoo", cuckooCounter, cuckooThreshold, cuckooNext);
		replicate("relate", relateCounter, relateThreshold, relateNext);

		ArrayList<Integer> accesses = SharedResource.getAccesses();
		// データ要求があった場合，複製配置終了までのカウンターをリセット
		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			// System.out.println("ID " + dataID + ": " + accesses.get(dataID));
			if (!accesses.get(dataID).equals(0)) {
				passedCycle.set(dataID, 0);
				// データ要求が無く，既に低需要と判定されている場合，カウンターの値を増加
			} else if (!cuckooThreshold.get(dataID).equals(-1)) {
				passedCycle.set(dataID, passedCycle.get(dataID) + 1);
			}
		}

		showLowDemandData();

		cycle++;

		return false;
	}

}