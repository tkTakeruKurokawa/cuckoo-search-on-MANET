import java.lang.Math;
import java.util.Random;
// import org.apache.commons.math3.distribution.*;

// compile: javac -cp ./:commons-math3-3.6.1.jar FunctionTest.java
// run: java -cp ./:commons-math3-3.6.1.jar FunctionTest

public class FunctionTest{
	public static Random random = new Random();
	public static Double u = 0.5;
	public static Double lam = 100.0;
	public static Double p = 10.0/500.0;
	public static Double total = 0.0;
	public static Boolean decrease = false;
	// public static PoissonDistribution pd = new PoissonDistribution(1500.0, 500.0);
	// public static Integer k;

	public static Double loop(Double k){
		if(k > 0.0) return k*loop(k-1.0);
		return 1.0; 
	}

	private static int numOfReplications(int incriment){
		return random.nextInt(incriment)+(incriment-random.nextInt(incriment));
	}

	public static void main(String[] args){

		// for(int i=0; i<500; i++){
			// System.out.println(pd.probability(i));
			// System.out.println(pd.sample());
		// }


		// for(Integer x=1; x<501; x++){
		// 	double max = 1500.0;
		// 	double cycle = 50.0;
		// 	double max_cycle = 500.0;

		// 	System.out.printf("%d, ",x);
		// 	if((total + Math.floor(max/cycle)) > max)
		// 		decrease = true;
		// 	if(decrease == false)
		// 		total += Math.floor(max/cycle);
		// 	if(decrease == true){
		// 		if((total - Math.floor(0.5*max/cycle))<0.0)
		// 			total = 0.0;
		// 		else
		// 			total -= Math.floor(0.5*(max/cycle));
		// 	}
		// 	System.out.println(total);
		// }

		// int max = 10;
		// int peak = 100;
		// int sum = 0;
		// int incriment = max/peak;
		// for(int i=1; i<500; i++){
		// 	if(!decrease){
		// 		if(sum<max-(incriment*2))
		// 			sum += numOfReplications(incriment);
		// 		else
		// 			decrease = true;
		// 	}
		// 	if(decrease){
		// 		if(sum<=max*0.05)
		// 			System.out.println("LowDemand");
		// 		if(sum-(incriment)<0){
		// 			sum = 0;
		// 			System.out.println(i + ", " + sum);
		// 			continue;
		// 		}
		// 		sum -= random.nextInt(incriment);
		// 	}
		// 	System.out.println(i + ", " + sum);
		// }

		// for(Integer i=1; i<500; i++){
		// 	Double k = 1 / (500.0- i.doubleValue());
		// 	System.out.println( (Math.exp(-1*lam)*Math.pow(lam,  i.doubleValue()))/loop(i.doubleValue()));
		// }

		// for(Integer t = 1; t < 100; t++){
		// 	System.out.println(u*Math.exp(-1*u*t));
		// 	System.out.println();
		// }
	}
}