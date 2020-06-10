import java.io.*;
import java.util.ArrayList;
import java.util.Objects;

public class ChangeResultName {
    static String prefix = "./plot_configs";
    static int type;
    static int stopCycle;
    static String directoryName;

    public static void run(String place) {
        if (type == 0) {
            directoryName = "../result" + stopCycle + "/normal";
        } else if (type == 1) {
            directoryName = "../result" + stopCycle + "/pareto";
        } else {
            System.exit(-3);
        }

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(place)));

            String line;
            ArrayList<String> outputs = new ArrayList<String>();
            while ((line = br.readLine()) != null) {
                line = checkAndReplace(place, line);
                outputs.add(line);
            }
            br.close();

            PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(new File(place))));
            for (String output : outputs) {
                // System.out.println(output);
                writer.println(output);
            }
            writer.close();

        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public static String checkAndReplace(String place, String line) {
        if (place.startsWith(prefix)) {
            if (line.indexOf("result") != -1) {
                return changeResultNumber(line);
            } else {
                return changeFilePath(place, line);
            }
        } else {
            if (line.startsWith("type")) {
                line = "type " + type;
            } else if (line.startsWith("stopCycle")) {
                line = "stopCycle " + stopCycle;
            }
        }

        return line;
    }

    public static String changeResultNumber(String line) {
        return line.replaceFirst("../result[0-9]+/[a-z]+/", directoryName + "/");
    }

    public static String changeFilePath(String place, String line) {
        if (line.indexOf("eps") != -1) {
            return line.replace("eps/", directoryName + "/eps/");
        }
        if (place.indexOf("count") != -1) {
            return line.replace("counter", directoryName + "/counter");
        }
        if (place.indexOf("networkCost") != -1) {
            if (line.indexOf("searchCost") != -1) {
                return line.replace("searchCost", directoryName + "/searchCost");
            }
            if (line.indexOf("replicationCost") != -1) {
                return line.replace("replicationCost", directoryName + "/replicationCost");
            }
        }
        if (place.indexOf("occupancy") != -1) {
            return line.replace("occupancy", directoryName + "/occupancy");
        }
        if (place.indexOf("remaining") != -1) {
            return line.replace("remaining", directoryName + "/remaining");
        }

        return line;
    }

    public static void main(String[] args) {
        String errorMessage = "";
        if (args.length == 0) {
            errorMessage += "従う分布モデルを第一引数に入力（0：正規分布，1：パレート分布）\n";
            errorMessage += "複製配置を止めるサイクル数を第二引数に入力\n";
        }
        if (args.length == 1) {
            errorMessage += "複製配置を止めるサイクル数を第二引数に入力\n";
        }
        if (errorMessage.length() > 0) {
            System.out.println(errorMessage);
            System.exit(-1);
        }

        type = Integer.valueOf(args[0]);
        stopCycle = Integer.valueOf(args[1]);

        String[] places = { "./src/research/config.txt", prefix + "/counter.plt", prefix + "/networkCost.plt",
                prefix + "/occupancy.plt", prefix + "/remaining.plt" };

        for (String place : places) {
            run(place);
        }
    }
}