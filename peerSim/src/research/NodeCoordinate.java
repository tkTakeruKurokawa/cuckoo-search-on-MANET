package research;

import peersim.core.*;
import peersim.config.*;
import java.math.BigDecimal;
import java.util.Random;


public class NodeCoordinate implements Protocol{
	private static final String PAR_D = "d";
	private static double d;

	private Random random = new Random();
	private double x, y;
	private BigDecimal a,b;

	public NodeCoordinate(String prefix){
		d = Configuration.getDouble(prefix + "." + PAR_D);
	}

	public Object clone() {
		NodeCoordinate coordinate = null;
		try {
			coordinate = (NodeCoordinate) super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return coordinate;
	}

	public void setCoordinate(){
		a = BigDecimal.valueOf(random.nextDouble());
		b = BigDecimal.valueOf(d);
		BigDecimal x = a.multiply(b);
		this.x = x.doubleValue();

		a = BigDecimal.valueOf(random.nextDouble());
		b = BigDecimal.valueOf(d);
		BigDecimal y = a.multiply(b);
		this.y = y.doubleValue();
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}


	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("Coordinate = " + "(" + getX() + " ," + getY());
		return buffer.append(")").toString();
	}
} 