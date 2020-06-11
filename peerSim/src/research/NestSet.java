package research;

import peersim.core.*;
import peersim.config.*;
import java.lang.Math;
import java.util.*;

public class NestSet implements Control {
	private static final String PAR_SET_SIZE = "nestSize";
	public static int SET_SIZE;
	private static final String PAR_ABA_RATE = "abandonRate";
	private static double ABA_RATE;
	private static Data targetData;

	private Random random = new Random();
	private ArrayList<Integer> rand;
	private ArrayList<Nest> nests;
	private int nestSize;

	public NestSet(Data data) {
		nests = new ArrayList<Nest>();
		rand = new ArrayList<Integer>();
		targetData = data;

		for (int i = 0; i < Network.size(); i++) {
			rand.add(i);
		}

		if (Network.size() < SET_SIZE) {
			nestSize = Network.size();
		} else {
			nestSize = SET_SIZE;
		}

		Collections.shuffle(rand);
		int nestCount = 0;
		int i = 0;
		while (nestCount < nestSize) {
			Node node = Network.get(rand.get(i));
			Storage storage = SharedResource.getNodeStorage("cuckoo", node);
			if (!storage.contains(targetData)) {
				nests.add(new Nest(node));
				nestCount++;
			}

			i++;
		}

		Nest.sort(nests, 0, nestSize - 1);
	}

	public NestSet(String prefix) {
		SET_SIZE = Configuration.getInt(prefix + "." + PAR_SET_SIZE);
		ABA_RATE = Configuration.getDouble(prefix + "." + PAR_ABA_RATE);
	}

	public void alternate(Node base, int cycle, int generation) {
		// Collections.shuffle(rand);

		// int worst = 0;
		// int bound = 0;
		// while (bound < 50) {
		// // System.out.println("r1: " + r1 + " r2: " + r2);
		// // System.out.println("Target Node: " + nests.get(r2).getNode().getIndex() +
		// // "value " + nests.get(r2).getValue()
		// // + " (" + nests.get(r2).egg[0] + ", " + nests.get(r2).egg[1] + ")");
		// boolean success = nests.get(worst).replace(base, cycle);
		// if (success == true) {
		// break;
		// }
		// // System.out.println("Re levyWalk");
		// bound++;
		// }
		nests = Nest.runLevyWalk(nests, base, targetData, cycle, generation);

		// System.out.println("After UPDATE");
		// System.out.println("Target Node: " + nests.get(worst).getNode().getIndex() +
		// "
		// value "
		// + nests.get(worst).getValue() + " (" + nests.get(worst).egg[0] + ", " +
		// nests.get(worst).egg[1] + ")");
		Nest.sort(nests, 0, nestSize - 1);

		runAbandon();
	}

	public void runAbandon() {
		int i = 0;
		int id = 0;
		int abandonNum = (int) Math.floor((double) nestSize * ABA_RATE);
		// System.out.println("ABANDON:");
		while ((i < abandonNum) && (Network.size() > nestSize)) {
			boolean contain = false;
			int newNodeID = rand.get(id);

			// 巣に交換予定のノードが入っているかチェック
			for (int nestID = 0; nestID < nestSize; nestID++) {
				if (Objects.equals(nests.get(nestID).getNode().getIndex(), newNodeID)) {
					contain = true;
				}
			}

			if (contain) {
				id++;
				continue;
			} else {
				// 巣に入っていないかつ，交換予定のノードの評価が交換されるノードより良ければ交換
				// System.out.println("Abandon nestID: " + i);
				nests.get(i).abandon(newNodeID);
				id = 0;
			}
			// System.out.printf("\t%d: ", i);
			// System.out.println("Node: " + nests.get(i).getNode().getIndex() + " value " +
			// nests.get(i).getValue() + " ("
			// + nests.get(i).egg[0] + ", " + nests.get(i).egg[1] + ")");
			// nests.get(i).abandon();
			i++;
		}

		Nest.sort(nests, 0, nestSize - 1);
	}

	public ArrayList<Nest> getNestSet() {
		return nests;
	}

	public Node getBestNode() {
		return nests.get(nests.size() - 1).getNode();
	}

	public Node getBestNode(int num) {
		return nests.get(nests.size() - 1 - num).getNode();
	}

	public int getNestSize() {
		return nestSize;
	}

	public boolean execute() {
		return false;
	}

}