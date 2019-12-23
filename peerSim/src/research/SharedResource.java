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

	private static Random random;
	private static int rand;
	private static Data data;
	private static ArrayList<Integer> dataCounter;
	private static ArrayList<Integer> ownerHighCounter;
	private static ArrayList<Integer> ownerLowCounter;
	private static ArrayList<Integer> pathHighCounter;
	private static ArrayList<Integer> pathLowCounter;
	private static ArrayList<Integer> relateHighCounter;
	private static ArrayList<Integer> relateLowCounter;
	private static ArrayList<Integer> cuckooHighCounter;
	private static ArrayList<Integer> cuckooLowCounter;
	private static ArrayList<Boolean> dataRequest;
	private static ArrayList<Integer> highTotal;
	private static ArrayList<Integer> lowTotal;

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
	}

	public static void setDataCounter(ArrayList<Integer> dc) {
		dataCounter = dc;
	}

	public static void setOwnerHighCounter(ArrayList<Integer> dc) {
		ownerHighCounter = dc;
	}

	public static void setOwnerLowCounter(ArrayList<Integer> dc) {
		ownerLowCounter = dc;
	}

	public static void setPathHighCounter(ArrayList<Integer> dc) {
		pathHighCounter = dc;
	}

	public static void setPathLowCounter(ArrayList<Integer> dc) {
		pathLowCounter = dc;
	}

	public static void setRelateHighCounter(ArrayList<Integer> dc) {
		relateHighCounter = dc;
	}

	public static void setRelateLowCounter(ArrayList<Integer> dc) {
		relateLowCounter = dc;
	}

	public static void setCuckooHighCounter(ArrayList<Integer> dc) {
		cuckooHighCounter = dc;
	}

	public static void setCuckooLowCounter(ArrayList<Integer> dc) {
		cuckooLowCounter = dc;
	}

	public static void setDataRequest(ArrayList<Boolean> dr) {
		dataRequest = dr;
	}

	public static void setHighTotal(String dest, int value) {
		switch (dest) {
		case "owner":
			highTotal.set(0, value);
			break;
		case "path":
			highTotal.set(1, value);
			break;
		case "relate":
			highTotal.set(2, value);
			break;
		case "cuckoo":
			highTotal.set(3, value);
			break;
		default:
			break;
		}
	}

	public static void setLowTotal(String dest, int value) {
		switch (dest) {
		case "owner":
			lowTotal.set(0, value);
			break;
		case "path":
			lowTotal.set(1, value);
			break;
		case "relate":
			lowTotal.set(2, value);
			break;
		case "cuckoo":
			lowTotal.set(3, value);
			break;
		default:
			break;
		}
	}

	public static Link getLink(Node node) {
		return (Link) node.getProtocol(pid_lnk);
	}

	public static NodeParameter getParameter(Node node) {
		return (NodeParameter) node.getProtocol(pid_bprm);
	}

	public static NodeCoordinate getCoordinate(Node node) {
		return (NodeCoordinate) node.getProtocol(pid_crd);
	}

	public static NodeRequestCycle getNodeRequestCycle(Node node) {
		return (NodeRequestCycle) node.getProtocol(pid_nrc);
	}

	public static StorageOwner getSOwner(Node node) {
		return (StorageOwner) node.getProtocol(pid_so);
	}

	public static StoragePath getSPath(Node node) {
		return (StoragePath) node.getProtocol(pid_sp);
	}

	public static StorageRelate getSRelate(Node node) {
		return (StorageRelate) node.getProtocol(pid_sr);
	}

	public static StorageCuckoo getSCuckoo(Node node) {
		return (StorageCuckoo) node.getProtocol(pid_sc);
	}

	public static NPOwner getNPOwner(Node node) {
		return (NPOwner) node.getProtocol(pid_npo);
	}

	public static NPPath getNPPath(Node node) {
		return (NPPath) node.getProtocol(pid_npp);
	}

	public static NPRelate getNPRelate(Node node) {
		return (NPRelate) node.getProtocol(pid_npr);
	}

	public static NPCuckoo getNPCuckoo(Node node) {
		return (NPCuckoo) node.getProtocol(pid_npc);
	}

	public static Data getDataInst() {
		return data;
	}

	public static ArrayList<Integer> getDataCounter() {
		return dataCounter;
	}

	public static ArrayList<Integer> getOwnerHighCounter() {
		return ownerHighCounter;
	}

	public static ArrayList<Integer> getOwnerLowCounter() {
		return ownerLowCounter;
	}

	public static ArrayList<Integer> getPathHighCounter() {
		return pathHighCounter;
	}

	public static ArrayList<Integer> getPathLowCounter() {
		return pathLowCounter;
	}

	public static ArrayList<Integer> getRelateHighCounter() {
		return relateHighCounter;
	}

	public static ArrayList<Integer> getRelateLowCounter() {
		return relateLowCounter;
	}

	public static ArrayList<Integer> getCuckooHighCounter() {
		return cuckooHighCounter;
	}

	public static ArrayList<Integer> getCuckooLowCounter() {
		return cuckooLowCounter;
	}

	public static ArrayList<Boolean> getDataRequest() {
		return dataRequest;
	}

	public static int getHighTotal(String dest) {
		int value = 0;
		switch (dest) {
		case "owner":
			return highTotal.get(0);
		case "path":
			return highTotal.get(1);
		case "relate":
			return highTotal.get(2);
		case "cuckoo":
			return highTotal.get(3);
		default:
			break;
		}
		return value;
	}

	public static int getLowTotal(String dest) {
		int value = 0;
		switch (dest) {
		case "owner":
			return lowTotal.get(0);
		case "path":
			return lowTotal.get(1);
		case "relate":
			return lowTotal.get(2);
		case "cuckoo":
			return lowTotal.get(3);
		default:
			break;
		}
		return value;
	}

	public static int getTTL(int base) {
		return base + rand;
	}

	public static void nextRand() {
		rand = random.nextInt(5);
	}

	public boolean execute() {
		data = new Data();
		dataCounter = new ArrayList<Integer>();
		ownerHighCounter = new ArrayList<Integer>();
		ownerLowCounter = new ArrayList<Integer>();
		pathHighCounter = new ArrayList<Integer>();
		pathLowCounter = new ArrayList<Integer>();
		relateHighCounter = new ArrayList<Integer>();
		relateLowCounter = new ArrayList<Integer>();
		cuckooHighCounter = new ArrayList<Integer>();
		cuckooLowCounter = new ArrayList<Integer>();
		highTotal = new ArrayList<Integer>();
		lowTotal = new ArrayList<Integer>();
		dataRequest = new ArrayList<Boolean>();
		random = new Random();

		for (int dataID = 0; dataID < Data.getMaxVariety(); dataID++) {
			dataCounter.add(dataID, 0);
			ownerHighCounter.add(dataID, 0);
			ownerLowCounter.add(dataID, 0);
			pathHighCounter.add(dataID, 0);
			pathLowCounter.add(dataID, 0);
			relateHighCounter.add(dataID, 0);
			relateLowCounter.add(dataID, 0);
			cuckooHighCounter.add(dataID, 0);
			cuckooLowCounter.add(dataID, 0);
			dataRequest.add(dataID, false);
		}

		for (int i = 0; i < 4; i++) {
			highTotal.add(0);
			lowTotal.add(0);
		}

		return false;
	}
}