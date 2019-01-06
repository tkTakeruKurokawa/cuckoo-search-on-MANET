package research;

import peersim.config.*;
import peersim.core.*;
import java.util.*;

public class ControlTest implements Control{

	private static final String PAR_PROT0 = "protocol0";
	private static int pid_str;
	private static final String PAR_PROT1 = "protocol1";
	private static int pid_rp;

	private static Node node;
	private static Data data;
	private static RequestProbability request;
	private static ArrayList<Data> requestList;
	public static ArrayList<Integer> dataCounter = new ArrayList<Integer>();
	private static Storage storage;
	private static ArrayList<Integer> rnd = new ArrayList<Integer>();
	private static ArrayList<Boolean> uploaded = new ArrayList<Boolean>();
	private static Random random = new Random();
	
	private static int count = 1;

	public ControlTest(String prefix){
		pid_str = Configuration.getPid(prefix + "." + PAR_PROT0);
		pid_rp = Configuration.getPid(prefix + "." + PAR_PROT1);

		// System.out.println(FastConfig.numLinkables(pid));
		// System.out.println(FastConfig.getLinkable(pid));
		data = SharedResource.getDataInst();

		for(int dataID=0; dataID<Data.getMaxVariety(); dataID++){
			dataCounter.add(dataID, 0);
			uploaded.add(dataID, false);
		}
	}

	public static void setDataCounter(ArrayList<Integer> dc){
		dataCounter = dc;
	}

	private static void pathReplication(Data data){
		for(Map.Entry<Integer, Node> path : Flooding.getPath().entrySet()){
			storage = (Storage) path.getValue().getProtocol(pid_str);
			storage.setData(node, data);
			// System.out.println("TTL " + path.getKey() + ", Node " + path.getValue().getIndex());
		}
	}

	public boolean execute(){


		rnd.clear();
		for(int i=0; i<Network.size(); i++)
			rnd.add(i);
		// for(int i=0; i<data.getVariety(); i++)
		// 	System.out.println("\tData: " + i + " num of Data: " + dataCounter.get(i));

		// make Not overlap random 

		// Set Test
		// for(int dataID=0; dataID<data.getVariety(); dataID++){
		// 	int replications = data.getReplications(dataID);
		// 	// System.out.println("Data :" + dataID + " Replications: " + replications);
		// 	Collections.shuffle(rnd);

		// 	int num = 0;
		// 	int element = 0;
		// 	while(num < replications){
		// 		node = Network.get(rnd.get(element));
		// 		storage = (Storage) node.getProtocol(pid_str);
		// 		element++;
		// 		boolean success =  storage.setData(data.getData(dataID));
		// 		if(!success) continue;
		// 		// System.out.println("   Data: " + dataID + " to NodeIndex: " + node.getIndex());
		// 		num++;
		// 	}
		// }

		//ダウンロード関連
		for(int nodeID=0; nodeID<Network.size(); nodeID++){
			node = Network.get(nodeID);
			request = (RequestProbability) node.getProtocol(pid_rp);
			requestList = request.dataRequests();
			// requestListには入っているデータについてのダウンロード要求を、ノードにさせる
			storage = (Storage) node.getProtocol(pid_str);
			for(int dataID=0; dataID<requestList.size(); dataID++){
				// System.out.println(requestList.get(i).getID());
				data = requestList.get(dataID);
				if(!uploaded.get(data.getID())){
					storage.setData(node, data);
				}
				else
					if(!storage.contains(data)){
						// System.out.println("**********   Node " + node.getIndex() + " Request Data " + data.getID() + "   **********");
						// boolean hit = Flooding.search(node, data);
						// if(hit){
						// 	storage.setData(node, data);
						// 	// pathReplication(data);
						// }
						// else{
							// System.out.println("Node " + nodeID + "can't find Data "+ data.getID());
						// }
					}
				}
			}

			// ピークサイクルになってもアップロードされたデータが0の時の処理
		// 	for(int dataID=0; dataID<Data.getVariety(); dataID++){
		// 		data = getData(dataID);
		// 		if(Objects.equals(data.getNowCycle(), data.getPeakCycle()) && Objects.equals(dataCounter.get(dataID), 0)){
		// 			Collections.shuffle(rnd);

		// 			int num = 0;
		// 			int element = 0;
		// 			int nodeNum = random.nextInt(10)+5;
		// 			while(num < nodeNum){
		// 				node = Network.get(rnd.get(element));
		// 				// System.out.println();
		// 				storage = (Storage) node.getProtocol(pid_str);
		// 				element++;
		// 				boolean success =  storage.setData(data.getData(dataID));
		// 				if(!success) continue;
		// 		// System.out.println("   Data: " + dataID + " to NodeIndex: " + node.getIndex());
		// 				num++;
		// // 	}
		// 			}
		// 		}
		// 	}
		// System.out.println("*************** END SET DATA ***************");
		// System.out.println();

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
		// 	node = Network.get(nodeID);
		// 	storage = (Storage) node.getProtocol(pid);
		// 	if(storage.getData().size() == 0) continue;
		// 	// System.out.println("Node ID: " + nodeID);
		// 	for(Data d: storage.getData()){
		// 		// System.out.println("   Data ID: " + d.getID());
		// 	}
		// }

		// System.out.println("*************** END GET DATA ***************");
		// System.out.println();

			for(int nodeID=0; nodeID<Network.size(); nodeID++){
				node = Network.get(nodeID);
				storage = (Storage) node.getProtocol(pid_str);
				storage.reduceTTL(node);
				// if(!storage.isEmpty()){
				// 	System.out.printf("\tNode %d having Data: ", nodeID);
				// 	for(Data d: storage.getData())
				// 		System.out.printf("%d, ", d.getID());
				// }
				// System.out.println();
			}

			for(int dataID=0; dataID<data.getNowVariety(); dataID++){
				if(Objects.equals(true, data.getData(dataID).lowDemand)){
				System.out.printf("%d, %d, ",count, dataID);
				// System.out.printf("%d, ", count);
				System.out.println(dataCounter.get(dataID));	
				}
				if(dataCounter.get(dataID) > 0)
					uploaded.set(dataID, true);
			}

			System.out.println(uploaded.size());

			// Optional<Integer> max = dataCounter.stream()
			// .max((a, b) -> a.compareTo(b));
			// System.out.println("max : " + max.get());

			System.out.println();
			System.out.println();
			count++;
			return false;
		}
	}