package research;

import peersim.core.*;
import peersim.config.*;
import java.util.*;

public class NodeParameter implements Parameter {
	private static final double DEFAULT_INITIAL_BASE = 50.0;
	private static final String PAR_BASE = "baseBattery";
	private static double base;
	private static final int DEFAULT_INITIAL_MAXCAPACITY = 100;
	private static final String PAR_MAXCAPACITY = "maxCapacity";
	private static int maxCapacity;

	private Random random = new Random();

	private double battery;
	private int capacity;

	public NodeParameter(String prefix) {
		base = Configuration.getDouble(prefix + "." + PAR_BASE, DEFAULT_INITIAL_BASE);
		maxCapacity = Configuration.getInt(prefix + "." + PAR_MAXCAPACITY, DEFAULT_INITIAL_MAXCAPACITY);
	}

	public Object clone() {
		NodeParameter parameter = null;
		try {
			parameter = (NodeParameter) super.clone();
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
		buffer.append("NodeParameter: Battery = " + getBattery() + " Capacity = ");
		return buffer.append(getCapacity()).toString();
	}
}