package research;

import peersim.config.*;
import peersim.core.*;
import java.util.*;

public class IncreaseReplications implements Control{
	private static Random random = new Random();
	private static ArrayList<Data> tmp = new ArrayList<Data>();
	private static ArrayList<Boolean> upLoaded;
	private static ArrayList<Data> requestList;
	private static Node node;

	private static int successFloodO=0;
	private static int successFloodP=0;
	private static int successFloodR=0;
	private static int successFloodC=0;
	private static int failFloodO=0;
	private static int failFloodP=0;
	private static int failFloodR=0;
	private static int failFloodC=0;
	private static int failSetO=0;
	private static int failSetP=0;
	private static int failSetR=0;
	private static int failSetC=0;
	private static int successSetO=0;
	private static int successSetP=0;
	private static int successSetR=0;
	private static int successSetC=0;
	public static int replicaC=0;
	public static int replicaR=0;


	public IncreaseReplications(String prefix){
	}


	private static boolean check(Storage storage, Parameter parameter, Data data){
		int capacity = parameter.getCapacity();
		int occupancy = data.getSize();
		int newCapacity = capacity-occupancy;

		if(!storage.contains(data) && (newCapacity>=0)){
			return true;
		}

		return false;
	}

	private static void pathReplication(Data data){
		for(Map.Entry<Integer, Node> path : Flooding.getPath().entrySet()){
			StoragePath sPath = SharedResource.getSPath(path.getValue());
			if(sPath.setData(path.getValue(), data)){
				successSetP++;
			}else{
				failSetP++;
			}
			// System.out.println("TTL " + path.getKey() + ", Node " + path.getValue().getIndex());
		}
	}


	public static void owner(){
		StorageOwner sOwner = SharedResource.getSOwner(node);

		for(int dataID=0; dataID<requestList.size(); dataID++){
			Data data = requestList.get(dataID);
			if(!upLoaded.get(data.getID())){
				while(true){
					Parameter parameter = SharedResource.getNPOwner(node);
					Storage storage = SharedResource.getSOwner(node);

					boolean success = check(storage, parameter, data);
					if(success){
						break;
					}

					node = Network.get(random.nextInt(Network.size()));
				}
				sOwner.setData(node, data);
			}else{
				if(!sOwner.contains(data)){
					boolean hit = Flooding.search(node, data, 0);
					if(hit){
						successFloodO++;
						if(sOwner.setData(node, data)){
							successSetO++;
						}else{
							failSetO++;
						}
					}else{
						failFloodO++;
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
				while(true){
					Parameter parameter = SharedResource.getNPPath(node);
					Storage storage = SharedResource.getSPath(node);

					boolean success = check(storage, parameter, data);
					if(success){
						break;
					}

					node = Network.get(random.nextInt(Network.size()));
				}
				sPath.setData(node, data);
			}else{
				if(!sPath.contains(data)){
					boolean hit = Flooding.search(node, data, 1);
					if(hit){
						successFloodP++;
						pathReplication(data);
					}else{
						failFloodP++;
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
				while(true){
					Parameter parameter = SharedResource.getNPRelate(node);
					Storage storage = SharedResource.getSRelate(node);

					boolean success = check(storage, parameter, data);
					if(success){
						break;
					}

					node = Network.get(random.nextInt(Network.size()));
				}
				sRelate.setData(node, data);
			}else{
				if(!sRelate.contains(data)){
					boolean hit = Flooding.search(node, data, 2);
					if(hit){
						successFloodR++;
						if(sRelate.setData(node, data)){
							successSetR++;
						}else{
							failSetR++;
						}
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
				while(true){
					Parameter parameter = SharedResource.getNPCuckoo(node);
					Storage storage = SharedResource.getSCuckoo(node);

					boolean success = check(storage, parameter, data);
					if(success){
						break;
					}

					node = Network.get(random.nextInt(Network.size()));
				}
				sCuckoo.setData(node, data);
			}else{
				if(!sCuckoo.contains(data)){
					boolean hit = Flooding.search(node, data, 3);
					if(hit){
						successFloodC++;
						if(sCuckoo.setData(node, data)){
							successSetC++;
						}else{
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
		ArrayList<Data> cyclesRequestList = SharedResource.getCyclesRequestList();

		for(int nodeID=0; nodeID<Network.size(); nodeID++){
			node = Network.get(nodeID);

			RequestProbability request = SharedResource.getRequestProbability(node);
			requestList = request.dataRequests();

			owner();
			path();
			relate();
			cuckoo();

			SharedResource.nextRand();

			for(int dataID=0; dataID<requestList.size(); dataID++){
				Data data = requestList.get(dataID);
				if(!cyclesRequestList.contains(data)){
					cyclesRequestList.set(data.getID(), data);
				}

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

		// System.out.println("Owner Num: Success Flooding " + successFloodO);
		// System.out.println("Owner Num: Fail Flooding " + failFloodO);
		// System.out.println("Owner Num: Fail setData " + failSetO);
		// System.out.println("Owner Num: Success setData " + successSetO);
		// System.out.println();

		// System.out.println("Path Num: Success Flooding " + successFloodP);
		// System.out.println("Path Num: Fail Flooding " + failFloodP);
		// System.out.println("Path Num: Fail setData " + failSetP);
		// System.out.println("Path Num: Success setData " + successSetP);
		// System.out.println();


		// System.out.println("Relate Num: Success Flooding " + successFloodR);
		// System.out.println("Relate Num: Fail Flooding " + failFloodR);
		// System.out.println("Relate Num: Fail setData " + failSetR);
		// System.out.println("Relate Num: Success setData " + successSetR);
		// System.out.println("Relate Num: setData to seting Replica Node " + replicaR);
		// System.out.println();

		// System.out.println("Cuckoo Num: Success Flooding " + successFloodC);
		// System.out.println("Cuckoo Num: Fail Flooding " + failFloodC);
		// System.out.println("Cuckoo Num: Fail setData " + failSetC);
		// System.out.println("Cuckoo Num: Success setData " + successSetC);

		// System.out.println("Cuckoo Num: setData to seting Replica Node " + replicaC);
		// System.out.println();

		SharedResource.setUpLoaded(upLoaded);
		SharedResource.setCyclesRequestList(cyclesRequestList);

		return false;
	}

}