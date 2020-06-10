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
	private static int distribution;

	private static ArrayList<Data> dataList = new ArrayList<Data>();
	private static ArrayList<Double> maxRequesedRate = new ArrayList<Double>();
	private static ArrayList<Integer> uploads = new ArrayList<Integer>();
	private static ArrayList<Integer> requestedNum = new ArrayList<Integer>();
	private static Random random = new Random();
	private static ArrayList<Double> normalDistributionValues = new ArrayList<Double>(Arrays.asList(0.991802, 0.989276,
			0.986097, 0.982136, 0.977250, 0.971283, 0.964070, 0.955435, 0.945201, 0.933193, 0.919243, 0.903200,
			0.884930, 0.864334, 0.841345, 0.815940, 0.788145, 0.758036, 0.725747, 0.691462, 0.655422, 0.617911,
			0.579260, 0.539828, 0.500000, 0.460172, 0.420740, 0.382089, 0.344578, 0.308538, 0.274253, 0.241964,
			0.211855, 0.184060, 0.158655, 0.135666, 0.115070, 0.096800, 0.080757, 0.066807, 0.054799, 0.044565,
			0.035930, 0.028717, 0.022750, 0.017864, 0.013903, 0.010724, 0.008198, 0.006210));
	private static ArrayList<Double> paretoDistributionValues = new ArrayList<Double>(Arrays.asList(1.000000, 0.250000,
			0.111111, 0.062500, 0.040000, 0.027778, 0.020408, 0.015625, 0.012346, 0.010000, 0.008264, 0.006944,
			0.005917, 0.005102, 0.004444, 0.003906, 0.003460, 0.003086, 0.002770, 0.002500, 0.002268, 0.002066,
			0.001890, 0.001736, 0.001600, 0.001479, 0.001372, 0.001276, 0.001189, 0.001111, 0.001041, 0.000977,
			0.000918, 0.000865, 0.000816, 0.000772, 0.000730, 0.000693, 0.000657, 0.000625, 0.000595, 0.000567,
			0.000541, 0.000517, 0.000494, 0.000473, 0.000453, 0.000434, 0.000416, 0.000400));
	private static int variety = 0;

	private int localCycle = 0;
	private int index;
	private int size;

	public Data() {
	}

	public Data(String s) {
		maxVariety = Configuration.getInt(s + "." + PAR_MAXVARIETY);
		maxSize = Configuration.getInt(s + "." + PAR_MAXSIZE);
		distribution = Configuration.getInt(s + "." + PAR_DISTRIBUTION);
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
		// double mu = 25.0;
		// double sigma = 100.0;
		// for (int i = 0; i < maxVariety; i++) {
		// double x = (double) i;
		// double f = (1.0 / 2.0) * (1.0 + Erf.erf((x - mu) / Math.sqrt(2.0 * sigma)));
		// maxRequesedRate.add(f); // リストの要素に最大データ要求割合が入る
		// }
		// Collections.shuffle(maxRequesedRate); // アップロードされる順番をシャッフル
		// Collections.reverse(maxRequesedRate);

		maxRequesedRate = normalDistributionValues;
	}

	private static void powerDistribution() {
		// double alpha = 1.0;
		// for (int i = 1; i <= maxVariety; i++) {
		// double x = (double) i;
		// double f = alpha * Math.pow(1, alpha) / Math.pow(x, alpha + 1);
		// maxRequesedRate.add(f);
		// }
		// Collections.shuffle(maxRequesedRate);

		maxRequesedRate = paretoDistributionValues;
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
		if (distribution == 0) {
			normalDistribution();
		} else if (distribution == 1) {
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