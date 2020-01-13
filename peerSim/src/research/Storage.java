package research;

import peersim.core.*;
import java.util.*;

public interface Storage extends Protocol {

	public Object clone();

	public boolean setData(Node node, Data data);

	public boolean isEmpty();

	public boolean contains(Data data);

	public ArrayList<Data> getData();

	public void clear();

	public String toString();
}