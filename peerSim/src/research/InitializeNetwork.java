package research;

import peersim.config.*;
import peersim.core.*;
import java.util.*;

public class InitializeNetwork implements Control{
	private static final String PAR_PROT0 = "protocol0";
	private static int pid_str;
	private static final String PAR_PROT1 = "protocol1";
	private static int pid_rp;

	private static Random random = new Random();
	private static Node node;
	private static Data data = new Data();
	private static Storage storage = new Storage();
	private static RequestProbability request = new RequestProbability();
	private static ArrayList<Data> requestList;
	private static ArrayList<Integer> dataCounter = new ArrayList<Integer>();


	public InitializeNetwork(String prefix){
		pid_str = Configuration.getPid(prefix + "." + PAR_PROT0);
		pid_rp = Configuration.getPid(prefix + "." + PAR_PROT1);
	}

	public static Data getData(){
		return data;
	}

	public static ArrayList<Integer> getDataCounter(){
		return dataCounter;
	}

	public boolean execute(){

		for(int i=0; i<Data.getVariety(); i++)
			dataCounter.add(i, 0);

		for(int nodeID=0; nodeID<Network.size(); nodeID++){
			node = Network.get(nodeID);
			request = (RequestProbability) node.getProtocol(pid_rp);
			requestList = request.dataRequests();
			// requestListには入っているデータについてのダウンロード要求を、ノードにさせる
			storage = (Storage) node.getProtocol(pid_str);
			for(int i=0; i<requestList.size(); i++){
				storage.setData(requestList.get(i));
			}
		}

		// for(int dataID = 0; dataID<data.getVariety(); dataID++){
		// 	int replications = data.getReplications(dataID);
		// 	// System.out.println("Data " + dataID + " Replications: " + replications);

		// 	int i = 0;
		// 	while(i<replications){
		// 		node = Network.get(random.nextInt(Network.size()));
		// 		storage = (Storage) node.getProtocol(pid);
		// 		boolean success = storage.setData(Data.getData(dataID));
		// 		if(success == false) continue; 
		// 		// System.out.println("   Data: " + dataID + " NodeIndex: " + node.getIndex());
		// 		i++;
		// 	}
		// 	// System.out.println("Fd: " + (0.5*(double)replications+(1-0.5)*(double)realReplications));
		// }
		return false;
	}
}