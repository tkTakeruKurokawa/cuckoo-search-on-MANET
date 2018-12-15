package research;

import peersim.config.*;
import peersim.core.*;
import peersim.util.*;
import java.util.*;

public class printStatus implements Control {

	private Link linkable;
	private Random random = new Random();
	private Node node;
	private static final String PAR_PROT = "protocol";
	private static int pid;

	public printStatus(String prefix){
		pid = Configuration.getPid(prefix + "." + PAR_PROT);
		// System.out.println(FastConfig.numLinkables(pid));
		// System.out.println(FastConfig.getLinkable(pid));
	}

	public boolean execute(){

		// int r = random.nextInt(Network.size());
		// System.out.println();
		// System.out.println("add Node ID: " + r);
		// Node node = Network.get(r);
		// Network.add(node);

		// System.out.println("All Nodes: " + Network.size());
		// System.out.println();
		System.out.println("All Nodes: " + Network.size());


		int r = random.nextInt(Network.size());
		System.out.println();
		System.out.println("remove Node ID: " + r);
		ModifyNetwork.addNode();
		node = Network.get(r);
		ModifyNetwork.removeLink(node);
		ModifyNetwork.removeNode(node);

		System.out.println();
		System.out.println();

		for (int i = 0; i < Network.size(); i++) {
			node = Network.get(i);

			Link linkable = (Link) node.getProtocol(pid);

			// Linkable linkable = (Linkable) Network.get(i).getProtocol(pid);

			System.out.printf("NodeIndex: %d\n   Neighbors Index:", node.getIndex());

			for(int j = 0; j<linkable.degree(); j++){
				Node peer = linkable.getNeighbor(j);
				System.out.printf(" %d", peer.getIndex());
			}
			System.out.println();
		}

		System.out.println();
		System.out.println();

		// System.out.println(node.toString());
		return false;
	}
}