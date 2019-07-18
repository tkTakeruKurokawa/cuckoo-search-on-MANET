package research;

import java.util.*;

import peersim.config.*;
import peersim.core.*;

public class Data implements Control {
	private static final String PAR_MAXVARIETY = "maxVariety";
	private static int maxVariety;
	private static final String PAR_MAXSIZE = "size";
	private static int maxSize;
	private static final String PAR_HIGHDEMAND = "highDemand";
	private static int maxHighDemand;
	private static final String PAR_LOWDEMAND = "lowDemand";
	private static int maxLowDemand;

	private static ArrayList<Data> dataList = new ArrayList<Data>();
	private static Random random = new Random();

	private static int highDemand = 0;
	private static int lowDemand = 0;
	private static int variety = 0;
	private static int globalCycle = 0;
	private int localCycle = 0;
	private int index;
	private int size;
	private double lambda;
	private String type;

	public Data() {
	}

	public Data(String s) {
		maxSize = Configuration.getInt(s + "." + PAR_MAXSIZE);
		maxVariety = Configuration.getInt(s + "." + PAR_MAXVARIETY);
		maxHighDemand = Configuration.getInt(s + "." + PAR_HIGHDEMAND);
		maxLowDemand = Configuration.getInt(s + "." + PAR_LOWDEMAND);
	}

	public Data(int index, String string) {
		this.index = index;
		this.size = maxSize;

		// 低需要
		if (Objects.equals(string, "low")) {
			type = "low";
			lambda = ((double) random.nextInt(4) + 1) / 100.0;
		}

		// 高需要
		else {
			type = "high";
			lambda = ((double) random.nextInt(10) + 1) / 10.0;
			// lambda = lambda * 1.5;
		}
	}

	public static void makeData() {
		int probability = random.nextInt(2);
		if (probability == 0) {
			if (lowDemand >= maxLowDemand) {
				dataList.add(new Data(variety, "high"));
				highDemand++;
			} else {
				dataList.add(new Data(variety, "low"));
				lowDemand++;
			}
		} else {
			if (highDemand >= maxHighDemand) {
				dataList.add(new Data(variety, "low"));
				lowDemand++;
			} else {
				dataList.add(new Data(variety, "high"));
				highDemand++;
			}
		}

		variety++;
	}

	public static int getNowVariety() {
		return variety;
	}

	public static int getMaxVariety() {
		return maxVariety;
	}

	public void nextCycle() {
		localCycle++;
	}

	public int getSize() {
		return size;
	}

	public int getID() {
		return index;
	}

	public double getLambda() {
		return lambda;
	}

	public String getType() {
		return type;
	}

	public int getNowCycle() {
		return localCycle;
	}

	public static Data getData(int id) {
		return dataList.get(id);
	}

	@Override
	public boolean execute() {
		for (int dataID = 0; dataID < getNowVariety(); dataID++) {
			Data.getData(dataID).nextCycle();
		}

		if (globalCycle % 5 == 0) {
			if (getNowVariety() < getMaxVariety()) {
				makeData();
			}
		}

		globalCycle++;

		return false;
	}
}