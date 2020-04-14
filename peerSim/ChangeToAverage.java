import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

class ChangeToAverage {

    public static void make_directory(String directory) {
        File dir = new File(directory);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    public static void run(String type, String method) {
        try {
            String[] directories = { "networkCost_average", "networkCost_average/result_normal",
                    "networkCost_average/result_power" };
            for (String directory : directories) {
                make_directory(directory);
            }

            String place = "./result_" + type + "/networkCost_" + method + ".tsv";
            File source = new File(place);
            PrintWriter output = new PrintWriter(new BufferedWriter(new FileWriter(
                    new File("networkCost_average/result_" + type + "/networkCost_" + method + ".tsv"))));
            output.println("Average");
            output.println();

            if (checkBeforeReadfile(source)) {
                BufferedReader br = new BufferedReader(new FileReader(source));

                String str;
                String[] words = new String[3];
                int count = 1;
                while ((str = br.readLine()) != null) {
                    words = str.split("\t");
                    if (words[0].equals("Cycle") || words[0].isEmpty()) {
                        continue;
                    }
                    for (int i = 0; i < words.length; i++) {
                        System.out.println(words[i]);

                        if (i == 2) {
                            double num = Double.parseDouble(words[i]);
                            double average = num / ((double) count);
                            output.println(average);
                            System.out.println(average);
                        }
                    }
                    System.out.println();
                    count++;
                }

                br.close();
                output.close();
            } else {
                System.out.println("ファイルが見つからないか開けません");
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static void main(String args[]) {
        String[] types = { "normal", "power" };
        String[] methods = { "cuckoo", "relate", "owner", "path" };
        for (String type : types) {
            for (String method : methods) {
                run(type, method);
            }
        }
    }

    private static boolean checkBeforeReadfile(File file) {
        if (file.exists()) {
            if (file.isFile() && file.canRead()) {
                return true;
            }
        }

        return false;
    }
}