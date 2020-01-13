import java.lang.*;
import java.util.*;
import java.io.*;
import org.apache.commons.math3.special.*;

public class distribution {
    static double pi = Math.PI;
    static double sigma = 100.0;
    static double mu = 25.0;
    static double total = 0.0;
    static Random random = new Random();
    static List list = new ArrayList<Double>();
    static PrintWriter nd, pd;

    public static void normal_distribution() {
        for (int i = -24; i <= 25; i++) {
            double x = (double) i;
            double f = (1.0 / Math.sqrt(2.0 * pi * sigma)) * (Math.exp((-1.0) * (Math.pow(x - mu, 2.0) / 2.0 * sigma)));
            total += f;
            System.out.println(f);
        }
    }

    public static void normal_cdf() {
        for (int i = 0; i < 50; i++) {
            double x = (double) i;
            double f = (1.0 / 2.0) * (1.0 + Erf.erf((x - mu) / Math.sqrt(2.0 * sigma)));
            // f = f * 0.7 + 0.1;
            System.out.println(f);
            nd.println(i + "\t" + f);
        }
        nd.close();
    }

    public static void power_distribution() {
        double c = 1.0;
        double k = 1.0;
        double alfa = 1.0;
        for (int i = 0; i < 50; i++) {
            double x = (double) i;
            // double f = c * k * Math.pow(x, c - 1) / Math.pow(1 + Math.pow(x, c), k + 1);
            // double f = alfa * Math.pow(1, alfa) / Math.pow(x, alfa + 1);
            double r;
            do {
                r = random.nextDouble();
            } while (r < 0.1);
            double f = Math.pow(r * 10.0, -2.0);
            // double f = Math.sqrt((c / 2.0 * pi)) * (Math.exp(-1.0 * c / 2.0 * x) /
            // Math.pow(x, 1.5));
            // System.out.println(f);
            list.add(f);
        }
        list.sort(Comparator.reverseOrder());
        for (int i = 0; i < 50; i++) {
            pd.println(i + "\t" + list.get(i));
        }
        pd.close();
    }

    public static void power_cdf() {
    }

    public static void main(String args[]) {
        File normal = new File("./normal_distribution.tsv");
        File power = new File("./power_distribution.tsv");
        try {
            nd = new PrintWriter(new BufferedWriter(new FileWriter(normal, false)));
            pd = new PrintWriter(new BufferedWriter(new FileWriter(power, false)));
        } catch (Exception e) {
            // TODO: handle exception
        }

        System.out.println("正規分布");
        normal_distribution();

        System.out.println();
        System.out.println("正規分布の累積分布");
        normal_cdf();

        System.out.println();
        System.out.println("指数分布");
        power_distribution();

    }
}