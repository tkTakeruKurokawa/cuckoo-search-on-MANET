package research;

import peersim.config.*;
import peersim.core.*;
import java.util.*;

public class InitializeNetwork implements Control{
	private static final String PAR_PROT = "protocol";
	private static int pid;

	private static Node node;
	private static Data data = new Data();
	private static Storage storage = new Storage();
	private static Random random = new Random();

	public InitializeNetwork(String prefix){
		pid = Configuration.getPid(prefix + "." + PAR_PROT);
		storage.clone();
	}

	public boolean execute(){
		// for (int i=0; i<data.getVariety(); i++) {
		// 	data.makeData();
		// }

		for(int dataID=0; dataID<data.getVariety(); dataID++){
			int replications = data.getReplications(dataID);
			System.out.println("Data " + dataID + " Replications" + replications);
			for(int i=0; i<replications; i++){
				node = Network.get(random.nextInt(Network.size()));
				storage = (Storage) node.getProtocol(pid);
				storage.setData(data.getData(dataID));
				System.out.println("   Data: " + dataID + " NodeIndex: " + node.getIndex());
			}
			// System.out.println("Fd: " + (0.5*(double)replications+(1-0.5)*(double)realReplications));
		}
		
		return false;
	}
}