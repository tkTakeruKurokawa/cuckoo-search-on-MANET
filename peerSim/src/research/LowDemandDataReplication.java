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
	private static ArrayList<Integer> succFlood = new ArrayList<Integer>();
	private static ArrayList<Integer> failFlood = new ArrayList<Integer>();
	private static ArrayList<Integer> succSet = new ArrayList<Integer>();
	private static ArrayList<Integer> failSet = new ArrayList<Integer>();
	private static ArrayList<Double> relateSum = new ArrayList<Double>();
	private static ArrayList<Double> cuckooSum = new ArrayList<Double>();
	private static ArrayList<Integer> relateCount = new ArrayList<Integer>();
	private static ArrayList<Integer> cuckooCount = new ArrayList<Integer>();
	private static ArrayList<Integer> passedCycle = new ArrayList<Integer>();
	private static ArrayList<Double> fdNow = new ArrayList<Double>();
	private static AbstractList<Integer> threshold = new ArrayList<Integer>();
	private static ArrayList<Boolean> lowDemand = new ArrayList<Boolean>();

	private static int cycle = 0;

	public LowDemandDataReplication(String prefix) {
		alfa = Configuration.getDouble(prefix + "." + PAR_ALFA);
		coefficient = Configuration.getDouble(prefix + "." + PAR_COEFFICIENT);
		safe = Configuration.getInt(prefix + "." + PAR_SAFE_NUM);

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
			fdNow.add(i, -2.0);
			threshold.add(i, -1);
			lowDemand.add(i, false);
		}
	}

	public static void relatedResearch(Data data, int max) {
		int addNum = 0;
		Node node;
		while (addNum < max) {
			node = Network.get(random.nextInt(Network.size()));
			node = RelatedResearch.getBestNode(node, data, cycle);

			if (node != null) {
				succFlood.set(2, succFlood.get(2) + 1);
				Storage storage = SharedResource.getNodeStorage("relate", node);
				boolean success = storage.setData(node, data);
				if (success) {
					Parameter parameter = (Parameter) node.getProtocol(5);
					OutPut.writeCompare("relate", parameter);

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

	public static void cuckooSearch(Data data, int max) {
		int addNum = 0;
		Node node;
		// System.out.println("Add Num: " + diff);
		while (addNum < max) {
			node = CuckooSearch.search(data, cycle);
			// System.out.println("CS Node: " + node);

			if (node != null) {
				succFlood.set(3, succFlood.get(3) + 1);
				Storage storage = SharedResource.getNodeStorage("cuckoo", node);
				boolean success = storage.setData(node, data);
				if (success) {
					Parameter parameter = (Parameter) node.getProtocol(6);
					OutPut.writeCompare("cuckoo", parameter);

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

	public static void defineThreshold(ArrayList<Integer> accesses) {
		Double total = 0.0;
		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			total += (double) accesses.get(dataID);
			lowDemand.set(dataID, false);
		}

		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			if (threshold.get(dataID).equals(-1)) {
				if (((double) accesses.get(dataID)) <= (total * 0.05d)) {
					threshold.set(dataID, accesses.get(dataID));
					lowDemand.set(dataID, true);
				}
			}
		}
	}

	public static void exponentialSmoothing(ArrayList<Integer> accesses) {
		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			Double md = (double) accesses.get(dataID);

			if (fdNow.get(dataID) < -1.0d) {
				fdNow.set(dataID, md);
			} else {
				double value = alfa * md + (1 - alfa) * fdNow.get(dataID);
				if (((int) Math.round(value)) <= threshold.get(dataID)) {
					threshold.set(dataID, accesses.get(dataID));
					lowDemand.set(dataID, true);
				}
				fdNow.set(dataID, value);
			}
		}
	}

	public static void calculateReplications(String type, ArrayList<Integer> accesses) {
		ArrayList<Integer> dataCounter = SharedResource.getCounter(type);

		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			if (lowDemand.get(dataID) == true) {
				Double x = fdNow.get(dataID) / (double) accesses.get(dataID);
				if (x.equals(0.0) || Double.isInfinite(x) || Double.isNaN(x)) {
					x = 1.0;
				}
				int dataNum = dataCounter.get(dataID);
				int safeNum = (int) Math.round(coefficient * (double) dataNum * x);

				if (dataNum < safeNum) {
					Data data = Data.getData(dataID);
					if (type.equals("relate")) {
						// relatedResearch(data, safeNum - dataNum);
						relatedResearch(data, 1);
					} else {
						if (passedCycle.get(dataID) < 100) {
							// cuckooSearch(data, safeNum - dataNum);
							cuckooSearch(data, 1);
						}
					}
				}
			}
		}
	}

	public void showLowDemandData() {
		ArrayList<Integer> relatelist = SharedResource.getCounter("relate");
		ArrayList<Integer> cuckoolist = SharedResource.getCounter("cuckoo");
		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			System.out.println("-----------------------------------");
			System.out.println("relate ID " + dataID + ": " + relatelist.get(dataID) + " " + threshold.get(dataID));

			System.out.println("cuckoo ID " + dataID + ": " + cuckoolist.get(dataID) + " " + threshold.get(dataID) + " "
					+ passedCycle.get(dataID));
		}
		System.out.println();
	}

	public boolean execute() {
		// 低需要の判定
		ArrayList<Integer> accesses = SharedResource.getAccesses();
		defineThreshold(accesses);
		exponentialSmoothing(accesses);
		calculateReplications("relate", accesses);
		calculateReplications("cuckoo", accesses);

		// データ要求があった場合，複製配置終了までのカウンターをリセット
		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			// System.out.println("ID " + dataID + ": " + accesses.get(dataID));
			if (!accesses.get(dataID).equals(0)) {
				passedCycle.set(dataID, 0);
				// データ要求が無く，既に低需要と判定されている場合，カウンターの値を増加
			} else if (!threshold.get(dataID).equals(-1)) {
				passedCycle.set(dataID, passedCycle.get(dataID) + 1);
			}
		}

		showLowDemandData();

		cycle++;

		return false;
	}

}