package research;

import java.io.*;

public class Statistic {
	private double maxB;
	private int maxC;
	private double minB;
	private int minC;
	private double aveB = 0.0;
	private int aveC = 0;

	private int count = 0;

	public PrintWriter output(PrintWriter stat) {
		stat.println("Allocation Num:\t" + count);
		stat.println("Max Battery:\t" + maxB);
		stat.println("Min Battery:\t" + minB);
		stat.println("Max Capacity:\t" + maxC);
		stat.println("Min Capacity:\t" + minC);
		stat.println("Average Battery:\t" + aveB / ((double) count));
		stat.println("Average Capacity:\t" + aveC / ((double) count));

		return stat;
	}

	public void set(Parameter parameter) {

		if (count == 0) {
			maxB = parameter.getBattery();
			minB = parameter.getBattery();
			maxC = parameter.getCapacity();
			minC = parameter.getCapacity();
		}

		if (maxB < parameter.getBattery()) {
			maxB = parameter.getBattery();
		}
		if (minB > parameter.getBattery()) {
			minB = parameter.getBattery();
		}
		if (maxC < parameter.getCapacity()) {
			maxC = parameter.getCapacity();
		}
		if (minC > parameter.getCapacity()) {
			minC = parameter.getCapacity();
		}

		// System.out.println("Max Battery: " + maxB);
		// System.out.println("Min Battery: " + minB);
		// System.out.println("Max Capacity: " + maxC);
		// System.out.println("Min Capacity: " + minC);

		aveB += parameter.getBattery();
		aveC += parameter.getCapacity();

		count++;
	}
}