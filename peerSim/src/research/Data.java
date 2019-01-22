package research;

import peersim.config.*;
import peersim.core.*;
import java.util.*;
import java.lang.Math;

public class Data implements Control{
	private static final int DEFAULT_INITIAL_CYCLES = 500;
	private static final String PAR_CYCLES = "cycles";
	private static int maxCycle;
	private static final int DEFAULT_INITIAL_MAXVARIETY = 50;
	private static final String PAR_MAXVARIETY = "maxVariety";
	private static int maxVariety;
	private static final int DEFAULT_INITIAL_MAXSIZE = 2;
	private static final String PAR_MAXSIZE = "maxSize";
	private static int maxSize;

	private static ArrayList<Data> dataList = new ArrayList<Data>();
	private static Random random = new Random();

	private static int variety = 0;
	private static int count = -1;
	private int index;
	private int size;
	private int cycle;
	private int peakCycle;
	private double lambda;
	public boolean lowDemand;


	public Data(){}

	public Data(String s){
		maxSize = Configuration.getInt(s + "." + PAR_MAXSIZE, DEFAULT_INITIAL_MAXSIZE);
		maxVariety = Configuration.getInt(s + "." + PAR_MAXVARIETY, DEFAULT_INITIAL_MAXVARIETY);
		maxCycle = Configuration.getInt(s + "." + PAR_CYCLES, DEFAULT_INITIAL_CYCLES);
	}

	public Data(int index, boolean lowDemand){
		this.index = index;
		this.lowDemand = lowDemand;
		this.size = maxSize;
		cycle = 0;
		
		// 低需要
		if(lowDemand){
			// int maxReplications = random.nextInt(Network.size()/100);
			peakCycle = 50;
			// lambda = random.nextDouble()/((double) maxCycle);
			double tmp;
			while(true){
				tmp = random.nextDouble();
				if(tmp > 0.5d)
					break;
			}
			// System.out.println(tmp);
			lambda = tmp/500.0;
		}

		// 通常
		else {
			int sign = 1;
			// boolean flag = random.nextBoolean();
			// if(flag)
			// 	sign = 1;
			// else
			// 	sign = -1;

			peakCycle = 10 + (sign * random.nextInt(60));
			// lambda = random.nextDouble()/((double) maxCycle);
			// lambda = random.nextDouble()/500.0;
			lambda = 1.0/500.0;
		}
	}

	public static void makeData(){
		// int probability = random.nextInt(2);
		// if(probability == 0) dataList.add(new Data(variety, false));
		// else dataList.add(new Data(variety, true));

		// int probability = random.nextInt(5);
		// if(probability == 0) dataList.add(new Data(variety, true));
		// else dataList.add(new Data(variety, false));
		dataList.add(new Data(variety, false));
		variety++;
	}

	public void nextCycle(){
		cycle++;
	}

	public static int getNowVariety(){
		return variety;
	}

	public static int getMaxVariety(){
		return maxVariety;
	}

	public int getSize(){
		return size;
	}

	public int getID(){
		return index;
	}

	public double getLambda(){
		return lambda;
	}

	public int getPeakCycle(){
		return peakCycle;
	}

	public int getNowCycle(){
		return cycle;
	}

	public static Data getData(int id){
		return dataList.get(id);
	}

	public boolean execute(){
		for(int dataID=0; dataID<getNowVariety(); dataID++){
			Data.getData(dataID).nextCycle();			
		}

		if(count%5==0){
			if(getNowVariety()<getMaxVariety())
				makeData();
		}

		count++;

		return false;
	}
}