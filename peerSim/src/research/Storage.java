package research;

import peersim.core.*;
import java.util.*;

public class Storage implements Protocol{

	private ArrayList<Data> dataList = new ArrayList<Data>();

	public Storage(){
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
		return storage;
	}

	public boolean setData(Data data){
		if(!dataList.contains(data)){
			dataList.add(data);
			return true;
		}
		return false;
	}

	public ArrayList<Data> getData(){
		return dataList;
	}

	public void removeData(Data data){
		Iterator<Data> itr = dataList.iterator();
		while(itr.hasNext()){
			Data currentData = itr.next();
			if(Objects.equals(currentData, data)) itr.remove();
		}
	}
}