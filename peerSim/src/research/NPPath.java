package research;

public class NPPath implements Parameter {
	private double battery = 0.0;
	private int capacity = 0;

	public NPPath(String prefix) {
	}

	public Object clone() {
		NPPath parameter = null;
		try {
			parameter = (NPPath) super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return parameter;
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
		buffer.append("NPPath: Battery = " + getBattery() + " Capacity = ");
		return buffer.append(getCapacity()).toString();
	}
}