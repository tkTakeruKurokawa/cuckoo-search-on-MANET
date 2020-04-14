package research;

import java.io.*;
import java.util.*;

public class SectionAverage {
    private ArrayList<Double> availability = new ArrayList<Double>();
    private ArrayList<Double> remaining = new ArrayList<Double>();
    private ArrayList<Double> occupancy = new ArrayList<Double>();

    private double total_availability = 0.0;
    private double total_remaining = 0.0;
    private double total_occupancy = 0.0;

    private int availability_id_old = 0;
    private int remaining_id_old = 0;
    private int occupancy_id_old = 0;

    private PrintWriter sa;

    public SectionAverage(String string) {
        try {
            File dir = new File("result");
            if (!dir.exists()) {
                dir.mkdir();
            }

            String way = new File(".").getAbsoluteFile().getParent();
            String fileName = way + "/result/sectionAverage_" + string + ".tsv";
            File file = new File(fileName);

            sa = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
        } catch (IOException e) {
            System.out.println(e);
        }

        availability.add(0.0);
        remaining.add(0.0);
        occupancy.add(0.0);
    }

    public void setAvailability(int cycle, double value) {
        total_availability += value;

        int id = cycle / 100;
        if (id != availability_id_old) {
            availability.set(availability_id_old, availability.get(availability_id_old) / 100.0);
            availability.add(value);
        } else {
            availability.set(id, availability.get(id) + value);
        }
        availability_id_old = id;
    }

    public void setRemaining(int cycle, double value) {
        total_remaining += value;

        int id = cycle / 100;
        if (id != remaining_id_old) {
            remaining.set(remaining_id_old, remaining.get(remaining_id_old) / 100.0);
            remaining.add(value);
        } else {
            remaining.set(id, remaining.get(id) + value);
        }
        remaining_id_old = id;
    }

    public void setOccupancy(int cycle, double value) {
        total_occupancy += value;

        int id = cycle / 100;
        if (id != occupancy_id_old) {
            occupancy.set(occupancy_id_old, occupancy.get(occupancy_id_old) / 100.0);
            occupancy.add(value);
        } else {
            occupancy.set(id, occupancy.get(id) + value);
        }
        occupancy_id_old = id;
    }

    public void writeFile() {
        double average;
        double previous = 0.0;
        sa.println("Availability:");
        sa.println("Average\tDifference");
        for (int id = 0; id < availability.size(); id++) {
            if (id > 0) {
                sa.println(availability.get(id) + "\t" + (availability.get(id) - previous));
            } else {
                sa.println(availability.get(id));
            }
            previous = availability.get(id);
        }
        average = total_availability / 500.0;
        sa.println(average);
        sa.println();

        previous = 0.0;
        sa.println("Remaining:");
        sa.println("Average\tDifference");
        for (int id = 0; id < remaining.size(); id++) {
            if (id > 0) {
                sa.println(remaining.get(id) + "\t" + (remaining.get(id) - previous));
            } else {
                sa.println(remaining.get(id));
            }
            previous = remaining.get(id);
        }
        average = total_remaining / 500.0;
        sa.println(average);
        sa.println();

        previous = 0.0;
        sa.println("Occupancy:");
        sa.println("Average\tDifference");
        for (int id = 0; id < occupancy.size(); id++) {
            if (id > 0) {
                sa.println(occupancy.get(id) + "\t" + (occupancy.get(id) - previous));
            } else {
                sa.println(occupancy.get(id));
            }
            previous = occupancy.get(id);
        }
        average = total_occupancy / 500.0;
        sa.println(average);

        sa.close();
    }
}