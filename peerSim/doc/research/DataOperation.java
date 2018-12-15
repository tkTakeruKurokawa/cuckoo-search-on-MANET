package research;

import peersim.config.*;
import peersim.core.*;
import java.util.*;


public class DataOperation{

// ================= fields ========================================
// =================================================================

	// private static ArrayList<Data> data = new ArrayList<Data>();
	// private static final GeneralNode node = new GeneralNode(null);
	private static Random random = new Random();

	private static ArrayList<Integer> data = new ArrayList<Integer>();
	private static HashMap<Integer, ArrayList<Integer>> node = new HashMap<Integer, ArrayList<Integer>>();

// =============== public methods ==================================
// =================================================================

	public DataOperation(){
	}

	// public DataOperation clone(){
	// 	DataOperation d = (DataOperation) super.clone();
	// 	this.gn = (GeneralNode) gn.clone();
	// 	return d;
	// }

	// すべてのノードがひとつのデータのリストを共有している状態。要改善
	public static void setDataToNode(int dataID, int nodeID){
		data.add(dataID);
		node.put(nodeID, data);
		System.out.println(nodeID);
		for(Integer d: node.get(nodeID))
			System.out.println(d);
	}

	public static void getDataArray(int nodeID){
		System.out.println(nodeID);
		for(Integer d: node.get(nodeID))
			System.out.println(d);
	}

	public static void removeData(int dataID, int nodeID){
		Iterator<Integer> itr = node.get(nodeID).iterator();
		while(itr.hasNext()){
			Integer number = itr.next();
			if(Objects.equals(number, dataID)) itr.remove();
		}
	}

}