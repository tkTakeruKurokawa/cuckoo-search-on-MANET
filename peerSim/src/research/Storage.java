package research;

import peersim.cdsim.*;
import peersim.core.*;
import java.util.*;

public class Storage implements Protocol{

	private ArrayList<Data> dataList = new ArrayList<Data>();
	private HashMap<Data, Integer> dataTTL = new HashMap<Data, Integer>();
	private static ArrayList<Integer> dataCounter = new ArrayList<Integer>();


	public Storage(){
		System.out.println(Data.getVariety());
		for(int i=0; i<Data.getVariety(); i++)
			dataCounter.add(i, 0);
	}

	public Storage(String prefix){
	}

	public Object clone(){
		Storage storage = null;
		try{
			storage = (Storage) super.clone();
		}catch(CloneNotSupportedException e){
		}
		storage.dataList = new ArrayList<Data>(this.dataList.size());
		storage.dataTTL = new HashMap<Data, Integer>(this.dataTTL.size());
		return storage;
	}

	public boolean setData(Data data){
		if(!dataList.contains(data)){
			dataList.add(data);
			dataTTL.put(data, data.getPeakCycle());
			dataCounter.set(data.getID(), dataCounter.get(data.getID())+1);
			ControlTest.setDataCounter(dataCounter);
			return true;
		}
		System.out.println("*****  fail to setData. Re Roll   *****");
		return false;
	}

	public ArrayList<Data> getData(){
		return dataList;
	}

	public void removeData(Data data){
		Iterator<Data> itr = getData().iterator();
		while(itr.hasNext()){
			Data currentData = itr.next();
			if(Objects.equals(currentData, data)) {
				dataCounter.set(data.getID(), dataCounter.get(data.getID())-1);
				dataTTL.remove(currentData);
				itr.remove();
			}
		}
	}

	public void reduceTTL(Node node){
		// System.out.println("Node ID: " + node.getIndex());
		for(int i=0; i<getData().size(); i++){
			Data data = getData().get(i);
			int ttl = dataTTL.get(data);
			// System.out.println("   Data :" + data.getID() + " TTL :" + ttl);
			if(ttl > 0) dataTTL.put(data, ttl-1); 
			if(ttl == 0) removeData(data);
		}
	}
}