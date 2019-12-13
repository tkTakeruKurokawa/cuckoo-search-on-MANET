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

	private ArrayList<Double> hop = new ArrayList<Double>();
	private ArrayList<Double> hopHigh = new ArrayList<Double>();
	private ArrayList<Double> hopLow = new ArrayList<Double>();
	private ArrayList<Integer> miss = new ArrayList<Integer>();
	private ArrayList<Integer> missHigh = new ArrayList<Integer>();
	private ArrayList<Integer> missLow = new ArrayList<Integer>();
	private ArrayList<Integer> hit = new ArrayList<Integer>();
	private ArrayList<Integer> hitHigh = new ArrayList<Integer>();
	private ArrayList<Integer> hitLow = new ArrayList<Integer>();

	private ArrayList<Double> highRemainingValue = new ArrayList<Double>();
	private ArrayList<Double> highRemainingSum = new ArrayList<Double>();
	private ArrayList<Double> highRemainingAverage = new ArrayList<Double>();
	private ArrayList<Double> highRemainingDistribution = new ArrayList<Double>();
	private ArrayList<Double> highRemainingCount = new ArrayList<Double>();
	private ArrayList<Double> lowRemainingValue = new ArrayList<Double>();
	private ArrayList<Double> lowRemainingSum = new ArrayList<Double>();
	private ArrayList<Double> lowRemainingAverage = new ArrayList<Double>();
	private ArrayList<Double> lowRemainingDistribution = new ArrayList<Double>();
	private ArrayList<Double> lowRemainingCount = new ArrayList<Double>();

	private ArrayList<Double> highOccupancy = new ArrayList<Double>();
	private ArrayList<Double> lowOccupancy = new ArrayList<Double>();

	private ArrayList<SectionAverage> sa = new ArrayList<SectionAverage>();
	private SectionAverage ownerSA = new SectionAverage("owner");
	private SectionAverage pathSA = new SectionAverage("path");
	private SectionAverage relateSA = new SectionAverage("relate");
	private SectionAverage cuckooSA = new SectionAverage("cuckoo");

	private int cycle = 0;

	public GetReplications(String prefix) {
		capacity = Configuration.getDouble(prefix + "." + PAR_CAPACITY);

		for (int i = 0; i < 4; i++) {
			hop.add(i, 0.0);
			hopHigh.add(i, 0.0);
			hopLow.add(i, 0.0);
			miss.add(i, 0);
			missHigh.add(i, 0);
			missLow.add(i, 0);
			hit.add(i, 0);
			hitHigh.add(i, 0);
			hitLow.add(i, 0);
			highRemainingSum.add(i, 0.0);
			highRemainingAverage.add(i, 0.0);
			highRemainingDistribution.add(i, 0.0);
			highRemainingCount.add(i, 0.0);
			lowRemainingSum.add(i, 0.0);
			lowRemainingAverage.add(i, 0.0);
			lowRemainingDistribution.add(i, 0.0);
			lowRemainingCount.add(i, 0.0);

			highOccupancy.add(i, 0.0);
			lowOccupancy.add(i, 0.0);
		}

		sa.add(ownerSA);
		sa.add(pathSA);
		sa.add(relateSA);
		sa.add(cuckooSA);
	}

	public void calcHitRate() {
		for (int i = 0; i < 4; i++) {
			output.writeTotalHitRate(i, hit.get(i), hop.get(i) / ((double) hit.get(i)), miss.get(i));
			output.writeHighHitRate(i, hitHigh.get(i), hopHigh.get(i) / ((double) hitHigh.get(i)), missHigh.get(i));
			output.writeLowHitRate(i, hitLow.get(i), hopLow.get(i) / ((double) hitLow.get(i)), missLow.get(i));
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

			if (Objects.equals(data.getType(), "high")) {
				missHigh.set(id, missHigh.get(id) + 1);
			} else {
				missLow.set(id, missLow.get(id) + 1);

			}

			return;
		}

		Integer value = Flooding.hops(node, data, id);
		// System.out.println("Hops: " + value);
		if (value != null) {
			hop.set(id, hop.get(id) + Double.valueOf(value));
			hit.set(id, hit.get(id) + 1);

			if (Objects.equals(data.getType(), "high")) {
				hopHigh.set(id, hopHigh.get(id) + Double.valueOf(value));
				hitHigh.set(id, hitHigh.get(id) + 1);
			} else {
				hopLow.set(id, hopLow.get(id) + Double.valueOf(value));
				hitLow.set(id, hitLow.get(id) + 1);
			}
		} else {
			miss.set(id, miss.get(id) + 1);
			if (Objects.equals(data.getType(), "high")) {
				missHigh.set(id, missHigh.get(id) + 1);
			} else {
				missLow.set(id, missLow.get(id) + 1);

			}
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
		boolean high = false;
		boolean low = false;
		// double highRemaining = capacity;
		// double lowRemaining = capacity;
		for (Data data : storage.getData()) {
			if (Objects.equals(data.getType(), "high")) {
				// highRemaining -= ((double) data.getSize());
				highOccupancy.set(id, highOccupancy.get(id) + (double) data.getSize());
				high = true;
			}
			if (Objects.equals(data.getType(), "low")) {
				// lowRemaining -= ((double) data.getSize());
				lowOccupancy.set(id, lowOccupancy.get(id) + (double) data.getSize());
				low = true;
			}
		}

		if (high == true) {
			highRemainingValue.add(((double) parameter.getCapacity()));
			highRemainingSum.set(id, highRemainingSum.get(id) + ((double) parameter.getCapacity()));
			highRemainingCount.set(id, highRemainingCount.get(id) + 1.0);
			highRemainingAverage.set(id, highRemainingSum.get(id) / highRemainingCount.get(id));
		}
		if (low == true) {
			lowRemainingValue.add(((double) parameter.getCapacity()));
			lowRemainingSum.set(id, lowRemainingSum.get(id) + ((double) parameter.getCapacity()));
			lowRemainingCount.set(id, lowRemainingCount.get(id) + 1.0);
			lowRemainingAverage.set(id, lowRemainingSum.get(id) / lowRemainingCount.get(id));
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
		}

		int highAll = SharedResource.getHighTotal("owner");
		int lowAll = SharedResource.getLowTotal("owner");

		highAvailability = ((double) highSum) / ((double) highAll);
		lowAvailability = ((double) lowSum) / ((double) lowAll);

		output.writeHighCount("owner", cycle, highAvailability, highSum, highAll);
		output.writeLowCount("owner", cycle, lowAvailability, lowSum, lowAll);

		ownerSA.setHighAvailability(cycle + 1, highAvailability);
		ownerSA.setLowAvailability(cycle + 1, lowAvailability);
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
		}

		int highAll = SharedResource.getHighTotal("path");
		int lowAll = SharedResource.getLowTotal("path");

		highAvailability = ((double) highSum) / ((double) highAll);
		lowAvailability = ((double) lowSum) / ((double) lowAll);

		output.writeHighCount("path", cycle, highAvailability, highSum, highAll);
		output.writeLowCount("path", cycle, lowAvailability, lowSum, lowAll);

		pathSA.setHighAvailability(cycle + 1, highAvailability);
		pathSA.setLowAvailability(cycle + 1, lowAvailability);
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
		}

		int highAll = SharedResource.getHighTotal("relate");
		int lowAll = SharedResource.getLowTotal("relate");

		highAvailability = ((double) highSum) / ((double) highAll);
		lowAvailability = ((double) lowSum) / ((double) lowAll);

		output.writeHighCount("relate", cycle, highAvailability, highSum, highAll);
		output.writeLowCount("relate", cycle, lowAvailability, lowSum, lowAll);

		relateSA.setHighAvailability(cycle + 1, highAvailability);
		relateSA.setLowAvailability(cycle + 1, lowAvailability);
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
		}

		int highAll = SharedResource.getHighTotal("cuckoo");
		int lowAll = SharedResource.getLowTotal("cuckoo");

		highAvailability = ((double) highSum) / ((double) highAll);
		lowAvailability = ((double) lowSum) / ((double) lowAll);

		output.writeHighCount("cuckoo", cycle, highAvailability, highSum, highAll);
		output.writeLowCount("cuckoo", cycle, lowAvailability, lowSum, lowAll);

		cuckooSA.setHighAvailability(cycle + 1, highAvailability);
		cuckooSA.setLowAvailability(cycle + 1, lowAvailability);
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
			highRemainingCount.set(i, 0.0);
			lowRemainingSum.set(i, 0.0);
			lowRemainingAverage.set(i, 0.0);
			lowRemainingDistribution.set(i, 0.0);
			lowRemainingCount.set(i, 0.0);
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
			int highCount = highRemainingCount.get(i).intValue();
			for (int j = 0; j < highCount; j++) {
				highRemainingDistribution.set(i, highRemainingDistribution.get(i)
						+ Math.pow(highRemainingValue.get(j) - highRemainingAverage.get(i), 2));
			}
			highRemainingValue.subList(0, highCount).clear();
		}

		for (int i = 0; i < 4; i++) {
			int lowCount = lowRemainingCount.get(i).intValue();
			for (int j = 0; j < lowCount; j++) {
				lowRemainingDistribution.set(i, lowRemainingDistribution.get(i)
						+ Math.pow(lowRemainingValue.get(j) - lowRemainingAverage.get(i), 2));
			}
			lowRemainingValue.subList(0, lowCount).clear();
		}

		for (int i = 0; i < 4; i++) {
			if (highRemainingCount.get(i) < 1.0 && highRemainingSum.get(i) < 1.0) {
				output.writeHighRemaining(i, cycle, capacity, 0.0);
			} else {
				output.writeHighRemaining(i, cycle, highRemainingAverage.get(i),
						Math.sqrt(highRemainingDistribution.get(i) / highRemainingCount.get(i)));
			}
			if (lowRemainingCount.get(i) < 1.0 && lowRemainingSum.get(i) < 1.0) {
				output.writeLowRemaining(i, cycle, capacity, 0.0);
			} else {
				output.writeLowRemaining(i, cycle, lowRemainingAverage.get(i),
						Math.sqrt(lowRemainingDistribution.get(i) / lowRemainingCount.get(i)));
			}
			output.writeHighOccupancy(i, cycle, highOccupancy.get(i));
			output.writeLowOccupancy(i, cycle, lowOccupancy.get(i));
		}

		for (int id = 0; id < 4; id++) {
			sa.get(id).setHighRemaining(cycle + 1, highRemainingAverage.get(id));
			sa.get(id).setLowRemaining(cycle + 1, lowRemainingAverage.get(id));
			sa.get(id).setHighOccupancy(cycle + 1, highOccupancy.get(id));
			sa.get(id).setLowOccupancy(cycle + 1, lowOccupancy.get(id));
		}

		hitRate();

		cycle++;

		if (cycle == 500) {
			calcHitRate();
			for (int id = 0; id < 4; id++) {
				sa.get(id).writeFile();
			}

			output.closeFiles();

			// System.out.println("Final Nodes: " + Network.size());
			// System.out.println("High Owner occupancy: " + highOccupancy.get(0));
			// System.out.println("High Path occupancy: " + highOccupancy.get(1));
			// System.out.println("High Relate occupancy: " + highOccupancy.get(2));
			// System.out.println("High Cuckoo occupancy: " + highOccupancy.get(3));

			System.out.println("Low Owner occupancy: " + lowOccupancy.get(0));
			System.out.println("Low Path occupancy: " + lowOccupancy.get(1));
			System.out.println("Low Relate occupancy: " + lowOccupancy.get(2));
			System.out.println("Low Cuckoo occupancy: " + lowOccupancy.get(3));
			System.out.println();

			System.out.println("Cuckoo_Owner= " + (lowOccupancy.get(3) / lowOccupancy.get(0)));
			System.out.println("Relate_Owner = " + (lowOccupancy.get(2) / lowOccupancy.get(0)));
			System.out.println("Relate_Cuckoo = " + (lowOccupancy.get(2) / lowOccupancy.get(3)));
			System.out.println();

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