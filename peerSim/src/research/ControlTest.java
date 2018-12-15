package research;

import peersim.config.*;
import peersim.core.*;
import java.util.*;

public class ControlTest implements Control{
	private static Node node;
	private static Data data;
	private static Storage storage;
	private static Random random = new Random();
	private static final String PAR_PROT = "protocol";
	private static int pid;
	private static boolean doneReplicate;
	private static int realReplications;

	public ControlTest(String prefix){
		pid = Configuration.getPid(prefix + "." + PAR_PROT);
		// storage.clone();
		// System.out.println(FastConfig.numLinkables(pid));
		// System.out.println(FastConfig.getLinkable(pid));
	}

	// public int selectNode(int nodeID){
	// 	return random.nextInt(Network.size()+1);
	// }

	// public int selectData(){
	// 	return 
	// }

	public boolean execute(){

		System.out.println(node);
		System.out.println(data);
		System.out.println(storage);

		// Set Test
		// for(int dataID=0; dataID<data.getVariety(); dataID++){
		// 	realReplications=0;
		// 	int replications = data.getReplication(dataID);
		// 	System.out.println("Data " + dataID + " Replications" + replications);
		// 	for(int i=0; i<replications; i++){
		// 		node = Network.get(random.nextInt(Network.size()));
		// 		storage = (Storage) node.getProtocol(pid);
		// 		if( (storage.setData(data.getData(dataID))) )
		// 			realReplications++;
		// 		System.out.println("   Data: " + dataID + " to NodeID: " + node.getID() + " NodeIndex: " + node.getIndex() + " RealReplications: " + realReplications);
		// 	}
		// 	// System.out.println("Fd: " + (0.5*(double)replications+(1-0.5)*(double)realReplications));
		// }

		// Remove Test
		// for(int nodeID=0; nodeID<Network.size(); nodeID++){
		// 	node = Network.get(nodeID);
		// 	storage = (Storage) node.getProtocol(pid);
		// 	for(int dataID=0; dataID<data.getVariety(); dataID++){
		// 		storage.removeData(data.getData(dataID));				
		// 	}
		// }

		// Get Test
		// for(int nodeID=0; nodeID<Network.size(); nodeID++){
		for(int nodeID=0; nodeID<Network.size(); nodeID++){
			node = Network.get(nodeID);
			storage = (Storage) node.getProtocol(pid);
			System.out.println("Node ID: " + nodeID);
			for(Data d: storage.getData()){
				System.out.println("   Data ID: " + d.getID());
			}
		}
		System.out.println();
		System.out.println();
		return false;
	}
}