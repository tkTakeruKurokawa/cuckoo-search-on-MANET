import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.lang.Math;
import org.apache.commons.math3.special.*;

class Test {

    static Random random = new Random();
    private static ArrayList<Integer> hit = new ArrayList<Integer>();
    private static ArrayList<Integer> rnd = new ArrayList<Integer>();

    private static boolean probability(Double p) {
        hit = new ArrayList<Integer>();
        Collections.shuffle(rnd);

        int num = (int) Math.round(p * 100);
        // System.out.println(num);
        for (int i = 0; i < num; i++)
            hit.add(rnd.get(i));

        int candidate = random.nextInt(100);
        if (hit.contains(candidate)) {
            // System.out.println(num);
            return true;
        }

        return false;
    }

    public static double factorial(int src) {
        if (src == 0) {
            return ((double) 1.0);
        }
        double value = 1;
        for (int i = 1; i <= src; i++) {
            value *= i;
        }
        // System.out.println(value);
        return ((double) value);
    }

    private static int poisson() {
        double lambda = (random.nextDouble() * 20.0);
        System.out.println(lambda);
        int total = 0;

        for (int i = 0; i <= 100; i++) {
            double cycle = ((double) i);
            double p = Math.exp(-1 * lambda) * (Math.pow(lambda, cycle)) / factorial(i);
            System.out.println(i + ", " + p);
            for (int nodes = 0; nodes < 2000; nodes++) {
                boolean success = probability(p);
                if (success == true) {
                    total++;
                }
            }
        }

        return total;
    }

    private static void normalDistribution() {
        ArrayList<Double> list = new ArrayList<Double>();
        double mu = 25.0;
        double sigma = 100.0;
        for (int i = 0; i < 50; i++) {
            double x = (double) i;
            double f = (1.0 / 2.0) * (1.0 + Erf.erf((x - mu) / Math.sqrt(2.0 * sigma)));
            list.add(f);
        }
        Collections.reverse(list);
        for (Double value : list) {
            System.out.printf("%f, ", value);
        }
        System.out.println();
    }

    private static void powerDistribution() {
        double alpha = 1.0;
        for (int i = 1; i <= 50; i++) {
            double x = (double) i;
            double f = alpha * Math.pow(1, alpha) / Math.pow(x, alpha + 1);
            System.out.printf("%f, ", f);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        // for (int i = 0; i < 100; i++) {
        // rnd.add(i);
        // }
        // System.out.println(poisson());

        normalDistribution();
        powerDistribution();

    }
}
