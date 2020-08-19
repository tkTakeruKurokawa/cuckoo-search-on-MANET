import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;

public class ExtractGraphValue {
    static ArrayList<DataSet> cuckoo;
    static ArrayList<DataSet> relate;

    private static void extract(String place) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(place)));

            String line;
            int targetCycle = 199;
            while ((line = br.readLine()) != null) {
                String[] words = line.split("\t");

                if (words[0].equals(String.valueOf(targetCycle))) {
                    if (place.contains("cuckoo")) {
                        add(cuckoo, place, words);
                        targetCycle += 100;
                    } else if (place.contains("relate")) {
                        add(relate, place, words);
                        targetCycle += 100;
                    }
                }
            }
            br.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private static void add(ArrayList<DataSet> data, String place, String[] words) {
        int stopCycle = (Integer.valueOf(place.substring(8, 11)) / 100) - 1;
        if (place.contains("counter")) {
            data.get(stopCycle).counter.add(Double.valueOf(words[1]));
        }
        if (place.contains("occupancy")) {
            data.get(stopCycle).occupancy.add(Double.valueOf(words[1]));
        }
        if (place.contains("remaining")) {
            data.get(stopCycle).remaining.add(Double.valueOf(words[1]));
        }
        if (place.contains("replicationCost")) {
            data.get(stopCycle).replicationCost.add(Double.valueOf(words[2]));
        }
    }

    public static void main(String[] args) {
        String[] placeSet = { "./result100/pareto/counter_cuckoo.tsv", "./result200/pareto/counter_cuckoo.tsv",
                "./result300/pareto/counter_cuckoo.tsv", "./result100/pareto/counter_relate.tsv",
                "./result200/pareto/counter_relate.tsv", "./result300/pareto/counter_relate.tsv",
                "./result100/pareto/occupancy_cuckoo.tsv", "./result200/pareto/occupancy_cuckoo.tsv",
                "./result300/pareto/occupancy_cuckoo.tsv", "./result100/pareto/occupancy_relate.tsv",
                "./result200/pareto/occupancy_relate.tsv", "./result300/pareto/occupancy_relate.tsv",
                "./result100/pareto/remaining_cuckoo.tsv", "./result200/pareto/remaining_cuckoo.tsv",
                "./result300/pareto/remaining_cuckoo.tsv", "./result100/pareto/remaining_relate.tsv",
                "./result200/pareto/remaining_relate.tsv", "./result300/pareto/remaining_relate.tsv",
                "./result100/pareto/replicationCost_cuckoo.tsv", "./result200/pareto/replicationCost_cuckoo.tsv",
                "./result300/pareto/replicationCost_cuckoo.tsv", "./result100/pareto/replicationCost_relate.tsv",
                "./result200/pareto/replicationCost_relate.tsv", "./result300/pareto/replicationCost_relate.tsv" };

        cuckoo = new ArrayList<DataSet>();
        relate = new ArrayList<DataSet>();
        for (int i = 0; i < 3; i++) {
            cuckoo.add(new DataSet());
            relate.add(new DataSet());
        }

        for (String place : placeSet) {
            extract(place);
        }

        String[] dataSet = { "Data Availability", "Cumulative Occupancy", "Average Storage Remaining",
                "Replication Cost" };

        for (int stopCycle = 0; stopCycle < 3; stopCycle++) {
            System.out.println("Stop Cycle " + (stopCycle + 1) * 100);
            for (int listId = 0; listId < dataSet.length; listId++) {
                System.out.println("===============================================================");

                System.out.println(dataSet[listId] + ": ");
                System.out.println("Cycle & Proposed & Kageyama & 差分");
                for (int cycle = 0; cycle < 4; cycle++) {
                    double proposed = cuckoo.get(stopCycle).listSet.get(listId).get(cycle);
                    double kageyama = relate.get(stopCycle).listSet.get(listId).get(cycle);

                    if (listId == 0) {
                        proposed = Math.round(proposed * 10000.0) / 10000.0;
                        kageyama = Math.round(kageyama * 10000.0) / 10000.0;
                    } else if (listId == 2) {
                        proposed = Math.round(proposed * 10.0) / 10.0;
                        kageyama = Math.round(kageyama * 10.0) / 10.0;
                    } else {
                        proposed = Math.round(proposed);
                        kageyama = Math.round(kageyama);
                    }

                    String difference = "";
                    if (proposed > kageyama) {
                        double value = (1.0 - (kageyama / proposed)) * 100.0;
                        difference = "+" + BigDecimal.valueOf(value).toPlainString();
                    } else {
                        double value = (1.0 - (proposed / kageyama)) * 100.0;
                        difference = "-" + BigDecimal.valueOf(value).toPlainString();
                    }

                    System.out.println((cycle + 2) * 100 + " & " + proposed + " & " + kageyama + " & " + difference);
                }
                System.out.println("===============================================================");
            }
            System.out.println();
            System.out.println();
        }
    }
}

class DataSet {
    public ArrayList<Double> counter;
    public ArrayList<Double> occupancy;
    public ArrayList<Double> remaining;
    public ArrayList<Double> replicationCost;
    public ArrayList<ArrayList<Double>> listSet;

    public DataSet() {
        counter = new ArrayList<Double>();
        occupancy = new ArrayList<Double>();
        remaining = new ArrayList<Double>();
        replicationCost = new ArrayList<Double>();

        listSet = new ArrayList<ArrayList<Double>>();
        listSet.add(counter);
        listSet.add(occupancy);
        listSet.add(remaining);
        listSet.add(replicationCost);
    }
}