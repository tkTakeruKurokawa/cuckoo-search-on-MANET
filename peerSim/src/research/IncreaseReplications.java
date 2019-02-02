package research;

import peersim.config.*;
import peersim.core.*;
import java.util.*;

public class IncreaseReplications implements Control{
	private static Random random = new Random();
	private static ArrayList<Boolean> upLoaded;
	private static ArrayList<Data> requestList;
	private static Node node;
	private static Data data;

	private static int successFloodO=0;
	private static int successFloodP=0;
	private static int successFloodR=0;
	private static int successFloodC=0;
	private static int countO=0;
	private static int countP=0;
	private static int countR=0;
	private static int countC=0;
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


	private static boolean check(Parameter parameter, Storage storage){
		int capacity = parameter.getCapacity();
		int occupancy = data.getSize();
		int newCapacity = capacity-occupancy;

		if(!storage.contains(data) && (newCapacity>=0)){
			return true;
		}

		return false;
	}

	private static void firstUL(){
		while(true){
			Parameter parOwner = SharedResource.getNPOwner(node);
			Storage strOwner = SharedResource.getSOwner(node);
			Parameter parRelate = SharedResource.getNPRelate(node);
			Storage strRelate = SharedResource.getSRelate(node);
			Parameter parCuckoo = SharedResource.getNPCuckoo(node);
			Storage strCuckoo = SharedResource.getSCuckoo(node);

			boolean sucOwner = check(parOwner, strOwner);
			boolean sucRelate = check(parRelate, strRelate);
			boolean sucCuckoo = check(parCuckoo, strCuckoo);

			if(sucOwner == true && sucRelate == true && sucCuckoo == true){
				break;
			}

			node = Network.get(random.nextInt(Network.size()));
		}
	}

	private static void ownerReplication(Parameter parameter, Storage storage, int id){
		if(!upLoaded.get(data.getID())){
			firstUL();
			storage.setData(node, data);
		}else{
			if(check(parameter, storage)){
				boolean hit = Flooding.search(node, data, id);
				if(hit){
					if(storage.setData(node, data)){
					}else{
					}
				}else{
				// System.out.println("FALSE FLOODING. ID: " + id);
				}
			}
		}
	}

	private static void pathReplication(){
		for(Map.Entry<Integer, Node> path : Flooding.getPath().entrySet()){
			StoragePath sPath = SharedResource.getSPath(path.getValue());
			if(sPath.setData(path.getValue(), data)){
				successSetP++;
			}else{
				failSetP++;
			}
		}
	}


	public static void owner(){
		Parameter parameter = SharedResource.getNPOwner(node);
		Storage storage = SharedResource.getSOwner(node);

		ownerReplication(parameter, storage, 0);

		// if(!upLoaded.get(data.getID())){
		// 	while(true){
		// 		parameter = SharedResource.getNPOwner(node);
		// 		storage = SharedResource.getSOwner(node);

		// 		boolean success = check(parameter, storage);
		// 		if(success){
		// 			break;
		// 		}

		// 		node = Network.get(random.nextInt(Network.size()));
		// 	}
		// 	storage.setData(node, data);
		// }else{
		// 	ownerReplication(parameter, storage, 0);
		// }
	}

	public static void path(){
		Parameter parameter = SharedResource.getNPPath(node);
		Storage storage = SharedResource.getSPath(node);

		if(!upLoaded.get(data.getID())){
			while(true){
				parameter = SharedResource.getNPPath(node);
				storage = SharedResource.getSPath(node);

				boolean success = check(parameter, storage);
				if(success){
					break;
				}

				node = Network.get(random.nextInt(Network.size()));
			}

			storage.setData(node, data);
		}else{
			if(check(parameter, storage)){
				boolean hit = Flooding.search(node, data, 1);
				if(hit){
					successFloodP++;
					pathReplication();
				}else{
					failFloodP++;
				}
			}
		}
	}

	public static void relate(){
		Parameter parameter = SharedResource.getNPRelate(node);
		Storage storage = SharedResource.getSRelate(node);

		ownerReplication(parameter, storage, 2);

		// if(!upLoaded.get(data.getID())){
		// 	while(true){
		// 		parameter = SharedResource.getNPRelate(node);
		// 		storage = SharedResource.getSRelate(node);

		// 		boolean success = check(parameter, storage);
		// 		if(success){
		// 			break;
		// 		}

		// 		node = Network.get(random.nextInt(Network.size()));
		// 	}
		// 	storage.setData(node, data);
		// }else{
		// 	ownerReplication(parameter, storage, 2);
		// }
	}

	public static void cuckoo(){
		Parameter parameter = SharedResource.getNPCuckoo(node);
		Storage storage = SharedResource.getSCuckoo(node);

		ownerReplication(parameter, storage, 3);

		// if(!upLoaded.get(data.getID())){
		// 	while(true){
		// 		parameter = SharedResource.getNPCuckoo(node);
		// 		storage = SharedResource.getSCuckoo(node);

		// 		boolean success = check(parameter, storage);
		// 		if(success){
		// 			break;
		// 		}

		// 		node = Network.get(random.nextInt(Network.size()));
		// 	}
		// 	storage.setData(node, data);
		// }else{
		// 	ownerReplication(parameter, storage, 3);
		// }
	}

	public boolean execute(){
		upLoaded = SharedResource.getUpLoaded();
		ArrayList<Data> cyclesRequestList = SharedResource.getCyclesRequestList();

		for(int nodeID=0; nodeID<Network.size(); nodeID++){
			SharedResource.nextRand();

			node = Network.get(nodeID);

			RequestProbability request = SharedResource.getRequestProbability(node);
			requestList = request.dataRequests();

			for(int dataID=0; dataID<requestList.size(); dataID++){
				data = requestList.get(dataID);

				if(!cyclesRequestList.contains(data)){
					cyclesRequestList.set(data.getID(), data);
				}

				owner();
				path();
				relate();
				cuckoo();
			}
		}

		for(int i=0; i<Data.getNowVariety(); i++){
			if(cyclesRequestList.get(i) != null)
				upLoaded.set(i, true);
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

		// System.out.println("Owner Count: " + countO);
		// System.out.println("Path Count: " + countP);
		// System.out.println("Relate Count: " + countR);
		// System.out.println("Cuckoo Count: " + countC);

		SharedResource.setUpLoaded(upLoaded);
		SharedResource.setCyclesRequestList(cyclesRequestList);

		return false;
	}

}