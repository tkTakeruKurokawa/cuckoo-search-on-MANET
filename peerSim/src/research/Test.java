package research;

import peersim.config.*;
import peersim.core.*;
import peersim.util.*;
import java.util.*;

public class Test implements Control {

	private static final String PAR_PROT = "protocol";
	private static int pid;

	private Random random = new Random();
	private ArrayList<Double> battery = new ArrayList<Double>();
	private ArrayList<Integer> capacity = new ArrayList<Integer>();
	int count = 0;
	double max_b;
	int max_c;
	double min_b;
	int min_c;
	double ave_b=0.0;
	int ave_c=0;


	public Test(String prefix){
		pid = Configuration.getPid(prefix + "." + PAR_PROT);
	}


	public boolean execute(){

		// for(int i=0; i<10; i++){
		Node node = CuckooSearch.search();
		NodeParameter p = (NodeParameter) node.getProtocol(pid);

		// System.out.println("Best Node " + node.getIndex() + " (" + p.getBattery() + ", " + p.getCapacity() + ")");
		battery.add(p.getBattery());
		capacity.add(p.getCapacity());
		if(count == 0){
			max_b = battery.get(0);
			min_b = battery.get(0);
			max_c = capacity.get(0);
			min_c = capacity.get(0);
		}
		for(int i=0; i<battery.size(); i++){
			if(max_b < battery.get(i))
				max_b = battery.get(i);
			if(min_b > battery.get(i))
				min_b = battery.get(i);
			if(max_c < capacity.get(i))
				max_c = capacity.get(i);
			if(min_c > capacity.get(i))
				min_c = capacity.get(i);
			if(count==499){
				ave_b+=battery.get(i);
				ave_c+=capacity.get(i);
			}
		}

		if(count==499){
			System.out.println("Max Battery: "+max_b);
			System.out.println("Min Battery: "+min_b);
			System.out.println("Max Capacity: "+max_c);
			System.out.println("Min Capacity: "+min_c);
			System.out.println("Average Battery: "+ave_b/500.0);
			System.out.println("Average Capacity: "+ave_c/500.0);
			}
		// }

		count++;

		return false;
	}
}