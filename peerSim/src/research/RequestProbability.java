package research;

import peersim.core.*;
import peersim.config.*;
import java.lang.Math;
import java.util.*;

public class RequestProbability implements Protocol{
	private ArrayList<Data> requestList = new ArrayList<Data>();
	private ArrayList<Data> dataList = new ArrayList<Data>();
	private static ArrayList<Integer> rnd = new ArrayList<Integer>();
	private static ArrayList<Integer> hit = new ArrayList<Integer>();
	private static Random random = new Random();
	private Storage storage;

	private double lambda;
	private int peakCycle;
	private int cycle = 1;


	public RequestProbability(){
	}

	public RequestProbability(String prefix){
		for(int i=0; i<100; i++){
			rnd.add(i);
		}
	}

	public Object clone(){
		RequestProbability request = null;
		try{
			request = (RequestProbability) super.clone();
		}catch(CloneNotSupportedException e){
		}
		request.requestList = new ArrayList<Data>(this.requestList.size());
		request.dataList = new ArrayList<Data>(this.dataList.size());
		// request.rnd = new ArrayList<Integer>(this.rnd.size());
		// request.hit = new ArrayList<Integer>(this.hit.size());
		return request;
	}

	private boolean probability(Double p){
		hit.clear();
		Collections.shuffle(rnd);

		int num = (int) Math.round(p*100);
		// System.out.println(count + ", " + num);
		if(num > 1)
			num = random.nextInt(num)+1;
		for(int i=0; i<num; i++)
			hit.add(rnd.get(i));

		int candidate = random.nextInt(100);
		if(hit.contains(candidate)){
			// System.out.println(num);
			return true;
		}

		return false;


		// int num = (int) Math.round(p*100);
		// if(num > 1)
		// 	num = random.nextInt(num)+1;
		// int candidate = random.nextInt(100);
		// if(candidate <= num)
		// 	return true;

		// return false;
	}

	private boolean poisson(int dataID){
		lambda = Data.getData(dataID).getLambda();
		peakCycle = Data.getData(dataID).getPeakCycle();

		if(cycle <= peakCycle){
			double lam = lambda * (double) cycle;
			double p = Math.exp(-1*lam) * (Math.pow(lam,  1.0))/1.0;
			return probability(p);
		}

		return false;

	}

	public ArrayList<Data> dataRequests(){
		requestList.clear();

		for(int dataID=0; dataID<Data.getVariety(); dataID++){
			if(poisson(dataID))
				requestList.add(Data.getData(dataID));
		}

		cycle++;
		return requestList;
	}
}