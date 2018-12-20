package research;

import peersim.config.*;
import peersim.core.*;
import java.util.*;

public class ModifyNetwork implements Control{
	private static final String PAR_DEGREE = "k";
	private static int k;
	private static final String PAR_PROT = "protocol";
	private static int pid;

	private static Queue<Node> queue = new ArrayDeque<Node>();
	private static Random random = new Random();


	public ModifyNetwork(String prefix){
		k = Configuration.getInt(prefix + "." + PAR_DEGREE);
		pid = Configuration.getPid(prefix + "." + PAR_PROT);
	}

	public static boolean addLink(Link srcLink, Node addNode){
		return srcLink.addNeighbor(addNode);
	}


	public static boolean addNode(){
		Node newNode = (Node) Network.prototype.clone();
		Network.add(newNode);
		System.out.println("add Node ID: " + newNode.getIndex());

		Link newLink = (Link) newNode.getProtocol(pid);
		int num = random.nextInt(k-1)+1;
		System.out.println("num of Nodes to add: " + num);

		int i=0;
		while(i<num){
			int nodeID = random.nextInt(Network.size());
			System.out.println("Node: " + newNode.getIndex() +  " addNeighbor: " + nodeID);

			if(nodeID == newNode.getIndex()){
				System.out.println("addNodeID == thisNodeID. Done ReRoll.");
				continue;
			}

			boolean success = addLink(newLink, Network.get(nodeID));
			if(!success && num>Network.size()){
				System.out.println("addNodeID == havingNodeID. Done ReRoll.");
				continue;
			}

			Link tmp = (Link) Network.get(nodeID).getProtocol(pid);
			addLink(tmp, newNode);
			i++;
		}
		System.out.println();

		return true;
	}


	public static boolean removeLink(Node node){
		Link nodesLink = (Link) node.getProtocol(pid);

		// 削除するノードのリンクを取得し、隣接ノードをキューに入れる
		for(int i = 0; i<nodesLink.degree(); i++)
			queue.add(nodesLink.getNeighbor(i));

		// 隣接ノードに削除するノードを持つ,ノードから、該当ノードを削除する
		while(true){
			Node n = queue.poll();
			if(n == null) break;
			Link neighborsLink = (Link) n.getProtocol(pid);

			for(int i = 0; i<neighborsLink.degree(); i++){
				if(Objects.equals(neighborsLink.getNeighbor(i), node)){
					// System.out.printf("Node %d's len ", n.getIndex());
					neighborsLink.removeNeighbor(i);
				}	
			}

			if(Network.size() <= 2) return false;

			if(neighborsLink.degree()==0){
				int num = random.nextInt(k-1)+1;
				System.out.println("num of Nodes to add: " + num);
				int j = 0;
				while(j<num){
					int nodeID = random.nextInt(Network.size());
					System.out.println("Node: " + n.getIndex() +  " addNeighbor: " + nodeID);
					if(nodeID == node.getIndex()){
						System.out.println("addNodeID == removeNodeID. Done ReRoll.");
						continue;
					}
					if(nodeID == n.getIndex()){
						System.out.println("addNodeID == thisNodeID. Done ReRoll.");
						continue;	
					}
					// System.out.printf("Node %d's len ", n.getIndex());
					boolean success = addLink(neighborsLink, Network.get(nodeID));
					if(!success && num>Network.size()){						
						System.out.println("addNodeID == havingNodeID. Done ReRoll.");

						continue;
					}

					Link tmp = (Link) Network.get(nodeID).getProtocol(pid);
					addLink(tmp, n);
					j++;
				}
			}

		}
		return true;

	}


	public static boolean removeNode(Node node){
		Network.remove(node.getIndex());
		return true;
	}


	public boolean execute(){
		return false;
	}
}