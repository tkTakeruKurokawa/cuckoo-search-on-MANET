package research;

import peersim.config.*;
import java.util.*;

public class BaseParameter implements Parameter {
	private static final String PAR_BASE = "baseBattery";
	private static double base;
	private static final String PAR_MAXCAPACITY = "maxCapacity";
	private static int maxCapacity;

	private Random random = new Random();

	private double battery;
	private int capacity;

	public BaseParameter(String prefix) {
		base = Configuration.getDouble(prefix + "." + PAR_BASE);
		maxCapacity = Configuration.getInt(prefix + "." + PAR_MAXCAPACITY);
	}

	public Object clone() {
		BaseParameter parameter = null;
		try {
			parameter = (BaseParameter) super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return parameter;
	}

	public void setParameter() {
		battery = (random.nextDouble() * 50.0) + base;
		// capacity = random.nextInt(maxCapacity-10+1) + 10;
		capacity = maxCapacity;
	}

	public void setBattery(double battery) {
		this.battery = battery;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public double getBattery() {
		return battery;
	}

	public int getCapacity() {
		return capacity;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("BaseParameter: Battery = " + getBattery() + " Capacity = ");
		return buffer.append(getCapacity()).toString();
	}
}