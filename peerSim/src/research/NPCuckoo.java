package research;

public class NPCuckoo implements Parameter {
	private double battery = 0.0;
	private int capacity = 0;
	private int upTime = 0;

	public NPCuckoo(String prefix) {
	}

	public Object clone() {
		NPCuckoo parameter = null;
		try {
			parameter = (NPCuckoo) super.clone();
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

	public void setUpTime(int upTime) {
		this.upTime = upTime;
	}

	public double getBattery() {
		return battery;
	}

	public int getCapacity() {
		return capacity;
	}

	public int getUpTime() {
		return upTime;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("NPCuckoo: Battery = " + getBattery() + " Capacity = ");
		return buffer.append(getCapacity()).toString();
	}
}