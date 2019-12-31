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

	private Random random = new Random();
	private ArrayList<Integer> rand;
	private ArrayList<Nest> nest;
	private int nestSize;

	public NestSet() {
		nest = new ArrayList<Nest>();
		rand = new ArrayList<Integer>();

		for (int i = 0; i < Network.size(); i++) {
			rand.add(i);
		}

		if (Network.size() < SET_SIZE) {
			nestSize = Network.size();
		} else {
			nestSize = SET_SIZE;
		}
		for (int i = 0; i < nestSize; i++) {
			Collections.shuffle(rand);
			Node node = Network.get(rand.get(i));
			nest.add(new Nest(node));
		}

		sort(0, nestSize - 1);
	}

	public NestSet(String prefix) {
		SET_SIZE = Configuration.getInt(prefix + "." + PAR_SET_SIZE);
		ABA_RATE = Configuration.getDouble(prefix + "." + PAR_ABA_RATE);
	}

	public void alternate(int cycle) {
		Collections.shuffle(rand);

		int r1, r2;
		int bound = 0;
		while (bound < 50) {
			r1 = random.nextInt(nestSize);
			r2 = (r1 + (random.nextInt(nestSize - 1) + 1)) % nestSize;

			// System.out.println("r1: " + r1 + " r2: " + r2);
			// System.out.println("Target Node: " + nest.get(r2).getNode().getIndex() +
			// "value " + nest.get(r2).getValue()
			// + " (" + nest.get(r2).egg[0] + ", " + nest.get(r2).egg[1] + ")");
			boolean success = nest.get(r2).replace(nest.get(r1), cycle);
			if (success == true) {
				break;
			}
			// System.out.println("Re levyWalk");
			bound++;
		}
		// r2の巣を、r1の巣をベースにlevyWalkし、値を更新したものと置き換え
		// 巣の要素が更新される可能性があるのは、巣r2
		// System.out.println("After UPDATE");
		// System.out.println("Target Node: " + nest.get(r2).getNode().getIndex() + "
		// value " + nest.get(r2).getValue() + " (" + nest.get(r2).egg[0] + ", " +
		// nest.get(r2).egg[1] + ")");
		sort(0, nestSize - 1);

		int i = 0;
		int id = 0;
		int abandanNum = (int) Math.floor((double) nestSize * ABA_RATE);
		// System.out.println("ABANDAN:");
		while ((i < abandanNum) && (Network.size() > nestSize)) {
			boolean contain = false;
			int newNodeID = rand.get(id);

			// 巣に交換予定のノードが入っているかチェック
			for (int nestID = 0; nestID < nestSize; nestID++) {
				if (Objects.equals(nest.get(nestID).getNode().getIndex(), newNodeID)) {
					contain = true;
				}
			}

			if (contain) {
				id++;
				continue;
			} else {
				// 巣に入っていないかつ，交換予定のノードの評価が交換されるノードより良ければ交換
				// System.out.println("Avandan nestID: " + i);
				nest.get(i).abandon(newNodeID);
				id = 0;
			}
			// System.out.printf("\t%d: ", i);
			// System.out.println("Node: " + nest.get(i).getNode().getIndex() + " value " +
			// nest.get(i).getValue() + " ("
			// + nest.get(i).egg[0] + ", " + nest.get(i).egg[1] + ")");
			// nest.get(i).abandon();
			i++;
		}
		sort(0, nestSize - 1);
	}

	public void sort(int lb, int ub) {
		int i, j, k;
		double pivot;
		Nest tmpNest;

		if (lb < ub) {
			k = (lb + ub) / 2;
			pivot = nest.get(k).getValue();
			i = lb;
			j = ub;

			do {
				while (nest.get(i).getValue() < pivot) {
					i++;
				}
				while (nest.get(j).getValue() > pivot) {
					j--;
				}
				if (i <= j) {
					tmpNest = nest.get(i);

					nest.set(i, nest.get(j));

					nest.set(j, tmpNest);
					i++;
					j--;
				}
			} while (i <= j);
			sort(lb, j);
			sort(i, ub);
		}
	}

	public ArrayList<Nest> getNestSet() {
		return nest;
	}

	public Node getBestNode() {
		return nest.get(nest.size() - 1).getNode();
	}

	public Node getBestNode(int num) {
		return nest.get(nest.size() - 1 - num).getNode();
	}

	public int getNestSize() {
		return nestSize;
	}

	public boolean execute() {
		return false;
	}

}