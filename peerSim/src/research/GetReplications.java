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
	private ArrayList<Integer> miss = new ArrayList<Integer>();
	private ArrayList<Integer> hit = new ArrayList<Integer>();

	private ArrayList<Double> remainingValue = new ArrayList<Double>();
	private ArrayList<Double> remainingSum = new ArrayList<Double>();
	private ArrayList<Double> remainingAverage = new ArrayList<Double>();
	private ArrayList<Double> remainingDistribution = new ArrayList<Double>();
	private ArrayList<Double> remainingCount = new ArrayList<Double>();

	private ArrayList<Double> occupancy = new ArrayList<Double>();

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
			miss.add(i, 0);
			hit.add(i, 0);
			remainingSum.add(i, 0.0);
			remainingAverage.add(i, 0.0);
			remainingDistribution.add(i, 0.0);
			remainingCount.add(i, 0.0);

			occupancy.add(i, 0.0);
		}

		sa.add(ownerSA);
		sa.add(pathSA);
		sa.add(relateSA);
		sa.add(cuckooSA);
	}

	public void networkCost() {
		for (int i = 0; i < 4; i++) {
			ArrayList<Integer> searchCostList = SharedResource.getSearchCost(i);
			ArrayList<Integer> replicationCostList = SharedResource.getReplicationCost(i);
			ArrayList<Integer> replicationCountList = SharedResource.getReplicationCount(i);
			double searchTotal = 0.0;
			double replicationTotal = 0.0;

			for (int cycle = 0; cycle < searchCostList.size(); cycle++) {
				searchTotal += (double) searchCostList.get(cycle);
				replicationTotal += (double) replicationCostList.get(cycle);
			}

			output.writeSearchCost(i, cycle, searchCostList.get(cycle), searchTotal / ((double) (cycle + 1)));
			output.writeReplicationCost(i, cycle, replicationCostList.get(cycle),
					replicationTotal / ((double) (cycle + 1)), replicationCountList.get(cycle));
		}
	}

	public void calcHitRate() {
		for (int i = 0; i < 4; i++) {
			output.writeTotalHitRate(i, hit.get(i), hop.get(i) / ((double) hit.get(i)), miss.get(i));
		}
	}

	public boolean existsData(Data data, int id) {
		int num = 0;
		switch (id) {
			case 0:
				ArrayList<Integer> dcOwner = SharedResource.getCounter("owner");
				num = dcOwner.get(data.getID());
				break;
			case 1:
				ArrayList<Integer> dcPath = SharedResource.getCounter("path");
				num = dcPath.get(data.getID());
				break;
			case 2:
				ArrayList<Integer> dcRelate = SharedResource.getCounter("relate");
				num = dcRelate.get(data.getID());
				break;
			case 3:
				ArrayList<Integer> dcCuckoo = SharedResource.getCounter("cuckoo");
				num = dcCuckoo.get(data.getID());
				break;
		}
		if (num > 0) {
			return true;
		}
		return false;
	}

	public void hitOrMiss(Node node, Data data, int id) {
		if (!existsData(data, id)) {
			miss.set(id, miss.get(id) + 1);

			return;
		}

		Integer value = Flooding.hops(node, data, id);
		if (value != null) {
			hop.set(id, hop.get(id) + Double.valueOf(value));
			hit.set(id, hit.get(id) + 1);
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
		for (Data data : storage.getData()) {
			occupancy.set(id, occupancy.get(id) + (double) data.getSize());
		}

		remainingValue.add(((double) parameter.getCapacity()));
		remainingSum.set(id, remainingSum.get(id) + ((double) parameter.getCapacity()));
		remainingCount.set(id, remainingCount.get(id) + 1.0);
		remainingAverage.set(id, remainingSum.get(id) / remainingCount.get(id));
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

	public double calucAvailability(String type) {
		ArrayList<Integer> dataCounter = SharedResource.getCounter(type);

		int total = 0;
		double availability = 0.0;
		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			if (dataCounter.get(dataID) != null) {
				total += dataCounter.get(dataID);
			}
		}

		int all = SharedResource.getDataTotal(type);

		availability = ((double) total) / ((double) all);

		output.writeAvailability(type, cycle, availability, total, all);
		return availability;
	}

	public void owner() {
		double availability = calucAvailability("owner");
		ownerSA.setAvailability(cycle + 1, availability);
	}

	public void path() {
		double availability = calucAvailability("path");
		pathSA.setAvailability(cycle + 1, availability);
	}

	public void relate() {
		double availability = calucAvailability("relate");
		relateSA.setAvailability(cycle + 1, availability);
	}

	public void cuckoo() {
		double availability = calucAvailability("cuckoo");
		cuckooSA.setAvailability(cycle + 1, availability);
	}

	public boolean execute() {

		owner();
		path();
		cuckoo();
		relate();

		remainingValue = new ArrayList<Double>();
		for (int i = 0; i < 4; i++) {
			remainingSum.set(i, 0.0);
			remainingAverage.set(i, 0.0);
			remainingDistribution.set(i, 0.0);
			remainingCount.set(i, 0.0);

		}

		int allLinks = 0;
		for (int nodeID = 0; nodeID < Network.size(); nodeID++) {
			// 平均リンク数の測定用
			Node node = Network.get(nodeID);
			Link linkable = SharedResource.getLink(node);
			allLinks += linkable.degree();

			calcOccupancy(0, SharedResource.getNodeParameter("owner", Network.get(nodeID)),
					SharedResource.getNodeStorage("owner", Network.get(nodeID)));
			calcOccupancy(1, SharedResource.getNodeParameter("path", Network.get(nodeID)),
					SharedResource.getNodeStorage("path", Network.get(nodeID)));
			calcOccupancy(2, SharedResource.getNodeParameter("relate", Network.get(nodeID)),
					SharedResource.getNodeStorage("relate", Network.get(nodeID)));
			calcOccupancy(3, SharedResource.getNodeParameter("cuckoo", Network.get(nodeID)),
					SharedResource.getNodeStorage("cuckoo", Network.get(nodeID)));
		}
		output.writeAverageLinks(cycle, ((double) allLinks) / Network.size());

		for (int i = 0; i < 4; i++) {
			int count = remainingCount.get(i).intValue();
			for (int j = 0; j < count; j++) {
				remainingDistribution.set(i,
						remainingDistribution.get(i) + Math.pow(remainingValue.get(j) - remainingAverage.get(i), 2));
			}
			remainingValue.subList(0, count).clear();
		}

		for (int i = 0; i < 4; i++) {
			if (remainingCount.get(i) < 1.0 && remainingSum.get(i) < 1.0) {
				output.writeRemaining(i, cycle, capacity, 0.0);
			} else {
				output.writeRemaining(i, cycle, remainingAverage.get(i),
						Math.sqrt(remainingDistribution.get(i) / remainingCount.get(i)));
			}
			output.writeOccupancy(i, cycle, occupancy.get(i));
		}

		for (int id = 0; id < 4; id++) {
			sa.get(id).setRemaining(cycle + 1, remainingAverage.get(id));
			sa.get(id).setOccupancy(cycle + 1, occupancy.get(id));
		}

		networkCost();

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

			System.out.println("Low Owner occupancy: " + occupancy.get(0));
			System.out.println("Low Path occupancy: " + occupancy.get(1));
			System.out.println("Low Relate occupancy: " + occupancy.get(2));
			System.out.println("Low Cuckoo occupancy: " + occupancy.get(3));
			System.out.println();

			System.out.println("Cuckoo_Owner= " + (occupancy.get(3) / occupancy.get(0)));
			System.out.println("Relate_Owner = " + (occupancy.get(2) / occupancy.get(0)));
			System.out.println("Relate_Cuckoo = " + (occupancy.get(2) / occupancy.get(3)));
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