package research;

import peersim.config.*;
import peersim.core.*;
import java.util.*;

public class DecreaseReplications implements Control{
	private static Node node;


	public DecreaseReplications(String prefix){
	}


	public static void owner(){
		StorageOwner sOwner = SharedResource.getSOwner(node);
		sOwner.reduceTTL(node);
	}

	public static void path(){
		StoragePath sPath = SharedResource.getSPath(node);
		sPath.reduceTTL(node);
	}

	public static void relate(){
		StorageRelate sRelate = SharedResource.getSRelate(node);
		sRelate.reduceReplicaTTL(node);
		sRelate.reduceTTL(node);
	}

	public static void cuckoo(){
		StorageCuckoo sCuckoo = SharedResource.getSCuckoo(node);
		sCuckoo.reduceReplicaTTL(node);
		sCuckoo.reduceTTL(node);
	}

	public boolean execute(){
		for(int nodeID=0; nodeID<Network.size(); nodeID++){
			node = Network.get(nodeID);
			// Storage storage = SharedResource.getStorage(node);
			// storage.reduceTTL(node);
			owner();
			path();
			relate();
			cuckoo();
		}

		return false;
	}

}