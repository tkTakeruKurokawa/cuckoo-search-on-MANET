package research;

import peersim.cdsim.*;
import peersim.core.*;
import peersim.config.*;
import java.util.*;

public class StorageCuckoo implements Storage{
	private static final String PAR_PROT = "protocol";
	private static int pid_prm;

	private ArrayList<Data> dataList = new ArrayList<Data>();
	private ArrayList<Data> replicaList = new ArrayList<Data>();
	private HashMap<Data, Integer> dataTTL = new HashMap<Data, Integer>();
	private HashMap<Data, Integer> replicaTTL = new HashMap<Data, Integer>();
	private static ArrayList<Integer> dataCounter;
	private static ArrayList<Integer> replicaCounter;

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
		storage.replicaList = new ArrayList<Data>(this.replicaList.size());
		storage.replicaTTL = new HashMap<Data, Integer>(this.replicaTTL.size());
		return storage;
	}
	

	public boolean setReplica(Node node, Data data){
		NPCuckoo parameter = (NPCuckoo) node.getProtocol(pid_prm);
		int capacity = parameter.getCapacity();
		int occupancy = data.getSize();
		int newCapacity = capacity-occupancy;

		if(!dataList.contains(data) && (newCapacity>=0)){
			dataList.add(data);
			replicaList.add(data);

			ttl = SharedResource.getTTL(50);
			dataTTL.put(data, ttl);
			replicaTTL.put(data, ttl);

			dataCounter = SharedResource.getCuckooCounter();
			replicaCounter = SharedResource.getReplicaCounter();
			dataCounter.set(data.getID(), dataCounter.get(data.getID())+1);
			replicaCounter.set(data.getID(), replicaCounter.get(data.getID())+1);
			SharedResource.setCuckooCounter(dataCounter);
			SharedResource.setReplicaCounter(replicaCounter);

			parameter.setCapacity(newCapacity);

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
		dataCounter = SharedResource.getCuckooCounter();
		replicaCounter = SharedResource.getReplicaCounter();

		for(Data data: dataList){
			dataCounter.set(data.getID(), dataCounter.get(data.getID())-1);	
		}
		for(Data data: replicaList){
			replicaCounter.set(data.getID(), replicaCounter.get(data.getID())-1);	
		}

		dataList.clear();
		replicaList.clear();

		SharedResource.setCuckooCounter(dataCounter);
		SharedResource.setReplicaCounter(replicaCounter);
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
	}

	public void removeReplica(Node node, Data data){
		replicaCounter = SharedResource.getReplicaCounter();

		Iterator<Data> itr = replicaList.iterator();
		while(itr.hasNext()){
			Data currentData = itr.next();
			if(Objects.equals(currentData, data)) {
				replicaCounter.set(data.getID(), replicaCounter.get(data.getID())-1);
				replicaTTL.remove(currentData);
				itr.remove();
			}
		}
		SharedResource.setReplicaCounter(replicaCounter);
	}

	public void reduceTTL(Node node){
		for(int i=0; i<dataList.size(); i++){
			Data data = dataList.get(i);
			ttl = dataTTL.get(data);
			if(ttl > 0) dataTTL.put(data, ttl-1); 
			if(ttl == 0) removeData(node, data);

		}

		for(int i=0; i<replicaList.size(); i++){
			Data replica = replicaList.get(i);
			ttl = replicaTTL.get(replica);
			if(ttl > 0) replicaTTL.put(replica, ttl-1); 
			if(ttl == 0) removeReplica(node, replica);
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