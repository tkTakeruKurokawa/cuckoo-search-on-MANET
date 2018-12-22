package research;

import peersim.config.*;
import peersim.core.*;
import java.util.*;

public class InitializeNetwork implements Control{
	private static final String PAR_PROT = "protocol";
	private static int pid;

	private static Random random = new Random();
	private static Node node;
	private static Data data = new Data();
	private static Storage storage = new Storage();

	public InitializeNetwork(String prefix){
		pid = Configuration.getPid(prefix + "." + PAR_PROT);
		// storage.clone();
	}

	public static Data getData(){
		return data;
	}

	public boolean execute(){

		for(int dataID = 0; dataID<data.getVariety(); dataID++){
			int replications = data.getReplications(dataID);
			// System.out.println("Data " + dataID + " Replications: " + replications);

			int i = 0;
			while(i<replications){
				node = Network.get(random.nextInt(Network.size()));
				storage = (Storage) node.getProtocol(pid);
				boolean success = storage.setData(data.getData(dataID));
				if(success == false) continue; 
				// System.out.println("   Data: " + dataID + " NodeIndex: " + node.getIndex());
				i++;
			}
			// System.out.println("Fd: " + (0.5*(double)replications+(1-0.5)*(double)realReplications));
		}

		System.out.println();
		System.out.println();
		
		return false;
	}
}