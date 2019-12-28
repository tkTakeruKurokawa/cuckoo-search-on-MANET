package research;

import java.io.*;

public class OutPut {
	private PrintWriter ownerCounter, ownerRemaining, ownerOccupancy, ownerCompare, ownerHit;
	private PrintWriter pathCounter, pathRemaining, pathOccupancy, pathCompare, pathHit;
	private PrintWriter relateCounter, relateRemaining, relateOccupancy, relateCompare, relateHit;
	private PrintWriter cuckooCounter, cuckooRemaining, cuckooOccupancy, cuckooCompare, cuckooHit;
	private PrintWriter links;

	private static Statistic ownerStatistic = new Statistic();
	private static Statistic pathStatistic = new Statistic();
	private static Statistic relateStatistic = new Statistic();
	private static Statistic cuckooStatistic = new Statistic();

	private double totalLinks = 0;

	public OutPut() {
		try {
			File dir = new File("result");
			if (!dir.exists()) {
				dir.mkdir();
			}

			String way = new File(".").getAbsoluteFile().getParent();

			String owner = way + "/result/counter_owner.tsv";
			File counter_owner = new File(owner);
			owner = way + "/result/remaining_owner.tsv";
			File remaining_owner = new File(owner);
			owner = way + "/result/occupancy_owner.tsv";
			File occupancy_owner = new File(owner);
			owner = way + "/result/hitRate_owner.tsv";
			File hitRate_owner = new File(owner);
			owner = way + "/result/compare_owner.tsv";
			File compare_owner = new File(owner);

			String path = way + "/result/counter_path.tsv";
			File counter_path = new File(path);
			path = way + "/result/remaining_path.tsv";
			File remaining_path = new File(path);
			path = way + "/result/occupancy_path.tsv";
			File occupancy_path = new File(path);
			path = way + "/result/hitRate_path.tsv";
			File hitRate_path = new File(path);
			path = way + "/result/compare_path.tsv";
			File compare_path = new File(path);

			String relate = way + "/result/counter_relate.tsv";
			File counter_relate = new File(relate);
			relate = way + "/result/remaining_relate.tsv";
			File remaining_relate = new File(relate);
			relate = way + "/result/occupancy_relate.tsv";
			File occupancy_relate = new File(relate);
			relate = way + "/result/hitRate_relate.tsv";
			File hitRate_relate = new File(relate);
			relate = way + "/result/compare_relate.tsv";
			File compare_relate = new File(relate);

			String cuckoo = way + "/result/counter_cuckoo.tsv";
			File counter_cuckoo = new File(cuckoo);
			cuckoo = way + "/result/remaining_cuckoo.tsv";
			File remaining_cuckoo = new File(cuckoo);
			cuckoo = way + "/result/occupancy_cuckoo.tsv";
			File occupancy_cuckoo = new File(cuckoo);
			cuckoo = way + "/result/hitRate_cuckoo.tsv";
			File hitRate_cuckoo = new File(cuckoo);
			cuckoo = way + "/result/compare_cuckoo.tsv";
			File compare_cuckoo = new File(cuckoo);

			way = new File(".").getAbsoluteFile().getParent() + "/result/averageLinks.tsv";
			File al = new File(way);

			ownerCounter = new PrintWriter(new BufferedWriter(new FileWriter(counter_owner, true)));
			ownerRemaining = new PrintWriter(new BufferedWriter(new FileWriter(remaining_owner, true)));
			ownerOccupancy = new PrintWriter(new BufferedWriter(new FileWriter(occupancy_owner, true)));
			ownerHit = new PrintWriter(new BufferedWriter(new FileWriter(hitRate_owner, true)));
			ownerCompare = new PrintWriter(new BufferedWriter(new FileWriter(compare_owner, true)));

			pathCounter = new PrintWriter(new BufferedWriter(new FileWriter(counter_path, true)));
			pathRemaining = new PrintWriter(new BufferedWriter(new FileWriter(remaining_path, true)));
			pathOccupancy = new PrintWriter(new BufferedWriter(new FileWriter(occupancy_path, true)));
			pathHit = new PrintWriter(new BufferedWriter(new FileWriter(hitRate_path, true)));
			pathCompare = new PrintWriter(new BufferedWriter(new FileWriter(compare_path, true)));

			relateCounter = new PrintWriter(new BufferedWriter(new FileWriter(counter_relate, true)));
			relateRemaining = new PrintWriter(new BufferedWriter(new FileWriter(remaining_relate, true)));
			relateOccupancy = new PrintWriter(new BufferedWriter(new FileWriter(occupancy_relate, true)));
			relateHit = new PrintWriter(new BufferedWriter(new FileWriter(hitRate_relate, true)));
			relateCompare = new PrintWriter(new BufferedWriter(new FileWriter(compare_relate, true)));

			cuckooCounter = new PrintWriter(new BufferedWriter(new FileWriter(counter_cuckoo, true)));
			cuckooRemaining = new PrintWriter(new BufferedWriter(new FileWriter(remaining_cuckoo, true)));
			cuckooOccupancy = new PrintWriter(new BufferedWriter(new FileWriter(occupancy_cuckoo, true)));
			cuckooHit = new PrintWriter(new BufferedWriter(new FileWriter(hitRate_cuckoo, true)));
			cuckooCompare = new PrintWriter(new BufferedWriter(new FileWriter(compare_cuckoo, true)));

			links = new PrintWriter(new BufferedWriter(new FileWriter(al, true)));

		} catch (IOException e) {
			System.out.println(e);
		}

		ownerCounter = setCounterComments(ownerCounter);
		ownerRemaining = setRemainingComments(ownerRemaining);
		ownerOccupancy = setOccupancyComments(ownerOccupancy);
		pathCounter = setCounterComments(pathCounter);
		pathRemaining = setRemainingComments(pathRemaining);
		pathOccupancy = setOccupancyComments(pathOccupancy);
		relateCounter = setCounterComments(relateCounter);
		relateRemaining = setRemainingComments(relateRemaining);
		relateOccupancy = setOccupancyComments(relateOccupancy);
		cuckooCounter = setCounterComments(cuckooCounter);
		cuckooRemaining = setRemainingComments(cuckooRemaining);
		cuckooOccupancy = setOccupancyComments(cuckooOccupancy);

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

	public static void writeCompare(String type, Parameter parameter) {
		switch (type) {
		case "owner":
			ownerStatistic.set(parameter);
			break;
		case "path":
			pathStatistic.set(parameter);
			break;
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

	public void writeHighHitRate(int type, int numberOfHit, double averageHops, int numberOfMiss) {
		switch (type) {
		case 0:
			ownerHit.println("High:");
			ownerHit.println("Number of Hit\tAverage Hops\tNumber of Miss");
			ownerHit.println(numberOfHit + "\t" + averageHops + "\t" + numberOfMiss);
			ownerHit.println();
			break;
		case 1:
			pathHit.println("High:");
			pathHit.println("Number of Hit\tAverage Hops\tNumber of Miss");
			pathHit.println(numberOfHit + "\t" + averageHops + "\t" + numberOfMiss);
			pathHit.println();
			break;
		case 2:
			relateHit.println("High:");
			relateHit.println("Number of Hit\tAverage Hops\tNumber of Miss");
			relateHit.println(numberOfHit + "\t" + averageHops + "\t" + numberOfMiss);
			relateHit.println();
			break;
		case 3:
			cuckooHit.println("High:");
			cuckooHit.println("Number of Hit\tAverage Hops\tNumber of Miss");
			cuckooHit.println(numberOfHit + "\t" + averageHops + "\t" + numberOfMiss);
			cuckooHit.println();
			break;
		default:
			break;
		}
	}

	public void writeLowHitRate(int type, int numberOfHit, double averageHops, int numberOfMiss) {
		switch (type) {
		case 0:
			ownerHit.println("Low:");
			ownerHit.println("Number of Hit\tAverage Hops\tNumber of Miss");
			ownerHit.println(numberOfHit + "\t" + averageHops + "\t" + numberOfMiss);
			ownerHit.println();
			break;
		case 1:
			pathHit.println("Low:");
			pathHit.println("Number of Hit\tAverage Hops\tNumber of Miss");
			pathHit.println(numberOfHit + "\t" + averageHops + "\t" + numberOfMiss);
			pathHit.println();
			break;
		case 2:
			relateHit.println("Low:");
			relateHit.println("Number of Hit\tAverage Hops\tNumber of Miss");
			relateHit.println(numberOfHit + "\t" + averageHops + "\t" + numberOfMiss);
			relateHit.println();
			break;
		case 3:
			cuckooHit.println("Low:");
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

	public void closeFiles() {
		ownerCompare = ownerStatistic.output(ownerCompare);
		pathCompare = pathStatistic.output(pathCompare);
		relateCompare = relateStatistic.output(relateCompare);
		cuckooCompare = cuckooStatistic.output(cuckooCompare);

		ownerCounter.close();
		ownerRemaining.close();
		pathCounter.close();
		pathRemaining.close();
		relateCounter.close();
		relateRemaining.close();
		cuckooCounter.close();
		cuckooRemaining.close();
		ownerOccupancy.close();
		pathOccupancy.close();
		relateOccupancy.close();
		cuckooOccupancy.close();

		ownerHit.close();
		pathHit.close();
		relateHit.close();
		cuckooHit.close();

		ownerCompare.close();
		pathCompare.close();
		relateCompare.close();
		cuckooCompare.close();

		links.close();
	}
}