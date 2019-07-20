package research;

import peersim.core.*;
import java.lang.Math;
import java.util.*;

public class RequestProbability implements Protocol {
	private ArrayList<Data> requestList = new ArrayList<Data>();
	private ArrayList<Data> dataList = new ArrayList<Data>();
	private static ArrayList<Integer> rnd = new ArrayList<Integer>();
	private static ArrayList<Integer> hit = new ArrayList<Integer>();
	private static Random random = new Random();

	public RequestProbability(String prefix) {
		for (int i = 0; i < 100; i++) {
			rnd.add(i);
		}
	}

	public Object clone() {
		RequestProbability request = null;
		try {
			request = (RequestProbability) super.clone();
		} catch (CloneNotSupportedException e) {
		}
		request.requestList = new ArrayList<Data>(this.requestList.size());
		request.dataList = new ArrayList<Data>(this.dataList.size());
		// request.rnd = new ArrayList<Integer>(this.rnd.size());
		// request.hit = new ArrayList<Integer>(this.hit.size());
		return request;
	}

	private boolean probability(Double p) {
		hit = new ArrayList<Integer>();
		Collections.shuffle(rnd);

		int num = (int) Math.round(p * 100);
		// System.out.println(num);
		if (num > 0)
			num = random.nextInt(num + 1);
		for (int i = 0; i < num; i++)
			hit.add(rnd.get(i));

		int candidate = random.nextInt(100);
		if (hit.contains(candidate)) {
			// System.out.println(num);
			return true;
		}

		return false;

		// int num = (int) Math.round(p*100);
		// if(num > 1)
		// num = random.nextInt(num)+1;
		// int candidate = random.nextInt(100);
		// if(candidate <= num)
		// return true;

		// return false;
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

	private boolean poisson(int dataID) {
		double lambda = Data.getData(dataID).getLambda();
		int cycle = Data.getData(dataID).getNowCycle();

		double p = Math.exp(-1 * lambda) * (Math.pow(lambda, 1.0)) / factorial(cycle);
		return probability(p);
	}

	public ArrayList<Data> dataRequests(String demandType) {
		requestList = new ArrayList<Data>();

		for (int dataID = 0; dataID < Data.getNowVariety(); dataID++) {
			String dataType = Data.getData(dataID).getType();
			if (Objects.equals(demandType, dataType)) {
				if (poisson(dataID)) {
					requestList.add(Data.getData(dataID));

					ArrayList<Boolean> dataRequest = SharedResource.getDataRequest();
					dataRequest.set(dataID, true);
					SharedResource.setDataRequest(dataRequest);
				}
			}
		}
		return requestList;
	}
}