package research;

import peersim.core.*;
import peersim.config.*;
import java.util.*;

public class SharedResource implements Control {
	private static final String PAR_LNK = "link";
	private static int pid_lnk;
	private static final String PAR_CRD = "cordinate";
	private static int pid_crd;
	private static final String PAR_NRC = "node_request_cycle";
	private static int pid_nrc;
	private static final String PAR_SO = "storage_owner";
	private static int pid_so;
	private static final String PAR_SP = "storage_path";
	private static int pid_sp;
	private static final String PAR_SR = "storage_relate";
	private static int pid_sr;
	private static final String PAR_SC = "storage_cuckoo";
	private static int pid_sc;
	private static final String PAR_BPRM = "base_parameter";
	private static int pid_bprm;
	private static final String PAR_NPO = "node_parameter_owner";
	private static int pid_npo;
	private static final String PAR_NPP = "node_parameter_path";
	private static int pid_npp;
	private static final String PAR_NPR = "node_parameter_relate";
	private static int pid_npr;
	private static final String PAR_NPC = "node_parameter_cuckoo";
	private static int pid_npc;
	private static final String PAR_MAX_CYCLE = "max_cycle";
	private static int maxCycle;

	private static Random random;
	private static int rand;
	private static ArrayList<Integer> ownerSearchCost, pathSearchCost, relateSearchCost, cuckooSearchCost;
	private static ArrayList<Integer> ownerReplicationCost, pathReplicationCost, relateReplicationCost,
			cuckooReplicationCost;
	private static ArrayList<Integer> ownerReplicationCount, pathReplicationCount, relateReplicationCount,
			cuckooReplicationCount;
	private static ArrayList<Integer> ownerCounter, pathCounter, relateCounter, cuckooCounter;
	private static ArrayList<Boolean> dataRequest;
	private static ArrayList<Integer> dataTotal;
	private static ArrayList<Integer> accesses;

	public SharedResource(String prefix) {
		pid_lnk = Configuration.getPid(prefix + "." + PAR_LNK);
		pid_crd = Configuration.getPid(prefix + "." + PAR_CRD);
		pid_nrc = Configuration.getPid(prefix + "." + PAR_NRC);
		pid_so = Configuration.getPid(prefix + "." + PAR_SO);
		pid_sp = Configuration.getPid(prefix + "." + PAR_SP);
		pid_sr = Configuration.getPid(prefix + "." + PAR_SR);
		pid_sc = Configuration.getPid(prefix + "." + PAR_SC);
		pid_bprm = Configuration.getPid(prefix + "." + PAR_BPRM);
		pid_npo = Configuration.getPid(prefix + "." + PAR_NPO);
		pid_npp = Configuration.getPid(prefix + "." + PAR_NPP);
		pid_npr = Configuration.getPid(prefix + "." + PAR_NPR);
		pid_npc = Configuration.getPid(prefix + "." + PAR_NPC);
		maxCycle = Configuration.getInt(prefix + "." + PAR_MAX_CYCLE);
	}

	public static void setAccesses(ArrayList<Integer> acc) {
		accesses = acc;
	}

	public static void setSearchCost(int dest, ArrayList<Integer> cost) {
		switch (dest) {
			case 0:
				ownerSearchCost = cost;
				break;
			case 1:
				pathSearchCost = cost;
				break;
			case 2:
				relateSearchCost = cost;
				break;
			case 3:
				cuckooSearchCost = cost;
				break;
			default:
				System.exit(0);
		}
	}

	public static void setReplicationCost(int dest, ArrayList<Integer> cost) {
		switch (dest) {
			case 0:
				ownerReplicationCost = cost;
				break;
			case 1:
				pathReplicationCost = cost;
				break;
			case 2:
				relateReplicationCost = cost;
				break;
			case 3:
				cuckooReplicationCost = cost;
				break;
			default:
				System.exit(0);
		}
	}

	public static void setReplicationCount(int dest, ArrayList<Integer> count) {
		switch (dest) {
			case 0:
				ownerReplicationCount = count;
				break;
			case 1:
				pathReplicationCount = count;
				break;
			case 2:
				relateReplicationCount = count;
				break;
			case 3:
				cuckooReplicationCount = count;
				break;
			default:
				System.exit(0);
		}
	}

	public static void setCounter(String dest, ArrayList<Integer> dc) {
		switch (dest) {
			case "owner":
				ownerCounter = dc;
				break;
			case "path":
				pathCounter = dc;
				break;
			case "relate":
				relateCounter = dc;
				break;
			case "cuckoo":
				cuckooCounter = dc;
				break;
			default:
				System.exit(0);
		}
	}

	public static void setDataTotal(String dest, int value) {
		switch (dest) {
			case "owner":
				dataTotal.set(0, value);
				break;
			case "path":
				dataTotal.set(1, value);
				break;
			case "relate":
				dataTotal.set(2, value);
				break;
			case "cuckoo":
				dataTotal.set(3, value);
				break;
			default:
				System.exit(1);
		}
	}

	public static Link getLink(Node node) {
		return (Link) node.getProtocol(pid_lnk);
	}

	public static BaseParameter getBaseParameter(Node node) {
		return (BaseParameter) node.getProtocol(pid_bprm);
	}

	public static NodeCoordinate getCoordinate(Node node) {
		return (NodeCoordinate) node.getProtocol(pid_crd);
	}

	public static NodeRequestCycle getNodeRequestCycle(Node node) {
		return (NodeRequestCycle) node.getProtocol(pid_nrc);
	}

	public static ArrayList<Integer> getAccesses() {
		return accesses;
	}

	public static Storage getNodeStorage(String dest, Node node) {
		switch (dest) {
			case "owner":
				return (StorageOwner) node.getProtocol(pid_so);
			case "path":
				return (StoragePath) node.getProtocol(pid_sp);
			case "relate":
				return (StorageRelate) node.getProtocol(pid_sr);
			case "cuckoo":
				return (StorageCuckoo) node.getProtocol(pid_sc);
			default:
				break;
		}
		System.exit(2);
		return null;
	}

	public static Parameter getNodeParameter(String dest, Node node) {
		switch (dest) {
			case "owner":
				return (NPOwner) node.getProtocol(pid_npo);
			case "path":
				return (NPPath) node.getProtocol(pid_npp);
			case "relate":
				return (NPRelate) node.getProtocol(pid_npr);
			case "cuckoo":
				return (NPCuckoo) node.getProtocol(pid_npc);
			default:
				break;
		}
		System.exit(3);
		return null;
	}

	public static ArrayList<Integer> getSearchCost(int dest) {
		switch (dest) {
			case 0:
				return ownerSearchCost;
			case 1:
				return pathSearchCost;
			case 2:
				return relateSearchCost;
			case 3:
				return cuckooSearchCost;
			default:
				break;
		}
		System.exit(4);
		return null;
	}

	public static ArrayList<Integer> getReplicationCost(int dest) {
		switch (dest) {
			case 0:
				return ownerReplicationCost;
			case 1:
				return pathReplicationCost;
			case 2:
				return relateReplicationCost;
			case 3:
				return cuckooReplicationCost;
			default:
				break;
		}
		System.exit(4);
		return null;
	}

	public static ArrayList<Integer> getReplicationCount(int dest) {
		switch (dest) {
			case 0:
				return ownerReplicationCount;
			case 1:
				return pathReplicationCount;
			case 2:
				return relateReplicationCount;
			case 3:
				return cuckooReplicationCount;
			default:
				break;
		}
		System.exit(4);
		return null;
	}

	public static ArrayList<Integer> getCounter(String dest) {
		switch (dest) {
			case "owner":
				return ownerCounter;
			case "path":
				return pathCounter;
			case "relate":
				return relateCounter;
			case "cuckoo":
				return cuckooCounter;
			default:
				break;
		}
		System.exit(4);
		return null;
	}

	public static int getDataTotal(String dest) {
		int value = 0;
		switch (dest) {
			case "owner":
				return dataTotal.get(0);
			case "path":
				return dataTotal.get(1);
			case "relate":
				return dataTotal.get(2);
			case "cuckoo":
				return dataTotal.get(3);
			default:
				break;
		}
		System.exit(5);
		return value;
	}

	public static int getTTL(int base) {
		return base + rand;
	}

	public static void nextRand() {
		rand = random.nextInt(5);
	}

	public boolean execute() {
		ownerSearchCost = new ArrayList<Integer>();
		pathSearchCost = new ArrayList<Integer>();
		relateSearchCost = new ArrayList<Integer>();
		cuckooSearchCost = new ArrayList<Integer>();
		ownerReplicationCost = new ArrayList<Integer>();
		pathReplicationCost = new ArrayList<Integer>();
		relateReplicationCost = new ArrayList<Integer>();
		cuckooReplicationCost = new ArrayList<Integer>();
		ownerReplicationCount = new ArrayList<Integer>();
		pathReplicationCount = new ArrayList<Integer>();
		relateReplicationCount = new ArrayList<Integer>();
		cuckooReplicationCount = new ArrayList<Integer>();
		ownerCounter = new ArrayList<Integer>();
		pathCounter = new ArrayList<Integer>();
		relateCounter = new ArrayList<Integer>();
		cuckooCounter = new ArrayList<Integer>();
		dataTotal = new ArrayList<Integer>();
		dataRequest = new ArrayList<Boolean>();
		random = new Random();

		for (int dataID = 0; dataID < Data.getMaxVariety(); dataID++) {
			ownerCounter.add(dataID, 0);
			pathCounter.add(dataID, 0);
			relateCounter.add(dataID, 0);
			cuckooCounter.add(dataID, 0);
			dataRequest.add(dataID, false);
		}

		for (int i = 0; i < 4; i++) {
			dataTotal.add(0);
		}

		for (int i = 0; i < maxCycle; i++) {
			ownerSearchCost.add(i, 0);
			pathSearchCost.add(i, 0);
			relateSearchCost.add(i, 0);
			cuckooSearchCost.add(i, 0);
			ownerReplicationCost.add(i, 0);
			pathReplicationCost.add(i, 0);
			relateReplicationCost.add(i, 0);
			cuckooReplicationCost.add(i, 0);
			ownerReplicationCount.add(i, 0);
			pathReplicationCount.add(i, 0);
			relateReplicationCount.add(i, 0);
			cuckooReplicationCount.add(i, 0);
		}

		return false;
	}
}