package research;

import peersim.config.*;
import peersim.core.*;
import java.util.*;

import java.io.*;
import java.lang.Math;

public class GetReplications implements Control {
	private static final String PAR_ALPHA = "alpha";
	private static double alpha;
	private static final String PAR_CAPACITY = "capacity";
	private static double capacity;

	private Random random = new Random();
	private ArrayList<Integer> nowRelate = new ArrayList<Integer>();
	private ArrayList<Integer> preRelate = new ArrayList<Integer>();
	private ArrayList<Integer> relateReplica = new ArrayList<Integer>();
	private ArrayList<Integer> nowCuckoo = new ArrayList<Integer>();
	private ArrayList<Integer> preCuckoo = new ArrayList<Integer>();
	private ArrayList<Integer> cuckooReplica = new ArrayList<Integer>();
	private ArrayList<Boolean> decreaseReplicaR = new ArrayList<Boolean>();
	private ArrayList<Boolean> decreaseReplicaC = new ArrayList<Boolean>();
	private ArrayList<Integer> startReplicaR = new ArrayList<Integer>();
	private ArrayList<Integer> startReplicaC = new ArrayList<Integer>();
	private ArrayList<Double> total = new ArrayList<Double>();
	private ArrayList<Double> hit = new ArrayList<Double>();
	private ArrayList<Integer> miss = new ArrayList<Integer>();
	private ArrayList<Integer> calcNum = new ArrayList<Integer>();
	private ArrayList<Data> cyclesRequestList;

	private Statistic statR = new Statistic();
	private Statistic statC = new Statistic();

	private PrintWriter counterO;
	private PrintWriter counterP;
	private PrintWriter counterR;
	private PrintWriter counterC;
	private PrintWriter occuO;
	private PrintWriter occuP;
	private PrintWriter occuR;
	private PrintWriter occuC;
	private PrintWriter compR;
	private PrintWriter compC;
	private PrintWriter hitO;
	private PrintWriter hitP;
	private PrintWriter hitR;
	private PrintWriter hitC;
	private PrintWriter rawO;
	private PrintWriter rawP;
	private PrintWriter rawR;
	private PrintWriter rawC;
	private PrintWriter startReplication;

	private int cycle = 0;
	private boolean done = false;

	public GetReplications(String prefix) {
		alpha = Configuration.getDouble(prefix + "." + PAR_ALPHA);
		capacity = Configuration.getDouble(prefix + "." + PAR_CAPACITY);

		for (int i = 0; i < Data.getMaxVariety(); i++) {
			nowRelate.add(i, 0);
			nowCuckoo.add(i, 0);
			preRelate.add(i, 0);
			preCuckoo.add(i, 0);
			relateReplica.add(i, -1);
			cuckooReplica.add(i, -1);
			decreaseReplicaR.add(i, false);
			decreaseReplicaC.add(i, false);
			startReplicaR.add(i, -1);
			startReplicaC.add(i, -1);
		}

		try {
			File dir = new File("result");
			if (!dir.exists()) {
				dir.mkdir();
			}

			String way = new File(".").getAbsoluteFile().getParent();

			String owner = way + "/result/counter_owner.tsv";
			File ownerR = new File(owner);
			owner = way + "/result/occupancy_owner.tsv";
			File ownerO = new File(owner);
			owner = way + "/result/hitRate_owner.tsv";
			File ownerH = new File(owner);
			owner = way + "/result/rawCounter_owner.tsv";
			File ownerRaw = new File(owner);

			String path = way + "/result/counter_path.tsv";
			File pathR = new File(path);
			path = way + "/result/occupancy_path.tsv";
			File pathO = new File(path);
			path = way + "/result/hitRate_path.tsv";
			File pathH = new File(path);
			path = way + "/result/rawCounter_path.tsv";
			File pathRaw = new File(path);

			String relate = way + "/result/counter_relate.tsv";
			File relateR = new File(relate);
			relate = way + "/result/occupancy_relate.tsv";
			File relateO = new File(relate);
			relate = way + "/result/hitRate_relate.tsv";
			File relateH = new File(relate);
			relate = way + "/result/compare_relate.tsv";
			File relateC = new File(relate);
			relate = way + "/result/rawCounter_relate.tsv";
			File relateRaw = new File(relate);

			String cuckoo = way + "/result/counter_cuckoo.tsv";
			File cuckooR = new File(cuckoo);
			cuckoo = way + "/result/occupancy_cuckoo.tsv";
			File cuckooO = new File(cuckoo);
			cuckoo = way + "/result/hitRate_cuckoo.tsv";
			File cuckooH = new File(cuckoo);
			cuckoo = way + "/result/compare_cuckoo.tsv";
			File cuckooC = new File(cuckoo);
			cuckoo = way + "/result/rawCounter_cuckoo.tsv";
			File cuckooRaw = new File(cuckoo);

			String start = way + "/result/startReplication.tsv";
			File startRep = new File(start);

			counterO = new PrintWriter(new BufferedWriter(new FileWriter(ownerR, true)));
			counterP = new PrintWriter(new BufferedWriter(new FileWriter(pathR, true)));
			counterR = new PrintWriter(new BufferedWriter(new FileWriter(relateR, true)));
			counterC = new PrintWriter(new BufferedWriter(new FileWriter(cuckooR, true)));

			occuO = new PrintWriter(new BufferedWriter(new FileWriter(ownerO, true)));
			occuP = new PrintWriter(new BufferedWriter(new FileWriter(pathO, true)));
			occuR = new PrintWriter(new BufferedWriter(new FileWriter(relateO, true)));
			occuC = new PrintWriter(new BufferedWriter(new FileWriter(cuckooO, true)));

			compR = new PrintWriter(new BufferedWriter(new FileWriter(relateC, true)));
			compC = new PrintWriter(new BufferedWriter(new FileWriter(cuckooC, true)));

			hitO = new PrintWriter(new BufferedWriter(new FileWriter(ownerH, true)));
			hitP = new PrintWriter(new BufferedWriter(new FileWriter(pathH, true)));
			hitR = new PrintWriter(new BufferedWriter(new FileWriter(relateH, true)));
			hitC = new PrintWriter(new BufferedWriter(new FileWriter(cuckooH, true)));

			rawO = new PrintWriter(new BufferedWriter(new FileWriter(ownerRaw, true)));
			rawP = new PrintWriter(new BufferedWriter(new FileWriter(pathRaw, true)));
			rawR = new PrintWriter(new BufferedWriter(new FileWriter(relateRaw, true)));
			rawC = new PrintWriter(new BufferedWriter(new FileWriter(cuckooRaw, true)));

			startReplication = new PrintWriter(new BufferedWriter(new FileWriter(startRep, true)));

		} catch (IOException e) {
			System.out.println(e);
		}
		for (int i = 0; i < 4; i++) {
			total.add(i, 0.0);
			hit.add(i, 0.0);
			miss.add(i, 0);
			calcNum.add(i, 0);
		}

		rawO = setRawComments(rawO);
		counterO = setAvailavilityComments(counterO);
		rawP = setRawComments(rawP);
		counterP = setAvailavilityComments(counterP);
		rawR = setRawComments(rawR);
		counterR = setAvailavilityComments(counterR);
		rawC = setRawComments(rawC);
		counterC = setAvailavilityComments(counterC);
	}

	public void closeFiles() {
		compR = statR.output(compR);
		compC = statC.output(compC);

		counterO.println();
		counterP.println();
		counterR.println();
		counterC.println();
		occuO.println();
		occuP.println();
		occuR.println();
		occuC.println();
		compR.println();
		compC.println();
		startReplication.println();

		counterO.close();
		counterP.close();
		counterR.close();
		counterC.close();
		occuO.close();
		occuP.close();
		occuR.close();
		occuC.close();
		hitO.close();
		hitP.close();
		hitR.close();
		hitC.close();
		rawO.close();
		rawP.close();
		rawR.close();
		rawC.close();
		compR.close();
		compC.close();
		startReplication.close();
	}

	public void calcHitRate() {
		hitO.println("Number of Hit\tAverage Hops\tNumber of Miss");
		hitO.println(calcNum.get(0) + "\t" + hit.get(0) / ((double) calcNum.get(0)) + "\t" + miss.get(0));
		hitP.println("Number of Hit\tAverage Hops\tNumber of Miss");
		hitP.println(calcNum.get(1) + "\t" + hit.get(1) / ((double) calcNum.get(1)) + "\t" + miss.get(1));
		hitR.println("Number of Hit\tAverage Hops\tNumber of Miss");
		hitR.println(calcNum.get(2) + "\t" + hit.get(2) / ((double) calcNum.get(2)) + "\t" + miss.get(2));
		hitC.println("Number of Hit\tAverage Hops\tNumber of Miss");
		hitC.println(calcNum.get(3) + "\t" + hit.get(3) / ((double) calcNum.get(3)) + "\t" + miss.get(3));
	}

	public boolean exists(Data data, int id) {
		int num = 0;
		switch (id) {
		case 0:
			ArrayList<Integer> dcOwner = SharedResource.getOwnerCounter();
			num = dcOwner.get(data.getID());
			break;
		case 1:
			ArrayList<Integer> dcPath = SharedResource.getPathCounter();
			num = dcPath.get(data.getID());
			break;
		case 2:
			ArrayList<Integer> dcRelate = SharedResource.getRelateCounter();
			num = dcRelate.get(data.getID());
			break;
		case 3:
			ArrayList<Integer> dcCuckoo = SharedResource.getCuckooCounter();
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

		if (!exists(data, id)) {
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
		ArrayList<Boolean> upLoaded = SharedResource.getUpLoaded();
		int count = 0;
		for (int i = 0; i < Data.getNowVariety(); i++) {
			if (Objects.equals(upLoaded.get(i), true)) {
				count++;
			}
		}

		if (count != 0) {
			int nodeID = random.nextInt(Network.size());
			int dataID = random.nextInt(count);
			for (int i = 0; i < 4; i++) {
				hitOrMiss(Network.get(nodeID), Data.getData(dataID), i);
			}
		}
	}

	public void calcOccupancy(Parameter parameter, int id) {
		// double occupancy = ((double)parameter.getCapacity()) / capacity;
		// total.set(id, total.get(id)+occupancy);
		int occupancy = (int) capacity - parameter.getCapacity();
		total.set(id, total.get(id) + occupancy);
	}

	public void setOccupancy() {
		if (cycle == 0) {
			occuO.printf("cycle\toccupancy\n");
			occuP.printf("cycle\toccupancy\n");
			occuR.printf("cycle\toccupancy\n");
			occuC.printf("cycle\toccupancy\n");
		}

		// for(int i=0; i<4; i++){
		// total.set(i, 0.0d);
		// }
		for (int i = 0; i < Network.size(); i++) {
			calcOccupancy(SharedResource.getNPOwner(Network.get(i)), 0);
			calcOccupancy(SharedResource.getNPPath(Network.get(i)), 1);
			calcOccupancy(SharedResource.getNPRelate(Network.get(i)), 2);
			calcOccupancy(SharedResource.getNPCuckoo(Network.get(i)), 3);
		}
		// occuO.println(cycle + "\t" + (total.get(0)/(double)Network.size()));
		// occuP.println(cycle + "\t" + (total.get(1)/(double)Network.size()));
		// occuR.println(cycle + "\t" + (total.get(2)/(double)Network.size()));
		// occuC.println(cycle + "\t" + (total.get(3)/(double)Network.size()));
		occuO.println(cycle + "\t" + total.get(0));
		occuP.println(cycle + "\t" + total.get(1));
		occuR.println(cycle + "\t" + total.get(2));
		occuC.println(cycle + "\t" + total.get(3));
	}

	public PrintWriter setRawComments(PrintWriter pw) {

		pw.println("Cycle\tNow Replications\tTotal Replications");
		pw.println();

		return pw;
	}

	public PrintWriter setAvailavilityComments(PrintWriter pw) {

		pw.printf("Cycle\tData Availavility");
		pw.println();

		return pw;
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

	public void relatedResearch(int dataID, int dataNum) {
		Data data = Data.getData(dataID);
		int diff = relateReplica.get(dataID) - dataNum;
		if (dataID % 5 == 0 && dataID % 10 != 0) {
			// System.out.println("Data "+ dataID);
			// System.out.println("\trelateReplica " + relateReplica.get(dataID) + " dataNum
			// " + dataNum);
		}

		int addNum = 0;
		Node node;
		while (addNum < diff) {
			do {
				node = Network.get(random.nextInt(Network.size()));
				node = RelatedResearch.getBestNode(node, data);
			} while (Objects.equals(node, null));

			StorageRelate storage = SharedResource.getSRelate(node);
			NPRelate parameter = SharedResource.getNPRelate(node);
			boolean success = check(storage, parameter, data);

			if (!success) {
				continue;
			}

			if (done) {
				statR.set(parameter);
			}
			storage.setReplica(node, data);

			addNum++;
		}
	}

	public void cuckooSearch(int dataID, int dataNum) {
		Data data = Data.getData(dataID);
		int diff = cuckooReplica.get(dataID) - dataNum;
		if (dataID % 5 == 0 && dataID % 10 != 0) {
			// System.out.println("Data "+ dataID);
			// System.out.println("cuckooReplica " + cuckooReplica.get(dataID) + " dataNum "
			// + dataNum);
		}
		int addNum = 0;
		Node node;
		// System.out.println("Add Num: " + diff);
		while (addNum < diff) {
			node = CuckooSearch.search(data);
			if (Objects.equals(node, null)) {
				continue;
			}
			// System.out.println("CS Node: " + node);

			Parameter parameter = SharedResource.getNPCuckoo(node);
			statC.set(parameter);
			done = true;

			StorageCuckoo storage = SharedResource.getSCuckoo(node);
			storage.setReplica(node, data);
			addNum++;
		}
	}

	public void owner() {
		ArrayList<Integer> dataCounter = SharedResource.getOwnerCounter();

		counterO.printf("%d\t", cycle);
		int totalNum = 0;
		double availability = 0.0;
		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			Integer dataNum = dataCounter.get(dataID);
			// System.out.println("Data: " + dataID + "\tNum: " + dataNum);
			if (dataNum == null) {
				dataNum = 0;
			}

			totalNum += dataNum;
		}

		int all = SharedResource.getTotal("owner");
		availability = ((double) totalNum) / ((double) all);

		rawO.println(cycle + "\t" + totalNum + "\t" + all);
		counterO.println(availability);
	}

	public void path() {
		ArrayList<Integer> dataCounter = SharedResource.getPathCounter();

		counterP.printf("%d\t", cycle);
		int totalNum = 0;
		double availability = 0.0;
		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			Integer dataNum = dataCounter.get(dataID);

			if (dataNum == null) {
				dataNum = 0;
			}

			totalNum += dataNum;
		}

		int all = SharedResource.getTotal("path");
		availability = ((double) totalNum) / ((double) all);
		// System.out.println();
		// System.out.println("Availability: " + availability);
		// System.out.println("Now Replicas: " + totalNum);
		// System.out.println("total Replicas: " + all);

		rawP.println(cycle + "\t" + totalNum + "\t" + all);
		counterP.println(availability);
	}

	public void relate() {
		ArrayList<Integer> dataCounter = SharedResource.getRelateCounter();
		ArrayList<Integer> replicaCounter = SharedResource.getReplicaCounterR();

		ArrayList<Double> relateOccu = SharedResource.getRelateOccu();
		double sum = 0.0;
		for (int i = 0; i < Network.size(); i++) {
			Parameter parameter = SharedResource.getNPRelate(Network.get(i));
			double occupancy = ((double) parameter.getCapacity()) / capacity;

			sum += occupancy;
			relateOccu.set(i, relateOccu.get(i) + occupancy);
		}
		SharedResource.setRelateOccu(relateOccu);

		int total = 0;
		for (int i = 0; i < Data.getNowVariety(); i++) {
			total += dataCounter.get(i);
		}

		counterR.printf("%d\t", cycle);
		int totalNum = 0;
		double availability = 0.0;
		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			Integer dataNum = dataCounter.get(dataID);

			nowRelate.set(dataID, dataNum);
			if (preRelate.get(dataID) > nowRelate.get(dataID)) {
				decreaseReplicaR.set(dataID, true);
			}
			if (decreaseReplicaR.get(dataID) && cyclesRequestList.contains(Data.getData(dataID))) {
				startReplicaR.set(dataID, -1);
				decreaseReplicaR.set(dataID, false);
			}

			if (dataNum < total * 0.05) {
				if (decreaseReplicaR.get(dataID)) {
					if (startReplicaR.get(dataID) < 0) {
						int num = 5;
						relateReplica.set(dataID, num);
						startReplicaR.set(dataID, cycle);
					}
				}
			}

			if (0 <= startReplicaR.get(dataID)) {
				relatedResearch(dataID, replicaCounter.get(dataID));
				dataCounter = SharedResource.getRelateCounter();
			}

			preRelate.set(dataID, nowRelate.get(dataID));

			totalNum += (int) dataCounter.get(dataID);
		}

		int all = SharedResource.getTotal("relate");
		availability = ((double) totalNum) / ((double) all);

		rawR.println(cycle + "\t" + totalNum + "\t" + all);
		counterR.println(availability);
	}

	public void cuckoo() {
		ArrayList<Integer> dataCounter = SharedResource.getCuckooCounter();
		ArrayList<Integer> replicaCounter = SharedResource.getReplicaCounterC();

		double sum = 0.0;
		ArrayList<Double> cuckooOccu = SharedResource.getCuckooOccu();
		for (int i = 0; i < Network.size(); i++) {
			Parameter parameter = SharedResource.getNPCuckoo(Network.get(i));
			double occupancy = ((double) parameter.getCapacity()) / capacity;

			sum += occupancy;
			cuckooOccu.set(i, cuckooOccu.get(i) + occupancy);

			Node node = Network.get(i);
		}
		SharedResource.setCuckooOccu(cuckooOccu);

		int total = 0;
		for (int i = 0; i < Data.getNowVariety(); i++) {
			total += dataCounter.get(i);
		}
		// System.out.println(total*0.05);

		counterC.printf("%d\t", cycle);
		int totalNum = 0;
		double availability = 0.0;
		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			Integer dataNum = dataCounter.get(dataID);

			nowCuckoo.set(dataID, dataNum);
			// 現在のデータの数が前回のデータの数より少なくなった場合
			if (preCuckoo.get(dataID) > nowCuckoo.get(dataID)) {
				decreaseReplicaC.set(dataID, true);
			}
			// 減少中であるのもかかわらず、現在のデータ数が前回のデータ数を上回った場合
			// ただし、複製配置で増えた分は除く
			if (cyclesRequestList.get(dataID) != null) {
				// System.out.println("This cycle request Data " +
				// cyclesRequestList.get(dataID).getID());

			}
			if (decreaseReplicaC.get(dataID) && cyclesRequestList.contains(Data.getData(dataID))) {
				startReplicaC.set(dataID, -1);
				decreaseReplicaC.set(dataID, false);
			}

			if (dataNum < total * 0.05) {
				if (decreaseReplicaC.get(dataID)) {
					if (startReplicaC.get(dataID) < 0) {
						int num = 5;
						cuckooReplica.set(dataID, num);
						startReplicaC.set(dataID, cycle);
					}
				}
			}

			// System.out.println("Data " + dataID + " replica : " +
			// replicaCounter.get(dataID));

			if (0 <= startReplicaC.get(dataID) && (cycle - startReplicaC.get(dataID)) < 100) {
				// System.out.println("befor Num " + dataCounter.get(dataID));
				cuckooSearch(dataID, replicaCounter.get(dataID));
				dataCounter = SharedResource.getCuckooCounter();
				// System.out.println("after Num " + dataCounter.get(dataID));
				// System.out.println();
			}

			preCuckoo.set(dataID, nowCuckoo.get(dataID));

			if (dataNum == null)
				dataNum = 0;

			totalNum += (int) dataCounter.get(dataID);
		}

		int all = SharedResource.getTotal("cuckoo");
		availability = ((double) totalNum) / ((double) all);

		rawC.println(cycle + "\t" + totalNum + "\t" + all);
		counterC.println(availability);
	}

	public boolean execute() {
		cyclesRequestList = SharedResource.getCyclesRequestList();

		owner();
		path();
		cuckoo();
		relate();

		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			cyclesRequestList.set(dataID, null);
		}
		SharedResource.setCyclesRequestList(cyclesRequestList);

		setOccupancy();
		hitRate();

		done = false;
		cycle++;

		if (cycle == 500) {

			for (int i = 0; i < Data.getMaxVariety(); i++) {
				startReplication.println("Data " + i + " start Replication cycle");
				startReplication.println("Relate: " + startReplicaR.get(i) + " Cuckoo: " + startReplicaC.get(i));
			}
			calcHitRate();
			closeFiles();
			System.out.println("Final Nodes: " + Network.size());
			System.out.println("Owner occupancy: " + total.get(0));
			System.out.println("Path occupancy: " + total.get(1));
			System.out.println("Relate occupancy: " + total.get(2));
			System.out.println("Cuckoo occupancy: " + total.get(3));

			// System.out.println("Cuckoo_Owner= " + (1.0 - (total.get(0) / total.get(3))));
			// System.out.println("Relate_Owner = " + (1.0 - (total.get(0) /
			// total.get(2))));
			// System.out.println("Relate_Cuckoo = " + (1.0 - (total.get(3) /
			// total.get(2))));

			// System.out.println("Storage Occupancy");
			// System.out.println("\tCuckoo-Owner= " + (total.get(3) - total.get(0)));
			// System.out.println("\tRelate-Owner= " + (total.get(2) - total.get(0)));
			// System.out.println("\tRelate-Cuckoo= " + (total.get(2) - total.get(3)));
		}

		return false;
	}

}