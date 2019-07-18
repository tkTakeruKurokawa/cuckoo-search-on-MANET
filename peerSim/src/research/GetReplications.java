package research;

import peersim.config.*;
import peersim.core.*;
import java.util.*;

import java.lang.Math;

public class GetReplications implements Control {
	private static final String PAR_CAPACITY = "capacity";
	private static double capacity;

	private Random random = new Random();
	private OutPut output = new OutPut();
	private ArrayList<Double> hit = new ArrayList<Double>();
	private ArrayList<Integer> miss = new ArrayList<Integer>();
	private ArrayList<Integer> calcNum = new ArrayList<Integer>();
	private ArrayList<Double> highRemainingValue = new ArrayList<Double>();
	private ArrayList<Double> highRemainingSum = new ArrayList<Double>();
	private ArrayList<Double> highRemainingAverage = new ArrayList<Double>();
	private ArrayList<Double> highRemainingDistribution = new ArrayList<Double>();
	private ArrayList<Double> highOccupancyCount = new ArrayList<Double>();
	private ArrayList<Double> lowRemainingValue = new ArrayList<Double>();
	private ArrayList<Double> lowRemainingSum = new ArrayList<Double>();
	private ArrayList<Double> lowRemainingAverage = new ArrayList<Double>();
	private ArrayList<Double> lowRemainingDistribution = new ArrayList<Double>();
	private ArrayList<Double> lowOccupancyCount = new ArrayList<Double>();

	private ArrayList<Double> highOccupancy = new ArrayList<Double>();
	private ArrayList<Double> lowOccupancy = new ArrayList<Double>();

	private int cycle = 0;

	public GetReplications(String prefix) {
		capacity = Configuration.getDouble(prefix + "." + PAR_CAPACITY);

		for (int i = 0; i < 4; i++) {
			hit.add(i, 0.0);
			miss.add(i, 0);
			calcNum.add(i, 0);
			highRemainingSum.add(i, 0.0);
			highRemainingAverage.add(i, 0.0);
			highRemainingDistribution.add(i, 0.0);
			highOccupancyCount.add(i, 0.0);
			lowRemainingSum.add(i, 0.0);
			lowRemainingAverage.add(i, 0.0);
			lowRemainingDistribution.add(i, 0.0);
			lowOccupancyCount.add(i, 0.0);

			highOccupancy.add(i, 0.0);
			lowOccupancy.add(i, 0.0);
		}
	}

	public void calcHitRate() {
		for (int i = 0; i < 4; i++) {
			output.writeHitRate(i, calcNum.get(i), hit.get(i) / ((double) calcNum.get(i)), miss.get(i));
		}
	}

	public boolean existsHigh(Data data, int id) {
		int num = 0;
		switch (id) {
		case 0:
			ArrayList<Integer> dcOwner = SharedResource.getOwnerHighCounter();
			num = dcOwner.get(data.getID());
			break;
		case 1:
			ArrayList<Integer> dcPath = SharedResource.getPathHighCounter();
			num = dcPath.get(data.getID());
			break;
		case 2:
			ArrayList<Integer> dcRelate = SharedResource.getRelateHighCounter();
			num = dcRelate.get(data.getID());
			break;
		case 3:
			ArrayList<Integer> dcCuckoo = SharedResource.getCuckooHighCounter();
			num = dcCuckoo.get(data.getID());
			break;
		}
		if (num > 0) {
			return true;
		}
		return false;
	}

	public boolean existsLow(Data data, int id) {
		int num = 0;
		switch (id) {
		case 0:
			ArrayList<Integer> dcOwner = SharedResource.getOwnerLowCounter();
			num = dcOwner.get(data.getID());
			break;
		case 1:
			ArrayList<Integer> dcPath = SharedResource.getPathLowCounter();
			num = dcPath.get(data.getID());
			break;
		case 2:
			ArrayList<Integer> dcRelate = SharedResource.getRelateLowCounter();
			num = dcRelate.get(data.getID());
			break;
		case 3:
			ArrayList<Integer> dcCuckoo = SharedResource.getCuckooLowCounter();
			num = dcCuckoo.get(data.getID());
			break;
		}
		if (num > 0) {
			return true;
		}
		return false;
	}

	public void hitOrMiss(Node node, Data data, int id) {
		// System.out.println("ID: " + id + "Node: " + node.getIndex() + " Data: " +
		// data.getID());

		if (!existsHigh(data, id) && !existsLow(data, id)) {
			miss.set(id, miss.get(id) + 1);
			return;
		}

		Integer value = Flooding.hops(node, data, id);
		// System.out.println("Hops: " + value);
		if (value != null) {
			hit.set(id, hit.get(id) + Double.valueOf(value));
			calcNum.set(id, calcNum.get(id) + 1);
		} else {
			miss.set(id, miss.get(id) + 1);
		}
	}

	public void hitRate() {
		int nodeID = random.nextInt(Network.size());
		int dataID = random.nextInt(Data.getNowVariety());
		for (int i = 0; i < 4; i++) {
			hitOrMiss(Network.get(nodeID), Data.getData(dataID), i);
		}
	}

	public void calcOccupancy(int id, Parameter parameter, Storage storage) {
		// double occupancy = ((double)parameter.getCapacity()) / capacity;
		// highRemainingSum.set(id, highRemainingSum.get(id)+occupancy);
		boolean high = false;
		boolean low = false;
		double highRemaining = capacity;
		double lowRemaining = capacity;
		for (Data data : storage.getData()) {
			if (Objects.equals(data.getType(), "high")) {
				highRemaining -= ((double) data.getSize());
				highOccupancy.set(id, highOccupancy.get(id) + (double) data.getSize());
				high = true;
			}
			if (Objects.equals(data.getType(), "low")) {
				lowRemaining -= ((double) data.getSize());
				lowOccupancy.set(id, lowOccupancy.get(id) + (double) data.getSize());
				low = true;
			}
		}

		if (high == true) {
			highRemainingValue.add(highRemaining);
			highRemainingSum.set(id, highRemainingSum.get(id) + highRemaining);
			highOccupancyCount.set(id, highOccupancyCount.get(id) + 1.0);
			highRemainingAverage.set(id, highRemainingSum.get(id) / highOccupancyCount.get(id));
		}
		if (low == true) {
			lowRemainingValue.add(lowRemaining);
			lowRemainingSum.set(id, lowRemainingSum.get(id) + lowRemaining);
			lowOccupancyCount.set(id, lowOccupancyCount.get(id) + 1.0);
			lowRemainingAverage.set(id, lowRemainingSum.get(id) / lowOccupancyCount.get(id));
		}

	}

	public boolean check(Storage storage, Parameter parameter, Data data) {
		int capacity = parameter.getCapacity();
		int occupancy = data.getSize();
		int newCapacity = capacity - occupancy;

		if (!storage.contains(data) && (newCapacity >= 0)) {
			return true;
		}

		return false;
	}

	public void owner() {
		ArrayList<Integer> highCounter = SharedResource.getOwnerHighCounter();
		ArrayList<Integer> lowCounter = SharedResource.getOwnerLowCounter();

		int highSum = 0;
		int lowSum = 0;
		double highAvailability = 0.0;
		double lowAvailability = 0.0;
		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			if (Objects.equals(Data.getData(dataID).getType(), "high") && highCounter.get(dataID) != null) {
				highSum += highCounter.get(dataID);
			}
			if (Objects.equals(Data.getData(dataID).getType(), "low") && lowCounter.get(dataID) != null) {
				lowSum += lowCounter.get(dataID);
			}
			// System.out.println("Data: " + dataID + "\tNum: " + dataNum);
		}

		int highAll = SharedResource.getHighTotal("owner");
		int lowAll = SharedResource.getLowTotal("owner");

		highAvailability = ((double) highSum) / ((double) highAll);
		lowAvailability = ((double) lowSum) / ((double) lowAll);

		output.writeHighCount("owner", cycle, highAvailability, highSum, highAll);
		output.writeLowCount("owner", cycle, lowAvailability, lowSum, lowAll);
	}

	public void path() {
		ArrayList<Integer> highCounter = SharedResource.getPathHighCounter();
		ArrayList<Integer> lowCounter = SharedResource.getPathLowCounter();

		int highSum = 0;
		int lowSum = 0;
		double highAvailability = 0.0;
		double lowAvailability = 0.0;
		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			if (Objects.equals(Data.getData(dataID).getType(), "high") && highCounter.get(dataID) != null) {
				highSum += highCounter.get(dataID);
			}
			if (Objects.equals(Data.getData(dataID).getType(), "low") && lowCounter.get(dataID) != null) {
				lowSum += lowCounter.get(dataID);
			}
			// System.out.println("Data: " + dataID + "\tNum: " + dataNum);
		}

		int highAll = SharedResource.getHighTotal("path");
		int lowAll = SharedResource.getLowTotal("path");

		highAvailability = ((double) highSum) / ((double) highAll);
		lowAvailability = ((double) lowSum) / ((double) lowAll);

		output.writeHighCount("path", cycle, highAvailability, highSum, highAll);
		output.writeLowCount("path", cycle, lowAvailability, lowSum, lowAll);
	}

	public void relate() {
		ArrayList<Integer> highCounter = SharedResource.getRelateHighCounter();
		ArrayList<Integer> lowCounter = SharedResource.getRelateLowCounter();

		int highSum = 0;
		int lowSum = 0;
		double highAvailability = 0.0;
		double lowAvailability = 0.0;
		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			if (Objects.equals(Data.getData(dataID).getType(), "high") && highCounter.get(dataID) != null) {
				highSum += highCounter.get(dataID);
			}
			if (Objects.equals(Data.getData(dataID).getType(), "low") && lowCounter.get(dataID) != null) {
				lowSum += lowCounter.get(dataID);
			}
			// System.out.println("Data: " + dataID + "\tNum: " + dataNum);
		}

		int highAll = SharedResource.getHighTotal("relate");
		int lowAll = SharedResource.getLowTotal("relate");

		highAvailability = ((double) highSum) / ((double) highAll);
		lowAvailability = ((double) lowSum) / ((double) lowAll);

		output.writeHighCount("relate", cycle, highAvailability, highSum, highAll);
		output.writeLowCount("relate", cycle, lowAvailability, lowSum, lowAll);
	}

	public void cuckoo() {
		ArrayList<Integer> highCounter = SharedResource.getCuckooHighCounter();
		ArrayList<Integer> lowCounter = SharedResource.getCuckooLowCounter();

		int highSum = 0;
		int lowSum = 0;
		double highAvailability = 0.0;
		double lowAvailability = 0.0;
		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			if (Objects.equals(Data.getData(dataID).getType(), "high") && highCounter.get(dataID) != null) {
				highSum += highCounter.get(dataID);
			}
			if (Objects.equals(Data.getData(dataID).getType(), "low") && lowCounter.get(dataID) != null) {
				lowSum += lowCounter.get(dataID);
			}
			// System.out.println("Data: " + dataID + "\tNum: " + dataNum);
		}

		int highAll = SharedResource.getHighTotal("cuckoo");
		int lowAll = SharedResource.getLowTotal("cuckoo");

		highAvailability = ((double) highSum) / ((double) highAll);
		lowAvailability = ((double) lowSum) / ((double) lowAll);

		output.writeHighCount("cuckoo", cycle, highAvailability, highSum, highAll);
		output.writeLowCount("cuckoo", cycle, lowAvailability, lowSum, lowAll);
	}

	public boolean execute() {

		owner();
		path();
		cuckoo();
		relate();

		highRemainingValue = new ArrayList<Double>();
		lowRemainingValue = new ArrayList<Double>();
		for (int i = 0; i < 4; i++) {
			highRemainingSum.set(i, 0.0);
			highRemainingAverage.set(i, 0.0);
			highRemainingDistribution.set(i, 0.0);
			highOccupancyCount.set(i, 0.0);
			lowRemainingSum.set(i, 0.0);
			lowRemainingAverage.set(i, 0.0);
			lowRemainingDistribution.set(i, 0.0);
			lowOccupancyCount.set(i, 0.0);
		}

		int allLinks = 0;
		for (int nodeID = 0; nodeID < Network.size(); nodeID++) {
			// 平均リンク数の測定用
			Node node = Network.get(nodeID);
			Link linkable = SharedResource.getLink(node);
			allLinks += linkable.degree();

			calcOccupancy(0, SharedResource.getNPOwner(Network.get(nodeID)),
					SharedResource.getSOwner(Network.get(nodeID)));
			calcOccupancy(1, SharedResource.getNPPath(Network.get(nodeID)),
					SharedResource.getSPath(Network.get(nodeID)));
			calcOccupancy(2, SharedResource.getNPRelate(Network.get(nodeID)),
					SharedResource.getSRelate(Network.get(nodeID)));
			calcOccupancy(3, SharedResource.getNPCuckoo(Network.get(nodeID)),
					SharedResource.getSCuckoo(Network.get(nodeID)));
		}
		output.writeAverageLinks(cycle, ((double) allLinks) / Network.size());

		for (int i = 0; i < 4; i++) {
			int highCount = highOccupancyCount.get(i).intValue();
			for (int j = 0; j < highCount; j++) {
				highRemainingDistribution.set(i, highRemainingDistribution.get(i)
						+ Math.pow(highRemainingValue.get(j) - highRemainingAverage.get(i), 2));
			}
			highRemainingValue.subList(0, highCount).clear();
		}

		for (int i = 0; i < 4; i++) {
			int lowCount = lowOccupancyCount.get(i).intValue();
			for (int j = 0; j < lowCount; j++) {
				lowRemainingDistribution.set(i, lowRemainingDistribution.get(i)
						+ Math.pow(lowRemainingValue.get(j) - lowRemainingAverage.get(i), 2));
			}
			lowRemainingValue.subList(0, lowCount).clear();
		}

		for (int i = 0; i < 4; i++) {
			if (highOccupancyCount.get(i) < 1.0 && highRemainingSum.get(i) < 1.0) {
				output.writeHighRemaining(i, cycle, capacity, 0.0);
			} else {
				output.writeHighRemaining(i, cycle, highRemainingAverage.get(i),
						Math.sqrt(highRemainingDistribution.get(i) / highOccupancyCount.get(i)));
			}
			if (lowOccupancyCount.get(i) < 1.0 && lowRemainingSum.get(i) < 1.0) {
				output.writeLowRemaining(i, cycle, capacity, 0.0);
			} else {
				output.writeLowRemaining(i, cycle, lowRemainingAverage.get(i),
						Math.sqrt(lowRemainingDistribution.get(i) / lowOccupancyCount.get(i)));
			}
			output.writeHighOccupancy(i, cycle, highOccupancy.get(i));
			output.writeLowOccupancy(i, cycle, lowOccupancy.get(i));
		}

		hitRate();

		cycle++;

		if (cycle == 500) {
			calcHitRate();
			output.closeFiles();

			// System.out.println("Final Nodes: " + Network.size());
			// System.out.println("High Owner occupancy: " + highOccupancy.get(0));
			// System.out.println("High Path occupancy: " + highOccupancy.get(1));
			// System.out.println("High Relate occupancy: " + highOccupancy.get(2));
			// System.out.println("High Cuckoo occupancy: " + highOccupancy.get(3));

			// System.out.println("Low Owner occupancy: " + lowOccupancy.get(0));
			// System.out.println("Low Path occupancy: " + lowOccupancy.get(1));
			// System.out.println("Low Relate occupancy: " + lowOccupancy.get(2));
			// System.out.println("Low Cuckoo occupancy: " + lowOccupancy.get(3));

			// System.out.println("Cuckoo_Owner= " + (1.0 - (highRemainingSum.get(0) /
			// highRemainingSum.get(3))));
			// System.out.println("Relate_Owner = " + (1.0 - (highRemainingSum.get(0) /
			// highRemainingSum.get(2))));
			// System.out.println("Relate_Cuckoo = " + (1.0 - (highRemainingSum.get(3) /
			// highRemainingSum.get(2))));

			// System.out.println("Storage Occupancy");
			// System.out.println("\tCuckoo-Owner= " + (highRemainingSum.get(3) -
			// highRemainingSum.get(0)));
			// System.out.println("\tRelate-Owner= " + (highRemainingSum.get(2) -
			// highRemainingSum.get(0)));
			// System.out.println("\tRelate-Cuckoo= " + (highRemainingSum.get(2) -
			// highRemainingSum.get(3)));
		}

		return false;
	}

}