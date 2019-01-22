package research;

import peersim.core.*;

public class ExhaustiveSearch{
	
	public static Node search(Data data){
		Node bestNode = null;
		double bestValue = 0.0;

		for(int nodeID=0; nodeID<Network.size(); nodeID++){
			Node node = Network.get(nodeID);
			StorageRelate storage = SharedResource.getSRelate(node);
			NPRelate parameter = SharedResource.getNPRelate(node);

			double battery = parameter.getBattery();
			int capacity = parameter.getCapacity();
			int occupancy = data.getSize();
			int newCapacity = capacity-occupancy;

			if(!storage.contains(data) && newCapacity>=0){
				double b = battery/100.0;
				double c = ((double)capacity)/10.0;
				double value = 1.0 * b + 0.5 * c;
				if(value>bestValue){
					bestNode = node;
					bestValue = value;
				}
			}
		}

		return bestNode;
	}
}