package research;

import peersim.config.*;
import peersim.core.*;
import peersim.dynamics.*;
import peersim.graph.*;
import java.util.*;

public class WireUndirectedGraph extends WireGraph{
   // ------------------------------------------------------------------------
    // Parameters
    // ------------------------------------------------------------------------
	private static final String PAR_PROT = "protocol";
	private static final String PAR_K = "k";

    // --------------------------------------------------------------------------
    // Fields
    // --------------------------------------------------------------------------
	private final int pid;
	private final int k;

	private Random random = new Random();
	private ArrayList<Integer> num = new ArrayList<Integer>();

    // --------------------------------------------------------------------------
    // Initialization
    // --------------------------------------------------------------------------

    /**
     * Standard constructor that reads the configuration parameters. Normally
     * invoked by the simulation engine.
     * 
     * @param prefix
     *            the configuration prefix for this class
     */
    public WireUndirectedGraph(String prefix) {
    	super(prefix);
    	k = Configuration.getInt(prefix + "." + PAR_K, 3);
    	pid = Configuration.getPid(prefix + "." + PAR_PROT);
    }

    /**
     * Performs the actual wiring.
     * @param g a {@link peersim.graph.Graph} interface object to work on.
     */
    public void wire(Graph g) {

    	for(int i=0; i<Network.size(); i++){
    		num.add(random.nextInt(k-2)+2);
    		// num.add(3);
    	}

    	// 全ノード分ループ
    	for(int wireSource=0; wireSource<Network.size(); wireSource++){
    		// 各ノードについて
    		// ランダムにリンクを張る
            while(num.get(wireSource)>0){
                int wireDestination = random.nextInt(Network.size());
                if(wireSource == wireDestination) continue;
                g.setEdge(wireSource, wireDestination);
                g.setEdge(wireDestination, wireSource);
                num.set(wireDestination, num.get(wireDestination) - 1);
                num.set(wireSource, num.get(wireSource)-1);
            }
        }
    }
}