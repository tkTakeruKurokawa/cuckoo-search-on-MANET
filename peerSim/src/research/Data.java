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

	private static Random random = new Random();
	private int index;
	private int value;
	private int cycle = 0;
	private int peakCycle;
	private int estimate = 0;
	private int maxReplications;
	private int realReplications;
	private boolean lowDemand;
	private boolean peak = false;

	public Data(){}

	public Data(String s){
		size = Configuration.getInt(s + "." + PAR_SIZE, DEFAULT_INITIAL_SIZE);
		variety = Configuration.getInt(s + "." + PAR_VARIETY, DEFAULT_INITIAL_VARIETY);
	}

	public Data(int index, boolean lowDemand){
		this.index = index;
		this.lowDemand = lowDemand;
		peakCycle = random.nextInt(51)+50;
		
		// 低需要
		if(lowDemand){
			int maxReplications = random.nextInt(Network.size()/100);
		}
		// 通常
		else {
		}
	}

	public static void makeData(){
		int probability = random.nextInt(3);
		if(probability == 0) data.add(new Data(id, false));
		else data.add(new Data(id, true));
		id++;
	}

	public int makeReplication(){
		if(!lowDemand){
			if(!peak){
				return increaseNormalDemand();
			}
		}if(lowDemand){
			if(!peak){
				return increaseLowDemand();
			}
		}
		return -1;
	}	

	private int increaseNormalDemand(){
		maxReplications = Network.size();
		int increase = Network.size() / peakCycle;

		if(cycle < peakCycle && (estimate + increase) > maxReplications){
			value = random.nextInt(increase);
			estimate += value;

			cycle++;
		}
		else
			peak = true;

		return value;
	}


	private int increaseLowDemand(){
		int increase = Network.size() / 1000;

		if(cycle < peakCycle && (estimate + increase) > maxReplications){
			value = random.nextInt(increase);
			estimate += value;

			cycle++;
		}
		else
			peak = true;

		return value;
	}


	public void setRealReplications(int realReplications){
		this.realReplications = realReplications;
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

	public int getReplications(int id){
		return getData(id).makeReplication();
	}

	private int getRealReplications(){
		return realReplications;
	}

	public int getPeakCycle(){
		return peakCycle;
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