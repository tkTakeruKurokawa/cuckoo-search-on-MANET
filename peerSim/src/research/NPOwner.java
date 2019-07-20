package research;

public class NPOwner implements Parameter {
	private double battery = 0.0;
	private int capacity = 0;

	public NPOwner(String prefix) {
	}

	public Object clone() {
		NPOwner parameter = null;
		try {
			parameter = (NPOwner) super.clone();
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
		buffer.append("NPOwner: Battery = " + getBattery() + " Capacity = ");
		return buffer.append(getCapacity()).toString();
	}
}