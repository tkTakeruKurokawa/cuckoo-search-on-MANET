package research;

import peersim.config.*;
import peersim.core.*;
import java.util.*;

public class IncreaseReplications implements Control{
	private static ArrayList<Data> tmp = new ArrayList<Data>();
	private static ArrayList<Boolean> upLoaded;
	private static ArrayList<Data> requestList;
	private static Node node;

	private static int failFloodC=0;
	private static int failFloodR=0;
	private static int failSetC=0;
	private static int failSetR=0;
	public static int replicaC=0;
	public static int replicaR=0;


	public IncreaseReplications(String prefix){
	}


	private static void pathReplication(Data data){
		for(Map.Entry<Integer, Node> path : Flooding.getPath().entrySet()){
			StoragePath sPath = SharedResource.getSPath(path.getValue());
			sPath.setData(path.getValue(), data);
			// System.out.println("TTL " + path.getKey() + ", Node " + path.getValue().getIndex());
		}
	}


	public static void owner(){
		StorageOwner sOwner = SharedResource.getSOwner(node);

		for(int dataID=0; dataID<requestList.size(); dataID++){
			Data data = requestList.get(dataID);
			if(!upLoaded.get(data.getID())){
				sOwner.setData(node, data);
			}else{
				if(!sOwner.contains(data)){
					boolean hit = Flooding.search(node, data, 0);
					if(hit){
						sOwner.setData(node, data);
					}
				}
			}
		}
	}

	public static void path(){
		StoragePath sPath = SharedResource.getSPath(node);

		for(int dataID=0; dataID<requestList.size(); dataID++){
			Data data = requestList.get(dataID);
			if(!upLoaded.get(data.getID())){
				sPath.setData(node, data);
			}else{
				if(!sPath.contains(data)){
					boolean hit = Flooding.search(node, data, 1);
					if(hit){
						pathReplication(data);
					}
				}
			}
		}
	}

	public static void relate(){
		StorageRelate sRelate = SharedResource.getSRelate(node);

		for(int dataID=0; dataID<requestList.size(); dataID++){
			Data data = requestList.get(dataID);
			if(!upLoaded.get(data.getID())){
				sRelate.setData(node, data);
			}else{
				if(!sRelate.contains(data)){
					boolean hit = Flooding.search(node, data, 2);
					if(hit){
						if(!sRelate.setData(node, data))
							failSetR++;
					}else{
						failFloodR++;
					}
				}
			}
		}
	}

	public static void cuckoo(){
		StorageCuckoo sCuckoo = SharedResource.getSCuckoo(node);

		for(int dataID=0; dataID<requestList.size(); dataID++){
			Data data = requestList.get(dataID);
			if(!upLoaded.get(data.getID())){
				sCuckoo.setData(node, data);
			}else{
				if(!sCuckoo.contains(data)){
					boolean hit = Flooding.search(node, data, 3);
					if(hit){
						if(!sCuckoo.setData(node, data)){
							failSetC++;
						}
					}else{
						failFloodC++;
					}
				}
			}
		}
	}

	public boolean execute(){
		// countC = 0;
		// countR = 0;
		tmp.clear();
		upLoaded = SharedResource.getUpLoaded();

		for(int nodeID=0; nodeID<Network.size(); nodeID++){
			node = Network.get(nodeID);

			RequestProbability request = SharedResource.getRequestProbability(node);
			requestList = request.dataRequests();

			owner();
			path();
			relate();
			cuckoo();
			for(int dataID=0; dataID<requestList.size(); dataID++){
				Data data = requestList.get(dataID);
				if(!upLoaded.get(data.getID())){
					tmp.add(data);
				}
			}

			// 		sOwner.setData(node, data);
			// 		sPath.setData(node, data);
			// 		sRelate.setData(node, data);
			// 		sCuckoo.setData(node, data);
			// 	}
			// 	else{
			// 		if(!storage.contains(data)){
			// 			boolean hit = Flooding.search(node, data);
			// 			if(hit){
			// 				storage.setData(node, data);
			// 			}
			// 		}
			// 	}
		}

		for(int i=0; i<tmp.size(); i++){
			upLoaded.set(tmp.get(i).getID(), true);
		}

		System.out.println("Relate Num: Fail Flooding " + failFloodR);
		System.out.println("Relate Num: Fail setData " + failSetR);
		System.out.println("Relate Num: setData to seting Replica Node " + replicaR);

		System.out.println("Cuckoo Num: Fail Flooding " + failFloodC);
		System.out.println("Cuckoo Num: Fail setData " + failSetC);
		System.out.println("Cuckoo Num: setData to seting Replica Node " + replicaC);


		SharedResource.setUpLoaded(upLoaded);

		return false;
	}

}