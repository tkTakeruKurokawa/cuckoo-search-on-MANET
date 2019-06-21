import java.lang.Math;
import java.util.*;
// import org.apache.commons.math3.special.*;

// compile: javac -cp ./:commons-math3-3.6.1.jar FunctionTest.java
// run: java -cp ./:commons-math3-3.6.1.jar FunctionTest

public class FunctionTest {
	private static ArrayList<Integer> r = new ArrayList<Integer>();
	private static ArrayList<Integer> hit = new ArrayList<Integer>();
	public static Double lam;
	static Integer peak;
	static int count = 0;
	static int num;
	static int total;
	static int tr = 0;
	static int zzz = 0;
	static int average = 0;
	private static final double GAMMA = 1.329340388179137020474;
	private static final double GAMMA2 = 0.9064024770554770779827;
	private static final double PI = Math.PI;
	private static final double BETA = 1.5;
	private static final double NUME = GAMMA * Math.sin(PI * BETA / 2.0);
	private static final double DENOM = GAMMA2 * BETA * Math.pow(2.0, (BETA - 1.0) / 2.0);
	private static final double SIGMA = Math.pow(NUME / DENOM, 1.0 / BETA);

	private static Random random = new Random();
	private static double RAND = random.nextDouble();
	private static double RAND_N = Math.sqrt(-2.0 * Math.log(RAND) * Math.cos(2.0 * PI * RAND));

	private static Integer[] neighbors = new Integer[10];

	// public static PoissonDistribution pd = new PoissonDistribution(1500.0,
	// 500.0);

	public static Double loop(Double k) {
		if (k > 0.0)
			return k * loop(k - 1.0);
		return 1.0;
	}

	static double gamma(double x, int ier[]) {
		double err, g, s, t, v, w, y;
		int k;

		ier[0] = 0;

		if (x > 5.0) {
			v = 1.0 / x;
			s = ((((((-0.000592166437354 * v + 0.0000697281375837) * v + 0.00078403922172) * v - 0.000229472093621) * v
					- 0.00268132716049) * v + 0.00347222222222) * v + 0.0833333333333) * v + 1.0;
			g = 2.506628274631001 * Math.exp(-x) * Math.pow(x, x - 0.5) * s;
		}

		else {

			err = 1.0e-20;
			w = x;
			t = 1.0;

			if (x < 1.5) {

				if (x < err) {
					k = (int) x;
					y = (double) k - x;
					if (Math.abs(y) < err || Math.abs(1.0 - y) < err)
						ier[0] = -1;
				}

				if (ier[0] == 0) {
					while (w < 1.5) {
						t /= w;
						w += 1.0;
					}
				}
			}

			else {
				if (w > 2.5) {
					while (w > 2.5) {
						w -= 1.0;
						t *= w;
					}
				}
			}

			w -= 2.0;
			g = (((((((0.0021385778 * w - 0.0034961289) * w + 0.0122995771) * w - 0.00012513767) * w + 0.0740648982) * w
					+ 0.0815652323) * w + 0.411849671) * w + 0.422784604) * w + 0.999999926;
			g *= t;
		}

		return g;
	}

	public static void makeLambda() {
		int a = 1;
		if (random.nextBoolean())
			a = 1;
		else
			a = -1;
		Integer tmp = 50 + (a * random.nextInt(25) + 1);
		lam = tmp.doubleValue();
		// System.out.println(lam);
	}

	public static boolean probability(Double sum) {
		hit.clear();
		Collections.shuffle(r);

		int num = (int) Math.round(sum * 100);
		// System.out.println(count + ", " + num);
		if (num > 0)
			num = random.nextInt(num + 1);
		for (int i = 0; i < num; i++)
			hit.add(r.get(i));

		int candidate = random.nextInt(100);
		if (hit.contains(candidate)) {
			// System.out.println(num);
			return true;
		}

		return false;

		// num = (int) Math.round(sum*100);
		// // System.out.println(count + ", " + num);
		// count++;

		// int candidate = random.nextInt(100);
		// if(candidate <= num)
		// return true;

		// return false;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			r.add(i);
		}

		// for(int i=0; i<500; i++){
		// System.out.println(pd.probability(i));
		// System.out.println(pd.sample());
		// }

		double sum = 0.0;
		int maxNode = 2000;
		double increase = 0.0;
		for (int cycle = 0; cycle < 500; cycle++) {

			double lambda = 2.0;
			double c = (double) cycle;

			System.out.println(Math.exp(-1 * lambda) * Math.pow(lambda, c) / loop(c));
			tr = 0;
			makeLambda();

			int a = 1;
			if (random.nextBoolean())
				a = 1;
			else
				a = -1;
			peak = 100 + (a * random.nextInt(50) + 1);
			peak = 5 + random.nextInt(3);
			lam = random.nextDouble() / 500.0;
			// lam = 1.0/500.0;
			for (Integer i = 0; i < 2000; i++) {
				double l = random.nextDouble() / 500.0 * cycle;
				double prefix = Math.exp(-1 * l);
				sum = (Math.pow(l, 1.0)) / loop(1.0);
				// System.out.printf("cycle %d, ", cycle);
				// System.out.println(prefix * sum);
				increase = increase + 0.5;
				if (probability(sum * prefix))
					tr++;
				// System.out.println("candidate " + i + " : " + probability(sum*prefix));
			}

			count++;
			if (tr == 0)
				zzz++;

			// System.out.println("True: " + tr);
			// System.out.println("False: " + (peak-tr));
			total += tr;
		}
		// int s = 0;
		// for(int i=108; i>0; i--){
		// s += i;
		// }
		// int r1,r2;
		// int SET_SIZE = 10;
		// for(int i=0; i<1000000; i++){
		// r1 = random.nextInt(SET_SIZE);
		// r2 = (r1 + (random.nextInt(SET_SIZE-1)+1)) % SET_SIZE;
		// if(r1 == r2)
		// System.out.println(r1 + ", " + r2);
		// }
		int ier[] = new int[1];
		ier[0] = 0;
		for (double i = 0.1; i < 1.0d; i += 0.1) {
			double RAND = random.nextDouble();
			double RAND_N = Math.sqrt(-2.0 * Math.log(RAND) * Math.cos(2.0 * PI * RAND));

			double u = RAND_N * SIGMA;
			double v = RAND_N;
			double s = u / Math.pow(Math.abs(v), 1.0 / BETA);
			// System.out.println(s);
			int d = (int) Math.round(s * 1.4);
			// if(s==NaN)
			// System.out.println(s*1.0);

			// System.out.println(d);
			// System.out.println(Math.cos(PI*2.0*i));
		}
		// for(int i=0; i<10; i++){
		// double x=random.nextDouble()*200.0;
		// double x2=random.nextDouble()*200.0;
		// double y=random.nextDouble()*200.0;
		// double y2=random.nextDouble()*200.0;
		// double threshold = 45.0;
		// double radian = Math.atan2(y2-y, x2-x);
		// double o = random.nextDouble()*359.9999999999d;
		// double degree = radian*180.0/PI;
		// if(y2-y<0.0)
		// degree = 360.0 - Math.abs(degree);

		// double up = degree + threshold;
		// double bottom = degree - threshold;
		// System.out.println(degree + ", "+up + ", " + bottom);
		// System.out.println("o: " + o);
		// if(bottom<= o && o<=up)
		// System.out.println("true");
		// if(bottom < 0.0){
		// bottom = 360.0 + bottom;
		// System.out.println("bottom: " + bottom);
		// if((bottom<=o && o<=360.0) || (0.0<o && o<=up))
		// System.out.println("true");
		// }
		// if(up > 360.0){
		// up = up - 360.0;
		// System.out.println("up: " + up);
		// if((bottom<=o && o<=360.0) || (0.0<=o && o<=up))
		// System.out.println("true");
		// }
		// }

		double a = 200.0 * 200.0;
		double d = 2000.0 / a;
		System.out.println(Math.sqrt((1.0 - 0.3) * Math.log(a) / d * Math.PI));

		// System.out.println(Gamma.logGamma(1.5));
		// System.out.println(r1 + ", " + r2);
		// System.out.println(total);
		// System.out.println("True == 0: " + zzz);
		// System.out.println("True > 0:" + (maxNode-zzz));

		// for(Integer t = 1; t < 100; t++){
		// System.out.println(u*Math.exp(-1*u*t));
		// System.out.println();
		// }
	}
}
