package research;

import peersim.core.*;
import java.lang.Math;
import java.util.*;

public class NodeRequestCycle implements Protocol {
	private ArrayList<Data> requestList = new ArrayList<Data>();
	private ArrayList<Integer> dataList = new ArrayList<Integer>();
	private static ArrayList<Integer> rnd = new ArrayList<Integer>();
	private static ArrayList<Integer> hit = new ArrayList<Integer>();
	private static Random random = new Random();

	public NodeRequestCycle(String prefix) {
		for (int i = 0; i < 100; i++) {
			rnd.add(i);
		}
		for (int i = 0; i < Data.getMaxVariety(); i++) {
			dataList.add(i, -1);
		}
	}

	public Object clone() {
		NodeRequestCycle request = null;
		try {
			request = (NodeRequestCycle) super.clone();
		} catch (CloneNotSupportedException e) {
		}
		request.requestList = new ArrayList<Data>(this.requestList.size());
		request.dataList = new ArrayList<Integer>(this.dataList.size());
		// request.rnd = new ArrayList<Integer>(this.rnd.size());
		// request.hit = new ArrayList<Integer>(this.hit.size());
		return request;
	}

	private boolean probability(Double p) {
		hit = new ArrayList<Integer>();
		Collections.shuffle(rnd);

		int num = (int) Math.round(p * 100);
		// System.out.println(num);
		for (int i = 0; i < num; i++)
			hit.add(rnd.get(i));

		int candidate = random.nextInt(100);
		if (hit.contains(candidate)) {
			// System.out.println(num);
			return true;
		}

		return false;
	}

	public static double factorial(int src) {
		if (src == 0) {
			return 0;
		}
		double value = 1;
		for (int i = 1; i <= src; i++) {
			value *= i;
		}
		// System.out.println(value);
		return ((double) value);
	}

	private int poisson() {
		double lambda = 1.0;

		for (int i = 1; i <= 5; i++) {
			double cycle = ((double) i);
			double p = Math.exp(-1 * lambda) * (Math.pow(lambda, cycle)) / factorial(i);
			boolean success = probability(p);
			if (success == true) {
				return i;
			}
		}

		return -1;
	}

	public ArrayList<Data> getRequestedDatas() {
		requestList = new ArrayList<Data>();

		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			if (dataList.get(dataID) == 0) {
				requestList.add(Data.getData(dataID));
			} else {
				dataList.set(dataID, dataList.get(dataID) - 1);
			}
		}

		return requestList;
	}

	public void setCycle(int dataID) {
		int cycle = poisson();
		dataList.set(dataID, cycle);
	}
}