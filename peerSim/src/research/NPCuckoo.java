package research;

import peersim.core.*;
import peersim.config.*;
import java.util.*;

public class NPCuckoo implements Parameter{
	private double battery=0.0;
	private int capacity=0;
	
	public NPCuckoo(String prefix){
	}

	public Object clone(){
		NPCuckoo parameter = null;
		try{
			parameter = (NPCuckoo) super.clone();
		}catch(CloneNotSupportedException e){
		}
		return parameter;
	}

	public void setBattery(double battery){
		this.battery = battery;
	}

	public void setCapacity(int capacity){
		this.capacity = capacity;
	}

	public double getBattery(){
		return battery;
	}

	public int getCapacity(){
		return capacity;
	}


	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("NPCuckoo: Battery = " + getBattery() + " Capacity = ");
		return buffer.append(getCapacity()).toString();
	}
}