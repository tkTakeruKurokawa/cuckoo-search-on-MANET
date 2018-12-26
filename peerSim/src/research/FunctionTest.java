import java.lang.Math;
import java.util.*;
// import org.apache.commons.math3.distribution.*;

// compile: javac -cp ./:commons-math3-3.6.1.jar FunctionTest.java
// run: java -cp ./:commons-math3-3.6.1.jar FunctionTest

public class FunctionTest{
	public static Random random = new Random();
	private static ArrayList<Integer> r = new ArrayList<Integer>();
	private static ArrayList<Integer> hit = new ArrayList<Integer>();
	public static Double lam;
	static Integer peak;
	static int count = 0;
	static int num;
	static int total;
	static int tr=0;
	static int zzz=0;
	static int average=0;

	// public static PoissonDistribution pd = new PoissonDistribution(1500.0, 500.0);

	public static Double loop(Double k){
		if(k > 0.0) return k*loop(k-1.0);
		return 1.0; 
	}

	public static void makeLambda(){
		int a = 1;
		if(random.nextBoolean())
			a = 1;
		else
			a = -1;
		Integer tmp = 50 + (a * random.nextInt(25)+1);
		lam = tmp.doubleValue();
		// System.out.println(lam);
	}

	public static boolean probability(Double sum){
		Collections.shuffle(r);

		num = (int) Math.round(sum*100);
		// System.out.println(count + ", " + num);
		for(int i=0; i<num; i++)
			hit.add(r.get(i));

		int candidate = random.nextInt(100);
		if(hit.contains(candidate)){
			hit.clear();
			return true;
		}

		hit.clear();
		return false;


		// num = (int) Math.round(sum*100);
		// // System.out.println(count + ", " + num);
		// count++;

		// int candidate = random.nextInt(100);
		// if(candidate <= num)
		// 	return true;

		// return false;
	}

	public static void main(String[] args){
		for(int i=0; i<100; i++){
			System.out.println(random.nextInt(1));
			r.add(i);
		}

		// for(int i=0; i<500; i++){
			// System.out.println(pd.probability(i));
			// System.out.println(pd.sample());
		// }

		int maxNode = 2000;
		for(int cycle=0; cycle<maxNode; cycle++){

			tr = 0;
			count = 0;
			makeLambda();

			int a = 1;
			if(random.nextBoolean())
				a = 1;
			else
				a = -1;
			peak = 100 + (a * random.nextInt(50)+1);
			peak = 5 + random.nextInt(3);
			lam = random.nextDouble()/500.0;
		// lam = 1.0/500.0;
			double sum = 0.0;
			for(Integer i=1; i<peak; i++){
				double l = lam*i.doubleValue();
				double prefix = Math.exp(-1*l);
				sum = (Math.pow(l,  1.0))/loop(1.0);
			// System.out.println(1.0 - (prefix * sum));
				if(probability(sum*prefix))
					tr++;
			}

			if(tr == 0)
				zzz++;

			System.out.println("True: " + tr);
			System.out.println("False: " + (peak-tr));
			total+=tr;
		}

		System.out.println("True == 0: " + zzz);
		System.out.println("True > 0:" + (maxNode-zzz));


		// for(Integer t = 1; t < 100; t++){
		// 	System.out.println(u*Math.exp(-1*u*t));
		// 	System.out.println();
		// }
	}
}