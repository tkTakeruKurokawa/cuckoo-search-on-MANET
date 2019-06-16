package research;

import peersim.core.*;
import peersim.config.*;
import java.lang.Math;
import java.util.*;

public class NestSet implements Control{
	private static final String PAR_SET_SIZE = "nestSize";
	public static int SET_SIZE;
	private static final String PAR_ABA_RATE = "abandonRate";
	private static double ABA_RATE;

	private Random random = new Random();
	private ArrayList<Integer> rand = new ArrayList<Integer>();
	private ArrayList<Nest> nest = new ArrayList<Nest>();
	private ArrayList<Integer> nodeList = new ArrayList<Integer>();

	public NestSet(){
		nest.clear();
		rand.clear();
		nodeList.clear();

		for(int i=0; i<Network.size(); i++)
			rand.add(i);

		for(int i=0; i<SET_SIZE; i++){
			Node node = Network.get(rand.get(i));
			nest.add(new Nest(node));
			nodeList.add(node.getIndex());
		}

		sort(0, SET_SIZE-1);
	}

	public NestSet(String prefix){
		SET_SIZE = Configuration.getInt(prefix + "." + PAR_SET_SIZE);
		ABA_RATE = Configuration.getDouble(prefix + "." + PAR_ABA_RATE);
	}
	
	public void alternate(){
		Collections.shuffle(rand);

		int r1,r2,addID;
		while(true){
			r1 = random.nextInt(SET_SIZE);
			r2 = (r1 + (random.nextInt(SET_SIZE-1)+1)) % SET_SIZE;

			// System.out.println("r1: " + r1 + " r2: " + r2);
			// System.out.println("Target Node: " + nest.get(r2).getNode().getIndex() + " value " + nest.get(r2).getValue() + " (" + nest.get(r2).egg[0] + ", " + nest.get(r2).egg[1] + ")");
			boolean success = nest.get(r2).replace(nest.get(r1));
			if(success == true){
				addID = nest.get(r2).getAddID();
				break;
			}
			// System.out.println("Re levyWalk");
		}

		nodeList.set(r2, addID); 
		//r2の巣を、r1の巣をベースにlevyWalkし、値を更新したものと置き換え
		//巣の要素が更新される可能性があるのは、巣r2
		// System.out.println("After UPDATE");
		// System.out.println("Target Node: " + nest.get(r2).getNode().getIndex() + " value " + nest.get(r2).getValue() + " (" + nest.get(r2).egg[0] + ", " + nest.get(r2).egg[1] + ")");

		int i = 0;
		int id = 0;
		int j = (int) Math.floor((double)SET_SIZE * ABA_RATE);
		// System.out.println("ABANDAN:");
		while(i<j){
			int nodeID = rand.get(id);
			if(nodeList.contains(nodeID)){
				id++;
				continue;
			}

			// System.out.printf("\t%d: ",i);
			// System.out.println("Node: " + nest.get(i).getNode().getIndex() + " value " + nest.get(i).getValue() + " (" + nest.get(i).egg[0] + ", " + nest.get(i).egg[1] + ")");
			nest.get(i).abandon(nodeID);
			// nest.get(i).abandon();
			nodeList.set(i, nodeID);
			id++;
			i++;
		}
		sort(0, SET_SIZE-1);
	}

	public void sort(int lb, int ub){
		int i,j,k;
		double pivot,iValue,jValue;
		Nest tmpNest;
		int tmpInt;

		if(lb<ub){
			k = (lb+ub)/2;
			pivot = nest.get(k).getValue();
			i = lb;
			j = ub;

			do{
				while(nest.get(i).getValue() < pivot){
					i++;
				}
				while(nest.get(j).getValue() > pivot){
					j--;
				}
				if(i <= j){
					tmpNest = nest.get(i);
					tmpInt = nodeList.get(i);

					nest.set(i, nest.get(j));
					nodeList.set(i, nodeList.get(j));

					nest.set(j, tmpNest);
					nodeList.set(j, tmpInt);
					i++;
					j--;
				}
			}while(i<=j);
			sort(lb, j);
			sort(i, ub);
		}
	}

	public ArrayList<Nest> getNestSet(){
		return nest;
	}

	public Node getBestNode(){
		return nest.get(nest.size()-1).getNode();
	}

	public Node getBestNode(int num){
		return nest.get(nest.size()-1-num).getNode();
	}

	public boolean execute(){
		return false;
	}

	
}