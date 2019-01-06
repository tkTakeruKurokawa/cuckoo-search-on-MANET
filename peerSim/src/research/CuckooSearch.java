package research;

import peersim.core.*;
import peersim.config.*;
import java.util.*;


public class CuckooSearch implements Control{
	private static final String PAR_MAXGENERATION = "generation";
	private static int maxGeneration;


	public CuckooSearch(String prefix){
		maxGeneration = Configuration.getInt(prefix + "." + PAR_MAXGENERATION);
	}

	public static Node search(){

		NestSet ns = new NestSet();
		for(int generation=0; generation<maxGeneration; generation++){
			// System.out.println("Generation: " + generation);
			ns.alternate();
		}
		Node bestNode = ns.getBestNode();

		return bestNode;
	}

	public static Node search(Data data){
		// long start = System.nanoTime();
		NestSet ns = new NestSet();
		for(int generation=0; generation<maxGeneration; generation++){
			// System.out.println("Generation: " + generation);
			ns.alternate();
		}
		// long end = System.nanoTime();
		// System.out.println("Cuckoo Time:" + (end - start) / 1000000f + "ms");

		Node bestNode;
		int addNum=0;
		while(true){
			bestNode = ns.getBestNode(addNum);
			StorageCuckoo storage = SharedResource.getSCuckoo(bestNode);
			NPCuckoo parameter = SharedResource.getNPCuckoo(bestNode);
			int capacity = parameter.getCapacity();
			int occupancy = data.getSize();
			int newCapacity = capacity-occupancy;

			if(!storage.contains(data) && newCapacity>=0){
				break;
			}
			addNum++;
		}

		// ArrayList<Nest> nest = ns.getNestSet();
		// System.out.println("NOWNEST:");
		// for(int k=0; k<200; k++){
		// 	if(k>200-10){
		// 		System.out.printf("\t%d: ",k);
		// 		System.out.println("Node: " + nest.get(k).getNode().getIndex() + " value " + nest.get(k).getValue() + " (" + nest.get(k).egg[0] + ", " + nest.get(k).egg[1] + ")");
		// 	}
		// }

		return bestNode;
	}

	public boolean execute(){
		return false;
	}

}