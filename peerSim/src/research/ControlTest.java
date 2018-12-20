package research;

import peersim.config.*;
import peersim.core.*;
import java.util.*;

public class ControlTest implements Control{

	private static Node node;
	private static Data data;
	private static ArrayList<Integer> dataCounter;
	private static Storage storage;
	private static ArrayList<Integer> rnd = new ArrayList<Integer>();
	private static Random random = new Random();
	
	private static final String PAR_PROT = "protocol";
	private static int pid;
	private static boolean doneReplicate;

	public ControlTest(String prefix){
		pid = Configuration.getPid(prefix + "." + PAR_PROT);
		// System.out.println(FastConfig.numLinkables(pid));
		// System.out.println(FastConfig.getLinkable(pid));
		data = InitializeNetwork.getData();
	}

	// public int selectNode(int nodeID){
	// 	return random.nextInt(Network.size()+1);
	// }

	// public int selectData(){
	// 	return 
	// }

	public static void setDataCounter(ArrayList<Integer> dc){
		dataCounter = dc;
	}

	public boolean execute(){

		for(int i=0; i<data.getVariety(); i++)
			System.out.println("\tData: " + i + " num of Data: " + dataCounter.get(i));

		// make Not overlap random 
		for(int i=0; i<Network.size(); i++)
			rnd.add(i);

		// Set Test
		for(int dataID=0; dataID<data.getVariety(); dataID++){
			int replications = data.getReplications(dataID);
			System.out.println("Data :" + dataID + " Replications: " + replications);
			Collections.shuffle(rnd);

			int num = 0;
			int element = 0;
			while(num < replications){
				node = Network.get(rnd.get(element));
				storage = (Storage) node.getProtocol(pid);
				element++;
				boolean success =  storage.setData(data.getData(dataID));
				if(!success) continue;
				System.out.println("   Data: " + dataID + " to NodeIndex: " + node.getIndex());
				num++;
			}
			// System.out.println("Fd: " + (0.5*(double)replications+(1-0.5)*(double)));
		}
		System.out.println("*************** END SET DATA ***************");
		System.out.println();

		// Remove Test
		// for(int nodeID=0; nodeID<Network.size(); nodeID++){
		// 	node = Network.get(nodeID);
		// 	storage = (Storage) node.getProtocol(pid);
		// 	for(int dataID=0; dataID<data.getVariety(); dataID++){
		// 		storage.removeData(data.getData(dataID));				
		// 	}
		// }

		// Get Test
		for(int nodeID=0; nodeID<Network.size(); nodeID++){
			node = Network.get(nodeID);
			storage = (Storage) node.getProtocol(pid);
			if(storage.getData().size() == 0) continue;
			System.out.println("Node ID: " + nodeID);
			for(Data d: storage.getData()){
				System.out.println("   Data ID: " + d.getID());
			}
		}

		System.out.println("*************** END GET DATA ***************");
		System.out.println();

		for(int nodeID=0; nodeID<Network.size(); nodeID++){
			node = Network.get(nodeID);
			storage = (Storage) node.getProtocol(pid);
			storage.reduceTTL(node);
		}

		// for(int i=0; i<data.getVariety(); i++)
		// 	System.out.println("\tData: " + i + " num of Data: " + dataCounter.get(i));
		
		System.out.println();
		System.out.println();
		return false;
	}
}