package research;

public class NPRelate implements Parameter {
	private double battery = 0.0;
	private int capacity = 0;
	private double contribution = 0.0;

	public NPRelate(String prefix) {
	}

	public Object clone() {
		NPRelate parameter = null;
		try {
			parameter = (NPRelate) super.clone();
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

	public void setContribution(double contribution) {
		this.contribution = contribution;
	}

	public double getBattery() {
		return battery;
	}

	public int getCapacity() {
		return capacity;
	}

	public double getContribution() {
		return contribution;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("NPRelate: Battery = " + getBattery() + " Capacity = ");
		return buffer.append(getCapacity()).toString();
	}
}