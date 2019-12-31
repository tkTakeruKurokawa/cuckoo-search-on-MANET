package research;

import peersim.core.*;
import peersim.config.*;
import java.util.*;

public class StoragePath implements Storage {
	private static final String PAR_PROT = "protocol";
	private static int pid_prm;

	private ArrayList<Data> dataList = new ArrayList<Data>();
	private static ArrayList<Integer> dataCounter;

	private static int dataTotal = 0;

	public StoragePath(String prefix) {
		pid_prm = Configuration.getPid(prefix + "." + PAR_PROT);
	}

	public Object clone() {
		StoragePath storage = null;
		try {
			storage = (StoragePath) super.clone();
		} catch (CloneNotSupportedException e) {
		}
		storage.dataList = new ArrayList<Data>(this.dataList.size());
		return storage;
	}

	public boolean setData(Node node, Data data) {
		NPPath parameter = (NPPath) node.getProtocol(pid_prm);
		int capacity = parameter.getCapacity();
		int occupancy = data.getSize();
		int newCapacity = capacity - occupancy;

		if (!dataList.contains(data) && (newCapacity >= 0)) {
			dataList.add(data);

			dataCounter = SharedResource.getCounter("path");
			dataCounter.set(data.getID(), dataCounter.get(data.getID()) + 1);
			SharedResource.setCounter("path", dataCounter);
			;

			parameter.setCapacity(newCapacity);

			dataTotal++;
			SharedResource.setDataTotal("path", dataTotal);

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
		dataCounter = SharedResource.getCounter("path");

		for (Data data : dataList) {
			dataCounter.set(data.getID(), dataCounter.get(data.getID()) - 1);
		}

		dataList = new ArrayList<Data>();

		SharedResource.setCounter("path", dataCounter);
		;
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