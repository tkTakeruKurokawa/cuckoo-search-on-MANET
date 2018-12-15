package research;

import peersim.core.*;
import java.util.*;

public class Storage implements Protocol{

	private ArrayList<Data> dataList = new ArrayList<Data>();
	int num = 0;
	// Data[] dataList = new Data[Data.getVariety()];

	public Storage(){
	}

	public Storage(String prefix){
	}

	public Object clone(){
		Storage storage = null;
		try{
			storage = (Storage) super.clone();
			// storage.dataList = this.dataList.clone();
		}catch(CloneNotSupportedException e){
		}
		storage.dataList = new ArrayList<Data>(this.dataList.size());
		return storage;
	}

	public void setDataToNode(Data data){
		dataList.add(data);
		// dataList[num] = data;
		// num++;
	}

	public ArrayList<Data> getDataList(){
		return dataList;
	}

	// public Data[] getDataList(){
	// 	return dataList
	// }

	// public void removeData(Data data){
	// 	Iterator<Data> itr = dataList.iterator();
	// 	while(itr.hasNext()){
	// 		Data currentData = itr.next();
	// 		if(Objects.equals(currentData, data)) itr.remove();
	// 	}
	// }
}