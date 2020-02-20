package research;

import java.util.*;
import peersim.config.*;
import peersim.core.*;
import org.apache.commons.math3.special.*;

public class Data implements Control {
	private static final String PAR_MAXVARIETY = "maxVariety";
	private static int maxVariety;
	private static final String PAR_MAXSIZE = "size";
	private static int maxSize;
	private static final String PAR_DISTRIBUTION = "distribution";
	private static String distribution;

	private static ArrayList<Data> dataList = new ArrayList<Data>();
	private static ArrayList<Double> maxRequesedRate = new ArrayList<Double>();
	private static ArrayList<Integer> uploads = new ArrayList<Integer>();
	private static ArrayList<Integer> requestedNum = new ArrayList<Integer>();
	private static Random random = new Random();

	private static int variety = 0;

	private int localCycle = 0;
	private int index;
	private int size;

	public Data() {
	}

	public Data(String s) {
		maxVariety = Configuration.getInt(s + "." + PAR_MAXVARIETY);
		maxSize = Configuration.getInt(s + "." + PAR_MAXSIZE);
		distribution = Configuration.getString(s + "." + PAR_DISTRIBUTION);
	}

	public Data(int index) {
		this.index = index;
		this.size = maxSize;
	}

	private static int powerLaw() {
		double r;
		do {
			r = random.nextDouble();
		} while (r < 0.1d);

		return (int) Math.round(Math.pow(r, -2.0));
	}

	private static void normalDistribution() {
		double mu = 25.0;
		double sigma = 100.0;
		for (int i = 0; i < maxVariety; i++) {
			double x = (double) i;
			double f = (1.0 / 2.0) * (1.0 + Erf.erf((x - mu) / Math.sqrt(2.0 * sigma)));
			maxRequesedRate.add(f); // リストの要素に最大データ要求割合が入る
		}
		Collections.shuffle(maxRequesedRate); // アップロードされる順番をシャッフル
	}

	private static void powerDistribution() {
		double alfa = 1.0;
		for (int i = 1; i <= maxVariety; i++) {
			double x = (double) i;
			double f = alfa * Math.pow(1, alfa) / Math.pow(x, alfa + 1);
			maxRequesedRate.add(f);
		}
		Collections.shuffle(maxRequesedRate);
	}

	public static void uploadData() {
		uploads.add(variety, powerLaw());

		double rate = maxRequesedRate.get(variety);
		double nodes = Network.size();
		requestedNum.add(variety, ((int) Math.round((nodes * rate))) - uploads.get(variety));

		dataList.add(new Data(variety));

		variety++;
	}

	public static void makeData() {
		if (distribution.equals("normal")) {
			normalDistribution();
		} else if (distribution.equals("power")) {
			powerDistribution();
		} else {
			System.exit(100);
		}
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

	public int getNowCycle() {
		return localCycle;
	}

	public int getID() {
		return index;
	}

	public static int getUploads(int id) {
		return uploads.get(id);
	}

	public static int getRequestedNum(int id) {
		return requestedNum.get(id);
	}

	public static Data getData(int id) {
		return dataList.get(id);
	}

	@Override
	public boolean execute() {

		makeData();

		uploadData();

		return false;
	}
}