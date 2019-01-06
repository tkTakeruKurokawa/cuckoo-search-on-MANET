package research;

import peersim.config.*;
import peersim.core.*;
import java.util.*;

public class Flooding implements Control{
	private static final String PAR_TTL = "ttl";
	private static int ttl;

	// private Node node;
	private static ArrayList<Node> checkedList = new ArrayList<Node>();
	private static HashMap<Node, Integer> next = new HashMap<Node, Integer>(); 
	private static Deque<Node> stack = new ArrayDeque<Node>();
	private static HashMap<Integer, Node> path = new HashMap<Integer, Node>();
	
	private static Data target;
	private static int id;
	private static boolean hit;


	public Flooding(String prefix){
		ttl = Configuration.getInt(prefix + "." + PAR_TTL);
	}

	public static HashMap<Integer, Node> getPath(){
		return path;		
	}

	private static void removePath(int ttl){
		Iterator<Map.Entry<Integer, Node>> iterator = path.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<Integer, Node> p = iterator.next();
			if (p.getKey() < ttl) {
				iterator.remove();
			}
		}
	}

	private static boolean nextSearch(Node node, int ttl){
		// System.out.println("TTL" + ttl);
		// System.out.printf("NodeIndex: %d\n   Neighbors Index:", node.getIndex());
		// Link link = (Link) node.getProtocol(pid_lnk);
		// for(int nodeID=0; nodeID<link.degree(); nodeID++){
		// 	Node n = link.getNeighbor(nodeID);
		// 	System.out.printf(" %d", n.getIndex());
		// }		
		// System.out.println();

		path.put(ttl, node);

		if(contains(node)){
			hit = true;
			removePath(ttl);
			return true;
		}

		if(ttl == 0) 
			return false;

		checkedList.add(node);
		Link linkable = SharedResource.getLink(node);
		if(!hit){
			for(int nodeID=0; nodeID<linkable.degree(); nodeID++){
				Node neighbor = linkable.getNeighbor(nodeID);

				if(!checkedList.contains(neighbor)){
					next.put(neighbor, ttl-1);
					// queue.add(neighbor);
					stack.push(neighbor);
					// nextSearch(neighbor, ttl-1);
				}
			}			
		}


		// while(true){
		// Node neighbor = queue.poll();
		if(stack.size()==0) return false;
		Node neighbor = stack.pop();
		if(hit == true) return true;
		if(neighbor == null) return false;
			// System.out.println("neighbor " + neighbor.getIndex() + " " + next.get(neighbor));

		nextSearch(neighbor, next.get(neighbor));
		// }


		return false;
	}

	private static boolean contains(Node node){
		if(Objects.equals(id, 0)){
			StorageOwner storage = SharedResource.getSOwner(node);
			return storage.contains(target);
		}

		if(Objects.equals(id, 1)){
			StoragePath storage = SharedResource.getSPath(node);
			return storage.contains(target);
		}

		if(Objects.equals(id, 2)){
			StorageRelate storage = SharedResource.getSRelate(node);
			return storage.contains(target);
		}

		if(Objects.equals(id, 3)){
			StorageCuckoo storage = SharedResource.getSCuckoo(node);
			return storage.contains(target);
		}
		Storage storage = SharedResource.getStorage(node);
		// for(Data d: storage.getData()){
		// 	System.out.println("\tNode " + node.getIndex() + " having Data " + d.getID());
		// }
		// System.out.println(storage.contains(target));
		// System.out.println();
		return storage.contains(target);
	}

	public static boolean search(Node node, Data data, int num){
		checkedList.clear();
		checkedList.add(node);

		path.clear();
		next.clear();

		while(stack.size() != 0)
			stack.pop();

		target = data;
		id = num;
		hit = false;

		nextSearch(node, ttl-1);
		if(!hit) return false;

		return true;
	}

	public boolean execute(){
		return false;
	}

}