package research;

import peersim.core.*;
import peersim.config.*;
import java.util.*;

public class StorageCuckoo implements Storage {
	private static final String PAR_PROT = "protocol";
	private static int pid_prm;

	private ArrayList<Data> dataList = new ArrayList<Data>();
	private static ArrayList<Integer> highDataCounter;
	private static ArrayList<Integer> lowDataCounter;

	private static int highTotal = 0;
	private static int lowTotal = 0;

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
		return storage;
	}

	public boolean setLowDemandData(Node node, Data data) {
		NPCuckoo parameter = (NPCuckoo) node.getProtocol(pid_prm);
		int capacity = parameter.getCapacity();
		int occupancy = data.getSize();
		int newCapacity = capacity - occupancy;

		if (!dataList.contains(data) && (newCapacity >= 0)) {
			OutPut.writeCompare("cuckoo", parameter);

			dataList.add(data);

			lowDataCounter = SharedResource.getCuckooLowCounter();
			lowDataCounter.set(data.getID(), lowDataCounter.get(data.getID()) + 1);
			SharedResource.setCuckooLowCounter(lowDataCounter);

			parameter.setCapacity(newCapacity);

			lowTotal++;
			SharedResource.setLowTotal("cuckoo", lowTotal);

			return true;
		}

		return false;
	}

	// nodeのstorageにdataを追加
	public boolean setHighDemandData(Node node, Data data) {
		NPCuckoo parameter = (NPCuckoo) node.getProtocol(pid_prm);
		int capacity = parameter.getCapacity();
		int occupancy = data.getSize();
		int newCapacity = capacity - occupancy;

		if (!dataList.contains(data) && (newCapacity >= 0)) {
			dataList.add(data);

			highDataCounter = SharedResource.getCuckooHighCounter();
			highDataCounter.set(data.getID(), highDataCounter.get(data.getID()) + 1);
			SharedResource.setCuckooHighCounter(highDataCounter);

			parameter.setCapacity(newCapacity);

			highTotal++;
			SharedResource.setHighTotal("cuckoo", highTotal);
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

	public void clear() {
		highDataCounter = SharedResource.getCuckooHighCounter();
		lowDataCounter = SharedResource.getCuckooLowCounter();

		for (Data data : dataList) {
			if (Objects.equals(data.getType(), "high")) {
				highDataCounter.set(data.getID(), highDataCounter.get(data.getID()) - 1);
			} else {
				lowDataCounter.set(data.getID(), lowDataCounter.get(data.getID()) - 1);
			}
		}

		dataList = new ArrayList<Data>();

		SharedResource.setCuckooHighCounter(highDataCounter);
		SharedResource.setCuckooLowCounter(lowDataCounter);
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