package research;

import peersim.core.*;
import peersim.config.*;
import java.util.*;

public class SharedResource implements Control {
	private static final String PAR_PROT0 = "protocol0";
	private static int pid_lnk;
	private static final String PAR_PROT1 = "protocol1";
	private static int pid_prm;
	private static final String PAR_PROT2 = "protocol2";
	private static int pid_crd;
	private static final String PAR_PROT3 = "protocol3";
	private static int pid_str;
	private static final String PAR_PROT4 = "protocol4";
	private static int pid_rp;
	private static final String PAR_PROT5 = "protocol5";
	private static int pid_owner;
	private static final String PAR_PROT6 = "protocol6";
	private static int pid_path;
	private static final String PAR_PROT7 = "protocol7";
	private static int pid_relate;
	private static final String PAR_PROT8 = "protocol8";
	private static int pid_cuckoo;
	private static final String PAR_PROT9 = "protocol9";
	private static int pid_npo;
	private static final String PAR_PROT10 = "protocol10";
	private static int pid_npp;
	private static final String PAR_PROT11 = "protocol11";
	private static int pid_npr;
	private static final String PAR_PROT12 = "protocol12";
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
		pid_lnk = Configuration.getPid(prefix + "." + PAR_PROT0);
		pid_prm = Configuration.getPid(prefix + "." + PAR_PROT1);
		pid_crd = Configuration.getPid(prefix + "." + PAR_PROT2);
		// pid_str = Configuration.getPid(prefix + "." + PAR_PROT3);
		pid_rp = Configuration.getPid(prefix + "." + PAR_PROT4);
		pid_owner = Configuration.getPid(prefix + "." + PAR_PROT5);
		pid_path = Configuration.getPid(prefix + "." + PAR_PROT6);
		pid_relate = Configuration.getPid(prefix + "." + PAR_PROT7);
		pid_cuckoo = Configuration.getPid(prefix + "." + PAR_PROT8);
		pid_npo = Configuration.getPid(prefix + "." + PAR_PROT9);
		pid_npp = Configuration.getPid(prefix + "." + PAR_PROT10);
		pid_npr = Configuration.getPid(prefix + "." + PAR_PROT11);
		pid_npc = Configuration.getPid(prefix + "." + PAR_PROT12);
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
		return (NodeParameter) node.getProtocol(pid_prm);
	}

	public static NodeCoordinate getCoordinate(Node node) {
		return (NodeCoordinate) node.getProtocol(pid_crd);
	}

	public static Storage getStorage(Node node) {
		return (Storage) node.getProtocol(pid_str);
	}

	public static RequestProbability getRequestProbability(Node node) {
		return (RequestProbability) node.getProtocol(pid_rp);
	}

	public static StorageOwner getSOwner(Node node) {
		return (StorageOwner) node.getProtocol(pid_owner);
	}

	public static StoragePath getSPath(Node node) {
		return (StoragePath) node.getProtocol(pid_path);
	}

	public static StorageRelate getSRelate(Node node) {
		return (StorageRelate) node.getProtocol(pid_relate);
	}

	public static StorageCuckoo getSCuckoo(Node node) {
		return (StorageCuckoo) node.getProtocol(pid_cuckoo);
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