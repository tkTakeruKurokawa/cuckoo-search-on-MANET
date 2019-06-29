package research;

import peersim.cdsim.*;
import peersim.core.*;
import peersim.config.*;
import java.util.*;

public class StorageOwner implements Storage {
	private static final String PAR_PROT = "protocol";
	private static int pid_prm;

	private ArrayList<Data> dataList = new ArrayList<Data>();
	private HashMap<Data, Integer> dataTTL = new HashMap<Data, Integer>();
	private static ArrayList<Integer> dataCounter;
	private static Deque<Data> removeList = new ArrayDeque<Data>();

	private static int totalReplica = 0;
	private int ttl;

	public StorageOwner(String prefix) {
		pid_prm = Configuration.getPid(prefix + "." + PAR_PROT);
	}

	public Object clone() {
		StorageOwner storage = null;
		try {
			storage = (StorageOwner) super.clone();
		} catch (CloneNotSupportedException e) {
		}
		storage.dataList = new ArrayList<Data>(this.dataList.size());
		storage.dataTTL = new HashMap<Data, Integer>(this.dataTTL.size());
		return storage;
	}

	// nodeのstorageにdataを追加
	public boolean setData(Node node, Data data) {
		NPOwner parameter = (NPOwner) node.getProtocol(pid_prm);
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

			dataCounter = SharedResource.getOwnerCounter();
			dataCounter.set(data.getID(), dataCounter.get(data.getID()) + 1);
			SharedResource.setOwnerCounter(dataCounter);

			parameter.setCapacity(newCapacity);

			totalReplica++;
			SharedResource.setTotal("owner", totalReplica);
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
		dataCounter = SharedResource.getOwnerCounter();

		for (Data data : dataList) {
			dataCounter.set(data.getID(), dataCounter.get(data.getID()) - 1);
		}

		dataList = new ArrayList<Data>();
		SharedResource.setOwnerCounter(dataCounter);
	}

	public void removeData(Node node) {
		while (removeList.size() > 0) {
			Data data = removeList.removeFirst();

			dataCounter = SharedResource.getOwnerCounter();

			NPOwner parameter = (NPOwner) node.getProtocol(pid_prm);
			int capacity = parameter.getCapacity();
			int occupancy = data.getSize();
			int newCapacity = capacity + occupancy;

			dataCounter.set(data.getID(), dataCounter.get(data.getID()) - 1);
			dataTTL.remove(data);
			dataList.remove(data);

			SharedResource.setOwnerCounter(dataCounter);
			parameter.setCapacity(newCapacity);
		}
	}

	public void reduceTTL(Node node) {
		// System.out.println("Node ID: " + node.getIndex());
		for (int i = 0; i < dataList.size(); i++) {
			Data data = dataList.get(i);
			ttl = dataTTL.get(data);
			// System.out.println(" Data :" + data.getID() + " TTL :" + ttl);
			if (ttl > 0) {
				dataTTL.put(data, ttl - 1);
			}
			if (ttl == 0) {
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