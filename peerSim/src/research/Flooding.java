package research;

import peersim.cdsim.*;
import peersim.core.*;

public class Flooding implements CDProtocol{

	public Flooding(String prefix){
	}

	public Object clone(){
		Flooding flooding = null;
		try{
			flooding = (Flooding) super.clone();
		}catch(CloneNotSupportedException e){
		}		
		return flooding;
	}

	public void nextCycle(Node node, int protocolID){
	}

}