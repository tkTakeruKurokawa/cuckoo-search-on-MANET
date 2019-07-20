package research;

import java.io.*;
import java.util.*;

public class SectionAverage {
    private ArrayList<Double> highAvailability = new ArrayList<Double>();
    private ArrayList<Double> lowAvailability = new ArrayList<Double>();
    private ArrayList<Double> highRemaining = new ArrayList<Double>();
    private ArrayList<Double> lowRemaining = new ArrayList<Double>();
    private ArrayList<Double> highOccupancy = new ArrayList<Double>();
    private ArrayList<Double> lowOccupancy = new ArrayList<Double>();

    private int ha_id_old = 0;
    private int la_id_old = 0;
    private int hr_id_old = 0;
    private int lr_id_old = 0;
    private int ho_id_old = 0;
    private int lo_id_old = 0;

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

        highAvailability.add(0.0);
        lowAvailability.add(0.0);
        highRemaining.add(0.0);
        lowRemaining.add(0.0);
        highOccupancy.add(0.0);
        lowOccupancy.add(0.0);
    }

    public void setHighAvailability(int cycle, double average) {
        int id = cycle / 100;
        if (id != ha_id_old) {
            highAvailability.set(ha_id_old, highAvailability.get(ha_id_old) / 100.0);
            highAvailability.add(average);
        } else {
            highAvailability.set(id, highAvailability.get(id) + average);
        }
        ha_id_old = id;
    }

    public void setLowAvailability(int cycle, double average) {
        int id = cycle / 100;
        if (id != la_id_old) {
            lowAvailability.set(la_id_old, lowAvailability.get(la_id_old) / 100.0);
            lowAvailability.add(average);
        } else {
            lowAvailability.set(id, lowAvailability.get(id) + average);
        }
        la_id_old = id;
    }

    public void setHighRemaining(int cycle, double average) {
        int id = cycle / 100;
        if (id != hr_id_old) {
            highRemaining.set(hr_id_old, highRemaining.get(hr_id_old) / 100.0);
            highRemaining.add(average);
        } else {
            highRemaining.set(id, highRemaining.get(id) + average);
        }
        hr_id_old = id;
    }

    public void setLowRemaining(int cycle, double average) {
        int id = cycle / 100;
        if (id != lr_id_old) {
            lowRemaining.set(lr_id_old, lowRemaining.get(lr_id_old) / 100.0);
            lowRemaining.add(average);
        } else {
            lowRemaining.set(id, lowRemaining.get(id) + average);
        }
        lr_id_old = id;
    }

    public void setHighOccupancy(int cycle, double average) {
        int id = cycle / 100;
        if (id != ho_id_old) {
            highOccupancy.set(ho_id_old, highOccupancy.get(ho_id_old) / 100.0);
            highOccupancy.add(average);
        } else {
            highOccupancy.set(id, highOccupancy.get(id) + average);
        }
        ho_id_old = id;
    }

    public void setLowOccupancy(int cycle, double average) {
        int id = cycle / 100;
        if (id != lo_id_old) {
            lowOccupancy.set(lo_id_old, lowOccupancy.get(lo_id_old) / 100.0);
            lowOccupancy.add(average);
        } else {
            lowOccupancy.set(id, lowOccupancy.get(id) + average);
        }
        lo_id_old = id;
    }

    public void writeFile() {
        double previous = 0.0;
        sa.println("High Availability:");
        sa.println("Average\tDifference");
        for (int id = 0; id < highAvailability.size(); id++) {
            if (id > 0) {
                sa.println(highAvailability.get(id) + "\t" + (highAvailability.get(id) - previous));
            } else {
                sa.println(highAvailability.get(id));
            }
            previous = highAvailability.get(id);
        }
        sa.println();

        previous = 0.0;
        sa.println("Low Availability:");
        sa.println("Average\tDifference");
        for (int id = 0; id < lowAvailability.size(); id++) {
            if (id > 0) {
                sa.println(lowAvailability.get(id) + "\t" + (lowAvailability.get(id) - previous));
            } else {
                sa.println(lowAvailability.get(id));
            }
            previous = lowAvailability.get(id);
        }
        sa.println();

        previous = 0.0;
        sa.println("High Remaining:");
        sa.println("Average\tDifference");
        for (int id = 0; id < highRemaining.size(); id++) {
            if (id > 0) {
                sa.println(highRemaining.get(id) + "\t" + (highRemaining.get(id) - previous));
            } else {
                sa.println(highRemaining.get(id));
            }
            previous = highRemaining.get(id);
        }
        sa.println();

        previous = 0.0;
        sa.println("Low Remaining:");
        sa.println("Average\tDifference");
        for (int id = 0; id < lowRemaining.size(); id++) {
            if (id > 0) {
                sa.println(lowRemaining.get(id) + "\t" + (lowRemaining.get(id) - previous));
            } else {
                sa.println(lowRemaining.get(id));
            }
            previous = lowRemaining.get(id);
        }
        sa.println();

        previous = 0.0;
        sa.println("High Occupancy:");
        sa.println("Average\tDifference");
        for (int id = 0; id < highOccupancy.size(); id++) {
            if (id > 0) {
                sa.println(highOccupancy.get(id) + "\t" + (highOccupancy.get(id) - previous));
            } else {
                sa.println(highOccupancy.get(id));
            }
            previous = highOccupancy.get(id);
        }
        sa.println();

        previous = 0.0;
        sa.println("Low Occupancy:");
        sa.println("Average\tDifference");
        for (int id = 0; id < lowOccupancy.size(); id++) {
            if (id > 0) {
                sa.println(lowOccupancy.get(id) + "\t" + (lowOccupancy.get(id) - previous));
            } else {
                sa.println(lowOccupancy.get(id));
            }
            previous = lowOccupancy.get(id);
        }

        sa.close();
    }
}