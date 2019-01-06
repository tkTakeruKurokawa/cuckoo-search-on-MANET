package research;

import peersim.cdsim.*;
import peersim.core.*;
import peersim.config.*;
import java.util.*;

public class StorageCuckoo implements Storage{
	private static final String PAR_PROT = "protocol";
	private static int pid_prm;

	private ArrayList<Data> dataList = new ArrayList<Data>();
	private HashMap<Data, Integer> dataTTL = new HashMap<Data, Integer>();
	private static ArrayList<Integer> dataCounter;
	private static ArrayList<Node> replica = new ArrayList<Node>();

	private static Random random = new Random();
	private int ttl;


	public StorageCuckoo(String prefix){
		pid_prm = Configuration.getPid(prefix + "." + PAR_PROT);
	}

	public Object clone(){
		StorageCuckoo storage = null;
		try{
			storage = (StorageCuckoo) super.clone();
		}catch(CloneNotSupportedException e){
		}
		storage.dataList = new ArrayList<Data>(this.dataList.size());
		storage.dataTTL = new HashMap<Data, Integer>(this.dataTTL.size());
		return storage;
	}
	

	public boolean setReplica(Node node, Data data){
		NPCuckoo parameter = (NPCuckoo) node.getProtocol(pid_prm);
		int capacity = parameter.getCapacity();
		int occupancy = data.getSize();
		int newCapacity = capacity-occupancy;

		if(!dataList.contains(data) && (newCapacity>=0)){
			dataList.add(data);

			ttl = SharedResource.getTTL(50);
			dataTTL.put(data, ttl);

			dataCounter = SharedResource.getCuckooCounter();
			dataCounter.set(data.getID(), dataCounter.get(data.getID())+1);
			SharedResource.setCuckooCounter(dataCounter);

			parameter.setCapacity(newCapacity);

			if(!replica.contains(node)){
				replica.add(node);
			}

			return true;
		}
		return false;
	}

	//nodeのstorageにdataを追加
	public boolean setData(Node node, Data data){
		NPCuckoo parameter = (NPCuckoo) node.getProtocol(pid_prm);
		int capacity = parameter.getCapacity();
		int occupancy = data.getSize();
		int newCapacity = capacity-occupancy;

		if(!dataList.contains(data) && (newCapacity>=0)){
			dataList.add(data);
			// dataTTL.put(data, data.getPeakCycle());
			if(data.getNowCycle()>data.getPeakCycle())
				ttl = SharedResource.getTTL(data.getNowCycle());
			else
				ttl = SharedResource.getTTL(data.getPeakCycle());
			dataTTL.put(data, ttl);

			dataCounter = SharedResource.getCuckooCounter();
			dataCounter.set(data.getID(), dataCounter.get(data.getID())+1);
			SharedResource.setCuckooCounter(dataCounter);

			parameter.setCapacity(newCapacity);
			// System.out.println("Node " + node.getIndex() + " capacity: " + newCapacity);

			if(replica.contains(node)){
				IncreaseReplications.replicaC++;
			}

			return true;
		}
		// System.out.println("*****  fail to setData. Re Roll   *****");
		return false;
	}

	public boolean isEmpty(){
		return dataList.isEmpty();
	}

	public boolean contains(Data data){
		return dataList.contains(data);
	}

	public ArrayList<Data> getData(){
		return dataList;
	}

	public void clear(){
		for(Data data: dataList){
			dataCounter.set(data.getID(), dataCounter.get(data.getID())-1);	
		}

		dataCounter = SharedResource.getCuckooCounter();
		dataList.clear();
		SharedResource.setCuckooCounter(dataCounter);
	}

	public void removeData(Node node, Data data){
		dataCounter = SharedResource.getCuckooCounter();

		NPCuckoo parameter = (NPCuckoo) node.getProtocol(pid_prm);
		int capacity = parameter.getCapacity();
		int occupancy = data.getSize();
		int newCapacity = capacity+occupancy;	

		Iterator<Data> itr = dataList.iterator();
		while(itr.hasNext()){
			Data currentData = itr.next();
			if(Objects.equals(currentData, data)) {
				dataCounter.set(data.getID(), dataCounter.get(data.getID())-1);
				dataTTL.remove(currentData);
				itr.remove();
			}
		}
		SharedResource.setCuckooCounter(dataCounter);
		parameter.setCapacity(newCapacity);
		// System.out.println("Node " + node.getIndex() + " capacity: " + newCapacity);
		
	}

	public void reduceTTL(Node node){
		// System.out.println("Node ID: " + node.getIndex());
		for(int i=0; i<dataList.size(); i++){
			Data data = dataList.get(i);
			ttl = dataTTL.get(data);
			// System.out.println("   Data :" + data.getID() + " TTL :" + ttl);
			if(ttl > 0) dataTTL.put(data, ttl-1); 
			if(ttl == 0) removeData(node, data);
		}
	}


	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("size=" + dataList.size() + " [");
		for (int i = 0; i < dataList.size(); ++i) {
			buffer.append(dataList.get(i).getID() + " ");
		}
		return buffer.append("]").toString();
	}
}