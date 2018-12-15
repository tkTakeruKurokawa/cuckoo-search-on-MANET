package research;

import peersim.config.*;
import peersim.core.*;
import java.util.*;
import java.lang.Math;

public class Data implements Control{
	private static final int DEFAULT_INITIAL_SIZE = 1;
	private static final String PAR_SIZE = "size";

	private static final int DEFAULT_INITIAL_VARIETY = 50;
	private static final String PAR_VARIETY = "variety";

	private static ArrayList<Data> data = new ArrayList<Data>();
	private static int size;
	private static int variety;
	private static int id = 0;

	private Random random = new Random();
	private int index;
	private boolean lowDemand;
	private int maxNode;
	private int maxReplication;
	private int peakCycle;
	private int replication;
	private int estimateReplication;
	private int realDoenloads;
	private boolean decrease = false;
	private int i = 1;
	private int value;

	public Data(){}

	public Data(String s){
		size = Configuration.getInt(s + "." + PAR_SIZE, DEFAULT_INITIAL_SIZE);
		variety = Configuration.getInt(s + "." + PAR_VARIETY, DEFAULT_INITIAL_VARIETY);
	}

	public Data(int index, boolean lowDemand){
		this.index = index;
		this.lowDemand = lowDemand;
		maxNode = Network.size();
		peakCycle = random.nextInt(51)+50;
		
		// 低需要：max[1, 20]
		if(lowDemand){
			maxReplication = random.nextInt(20)+1;
			replication = 1;
		}
		// 通常：max[最大ノード数の1/4, 最大ノード数-1]
		else {
			maxReplication = random.nextInt(maxNode-maxNode/4)+(maxNode/4);
			replication = maxReplication/peakCycle;
		}
	}

	public void makeData(){
		int probability = random.nextInt(3);
		if(probability == 0) data.add(new Data(id, false));
		else data.add(new Data(id, true));
		id++;
	}

	public int makeReplication(){
		// System.out.printf("No.%d, ",index);
		if(!lowDemand){
			if(!decrease){
				return increaseNormalDemand();
			}else{
				return decreaseNormalDemand();
			}
		}if(lowDemand){
			if(!decrease){
				return increaseLowDemand();
			}else{
				return decreaseLowDemand();
			}
		}
		return -1;
	}	

	private int calculateReplications(int replication){
		return random.nextInt(replication)+(replication-random.nextInt(replication));
	}

	public int increaseNormalDemand(){
		// Random(replication)+(replication - Random(replication))
		if(estimateReplication < maxReplication-(replication*2)){
			value = calculateReplications(replication);
			estimateReplication += value;
		}
		else
			decrease = true;

		// System.out.println(i + ", " + estimateReplication);
		i++;
		return value;
	}

	public int decreaseNormalDemand(){
		// if(estimateReplication <= maxReplication*0.05){
			// 	System.out.println("LowDemand");
			// 	return -1;
			// 	LowDemandRepricate();
			// }
		if(estimateReplication-(replication) < 0){
			estimateReplication = 0;
			// System.out.println(i + ", " + estimateReplication);
			return 0;
		}
		value = random.nextInt(replication);
		estimateReplication -= value;
		// System.out.println(i + ", " + estimateReplication);
		i++;

		return -1*value;
	}

	public int increaseLowDemand(){
			// Random(replication)+(replication - Random(replication))
		if(estimateReplication < maxReplication){
			value = random.nextInt(replication+1);
			estimateReplication += value;
		}
		else
			decrease = true;

		// System.out.println(i + ", " + estimateReplication);
		i++;
		return value;
	}

	public int decreaseLowDemand(){
		// if(estimateReplication <= Math.ceil((double)maxReplication/10.0)){
			// 	System.out.println((int)Math.ceil((double)maxReplication/10.0));
			// 	return -1;
			// 	LowDemandRepricate();
			// }
		if(estimateReplication-(replication) < 0){
			estimateReplication = 0;
			// System.out.println(i + ", " + estimateReplication);
			return 0;
		}
		value = random.nextInt(replication+1);
		estimateReplication -= value;
		// System.out.println(i + ", " + estimateReplication);
		i++;
		
		return -1*value;
	}


	public static int getSize(){
		return size;
	}

	public static int getVariety(){
		return variety;
	}

	public int getID(){
		return index;
	}

	public int getReplication(int id){
		return getData(id).makeReplication();
	}

	public Data getData(int id){
		return data.get(id);
	}

	public boolean execute(){
		for (int i=0; i<getVariety(); i++) {
			makeData();
		}

		return false;
	}
}