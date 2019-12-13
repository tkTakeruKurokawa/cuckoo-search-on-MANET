package research;

import peersim.core.*;
import java.util.*;

public class InitializeNetwork implements Control {
	private static Random random = new Random();

	public InitializeNetwork(String prefix) {
	}

	public boolean execute() {

		for (int nodeID = 0; nodeID < Network.size(); nodeID++) {
			Node node = Network.get(nodeID);

			NodeParameter parameter = SharedResource.getParameter(node);
			parameter.setParameter();

			NPOwner npo = SharedResource.getNPOwner(node);
			npo.setBattery(parameter.getBattery());
			npo.setCapacity(parameter.getCapacity());

			NPPath npp = SharedResource.getNPPath(node);
			npp.setBattery(parameter.getBattery());
			npp.setCapacity(parameter.getCapacity());

			NPRelate npr = SharedResource.getNPRelate(node);
			npr.setBattery(parameter.getBattery());
			npr.setCapacity(parameter.getCapacity());
			npr.setContribution(random.nextDouble());

			NPCuckoo npc = SharedResource.getNPCuckoo(node);
			npc.setBattery(parameter.getBattery());
			npc.setCapacity(parameter.getCapacity());
			npc.setUpTime(1);

			NodeCoordinate coordinate = SharedResource.getCoordinate(node);
			coordinate.setCoordinate();
		}
		return false;
	}
}