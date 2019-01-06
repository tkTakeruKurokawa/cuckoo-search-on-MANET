package research;

import peersim.core.*;
import peersim.config.*;
import java.math.BigDecimal;
import java.lang.Math;

public class RandomGeometricGraph implements Control{
	private static final String PAR_R = "r";
	private static double r;

	private static BigDecimal threshold;
	private static BigDecimal srcX;
	private static BigDecimal srcY;
	private static BigDecimal dstX;
	private static BigDecimal dstY;

	public RandomGeometricGraph(String prefix){
		r = Configuration.getDouble(prefix + "." + PAR_R);
		threshold = BigDecimal.valueOf(r);
	}

	private static boolean Euclidean(){
		BigDecimal x = dstX.subtract(srcX);
		BigDecimal y = dstY.subtract(srcY);
		BigDecimal sum = (x.multiply(x)).add(y.multiply(y));
		BigDecimal d = BigDecimal.valueOf(Math.sqrt(sum.doubleValue()));

		// System.out.println(d);

		if(d.compareTo(threshold) <= 0)
			return true;

		return false;
	} 

	public static boolean isConnect(NodeCoordinate src, NodeCoordinate dst){

		srcX = BigDecimal.valueOf(src.getX());
		srcY = BigDecimal.valueOf(src.getY());
		dstX = BigDecimal.valueOf(dst.getX());
		dstY = BigDecimal.valueOf(dst.getY());


		return Euclidean();
	} 

	public boolean execute(){
		return false;
	}

}