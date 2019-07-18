import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.lang.Math;

class Test {

    static Random random = new Random();

    private static boolean probability(Double p) {
        ArrayList<Integer> hit = new ArrayList<Integer>();
        ArrayList<Integer> rnd = new ArrayList<Integer>();

        for (int i = 0; i < 100; i++) {
            rnd.add(i);
        }
        Collections.shuffle(rnd);

        // System.out.println(p);
        int num = (int) Math.round(p * 100.0);
        // System.out.println(num);
        if (num > 0)
            num = random.nextInt(num + 1);
        for (int i = 0; i < num; i++)
            hit.add(rnd.get(i));

        int candidate = random.nextInt(100);
        if (hit.contains(candidate)) {
            // System.out.println(num);
            return true;
        }

        return false;
    }

    public static void placeNum() {
        double fd = ((double) random.nextInt(6) + 5); // 前回の予想値
        double md_old = fd;
        double md = fd; // 現在のデータ数
        // if (random.nextBoolean() && md > 0) {
        // md = fd + ((double) random.nextInt(3));
        // } else {
        // md = fd - ((double) random.nextInt(3));
        // }
        double fd_next; // 予想データ数
        int st; // 配置個数
        double beta = 1.0;

        double one_old = md;
        double two_old = md;
        double three_old = md;
        double avr = 0.0;
        ArrayList<Double> total = new ArrayList<Double>();
        total.add(0, 0.0);

        for (int i = 0; i < 500; i++) {
            fd_next = 0.5 * md + 0.5 * fd;
            avr = (one_old + two_old + three_old) / 3.0;
            total.set(0, total.get(0) + md);
            if (md_old / md > 1.0) {
                st = ((int) Math.ceil(md * (md_old / md)));
            } else {
                st = ((int) Math.ceil(md));
            }
            // st = ((int) Math.ceil(beta * md * avr / md));
            st = ((int) Math.round(beta * md * total.get(0) / (((double) i) + 1.0) / md));
            // st = ((int) Math.round(beta * fd_next));
            if (md <= 0) {
                st = 0;
            }
            // System.out.printf("st:%d fd_next:%.3f md:%.3f fd:%.3f\n", st, fd_next, md,
            // fd);
            System.out.printf("st:%d md:%.3f avr:%.3f\n", st, md, total.get(0) / (((double) i) + 1.0));

            fd = fd_next;
            if (random.nextBoolean() && md > 0) {
                three_old = two_old;
                two_old = one_old;
                one_old = md;
                md_old = md;
                md = ((double) st);
            } else {
                md_old = md;
                three_old = two_old;
                two_old = one_old;
                one_old = md;
                md = ((double) st) - ((double) random.nextInt(5));
            }
        }
    }

    public static double factorial(int src) {
        if (src == 0) {
            return 0;
        }
        double value = 1;
        for (int i = 1; i <= src; i++) {
            value *= i;
        }

        return ((double) value);
    }

    public static void main(String[] args) {

        ArrayList<Double> number = new ArrayList<Double>();

        for (int i = 0; i < 2000; i++) {
            number.add(i, 1.0);
        }
        // double temp = random.nextDouble() % 0.5 + 0.5;
        // double lambda = ((double) random.nextInt(10)) + 1.0;
        double lambda = 1.0 / (random.nextDouble() * 100.0);
        // double lambda = ((double) random.nextInt(4) + 1) / 100.0;
        // double lambda = 0.05;
        // System.out.println(lambda);

        double total = 0.0;
        int count = 0;
        double num = 0.0;

        for (int i = 0; i < 500; i++) {
            count = 0;
            for (int j = 0; j < 300; j++) {
                double p = Math.exp(-1 * lambda * number.get(j)) * (Math.pow(lambda * number.get(j), 1.0))
                        / factorial(1);
                // System.out.println(i + ": " + p);

                if (probability(p)) {
                    number.set(j, 1.0);
                    count++;
                } else {
                    number.set(j, number.get(j) + 1.0);
                }
                // }

            }
            System.out.println(count);

            total += count;

            // placeNum();
        }
        System.out.println(total);
        System.out.println("average: " + total / 500.0);
    }
}
