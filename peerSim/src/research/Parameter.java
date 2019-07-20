package research;

import peersim.core.*;

public interface Parameter extends Protocol {

	public Object clone();

	public void setBattery(double battery);

	public void setCapacity(int capacity);

	public double getBattery();

	public int getCapacity();

	public String toString();
}
