package research;

import peersim.config.*;
import peersim.core.*;
import java.util.*;
import java.io.*;
import java.lang.Math;

public class GetReplications implements Control{
	private static final String PAR_ALPHA = "alpha";
	private static double alpha;

	private  Random random = new Random();
	private  ArrayList<Integer> maxRelate = new ArrayList<Integer>();
	private  ArrayList<Integer> preRelate = new ArrayList<Integer>();
	private  ArrayList<Integer> relateReplica = new ArrayList<Integer>();
	private  ArrayList<Integer> maxCuckoo = new ArrayList<Integer>();
	private  ArrayList<Integer> preCuckoo = new ArrayList<Integer>();
	private  ArrayList<Integer> cuckooReplica = new ArrayList<Integer>();
	private  ArrayList<Boolean> relateDemand = new ArrayList<Boolean>();
	private  ArrayList<Boolean> cuckooDemand = new ArrayList<Boolean>();
	private  ArrayList<Integer> passCycle = new ArrayList<Integer>();
	private ArrayList<Double> total = new ArrayList<Double>();

	private Statistic statR = new Statistic();
	private Statistic statC = new Statistic();

	private PrintWriter replicaO;
	private PrintWriter replicaP;
	private PrintWriter replicaR;
	private PrintWriter replicaC;
	private PrintWriter occuO;
	private PrintWriter occuP;
	private PrintWriter occuR;
	private PrintWriter occuC;
	private PrintWriter compR;
	private PrintWriter compC;
	private PrintWriter histR;
	private PrintWriter histC;

	private int cycle = 0;


	public GetReplications(String prefix){
		alpha = Configuration.getDouble(prefix + "." + PAR_ALPHA);

		for(int i=0; i<Data.getMaxVariety(); i++){
			maxRelate.add(0);
			maxCuckoo.add(0);
			preRelate.add(0);
			preCuckoo.add(0);
			relateReplica.add(-1);
			cuckooReplica.add(-1);
			relateDemand.add(false);
			cuckooDemand.add(false);
			passCycle.add(-1);
		}

		try{
			String way = new File(".").getAbsoluteFile().getParent();

			String owner = way + "/result/owner_replica.csv";
			File ownerR = new File(owner);
			owner = way + "/result/owner_occupancy.csv";
			File ownerO = new File(owner);

			String path  = way + "/result/path_replica.csv";
			File pathR = new File(path);
			path  = way + "/result/path_occupancy.csv";
			File pathO = new File(path);

			String relate  = way + "/result/relate_replica.csv";
			File relateR = new File(relate);
			relate  = way + "/result/relate_occupancy.csv";
			File relateO = new File(relate);
			relate  = way + "/result/relate_compare.csv";
			File relateC = new File(relate);
			relate  = way + "/result/relate_hist.csv";
			File relateH = new File(relate);

			String cuckoo  = way + "/result/cuckoo_replica.csv";
			File cuckooR = new File(cuckoo);
			cuckoo  = way + "/result/cuckoo_occupancy.csv";
			File cuckooO = new File(cuckoo);
			cuckoo  = way + "/result/cuckoo_compare.csv";
			File cuckooC = new File(cuckoo);
			cuckoo  = way + "/result/cuckoo_hist.csv";
			File cuckooH = new File(cuckoo);

			replicaO = new PrintWriter(new BufferedWriter(new FileWriter(ownerR, true)));
			replicaP = new PrintWriter(new BufferedWriter(new FileWriter(pathR, true)));
			replicaR = new PrintWriter(new BufferedWriter(new FileWriter(relateR, true)));
			replicaC = new PrintWriter(new BufferedWriter(new FileWriter(cuckooR, true)));

			occuO = new PrintWriter(new BufferedWriter(new FileWriter(ownerO, true)));
			occuP = new PrintWriter(new BufferedWriter(new FileWriter(pathO, true)));
			occuR = new PrintWriter(new BufferedWriter(new FileWriter(relateO, true)));
			occuC = new PrintWriter(new BufferedWriter(new FileWriter(cuckooO, true)));
			
			compR = new PrintWriter(new BufferedWriter(new FileWriter(relateC, true)));
			compC = new PrintWriter(new BufferedWriter(new FileWriter(cuckooC, true)));

			histR = new PrintWriter(new BufferedWriter(new FileWriter(relateH, true)));
			histC = new PrintWriter(new BufferedWriter(new FileWriter(cuckooH, true)));

		}catch(IOException e){
			System.out.println(e);
		}
		for(int i=0; i<4; i++){
			total.add(i, 0.0);
		}
	}


	public void close(){
			compR = statR.output(compR);
			compC = statC.output(compC);

			replicaO.println();
			replicaP.println();
			replicaR.println();
			replicaC.println();
			occuO.println();
			occuP.println();
			occuR.println();
			occuC.println();
			compR.println();
			compC.println();
			histR.println();
			histC.println();

			replicaO.close();
			replicaP.close();
			replicaR.close();
			replicaC.close();
			occuO.close();
			occuP.close();
			occuR.close();
			occuC.close();
			compR.close();	
			compC.close();
			histR.close();
			histC.close();
	}

	public void histgram(){
		ArrayList<Double> relateOccu = SharedResource.getRelateOccu();
		ArrayList<Double> cuckooOccu = SharedResource.getCuckooOccu();
		double relateSum = 0.0;
		double cuckooSum = 0.0;
		int count = 0;
		ArrayList<Integer> relateFreq = new ArrayList<Integer>();
		ArrayList<Integer> cuckooFreq = new ArrayList<Integer>();
		for(int i=0; i<10; i++){
			relateFreq.add(i, 0);
			cuckooFreq.add(i, 0);
		}
		for(int i=0; i<Network.size(); i++){
			relateSum = relateOccu.get(i);
			cuckooSum = cuckooOccu.get(i);
			if(relateSum > cuckooSum){
				count++;
			}
			for(int j=0; j<10; j++){
				int value = (int)Math.floor(relateSum/500.0*10.0);
				if(j<=value && value<=j+1){
					relateFreq.set(j, relateFreq.get(j)+1);
				}
				if(value==10){
					relateFreq.set(9, relateFreq.get(9)+1);
				}

				value = (int)Math.floor(cuckooSum/500.0*10.0);
				if(j<=value && value<j+1){
					cuckooFreq.set(j, cuckooFreq.get(j)+1);
				}
				if(value==10){
					cuckooFreq.set(9, cuckooFreq.get(9)+1);
				}
			}
		}

		histR = setHistgram(histR, relateFreq);
		histC = setHistgram(histC, cuckooFreq);
	}

	public PrintWriter setHistgram(PrintWriter pw, ArrayList<Integer> freq){

		pw.printf("Bin\tRange\tFrequency\n");
		for(int i=0; i<10; i++){
			if(i!=9){
				pw.printf("%d\t[%.1f-%.1f)\t%d\n", i, (double)i/10.0, (double)i/10.0+0.1, freq.get(i));
			}else{
				pw.printf("%d\t[%.1f-%.1f]\t%d\n", i, (double)i/10.0, (double)i/10.0+0.1, freq.get(i));
			}
		}

		return pw;
	}

	public void setOccupancy(){
		if(cycle == 0){
			occuO.printf("cycle\tAve: occupancy\n");
			occuP.printf("cycle\tAve: occupancy\n");
			occuR.printf("cycle\tAve: occupancy\n");
			occuC.printf("cycle\tAve: occupancy\n");
		}

		for(int i=0; i<4; i++){
			total.set(i, 0.0d);			
		}
		for(int i=0; i<Network.size(); i++){
			calcOccupancy(SharedResource.getNPOwner(Network.get(i)), 0);
			calcOccupancy(SharedResource.getNPPath(Network.get(i)), 1);
			calcOccupancy(SharedResource.getNPRelate(Network.get(i)), 2);
			calcOccupancy(SharedResource.getNPCuckoo(Network.get(i)), 3);
		}
		occuO.println(cycle + "\t" + (total.get(0)/(double)Network.size()));
		occuP.println(cycle + "\t" + (total.get(1)/(double)Network.size()));
		occuR.println(cycle + "\t" + (total.get(2)/(double)Network.size()));
		occuC.println(cycle + "\t" + (total.get(3)/(double)Network.size()));
	}

	public PrintWriter setReplica(PrintWriter pw, int dataID, int dataNum){

		if(dataID%5 == 0){
			pw.printf("%6d", dataNum);
		}

		return pw;
	}

	public void calcOccupancy(Parameter parameter, int id){
		double occupancy = ((double)parameter.getCapacity()) / 10.0;
		total.set(id, total.get(id)+occupancy);
	}


	public boolean check(Storage storage, Parameter parameter, Data data){
		int capacity = parameter.getCapacity();
		int occupancy = data.getSize();
		int newCapacity = capacity-occupancy;

		if(!storage.contains(data) && (newCapacity>=0)){
			return true;
		}

		return false;
	}

	public  void relatedResearch(int dataID, int dataNum){
		Data data = Data.getData(dataID);
		int diff = relateReplica.get(dataID) - dataNum;
		int addNum=0;
		Node node;

		while(addNum<diff){
			while(true){
				node = Network.get(random.nextInt(Network.size()));
				// long start = System.nanoTime();
				node = RelatedResearch.getBestNode(node, data);
				// long end = System.nanoTime();
				// System.out.println("Relate Time:" + (end - start) / 1000000f + "ms");

				// System.out.println("RR Node: " + node);
				if(!Objects.equals(node, null)){
					break;
				}
			}

			StorageRelate storage = SharedResource.getSRelate(node);
			NPRelate parameter = SharedResource.getNPRelate(node);
			boolean success = check(storage, parameter, data);
			
			if(!success){
				continue;
			}

			statR.set(parameter);
			storage.setReplica(node, data);

			addNum++;
			maxRelate.set(dataID, dataNum+addNum);
		}

		relateDemand.set(dataID, false);
	}

	public  void cuckooSearch(int dataID, int dataNum){
		Data data = Data.getData(dataID);
		int diff = cuckooReplica.get(dataID) - dataNum;
		int addNum=0;
		Node node;

		while(addNum<diff){
			node = CuckooSearch.search(data);
			// System.out.println("CS Node: " + node);

			StorageCuckoo storage = SharedResource.getSCuckoo(node);
			Parameter parameter = SharedResource.getNPCuckoo(node);
			boolean success = check(storage, parameter, data);

			if(!success){
				continue;
			}

			statC.set(parameter);
			storage.setReplica(node, data);

			addNum++;
			maxCuckoo.set(dataID, dataNum+addNum);
		}
		cuckooDemand.set(dataID, false);
	}

	public  void owner(){
		ArrayList<Integer> dataCounter = SharedResource.getOwnerCounter();

		if(cycle == 0){
			replicaO.printf("cycle");
			for(int i=0; i<10; i++){
				replicaO.printf("%5s%d", "Data", i);
			}
			replicaO.println();
		}

		replicaO.printf("%d", cycle);
		for(int dataID=0; dataID<Data.getMaxVariety(); dataID++){
			Integer dataNum = dataCounter.get(dataID);

			if(dataNum == null) 
				dataNum = 0;

			replicaO = setReplica(replicaO, dataID, dataNum);
		}

		replicaO.println();
	}

	public  void path(){
		ArrayList<Integer> dataCounter = SharedResource.getPathCounter();

		if(cycle == 0){
			replicaP.printf("cycle");
			for(int i=0; i<10; i++){
				// replicaP.printf("\tData%d", i);
				replicaP.printf("%5s%d", "Data", i);
			}
			replicaP.println();
		}

		replicaP.printf("%d", cycle);
		for(int dataID=0; dataID<Data.getMaxVariety(); dataID++){
			Integer dataNum = dataCounter.get(dataID);

			if(dataNum == null) 
				dataNum = 0;

			replicaP = setReplica(replicaP, dataID, dataNum);
			// replicaP.printf("%d, %d, ",cycle, dataID);
			// replicaP.println(dataCounter.get(dataID));
		}

		replicaP.println();
	}

	public  void relate(){
		ArrayList<Integer> dataCounter = SharedResource.getRelateCounter();

		ArrayList<Double> relateOccu = SharedResource.getRelateOccu();
		double sum = 0.0;
		for(int i=0; i<Network.size(); i++){
			Parameter parameter = SharedResource.getNPRelate(Network.get(i));
			double occupancy = ((double)parameter.getCapacity()) / 10.0; 

			sum += occupancy;
			relateOccu.set(i, relateOccu.get(i)+occupancy);

		}
		SharedResource.setRelateOccu(relateOccu);

		int total=0;
		for(int i=0; i<Data.getNowVariety(); i++){
			total += dataCounter.get(i);
		}

		if(cycle == 0){
			replicaR.printf("cycle");
			for(int i=0; i<10; i++){
				// replicaR.printf("\tData%d", i);
				replicaR.printf("%5s%d", "Data", i);
			}
			replicaR.println();
		}

		replicaR.printf("%d", cycle);
		for(int dataID=0; dataID<Data.getMaxVariety(); dataID++){
			Integer dataNum = dataCounter.get(dataID);

			maxRelate.set(dataID, dataNum);
			if(preRelate.get(dataID)>maxRelate.get(dataID)){
				maxRelate.set(dataID, preRelate.get(dataID));
				relateDemand.set(dataID, true);
			}


			if(dataNum < total*0.05){
				if(relateDemand.get(dataID)){
					if(relateReplica.get(dataID)<0){
						int num = 5;
						relateReplica.set(dataID, num);
						maxRelate.set(dataID, num);
						relateDemand.set(dataID, false);
					}

					relatedResearch(dataID, dataNum);
				}
			}

			preRelate.set(dataID, maxRelate.get(dataID));

			if(dataNum == null) 
				dataNum = 0;

			replicaR = setReplica(replicaR, dataID, dataNum);
		}

		replicaR.println();
		SharedResource.setRelateCounter(dataCounter);
	}

	public  void cuckoo(){
		ArrayList<Integer> dataCounter = SharedResource.getCuckooCounter();

		double sum = 0.0;
		ArrayList<Double> cuckooOccu = SharedResource.getCuckooOccu();
		for(int i=0; i<Network.size(); i++){
			Parameter parameter = SharedResource.getNPCuckoo(Network.get(i));
			double occupancy = ((double)parameter.getCapacity()) / 10.0; 

			sum += occupancy;
			cuckooOccu.set(i, cuckooOccu.get(i)+occupancy);

		}
		SharedResource.setCuckooOccu(cuckooOccu);

		int total=0;
		for(int i=0; i<Data.getNowVariety(); i++){
			total += dataCounter.get(i);
		}

		if(cycle == 0){
			replicaC.printf("cycle");
			for(int i=0; i<10; i++){
				// replicaC.printf("\tData%d", i);
				replicaC.printf("%5s%d", "Data", i);

			}
			replicaC.println();
		}

		replicaC.printf("%d", cycle);
		for(int dataID=0; dataID<Data.getMaxVariety(); dataID++){
			Integer dataNum = dataCounter.get(dataID);

			if(passCycle.get(dataID)>=0){
				if(dataNum>maxCuckoo.get(dataID)){
					passCycle.set(dataID, -1);
				}else{
					passCycle.set(dataID, passCycle.get(dataID)+1);					
				}
				if(passCycle.get(dataID)==100){
					cuckooReplica.set(dataID, 0);
				}
			}


			maxCuckoo.set(dataID, dataNum);
			if(preCuckoo.get(dataID)>maxCuckoo.get(dataID)){
				maxCuckoo.set(dataID, preCuckoo.get(dataID));
				cuckooDemand.set(dataID, true);

				if(passCycle.get(dataID)==-1){
					passCycle.set(dataID, 0);
				}
			} 
			else{
				cuckooDemand.set(dataID,false);
			}


			if(dataNum < total*0.05){
				if(cuckooDemand.get(dataID)){
					if(cuckooReplica.get(dataID)<0){
						passCycle.set(dataID, 0);

						int num = 5;
						cuckooReplica.set(dataID, num);
						maxCuckoo.set(dataID, num);
						cuckooDemand.set(dataID, false);
					}
					cuckooSearch(dataID, dataNum);
				}
			}

			preCuckoo.set(dataID, maxCuckoo.get(dataID));

			if(dataNum == null) 
				dataNum = 0;

			replicaC = setReplica(replicaC, dataID, dataNum);
		}

		replicaC.println();
		SharedResource.setCuckooCounter(dataCounter);
	}


	public boolean execute(){
		owner();
		path();
		relate();
		cuckoo();

		setOccupancy();
		
		cycle++;
		SharedResource.nextRand();

		if(cycle==500){
			histgram();
			close();
		}

		return false;
	}

}