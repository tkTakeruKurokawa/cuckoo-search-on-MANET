package research;

import peersim.config.*;
import peersim.core.Control;

import java.io.*;

public class OutPut implements Control {
	private static final String PAR_DISTRIBUTION = "distribution";
	private static int distribution;
	private static final String PAR_STOP_CYCLE = "stopCycle";
	private static int stopCycle;

	private PrintWriter ownerCounter, ownerRemaining, ownerOccupancy, ownerHit, ownerSearchCost, ownerReplicationCost;
	private PrintWriter pathCounter, pathRemaining, pathOccupancy, pathHit, pathSearchCost, pathReplicationCost;
	private PrintWriter relateCounter, relateRemaining, relateOccupancy, relateHit, relateSearchCost,
			relateReplicationCost, relateCompare;
	private PrintWriter cuckooCounter, cuckooRemaining, cuckooOccupancy, cuckooHit, cuckooSearchCost,
			cuckooReplicationCost, cuckooCompare;
	private PrintWriter links;
	private static Statistic relateStatistic, cuckooStatistic;

	private double totalLinks = 0;
	private static String directoryName;

	public OutPut(String s) {
		distribution = Configuration.getInt(s + "." + PAR_DISTRIBUTION);
		stopCycle = Configuration.getInt(s + "." + PAR_STOP_CYCLE);

		if (distribution == 0) {
			directoryName = "result" + String.valueOf(stopCycle) + "/normal";
		} else if (distribution == 1) {
			directoryName = "result" + String.valueOf(stopCycle) + "/pareto";
		} else {
			System.exit(-2);
		}
	}

	public OutPut() {
		try {
			File dir = new File(directoryName + "/eps");
			if (!dir.exists()) {
				dir.mkdirs();
			}

			String way = new File(".").getAbsoluteFile().getParent();

			String owner = way + "/" + directoryName + "/counter_owner.tsv";
			File counter_owner = new File(owner);
			owner = way + "/" + directoryName + "/remaining_owner.tsv";
			File remaining_owner = new File(owner);
			owner = way + "/" + directoryName + "/occupancy_owner.tsv";
			File occupancy_owner = new File(owner);
			owner = way + "/" + directoryName + "/hitRate_owner.tsv";
			File hitRate_owner = new File(owner);
			owner = way + "/" + directoryName + "/searchCost_owner.tsv";
			File searchCost_owner = new File(owner);
			owner = way + "/" + directoryName + "/replicationCost_owner.tsv";
			File replicationCost_owner = new File(owner);

			String path = way + "/" + directoryName + "/counter_path.tsv";
			File counter_path = new File(path);
			path = way + "/" + directoryName + "/remaining_path.tsv";
			File remaining_path = new File(path);
			path = way + "/" + directoryName + "/occupancy_path.tsv";
			File occupancy_path = new File(path);
			path = way + "/" + directoryName + "/hitRate_path.tsv";
			File hitRate_path = new File(path);
			path = way + "/" + directoryName + "/searchCost_path.tsv";
			File searchCost_path = new File(path);
			path = way + "/" + directoryName + "/replicationCost_path.tsv";
			File replicationCost_path = new File(path);

			String relate = way + "/" + directoryName + "/counter_relate.tsv";
			File counter_relate = new File(relate);
			relate = way + "/" + directoryName + "/remaining_relate.tsv";
			File remaining_relate = new File(relate);
			relate = way + "/" + directoryName + "/occupancy_relate.tsv";
			File occupancy_relate = new File(relate);
			relate = way + "/" + directoryName + "/hitRate_relate.tsv";
			File hitRate_relate = new File(relate);
			relate = way + "/" + directoryName + "/searchCost_relate.tsv";
			File searchCost_relate = new File(relate);
			relate = way + "/" + directoryName + "/replicationCost_relate.tsv";
			File replicationCost_relate = new File(relate);
			relate = way + "/" + directoryName + "/compare_relate.tsv";
			File compare_relate = new File(relate);

			String cuckoo = way + "/" + directoryName + "/counter_cuckoo.tsv";
			File counter_cuckoo = new File(cuckoo);
			cuckoo = way + "/" + directoryName + "/remaining_cuckoo.tsv";
			File remaining_cuckoo = new File(cuckoo);
			cuckoo = way + "/" + directoryName + "/occupancy_cuckoo.tsv";
			File occupancy_cuckoo = new File(cuckoo);
			cuckoo = way + "/" + directoryName + "/hitRate_cuckoo.tsv";
			File hitRate_cuckoo = new File(cuckoo);
			cuckoo = way + "/" + directoryName + "/searchCost_cuckoo.tsv";
			File searchCost_cuckoo = new File(cuckoo);
			cuckoo = way + "/" + directoryName + "/replicationCost_cuckoo.tsv";
			File replicationCost_cuckoo = new File(cuckoo);
			cuckoo = way + "/" + directoryName + "/compare_cuckoo.tsv";
			File compare_cuckoo = new File(cuckoo);

			way = new File(".").getAbsoluteFile().getParent() + "/" + directoryName + "/averageLinks.tsv";
			File al = new File(way);

			ownerCounter = new PrintWriter(new BufferedWriter(new FileWriter(counter_owner, true)));
			ownerRemaining = new PrintWriter(new BufferedWriter(new FileWriter(remaining_owner, true)));
			ownerOccupancy = new PrintWriter(new BufferedWriter(new FileWriter(occupancy_owner, true)));
			ownerHit = new PrintWriter(new BufferedWriter(new FileWriter(hitRate_owner, true)));
			ownerSearchCost = new PrintWriter(new BufferedWriter(new FileWriter(searchCost_owner, true)));
			ownerReplicationCost = new PrintWriter(new BufferedWriter(new FileWriter(replicationCost_owner, true)));

			pathCounter = new PrintWriter(new BufferedWriter(new FileWriter(counter_path, true)));
			pathRemaining = new PrintWriter(new BufferedWriter(new FileWriter(remaining_path, true)));
			pathOccupancy = new PrintWriter(new BufferedWriter(new FileWriter(occupancy_path, true)));
			pathHit = new PrintWriter(new BufferedWriter(new FileWriter(hitRate_path, true)));
			pathSearchCost = new PrintWriter(new BufferedWriter(new FileWriter(searchCost_path, true)));
			pathReplicationCost = new PrintWriter(new BufferedWriter(new FileWriter(replicationCost_path, true)));

			relateCounter = new PrintWriter(new BufferedWriter(new FileWriter(counter_relate, true)));
			relateRemaining = new PrintWriter(new BufferedWriter(new FileWriter(remaining_relate, true)));
			relateOccupancy = new PrintWriter(new BufferedWriter(new FileWriter(occupancy_relate, true)));
			relateHit = new PrintWriter(new BufferedWriter(new FileWriter(hitRate_relate, true)));
			relateSearchCost = new PrintWriter(new BufferedWriter(new FileWriter(searchCost_relate, true)));
			relateReplicationCost = new PrintWriter(new BufferedWriter(new FileWriter(replicationCost_relate, true)));
			relateCompare = new PrintWriter(new BufferedWriter(new FileWriter(compare_relate, true)));

			cuckooCounter = new PrintWriter(new BufferedWriter(new FileWriter(counter_cuckoo, true)));
			cuckooRemaining = new PrintWriter(new BufferedWriter(new FileWriter(remaining_cuckoo, true)));
			cuckooOccupancy = new PrintWriter(new BufferedWriter(new FileWriter(occupancy_cuckoo, true)));
			cuckooHit = new PrintWriter(new BufferedWriter(new FileWriter(hitRate_cuckoo, true)));
			cuckooSearchCost = new PrintWriter(new BufferedWriter(new FileWriter(searchCost_cuckoo, true)));
			cuckooReplicationCost = new PrintWriter(new BufferedWriter(new FileWriter(replicationCost_cuckoo, true)));
			cuckooCompare = new PrintWriter(new BufferedWriter(new FileWriter(compare_cuckoo, true)));

			links = new PrintWriter(new BufferedWriter(new FileWriter(al, true)));

			relateStatistic = new Statistic();
			cuckooStatistic = new Statistic();

		} catch (IOException e) {
			System.out.println(e);
		}

		ownerCounter = setCounterComments(ownerCounter);
		ownerRemaining = setRemainingComments(ownerRemaining);
		ownerOccupancy = setOccupancyComments(ownerOccupancy);
		ownerSearchCost = setNetworkCostComments(ownerSearchCost);
		ownerReplicationCost = setNetworkCostComments(ownerReplicationCost);
		pathCounter = setCounterComments(pathCounter);
		pathRemaining = setRemainingComments(pathRemaining);
		pathOccupancy = setOccupancyComments(pathOccupancy);
		pathSearchCost = setNetworkCostComments(pathSearchCost);
		pathReplicationCost = setNetworkCostComments(pathReplicationCost);
		relateCounter = setCounterComments(relateCounter);
		relateRemaining = setRemainingComments(relateRemaining);
		relateOccupancy = setOccupancyComments(relateOccupancy);
		relateSearchCost = setNetworkCostComments(relateSearchCost);
		relateReplicationCost = setNetworkCostComments(relateReplicationCost);
		cuckooCounter = setCounterComments(cuckooCounter);
		cuckooRemaining = setRemainingComments(cuckooRemaining);
		cuckooOccupancy = setOccupancyComments(cuckooOccupancy);
		cuckooSearchCost = setNetworkCostComments(cuckooSearchCost);
		cuckooReplicationCost = setNetworkCostComments(cuckooReplicationCost);

		links.println("Cycle\tAvarage Links");
	}

	public PrintWriter setCounterComments(PrintWriter pw) {
		pw.println("Cycle\tAvailability\tNow Replications\tTotal Replications");
		pw.println();

		return pw;
	}

	public PrintWriter setRemainingComments(PrintWriter pw) {
		pw.println("Cycle\tAverage Remaining\tStandard Deviation");
		pw.println();

		return pw;
	}

	public PrintWriter setOccupancyComments(PrintWriter pw) {
		pw.println("Cycle\tCumulative Occupancy");
		pw.println();

		return pw;
	}

	public PrintWriter setNetworkCostComments(PrintWriter pw) {
		pw.println("Cycle\tNow Cost\tAverage Cost\tNumber of Replication");
		pw.println();

		return pw;
	}

	public static void writeCompare(String type, Parameter parameter) {
		switch (type) {
			case "relate":
				relateStatistic.set(parameter);
				break;
			case "cuckoo":
				cuckooStatistic.set(parameter);
				break;
			default:
				break;
		}
	}

	public void writeAvailability(String type, int cycle, double availability, double total, double all) {
		switch (type) {
			case "owner":
				ownerCounter.println(cycle + "\t" + availability + "\t" + total + "\t" + all);
				break;
			case "path":
				pathCounter.println(cycle + "\t" + availability + "\t" + total + "\t" + all);
				break;
			case "relate":
				relateCounter.println(cycle + "\t" + availability + "\t" + total + "\t" + all);
				break;
			case "cuckoo":
				cuckooCounter.println(cycle + "\t" + availability + "\t" + total + "\t" + all);
				break;
			default:
				break;
		}
	}

	public void writeRemaining(int type, int cycle, double averageRemaining, double standardDeviation) {
		switch (type) {
			case 0:
				ownerRemaining.println(cycle + "\t" + averageRemaining + "\t" + standardDeviation);
				break;
			case 1:
				pathRemaining.println(cycle + "\t" + averageRemaining + "\t" + standardDeviation);
				break;
			case 2:
				relateRemaining.println(cycle + "\t" + averageRemaining + "\t" + standardDeviation);
				break;
			case 3:
				cuckooRemaining.println(cycle + "\t" + averageRemaining + "\t" + standardDeviation);
				break;
			default:
				break;
		}
	}

	public void writeOccupancy(int type, int cycle, double occupancy) {
		switch (type) {
			case 0:
				ownerOccupancy.println(cycle + "\t" + occupancy);
				break;
			case 1:
				pathOccupancy.println(cycle + "\t" + occupancy);
				break;
			case 2:
				relateOccupancy.println(cycle + "\t" + occupancy);
				break;
			case 3:
				cuckooOccupancy.println(cycle + "\t" + occupancy);
				break;
			default:
				break;
		}
	}

	public void writeTotalHitRate(int type, int numberOfHit, double averageHops, int numberOfMiss) {
		switch (type) {
			case 0:
				ownerHit.println("Total:");
				ownerHit.println("Number of Hit\tAverage Hops\tNumber of Miss");
				ownerHit.println(numberOfHit + "\t" + averageHops + "\t" + numberOfMiss);
				ownerHit.println();
				break;
			case 1:
				pathHit.println("Total:");
				pathHit.println("Number of Hit\tAverage Hops\tNumber of Miss");
				pathHit.println(numberOfHit + "\t" + averageHops + "\t" + numberOfMiss);
				pathHit.println();
				break;
			case 2:
				relateHit.println("Total:");
				relateHit.println("Number of Hit\tAverage Hops\tNumber of Miss");
				relateHit.println(numberOfHit + "\t" + averageHops + "\t" + numberOfMiss);
				relateHit.println();
				break;
			case 3:
				cuckooHit.println("Total:");
				cuckooHit.println("Number of Hit\tAverage Hops\tNumber of Miss");
				cuckooHit.println(numberOfHit + "\t" + averageHops + "\t" + numberOfMiss);
				cuckooHit.println();
				break;
			default:
				break;
		}
	}

	public void writeAverageLinks(int cycle, double averageLinks) {
		links.println(cycle + "\t" + averageLinks);

		totalLinks += averageLinks;
		if (cycle == 499) {
			links.println("Average Links for 500 cycles:\t" + totalLinks / 500.0);
		}
	}

	public void writeSearchCost(int type, int cycle, int nowCost, double averageCost) {
		switch (type) {
			case 0:
				ownerSearchCost.println(cycle + "\t" + nowCost + "\t" + averageCost);
				break;
			case 1:
				pathSearchCost.println(cycle + "\t" + nowCost + "\t" + averageCost);
				break;
			case 2:
				relateSearchCost.println(cycle + "\t" + nowCost + "\t" + averageCost);
				break;
			case 3:
				cuckooSearchCost.println(cycle + "\t" + nowCost + "\t" + averageCost);
				break;
			default:
				break;
		}
	}

	public void writeReplicationCost(int type, int cycle, int nowCost, double averageCost, int replicationCount) {
		switch (type) {
			case 0:
				ownerReplicationCost.println(cycle + "\t" + nowCost + "\t" + averageCost + "\t" + replicationCount);
				break;
			case 1:
				pathReplicationCost.println(cycle + "\t" + nowCost + "\t" + averageCost + "\t" + replicationCount);
				break;
			case 2:
				relateReplicationCost.println(cycle + "\t" + nowCost + "\t" + averageCost + "\t" + replicationCount);
				break;
			case 3:
				cuckooReplicationCost.println(cycle + "\t" + nowCost + "\t" + averageCost + "\t" + replicationCount);
				break;
			default:
				break;
		}
	}

	public void closeFiles() {
		relateCompare = relateStatistic.output(relateCompare);
		cuckooCompare = cuckooStatistic.output(cuckooCompare);

		ownerCounter.close();
		ownerRemaining.close();
		ownerOccupancy.close();
		ownerHit.close();
		ownerSearchCost.close();
		ownerReplicationCost.close();

		pathCounter.close();
		pathRemaining.close();
		pathOccupancy.close();
		pathHit.close();
		pathSearchCost.close();
		pathReplicationCost.close();

		relateCounter.close();
		relateRemaining.close();
		relateOccupancy.close();
		relateHit.close();
		relateSearchCost.close();
		relateReplicationCost.close();
		relateCompare.close();

		cuckooCounter.close();
		cuckooRemaining.close();
		cuckooOccupancy.close();
		cuckooHit.close();
		cuckooSearchCost.close();
		cuckooReplicationCost.close();
		cuckooCompare.close();

		links.close();
	}

	public boolean execute() {
		return false;
	}
}