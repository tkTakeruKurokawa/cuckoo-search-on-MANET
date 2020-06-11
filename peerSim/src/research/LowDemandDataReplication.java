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
	private static final String PAR_STOP_CYCLE = "stop_cycle";
	private static int stopCycle;

	private static Random random = new Random();
	private static ArrayList<Integer> passedCycle = new ArrayList<Integer>();
	private static ArrayList<Double> relateNext = new ArrayList<Double>();
	private static ArrayList<Double> cuckooNext = new ArrayList<Double>();
	private static ArrayList<Integer> relateThreshold = new ArrayList<Integer>();
	private static ArrayList<Integer> cuckooThreshold = new ArrayList<Integer>();
	private static ArrayList<Boolean> lowDemand = new ArrayList<Boolean>();
	private static ArrayList<Integer> num = new ArrayList<Integer>();

	private static int cycle = 0;
	private static int dataID;
	private static double cuckooTotal;
	private static double relateTotal;

	public LowDemandDataReplication(String prefix) {
		alfa = Configuration.getDouble(prefix + "." + PAR_ALFA);
		coefficient = Configuration.getDouble(prefix + "." + PAR_COEFFICIENT);
		safe = Configuration.getInt(prefix + "." + PAR_SAFE_NUM);
		stopCycle = Configuration.getInt(prefix + "." + PAR_STOP_CYCLE);

		for (int i = 0; i < Data.getMaxVariety(); i++) {
			passedCycle.add(i, 0);
			relateNext.add(i, -2.0);
			cuckooNext.add(i, -2.0);
			relateThreshold.add(i, -1);
			cuckooThreshold.add(i, -1);
			lowDemand.add(i, false);
			num.add(i, 0);
		}
	}

	public static Node searchHavingNode(String type, Data data) {
		Node node;
		while (true) {
			int nodeID = random.nextInt(Network.size());
			node = Network.get(nodeID);

			Storage storage = SharedResource.getNodeStorage(type, node);
			if (storage.contains(data)) {
				break;
			}
		}

		return node;
	}

	// 最適なノードを発見後の複製配置する際ののコスト
	public static void calculateNetworkCost(int id, Node src, Node dst, int cycle) {
		Integer hops = Flooding.hops(src, dst);
		ArrayList<Integer> costList = SharedResource.getReplicationCost(id);
		costList.set(cycle, costList.get(cycle) + hops);
		SharedResource.setReplicationCost(id, costList);
	}

	public static void relatedResearch(Data data, int max) {
		int addNum = 0;
		int successCount = 0;
		Node node;
		Node base = searchHavingNode("relate", data);
		while (addNum < max) {
			node = RelatedResearch.getBestNode(base, data, cycle);

			if (Objects.nonNull(node)) {
				Storage storage = SharedResource.getNodeStorage("relate", node);
				boolean success = storage.setData(node, data);
				if (success) {
					Parameter parameter = SharedResource.getNodeParameter("relate", node);
					OutPut.writeCompare("relate", parameter);
					successCount++;
				} else {
				}
			} else {
			}

			addNum++;
		}

		ArrayList<Integer> count = SharedResource.getReplicationCount(2);
		count.set(cycle, count.get(cycle) + successCount);
		SharedResource.setReplicationCount(2, count);
	}

	public static void cuckooSearch(Data data, int max) {
		int addNum = 0;
		int successCount = 0;
		Node node;
		Node base = searchHavingNode("cuckoo", data);
		// System.out.println("Add Num: " + diff);
		while (addNum < max) {
			// System.out.println("----------START NODE: " + base.getIndex() + "
			// ----------");
			node = CuckooSearch.search(base, data, cycle);
			// System.out.println("CS Node: " + node);

			if (Objects.nonNull(node)) {
				Storage storage = SharedResource.getNodeStorage("cuckoo", node);
				boolean success = storage.setData(node, data);
				if (success) {
					Parameter parameter = SharedResource.getNodeParameter("cuckoo", node);
					OutPut.writeCompare("cuckoo", parameter);
					calculateNetworkCost(3, base, node, cycle);
					successCount++;
				} else {
				}
			} else {
			}

			addNum++;
		}

		ArrayList<Integer> count = SharedResource.getReplicationCount(3);
		count.set(cycle, count.get(cycle) + successCount);
		SharedResource.setReplicationCount(3, count);
	}

	public static void replicate(String type, ArrayList<Integer> counter, ArrayList<Integer> threshold,
			ArrayList<Double> next) {
		if (counter.get(dataID) > 0) {
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
	}

	public static ArrayList<Integer> defineThreshold(String type, ArrayList<Integer> counter,
			ArrayList<Integer> threshold) {
		lowDemand.set(dataID, false);
		double total;
		if (type.equals("cuckoo")) {
			total = cuckooTotal;
		} else {
			total = relateTotal;
		}

		if ((type.equals("relate") && threshold.get(dataID) < 0) || type.equals("cuckoo")) {
			// if (((double) counter.get(dataID)) <= (total * 0.05d)) {
			// threshold.set(dataID, counter.get(dataID));
			// lowDemand.set(dataID, true);
			// } else if (((double) counter.get(dataID)) <= (((double) Network.size()) *
			// 0.05d)) {
			// threshold.set(dataID, counter.get(dataID));
			// lowDemand.set(dataID, true);
			// }
			int condition = (int) Math.round(Math.log10(Network.size()) * 3.0);
			if (counter.get(dataID) <= condition) {
				threshold.set(dataID, condition);
				lowDemand.set(dataID, true);
			}
		}

		return threshold;
	}

	public static ArrayList<Double> exponentialSmoothing(ArrayList<Integer> counter, ArrayList<Integer> threshold,
			ArrayList<Double> next) {
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

		return next;
	}

	public static void calculateReplications(String type, ArrayList<Integer> counter, ArrayList<Integer> threshold,
			ArrayList<Double> next) {
		if (lowDemand.get(dataID).equals(true)) {
			int dataNum = counter.get(dataID);
			int safeNum = (int) Math.round(coefficient * (double) threshold.get(dataID));

			if (dataNum < safeNum && dataNum > 0) {
				Data data = Data.getData(dataID);
				int adds = (int) Math.round(Math.log10(Network.size()) - Math.log10(counter.get(dataID)));
				// int replications = safeNum + adds - dataNum;
				int replications = safeNum - dataNum;

				if (type.equals("relate")) {
					relatedResearch(data, replications);
				} else {
					if (passedCycle.get(dataID) < stopCycle) {
						cuckooSearch(data, replications);
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

			System.out.println("cuckoo ID: " + dataID + ", num: " + cuckooCounter.get(dataID) + ", max: "
					+ num.get(dataID) + ", Threshold: " + cuckooThreshold.get(dataID) + ", passedCycle: "
					+ passedCycle.get(dataID));
		}
		System.out.println();
	}

	public boolean execute() {
		ArrayList<Integer> relateCounter = SharedResource.getCounter("relate");
		ArrayList<Integer> cuckooCounter = SharedResource.getCounter("cuckoo");

		relateTotal = 0.0;
		cuckooTotal = 0.0;
		for (int id = 0; id < Data.getNowVariety(); id++) {
			relateTotal += (double) relateCounter.get(id);
			cuckooTotal += (double) cuckooCounter.get(id);

			if (num.get(id) < cuckooCounter.get(id)) {
				num.set(id, cuckooCounter.get(id));
			}
		}

		ArrayList<Integer> accesses = SharedResource.getAccesses();
		// データ要求があった場合，複製配置終了までのカウンターをリセット
		for (int id = 0; id < Data.getNowVariety(); id++) {
			dataID = id;
			if (!accesses.get(dataID).equals(0)) {
				relateThreshold.set(dataID, -1);
				cuckooThreshold.set(dataID, -1);
				passedCycle.set(dataID, 0);
				// データ要求が無い場合
			} else {
				replicate("cuckoo", cuckooCounter, cuckooThreshold, cuckooNext);
				replicate("relate", relateCounter, relateThreshold, relateNext);

				// 低需要と判定されている場合，カウンターの値を増加
				if (!cuckooThreshold.get(dataID).equals(-1)) {
					passedCycle.set(dataID, passedCycle.get(dataID) + 1);
				}
			}
		}

		showLowDemandData();

		cycle++;

		return false;
	}

}