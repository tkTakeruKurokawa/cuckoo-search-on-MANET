package research;

import peersim.config.*;
import peersim.core.*;
import java.util.*;

public class RelatedResearch implements Control{
	private static final String PAR_TTL = "ttl";
	private static int ttl;

	private static ArrayList<Node> checkedList = new ArrayList<Node>();
	private static HashMap<Node, Integer> next = new HashMap<Node, Integer>(); 
	private static Deque<Node> stack = new ArrayDeque<Node>();
	private static Node bestNode;
	private static double bestValue;
	private static Data target;


	public RelatedResearch(String prefix){
		ttl = Configuration.getInt(prefix + "." + PAR_TTL);
	}


	public static void objectiveFunction(Node node){
		StorageRelate storage = SharedResource.getSRelate(node);

		NPRelate parameter = SharedResource.getNPRelate(node);
		double battery = parameter.getBattery();
		int capacity = parameter.getCapacity();
		int occupancy = target.getSize();
		int newCapacity = capacity-occupancy;

		if(!storage.contains(target) && newCapacity>=0){
			double b = battery/100.0;
			double c = ((double)capacity)/10.0;
			double value = 1.0 * b + 0.5 * c;
			if(value>bestValue){
				bestNode = node;
				bestValue = value;
			}
		}
	}

	private static void nextSearch(Node node, int ttl){
		// System.out.println("TTL" + ttl);
		// System.out.printf("NodeIndex: %d\n   Neighbors Index:", node.getIndex());
		// Link link = (Link) node.getProtocol(pid_lnk);
		// for(int nodeID=0; nodeID<link.degree(); nodeID++){
		// 	Node n = link.getNeighbor(nodeID);
		// 	System.out.printf(" %d", n.getIndex());
		// }		
		// System.out.println();

		if(ttl == 0) 
			return;

		checkedList.add(node);
		objectiveFunction(node);

		Link linkable = SharedResource.getLink(node);
		for(int nodeID=0; nodeID<linkable.degree(); nodeID++){
			Node neighbor = linkable.getNeighbor(nodeID);

			if(!checkedList.contains(neighbor)){
				next.put(neighbor, ttl-1);
					// queue.add(neighbor);
				stack.push(neighbor);
					// nextSearch(neighbor, ttl-1);
			}
		}			


		// Node neighbor = queue.poll();
		if(stack.size()==0) return;
		Node neighbor = stack.pop();
		if(neighbor == null) return;
			// System.out.println("neighbor " + neighbor.getIndex() + " " + next.get(neighbor));

		nextSearch(neighbor, next.get(neighbor));

		return;
	}

	public static Node getBestNode(Node node, Data data){
		checkedList.clear();
		checkedList.add(node);

		next.clear();
		bestNode = null;
		bestValue = 0.0;

		while(stack.size() != 0)
			stack.pop();

		target = data;

		nextSearch(node, ttl-1);

		return bestNode;
	}

	public boolean execute(){
		return false;
	}

}