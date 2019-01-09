package research;

import peersim.config.*;
import peersim.core.*;
import java.util.*;
import java.io.*;
import java.lang.Math;

public class GetReplications implements Control{
	private static final String PAR_ALPHA = "alpha";
	private static double alpha;

	private Random random = new Random();
	private ArrayList<Integer> nowRelate = new ArrayList<Integer>();
	private ArrayList<Integer> preRelate = new ArrayList<Integer>();
	private ArrayList<Integer> relateReplica = new ArrayList<Integer>();
	private ArrayList<Integer> nowCuckoo = new ArrayList<Integer>();
	private ArrayList<Integer> preCuckoo = new ArrayList<Integer>();
	private ArrayList<Integer> cuckooReplica = new ArrayList<Integer>();
	private ArrayList<Boolean> decreaseReplicaR = new ArrayList<Boolean>();
	private ArrayList<Boolean> decreaseReplicaC = new ArrayList<Boolean>();
	private ArrayList<Integer> startReplicaR = new ArrayList<Integer>();
	private ArrayList<Integer> startReplicaC = new ArrayList<Integer>();
	private ArrayList<Double> total = new ArrayList<Double>();
	private ArrayList<Data> cyclesRequestList;

	private Statistic statR = new Statistic();
	private Statistic statC = new Statistic();

	private PrintWriter counterO;
	private PrintWriter counterP;
	private PrintWriter counterR;
	private PrintWriter counterC;
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
			nowRelate.add(i, 0);
			nowCuckoo.add(i, 0);
			preRelate.add(i, 0);
			preCuckoo.add(i, 0);
			relateReplica.add(i, -1);
			cuckooReplica.add(i, -1);
			decreaseReplicaR.add(i, false);
			decreaseReplicaC.add(i, false);
			startReplicaR.add(i, -1);
			startReplicaC.add(i, -1);
		}

		try{
			File dir = new File("result");
			if(!dir.exists()){
				dir.mkdir();
			}

			String way = new File(".").getAbsoluteFile().getParent();

			String owner = way + "/result/owner_counter.csv";
			File ownerR = new File(owner);
			owner = way + "/result/owner_occupancy.csv";
			File ownerO = new File(owner);

			String path  = way + "/result/path_counter.csv";
			File pathR = new File(path);
			path  = way + "/result/path_occupancy.csv";
			File pathO = new File(path);

			String relate  = way + "/result/relate_counter.csv";
			File relateR = new File(relate);
			relate  = way + "/result/relate_occupancy.csv";
			File relateO = new File(relate);
			relate  = way + "/result/relate_compare.csv";
			File relateC = new File(relate);
			relate  = way + "/result/relate_hist.csv";
			File relateH = new File(relate);

			String cuckoo  = way + "/result/cuckoo_counter.csv";
			File cuckooR = new File(cuckoo);
			cuckoo  = way + "/result/cuckoo_occupancy.csv";
			File cuckooO = new File(cuckoo);
			cuckoo  = way + "/result/cuckoo_compare.csv";
			File cuckooC = new File(cuckoo);
			cuckoo  = way + "/result/cuckoo_hist.csv";
			File cuckooH = new File(cuckoo);

			counterO = new PrintWriter(new BufferedWriter(new FileWriter(ownerR, true)));
			counterP = new PrintWriter(new BufferedWriter(new FileWriter(pathR, true)));
			counterR = new PrintWriter(new BufferedWriter(new FileWriter(relateR, true)));
			counterC = new PrintWriter(new BufferedWriter(new FileWriter(cuckooR, true)));

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

		counterO.println();
		counterP.println();
		counterR.println();
		counterC.println();
		occuO.println();
		occuP.println();
		occuR.println();
		occuC.println();
		compR.println();
		compC.println();
		histR.println();
		histC.println();

		counterO.close();
		counterP.close();
		counterR.close();
		counterC.close();
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

	public PrintWriter setCounter(PrintWriter pw, int dataID, int dataNum){

		if(dataID%5 == 0 && dataID%10 != 0){
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
		}

		decreaseReplicaR.set(dataID, false);
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
		}
	}

	public  void owner(){
		ArrayList<Integer> dataCounter = SharedResource.getOwnerCounter();

		if(cycle == 0){
			counterO.printf("cycle");
			for(int i=0; i<5; i++){
				counterO.printf("%5s%d", "Data", i*10+5);
			}
			counterO.println();
		}

		counterO.printf("%d", cycle);
		for(int dataID=0; dataID<Data.getMaxVariety(); dataID++){
			Integer dataNum = dataCounter.get(dataID);

			if(dataNum == null) 
				dataNum = 0;

			counterO = setCounter(counterO, dataID, dataNum);
		}

		counterO.println();
	}

	public  void path(){
		ArrayList<Integer> dataCounter = SharedResource.getPathCounter();

		if(cycle == 0){
			counterP.printf("cycle");
			for(int i=0; i<5; i++){
				counterP.printf("%5s%d", "Data", i*10+5);
			}
			counterP.println();
		}

		counterP.printf("%d", cycle);
		for(int dataID=0; dataID<Data.getMaxVariety(); dataID++){
			Integer dataNum = dataCounter.get(dataID);

			if(dataNum == null) 
				dataNum = 0;

			counterP = setCounter(counterP, dataID, dataNum);
		}

		counterP.println();
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
			counterR.printf("cycle");
			for(int i=0; i<5; i++){
				counterR.printf("%5s%d", "Data", i*10+5);
			}
			counterR.println();
		}

		counterR.printf("%d", cycle);
		for(int dataID=0; dataID<Data.getMaxVariety(); dataID++){
			Integer dataNum = dataCounter.get(dataID);

			nowRelate.set(dataID, dataNum);
			if(preRelate.get(dataID)>nowRelate.get(dataID)){
				decreaseReplicaR.set(dataID, true);
			}
			if(decreaseReplicaR.get(dataID) && cyclesRequestList.contains(Data.getData(dataID))){
				startReplicaR.set(dataID, -1);
				decreaseReplicaR.set(dataID, false);
			}


			if(dataNum < total*0.05){
				if(decreaseReplicaR.get(dataID)){
					if(startReplicaR.get(dataID)<0){
						int num = 5;
						relateReplica.set(dataID, num);
						startReplicaR.set(dataID, cycle);
					}
				}
			}

			if(0 <= startReplicaR.get(dataID)){
					relatedResearch(dataID, dataNum);
			}

			preRelate.set(dataID, nowRelate.get(dataID));

			if(dataNum == null) 
				dataNum = 0;

			counterR = setCounter(counterR, dataID, dataCounter.get(dataID));
		}

		counterR.println();
	}

	public  void cuckoo(){
		ArrayList<Integer> dataCounter = SharedResource.getCuckooCounter();
		ArrayList<Integer> replicaCounter = SharedResource.getReplicaCounter();

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
		System.out.println(total*0.05);

		if(cycle == 0){
			counterC.printf("cycle");
			for(int i=0; i<5; i++){
				counterC.printf("%5s%d", "Data", i*10+5);

			}
			counterC.println();
		}

		counterC.printf("%d", cycle);
		for(int dataID=0; dataID<Data.getMaxVariety(); dataID++){
			Integer dataNum = dataCounter.get(dataID);

			nowCuckoo.set(dataID, dataNum);
			// 現在のデータの数が前回のデータの数より少なくなった場合
			if(preCuckoo.get(dataID)>nowCuckoo.get(dataID)){
				decreaseReplicaC.set(dataID, true);
			}
			// 減少中であるのもかかわらず、現在のデータ数が前回のデータ数を上回った場合
			// ただし、複製配置で増えた分は除く
			if(cyclesRequestList.get(dataID)!=null){
			System.out.println("This cycle request Data " + cyclesRequestList.get(dataID).getID());

			}
			if(decreaseReplicaC.get(dataID) && cyclesRequestList.contains(Data.getData(dataID))){
				startReplicaC.set(dataID, -1);
				decreaseReplicaC.set(dataID, false);
			}

			if(dataNum < total*0.05){
				if(decreaseReplicaC.get(dataID)){
					if(startReplicaC.get(dataID)<0){
						int num = 5;
						cuckooReplica.set(dataID, num);
						startReplicaC.set(dataID, cycle);
					}
				}
			}

			// System.out.println("Data " + dataID + " replica : " + replicaCounter.get(dataID));

			if(0<=startReplicaC.get(dataID) && (cycle-startReplicaC.get(dataID))<100){
				cuckooSearch(dataID, replicaCounter.get(dataID));
			}

			preCuckoo.set(dataID, nowCuckoo.get(dataID));

			if(dataNum == null) 
				dataNum = 0;

			counterC = setCounter(counterC, dataID, dataCounter.get(dataID));

			cyclesRequestList.set(dataID, null);
		}

		counterC.println();
	}


	public boolean execute(){
		cyclesRequestList = SharedResource.getCyclesRequestList();

		owner();
		path();
		relate();
		cuckoo();

		SharedResource.setCyclesRequestList(cyclesRequestList);

		setOccupancy();

		cycle++;
		SharedResource.nextRand();

		if(cycle==500){
			histgram();
			close();

			for(int i=0; i<Data.getMaxVariety(); i++){
				System.out.println("Data " + i + " start Replication cycle");
				System.out.println("Relate: " + startReplicaR.get(i) + " Cuckoo: " + startReplicaC.get(i));				
			}
		}

		return false;
	}

}