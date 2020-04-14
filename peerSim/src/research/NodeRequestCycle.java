package research;

import peersim.core.*;
import peersim.config.*;
import java.lang.Math;
import java.util.*;

public class NodeRequestCycle implements Protocol {
	private static final String PAR_MAXVARIETY = "maxVariety";
	private static int maxVariety;

	private ArrayList<Data> requestList = new ArrayList<Data>();
	private ArrayList<Integer> dataList = new ArrayList<Integer>();
	private static ArrayList<Integer> rnd = new ArrayList<Integer>();
	private static ArrayList<Integer> hit = new ArrayList<Integer>();
	private static Random random = new Random();

	public NodeRequestCycle(String prefix) {
		maxVariety = Configuration.getInt(prefix + "." + PAR_MAXVARIETY);

		for (int i = 0; i < 100; i++) {
			rnd.add(i);
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
		for (int i = 0; i < maxVariety; i++) {
			request.dataList.add(i, -1);
		}
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

	public double factorial(int src) {
		if (src == 0) {
			return ((double) 1.0);
		}
		double value = 1;
		for (int i = 1; i <= src; i++) {
			value *= i;
		}
		// System.out.println(value);
		return ((double) value);
	}

	private int poisson() {
		double lambda = random.nextDouble() * 20.0;

		for (int i = 0; i <= 100; i++) {
			double cycle = ((double) i);
			double p = Math.exp(-1 * lambda) * (Math.pow(lambda, cycle)) / factorial(i);
			boolean success = probability(p);
			if (success == true) {
				return i;
			}
		}

		return -1;
	}

	public ArrayList<Data> getRequestedData() {
		requestList = new ArrayList<Data>();

		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			if (dataList.get(dataID).equals(0)) {
				requestList.add(Data.getData(dataID));
			}
			dataList.set(dataID, dataList.get(dataID) - 1);
		}

		return requestList;
	}

	public void setCycle(int dataID) {
		int cycle = poisson();
		dataList.set(dataID, cycle);
	}
}