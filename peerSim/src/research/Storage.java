package research;

import peersim.cdsim.*;
import peersim.core.*;
import peersim.config.*;
import java.util.*;

public interface Storage extends Protocol{

	public Object clone();

	public boolean setData(Node node, Data data);

	public boolean isEmpty();

	public boolean contains(Data data);

	public ArrayList<Data> getData();

	public void clear();

	public void removeData(Node node, Data data);

	public void reduceTTL(Node node);

	public String toString();
}