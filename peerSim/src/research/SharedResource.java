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
	private static ArrayList<Integer> ownerCounter;
	private static ArrayList<Integer> pathCounter;
	private static ArrayList<Integer> relateCounter;
	private static ArrayList<Integer> cuckooCounter;
	private static ArrayList<Boolean> upLoaded;
	private static ArrayList<Double> relateOccu;
	private static ArrayList<Double> cuckooOccu;
	private static ArrayList<Integer> replicaCounterR;
	private static ArrayList<Integer> replicaCounterC;
	private static ArrayList<Data> cyclesRequestList;
	private static ArrayList<Integer> total;

	public SharedResource(String prefix) {
		pid_lnk = Configuration.getPid(prefix + "." + PAR_PROT0);
		pid_prm = Configuration.getPid(prefix + "." + PAR_PROT1);
		pid_crd = Configuration.getPid(prefix + "." + PAR_PROT2);
		pid_str = Configuration.getPid(prefix + "." + PAR_PROT3);
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

	public static void setOwnerCounter(ArrayList<Integer> dc) {
		ownerCounter = dc;
	}

	public static void setPathCounter(ArrayList<Integer> dc) {
		pathCounter = dc;
	}

	public static void setRelateCounter(ArrayList<Integer> dc) {
		relateCounter = dc;
	}

	public static void setCuckooCounter(ArrayList<Integer> dc) {
		cuckooCounter = dc;
	}

	public static void setUpLoaded(ArrayList<Boolean> ul) {
		upLoaded = ul;
	}

	public static void setRelateOccu(ArrayList<Double> ro) {
		relateOccu = ro;
	}

	public static void setCuckooOccu(ArrayList<Double> co) {
		cuckooOccu = co;
	}

	public static void setReplicaCounterR(ArrayList<Integer> rc) {
		replicaCounterR = rc;
	}

	public static void setReplicaCounterC(ArrayList<Integer> rc) {
		replicaCounterC = rc;
	}

	public static void setCyclesRequestList(ArrayList<Data> crl) {
		cyclesRequestList = crl;
	}

	public static void setTotal(String dest, int value) {
		switch (dest) {
		case "owner":
			total.set(0, value);
			break;
		case "path":
			total.set(1, value);
			break;
		case "relate":
			total.set(2, value);
			break;
		case "cuckoo":
			total.set(3, value);
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

	public static ArrayList<Integer> getOwnerCounter() {
		return ownerCounter;
	}

	public static ArrayList<Integer> getPathCounter() {
		return pathCounter;
	}

	public static ArrayList<Integer> getRelateCounter() {
		return relateCounter;
	}

	public static ArrayList<Integer> getCuckooCounter() {
		return cuckooCounter;
	}

	public static ArrayList<Boolean> getUpLoaded() {
		return upLoaded;
	}

	public static ArrayList<Double> getRelateOccu() {
		return relateOccu;
	}

	public static ArrayList<Double> getCuckooOccu() {
		return cuckooOccu;
	}

	public static ArrayList<Integer> getReplicaCounterR() {
		return replicaCounterR;
	}

	public static ArrayList<Integer> getReplicaCounterC() {
		return replicaCounterC;
	}

	public static ArrayList<Data> getCyclesRequestList() {
		return cyclesRequestList;
	}

	public static int getTotal(String dest) {
		int value = 0;
		switch (dest) {
		case "owner":
			value = total.get(0);
			break;
		case "path":
			value = total.get(1);
		case "relate":
			value = total.get(2);
		case "cuckoo":
			value = total.get(3);
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
		ownerCounter = new ArrayList<Integer>();
		pathCounter = new ArrayList<Integer>();
		relateCounter = new ArrayList<Integer>();
		cuckooCounter = new ArrayList<Integer>();
		upLoaded = new ArrayList<Boolean>();
		relateOccu = new ArrayList<Double>();
		cuckooOccu = new ArrayList<Double>();
		replicaCounterR = new ArrayList<Integer>();
		replicaCounterC = new ArrayList<Integer>();
		cyclesRequestList = new ArrayList<Data>();
		total = new ArrayList<Integer>();
		random = new Random();

		for (int i = 0; i < Network.size(); i++) {
			relateOccu.add(0.0);
			cuckooOccu.add(0.0);
		}

		for (int dataID = 0; dataID < Data.getMaxVariety(); dataID++) {
			dataCounter.add(dataID, 0);
			ownerCounter.add(dataID, 0);
			pathCounter.add(dataID, 0);
			relateCounter.add(dataID, 0);
			cuckooCounter.add(dataID, 0);
			replicaCounterR.add(dataID, 0);
			replicaCounterC.add(dataID, 0);
			cyclesRequestList.add(dataID, null);
			upLoaded.add(dataID, false);
		}

		for (int i = 0; i < 4; i++) {
			total.add(0);
		}

		return false;
	}
}