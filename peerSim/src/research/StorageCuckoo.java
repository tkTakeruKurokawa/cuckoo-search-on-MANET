package research;

import peersim.cdsim.*;
import peersim.core.*;
import peersim.config.*;
import java.util.*;

public class StorageCuckoo implements Storage {
	private static final String PAR_PROT = "protocol";
	private static int pid_prm;

	private ArrayList<Data> dataList = new ArrayList<Data>();
	private ArrayList<Data> replicaList = new ArrayList<Data>();
	private HashMap<Data, Integer> dataTTL = new HashMap<Data, Integer>();
	private HashMap<Data, Integer> replicaTTL = new HashMap<Data, Integer>();
	private static ArrayList<Integer> dataCounter;
	private static ArrayList<Integer> replicaCounter;
	private static Deque<Data> removeList = new ArrayDeque<Data>();

	private static Random random = new Random();
	private static int totalReplica = 0;
	private int ttl;

	public StorageCuckoo(String prefix) {
		pid_prm = Configuration.getPid(prefix + "." + PAR_PROT);
	}

	public Object clone() {
		StorageCuckoo storage = null;
		try {
			storage = (StorageCuckoo) super.clone();
		} catch (CloneNotSupportedException e) {
		}
		storage.dataList = new ArrayList<Data>(this.dataList.size());
		storage.dataTTL = new HashMap<Data, Integer>(this.dataTTL.size());
		storage.replicaList = new ArrayList<Data>(this.replicaList.size());
		storage.replicaTTL = new HashMap<Data, Integer>(this.replicaTTL.size());
		return storage;
	}

	public boolean setReplica(Node node, Data data) {
		NPCuckoo parameter = (NPCuckoo) node.getProtocol(pid_prm);
		int capacity = parameter.getCapacity();
		int occupancy = data.getSize();
		int newCapacity = capacity - occupancy;

		if (!dataList.contains(data) && (newCapacity >= 0)) {
			dataList.add(data);
			replicaList.add(data);

			ttl = 50 + random.nextInt(10);
			dataTTL.put(data, ttl);
			replicaTTL.put(data, ttl);

			dataCounter = SharedResource.getCuckooCounter();
			replicaCounter = SharedResource.getReplicaCounterC();
			dataCounter.set(data.getID(), dataCounter.get(data.getID()) + 1);
			replicaCounter.set(data.getID(), replicaCounter.get(data.getID()) + 1);
			SharedResource.setCuckooCounter(dataCounter);
			SharedResource.setReplicaCounterC(replicaCounter);

			parameter.setCapacity(newCapacity);

			totalReplica++;
			SharedResource.setTotal("cuckoo", totalReplica);
			return true;
		}

		return false;
	}

	// nodeのstorageにdataを追加
	public boolean setData(Node node, Data data) {
		NPCuckoo parameter = (NPCuckoo) node.getProtocol(pid_prm);
		int capacity = parameter.getCapacity();
		int occupancy = data.getSize();
		int newCapacity = capacity - occupancy;

		if (!dataList.contains(data) && (newCapacity >= 0)) {
			dataList.add(data);
			// dataTTL.put(data, data.getPeakCycle());
			if (data.getNowCycle() > data.getPeakCycle())
				ttl = SharedResource.getTTL(data.getNowCycle());
			else
				ttl = SharedResource.getTTL(data.getPeakCycle());
			dataTTL.put(data, ttl);

			dataCounter = SharedResource.getCuckooCounter();
			dataCounter.set(data.getID(), dataCounter.get(data.getID()) + 1);
			SharedResource.setCuckooCounter(dataCounter);

			parameter.setCapacity(newCapacity);

			totalReplica++;
			SharedResource.setTotal("cuckoo", totalReplica);
			return true;
		}
		return false;
	}

	public boolean isEmpty() {
		return dataList.isEmpty();
	}

	public boolean contains(Data data) {
		return dataList.contains(data);
	}

	public ArrayList<Data> getData() {
		return dataList;
	}

	public int getTotal() {
		return totalReplica;
	}

	public void clear() {
		dataCounter = SharedResource.getCuckooCounter();
		replicaCounter = SharedResource.getReplicaCounterC();

		for (Data data : dataList) {
			dataCounter.set(data.getID(), dataCounter.get(data.getID()) - 1);
		}
		for (Data data : replicaList) {
			replicaCounter.set(data.getID(), replicaCounter.get(data.getID()) - 1);
		}

		dataList = new ArrayList<Data>();
		replicaList = new ArrayList<Data>();

		SharedResource.setCuckooCounter(dataCounter);
		SharedResource.setReplicaCounterC(replicaCounter);
	}

	public void removeData(Node node) {
		while (removeList.size() > 0) {
			Data data = removeList.removeFirst();

			dataCounter = SharedResource.getCuckooCounter();

			NPCuckoo parameter = (NPCuckoo) node.getProtocol(pid_prm);
			int capacity = parameter.getCapacity();
			int occupancy = data.getSize();
			int newCapacity = capacity + occupancy;

			dataCounter.set(data.getID(), dataCounter.get(data.getID()) - 1);
			dataTTL.remove(data);
			dataList.remove(data);

			SharedResource.setCuckooCounter(dataCounter);
			parameter.setCapacity(newCapacity);
		}
	}

	public void removeReplica() {
		while (removeList.size() > 0) {
			Data data = removeList.removeFirst();

			replicaCounter = SharedResource.getReplicaCounterC();

			replicaCounter.set(data.getID(), replicaCounter.get(data.getID()) - 1);
			replicaTTL.remove(data);
			replicaList.remove(data);

			SharedResource.setReplicaCounterC(replicaCounter);
		}
	}

	public void reduceReplicaTTL(Node node) {
		for (int i = 0; i < replicaList.size(); i++) {
			Data replica = replicaList.get(i);
			ttl = replicaTTL.get(replica);

			if (ttl > 0) {
				replicaTTL.put(replica, ttl - 1);
			}
			if (ttl == 0) {
				// removeReplica(node, replica);
				removeList.addFirst(replica);
			}
		}

		removeReplica();
	}

	public void reduceTTL(Node node) {
		for (int i = 0; i < dataList.size(); i++) {
			Data data = dataList.get(i);
			ttl = dataTTL.get(data);

			if (ttl > 0) {
				dataTTL.put(data, ttl - 1);
			}
			if (ttl == 0) {
				// removeData(node, data);
				removeList.addFirst(data);
			}
		}

		removeData(node);
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("size=" + dataList.size() + " [");
		for (int i = 0; i < dataList.size(); ++i) {
			buffer.append(dataList.get(i).getID() + " ");
		}
		return buffer.append("]").toString();
	}
}