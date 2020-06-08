import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.lang.Math;

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

    public static void main(String[] args) {
        // for (int i = 0; i < 100; i++) {
        // rnd.add(i);
        // }

        // System.out.println(poisson());
        double n = 6.7;
        double total = n;
        for (int i = 1; i < 8; i++) {
            n = n * 2.0;
            total += n;
            System.out.println(n);
        }
        System.out.println("total: " + total);
    }
}
