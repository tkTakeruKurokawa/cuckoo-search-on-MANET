package research;

import peersim.config.*;
import peersim.core.*;
import java.util.*;
import java.lang.Math;

public class Data implements Control{
	private static final int DEFAULT_INITIAL_SIZE = 1;
	private static final String PAR_SIZE = "size";
	private static int size;

	private static final int DEFAULT_INITIAL_VARIETY = 50;
	private static final String PAR_VARIETY = "variety";
	private static int variety;

	private static final int DEFAULT_INITIAL_CYCLES = 1;
	private static final String PAR_CYCLES = "cycles";
	private static int maxCycle;

	private static ArrayList<Data> data = new ArrayList<Data>();
	
	private static int id = 0;

	private static Random random = new Random();
	private int index;
	private int value;
	private int cycle = 0;
	private int peakCycle;
	private double lambda;
	private int estimate = 0;
	private int maxReplications;
	private int realReplications;
	public boolean lowDemand;
	private boolean peak = false;


	public Data(){}

	public Data(String s){
		size = Configuration.getInt(s + "." + PAR_SIZE, DEFAULT_INITIAL_SIZE);
		variety = Configuration.getInt(s + "." + PAR_VARIETY, DEFAULT_INITIAL_VARIETY);
		maxCycle = Configuration.getInt(s + "." + PAR_CYCLES, DEFAULT_INITIAL_CYCLES);
	}

	public Data(int index, boolean lowDemand){
		this.index = index;
		this.lowDemand = lowDemand;
		
		// 低需要
		if(lowDemand){
			// int maxReplications = random.nextInt(Network.size()/100);
			peakCycle = 5;
			// lambda = random.nextDouble()/((double) maxCycle);
			lambda = random.nextDouble()/500.0;
		}
		// 通常
		else {
			if(random.nextBoolean()){
				peakCycle = random.nextInt(25);
			}
			else{
				peakCycle = -1 * random.nextInt(50);
			}
			
			peakCycle = 75 + peakCycle;
			// lambda = random.nextDouble()/((double) maxCycle);
			lambda = random.nextDouble()/500.0;
		}
	}

	public static void makeData(){
		int probability = random.nextInt(3);
		if(probability == 0) data.add(new Data(id, false));
		else data.add(new Data(id, true));
		id++;
	}

	// public int makeReplication(){
	// 	if(!lowDemand){
	// 		if(!peak){
	// 			return increaseNormalDemand();
	// 		}
	// 	}if(lowDemand){
	// 		if(!peak){
	// 			return increaseLowDemand();
	// 		}
	// 	}
	// 	return -1;
	// }


	// private int increaseNormalDemand(){
	// 	maxReplications = Network.size();
	// 	int increase = Network.size() / peakCycle;

	// 	if(cycle < peakCycle && (estimate + increase) > maxReplications){
	// 		value = random.nextInt(increase);
	// 		estimate += value;

	// 		cycle++;
	// 	}
	// 	else
	// 		peak = true;

	// 	return value;
	// }


	// private int increaseLowDemand(){
	// 	int increase = Network.size() / 1000;

	// 	if(cycle < peakCycle && (estimate + increase) > maxReplications){
	// 		value = random.nextInt(increase);
	// 		estimate += value;

	// 		cycle++;
	// 	}
	// 	else
	// 		peak = true;

	// 	return value;
	// }


	public static int getSize(){
		return size;
	}

	public static int getVariety(){
		return variety;
	}

	public int getID(){
		return index;
	}

	// public int getReplications(int id){
	// 	return getData(id).makeReplication();
	// }

	public double getLambda(){
		return lambda;
	}

	public int getPeakCycle(){
		return peakCycle;
	}

	public static Data getData(int id){
		return data.get(id);
	}

	public boolean execute(){
		for (int i=0; i<getVariety(); i++) {
			makeData();
		}

		return false;
	}
}