package research;

import java.io.*;

public class OutPut {
	private PrintWriter ownerHighCounter, pathHighCounter, relateHighCounter, cuckooHighCounter;
	private PrintWriter ownerHighRemaining, pathHighRemaining, relateHighRemaining, cuckooHighRemaining;
	private PrintWriter ownerLowCounter, pathLowCounter, relateLowCounter, cuckooLowCounter;
	private PrintWriter ownerLowRemaining, pathLowRemaining, relateLowRemaining, cuckooLowRemaining;
	private PrintWriter ownerHighOccupancy, pathHighOccupancy, relateHighOccupancy, cuckooHighOccupancy;
	private PrintWriter ownerLowOccupancy, pathLowOccupancy, relateLowOccupancy, cuckooLowOccupancy;
	private PrintWriter ownerCompare, pathCompare, relateCompare, cuckooCompare;
	private PrintWriter ownerHit, pathHit, relateHit, cuckooHit;
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

			String owner = way + "/result/highCounter_owner.tsv";
			File hco = new File(owner);
			owner = way + "/result/lowCounter_owner.tsv";
			File lco = new File(owner);
			owner = way + "/result/highRemaining_owner.tsv";
			File hro = new File(owner);
			owner = way + "/result/lowRemaining_owner.tsv";
			File lro = new File(owner);
			owner = way + "/result/highOccupancy_owner.tsv";
			File hoo = new File(owner);
			owner = way + "/result/lowOccupancy_owner.tsv";
			File loo = new File(owner);
			owner = way + "/result/hitRate_owner.tsv";
			File ho = new File(owner);
			owner = way + "/result/compare_owner.tsv";
			File co = new File(owner);

			String path = way + "/result/highCounter_path.tsv";
			File hcp = new File(path);
			path = way + "/result/lowCounter_path.tsv";
			File lcp = new File(path);
			path = way + "/result/highRemaining_path.tsv";
			File hrp = new File(path);
			path = way + "/result/lowRemaining_path.tsv";
			File lrp = new File(path);
			path = way + "/result/highOccupancy_path.tsv";
			File hop = new File(path);
			path = way + "/result/lowOccupancy_path.tsv";
			File lop = new File(path);
			path = way + "/result/hitRate_path.tsv";
			File hp = new File(path);
			path = way + "/result/compare_path.tsv";
			File cp = new File(path);

			String relate = way + "/result/highCounter_relate.tsv";
			File hcr = new File(relate);
			relate = way + "/result/lowCounter_relate.tsv";
			File lcr = new File(relate);
			relate = way + "/result/highRemaining_relate.tsv";
			File hrr = new File(relate);
			relate = way + "/result/lowRemaining_relate.tsv";
			File lrr = new File(relate);
			relate = way + "/result/highOccupancy_relate.tsv";
			File hor = new File(relate);
			relate = way + "/result/lowOccupancy_relate.tsv";
			File lor = new File(relate);
			relate = way + "/result/hitRate_relate.tsv";
			File hr = new File(relate);
			relate = way + "/result/compare_relate.tsv";
			File cr = new File(relate);

			String cuckoo = way + "/result/highCounter_cuckoo.tsv";
			File hcc = new File(cuckoo);
			cuckoo = way + "/result/lowCounter_cuckoo.tsv";
			File lcc = new File(cuckoo);
			cuckoo = way + "/result/highRemaining_cuckoo.tsv";
			File hrc = new File(cuckoo);
			cuckoo = way + "/result/lowRemaining_cuckoo.tsv";
			File lrc = new File(cuckoo);
			cuckoo = way + "/result/highOccupancy_cuckoo.tsv";
			File hoc = new File(cuckoo);
			cuckoo = way + "/result/lowOccupancy_cuckoo.tsv";
			File loc = new File(cuckoo);
			cuckoo = way + "/result/hitRate_cuckoo.tsv";
			File hc = new File(cuckoo);
			cuckoo = way + "/result/compare_cuckoo.tsv";
			File cc = new File(cuckoo);

			way = new File(".").getAbsoluteFile().getParent() + "/result/averageLinks.tsv";
			File al = new File(way);

			ownerHighCounter = new PrintWriter(new BufferedWriter(new FileWriter(hco, true)));
			pathHighCounter = new PrintWriter(new BufferedWriter(new FileWriter(hcp, true)));
			relateHighCounter = new PrintWriter(new BufferedWriter(new FileWriter(hcr, true)));
			cuckooHighCounter = new PrintWriter(new BufferedWriter(new FileWriter(hcc, true)));

			ownerHighRemaining = new PrintWriter(new BufferedWriter(new FileWriter(hro, true)));
			pathHighRemaining = new PrintWriter(new BufferedWriter(new FileWriter(hrp, true)));
			relateHighRemaining = new PrintWriter(new BufferedWriter(new FileWriter(hrr, true)));
			cuckooHighRemaining = new PrintWriter(new BufferedWriter(new FileWriter(hrc, true)));

			ownerLowCounter = new PrintWriter(new BufferedWriter(new FileWriter(lco, true)));
			pathLowCounter = new PrintWriter(new BufferedWriter(new FileWriter(lcp, true)));
			relateLowCounter = new PrintWriter(new BufferedWriter(new FileWriter(lcr, true)));
			cuckooLowCounter = new PrintWriter(new BufferedWriter(new FileWriter(lcc, true)));

			ownerLowRemaining = new PrintWriter(new BufferedWriter(new FileWriter(lro, true)));
			pathLowRemaining = new PrintWriter(new BufferedWriter(new FileWriter(lrp, true)));
			relateLowRemaining = new PrintWriter(new BufferedWriter(new FileWriter(lrr, true)));
			cuckooLowRemaining = new PrintWriter(new BufferedWriter(new FileWriter(lrc, true)));

			ownerHighOccupancy = new PrintWriter(new BufferedWriter(new FileWriter(hoo, true)));
			pathHighOccupancy = new PrintWriter(new BufferedWriter(new FileWriter(hop, true)));
			relateHighOccupancy = new PrintWriter(new BufferedWriter(new FileWriter(hor, true)));
			cuckooHighOccupancy = new PrintWriter(new BufferedWriter(new FileWriter(hoc, true)));

			ownerLowOccupancy = new PrintWriter(new BufferedWriter(new FileWriter(loo, true)));
			pathLowOccupancy = new PrintWriter(new BufferedWriter(new FileWriter(lop, true)));
			relateLowOccupancy = new PrintWriter(new BufferedWriter(new FileWriter(lor, true)));
			cuckooLowOccupancy = new PrintWriter(new BufferedWriter(new FileWriter(loc, true)));

			ownerHit = new PrintWriter(new BufferedWriter(new FileWriter(ho, true)));
			pathHit = new PrintWriter(new BufferedWriter(new FileWriter(hp, true)));
			relateHit = new PrintWriter(new BufferedWriter(new FileWriter(hr, true)));
			cuckooHit = new PrintWriter(new BufferedWriter(new FileWriter(hc, true)));

			ownerCompare = new PrintWriter(new BufferedWriter(new FileWriter(co, true)));
			pathCompare = new PrintWriter(new BufferedWriter(new FileWriter(cp, true)));
			relateCompare = new PrintWriter(new BufferedWriter(new FileWriter(cr, true)));
			cuckooCompare = new PrintWriter(new BufferedWriter(new FileWriter(cc, true)));

			links = new PrintWriter(new BufferedWriter(new FileWriter(al, true)));

		} catch (IOException e) {
			System.out.println(e);
		}

		ownerHighCounter = setCounterComments(ownerHighCounter);
		ownerHighRemaining = setRemainingComments(ownerHighRemaining);
		ownerHighOccupancy = setOccupancyComments(ownerHighOccupancy);
		pathHighCounter = setCounterComments(pathHighCounter);
		pathHighRemaining = setRemainingComments(pathHighRemaining);
		pathHighOccupancy = setOccupancyComments(pathHighOccupancy);
		relateHighCounter = setCounterComments(relateHighCounter);
		relateHighRemaining = setRemainingComments(relateHighRemaining);
		relateHighOccupancy = setOccupancyComments(relateHighOccupancy);
		cuckooHighCounter = setCounterComments(cuckooHighCounter);
		cuckooHighRemaining = setRemainingComments(cuckooHighRemaining);
		cuckooHighOccupancy = setOccupancyComments(cuckooHighOccupancy);

		ownerLowCounter = setCounterComments(ownerLowCounter);
		ownerLowRemaining = setRemainingComments(ownerLowRemaining);
		ownerLowOccupancy = setOccupancyComments(ownerLowOccupancy);
		pathLowCounter = setCounterComments(pathLowCounter);
		pathLowRemaining = setRemainingComments(pathLowRemaining);
		pathLowOccupancy = setOccupancyComments(pathLowOccupancy);
		relateLowCounter = setCounterComments(relateLowCounter);
		relateLowRemaining = setRemainingComments(relateLowRemaining);
		relateLowOccupancy = setOccupancyComments(relateLowOccupancy);
		cuckooLowCounter = setCounterComments(cuckooLowCounter);
		cuckooLowRemaining = setRemainingComments(cuckooLowRemaining);
		cuckooLowOccupancy = setOccupancyComments(cuckooLowOccupancy);

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

	public void writeHighCount(String type, int cycle, double highAvailability, double highTotal, double highAll) {
		switch (type) {
		case "owner":
			ownerHighCounter.println(cycle + "\t" + highAvailability + "\t" + highTotal + "\t" + highAll);
			break;
		case "path":
			pathHighCounter.println(cycle + "\t" + highAvailability + "\t" + highTotal + "\t" + highAll);
			break;
		case "relate":
			relateHighCounter.println(cycle + "\t" + highAvailability + "\t" + highTotal + "\t" + highAll);
			break;
		case "cuckoo":
			cuckooHighCounter.println(cycle + "\t" + highAvailability + "\t" + highTotal + "\t" + highAll);
			break;
		default:
			break;
		}
	}

	public void writeLowCount(String type, int cycle, double lowhAvailability, double lowhTotal, double lowhAll) {
		switch (type) {
		case "owner":
			ownerLowCounter.println(cycle + "\t" + lowhAvailability + "\t" + lowhTotal + "\t" + lowhAll);
			break;
		case "path":
			pathLowCounter.println(cycle + "\t" + lowhAvailability + "\t" + lowhTotal + "\t" + lowhAll);
			break;
		case "relate":
			relateLowCounter.println(cycle + "\t" + lowhAvailability + "\t" + lowhTotal + "\t" + lowhAll);
			break;
		case "cuckoo":
			cuckooLowCounter.println(cycle + "\t" + lowhAvailability + "\t" + lowhTotal + "\t" + lowhAll);
			break;
		default:
			break;
		}
	}

	public void writeHighRemaining(int type, int cycle, double averageRemaining, double standardDeviation) {
		switch (type) {
		case 0:
			ownerHighRemaining.println(cycle + "\t" + averageRemaining + "\t" + standardDeviation);
			break;
		case 1:
			pathHighRemaining.println(cycle + "\t" + averageRemaining + "\t" + standardDeviation);
			break;
		case 2:
			relateHighRemaining.println(cycle + "\t" + averageRemaining + "\t" + standardDeviation);
			break;
		case 3:
			cuckooHighRemaining.println(cycle + "\t" + averageRemaining + "\t" + standardDeviation);
			break;
		default:
			break;
		}
	}

	public void writeLowRemaining(int type, int cycle, double averageRemaining, double standardDeviation) {
		switch (type) {
		case 0:
			ownerLowRemaining.println(cycle + "\t" + averageRemaining + "\t" + standardDeviation);
			break;
		case 1:
			pathLowRemaining.println(cycle + "\t" + averageRemaining + "\t" + standardDeviation);
			break;
		case 2:
			relateLowRemaining.println(cycle + "\t" + averageRemaining + "\t" + standardDeviation);
			break;
		case 3:
			cuckooLowRemaining.println(cycle + "\t" + averageRemaining + "\t" + standardDeviation);
			break;
		default:
			break;
		}
	}

	public void writeHighOccupancy(int type, int cycle, double occupancy) {
		switch (type) {
		case 0:
			ownerHighOccupancy.println(cycle + "\t" + occupancy);
			break;
		case 1:
			pathHighOccupancy.println(cycle + "\t" + occupancy);
			break;
		case 2:
			relateHighOccupancy.println(cycle + "\t" + occupancy);
			break;
		case 3:
			cuckooHighOccupancy.println(cycle + "\t" + occupancy);
			break;
		default:
			break;
		}
	}

	public void writeLowOccupancy(int type, int cycle, double occupancy) {
		switch (type) {
		case 0:
			ownerLowOccupancy.println(cycle + "\t" + occupancy);
			break;
		case 1:
			pathLowOccupancy.println(cycle + "\t" + occupancy);
			break;
		case 2:
			relateLowOccupancy.println(cycle + "\t" + occupancy);
			break;
		case 3:
			cuckooLowOccupancy.println(cycle + "\t" + occupancy);
			break;
		default:
			break;
		}
	}

<<<<<<< HEAD
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
=======
	public void writeHitRate(int type, int numberOfHit, double averageHops, int numberOfMiss) {
		switch (type) {
		case 0:
			ownerHit.println("Number of Hit\tAverage Hops\tNumber of Miss");
			ownerHit.println(numberOfHit + "\t" + averageHops + "\t" + numberOfMiss);
			break;
		case 1:
			pathHit.println("Number of Hit\tAverage Hops\tNumber of Miss");
			pathHit.println(numberOfHit + "\t" + averageHops + "\t" + numberOfMiss);
			break;
		case 2:
			relateHit.println("Number of Hit\tAverage Hops\tNumber of Miss");
			relateHit.println(numberOfHit + "\t" + averageHops + "\t" + numberOfMiss);
			break;
		case 3:
			cuckooHit.println("Number of Hit\tAverage Hops\tNumber of Miss");
			cuckooHit.println(numberOfHit + "\t" + averageHops + "\t" + numberOfMiss);
>>>>>>> 626e2fe6e0bf327585beaccb0c29f3f00fffbb99
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

		ownerHighCounter.close();
		ownerHighRemaining.close();
		pathHighCounter.close();
		pathHighRemaining.close();
		relateHighCounter.close();
		relateHighRemaining.close();
		cuckooHighCounter.close();
		cuckooHighRemaining.close();
		ownerHighOccupancy.close();
		pathHighOccupancy.close();
		relateHighOccupancy.close();
		cuckooHighOccupancy.close();

		ownerLowCounter.close();
		ownerLowRemaining.close();
		pathLowCounter.close();
		pathLowRemaining.close();
		relateLowCounter.close();
		relateLowRemaining.close();
		cuckooLowCounter.close();
		cuckooLowRemaining.close();
		ownerLowOccupancy.close();
		pathLowOccupancy.close();
		relateLowOccupancy.close();
		cuckooLowOccupancy.close();

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