package research;

import peersim.config.*;
import peersim.core.*;
import peersim.dynamics.*;
import peersim.graph.*;

public class WireRGG extends WireGraph {
    private static final String PAR_PROT = "protocol";
    private final int pid;

    private Graph g;

    public WireRGG(String prefix) {
        super(prefix);
        pid = Configuration.getPid(prefix + "." + PAR_PROT);
    }

    private void relocate(int srcID) {
        Node srcNode = Network.get(srcID);
        NodeCoordinate srcCrd = SharedResource.getCoordinate(srcNode);

        while (true) {
            int dstID = 0;
            while (dstID < Network.size()) {
                if (srcID == dstID) {
                    dstID++;
                    continue;
                }
                Node dstNode = Network.get(dstID);

                NodeCoordinate dstCrd = SharedResource.getCoordinate(dstNode);
                if (RandomGeometricGraph.isConnect(srcCrd, dstCrd)) {
                    g.setEdge(srcID, dstID);
                    g.setEdge(dstID, srcID);
                }
                dstID++;
            }

            Link linkable = (Link) srcNode.getProtocol(pid);

            if (linkable.degree() > 0) {
                return;
            } else {
                srcCrd.setCoordinate();
            }
        }
    }

    public void wire(Graph g) {
        this.g = g;

        int srcID = 0;
        while (srcID < Network.size()) {
            Node srcNode = Network.get(srcID);
            NodeCoordinate srcCrd = SharedResource.getCoordinate(srcNode);
            int dstID = srcID + 1;
            while (dstID < Network.size()) {
                Node dstNode = Network.get(dstID);

                NodeCoordinate dstCrd = SharedResource.getCoordinate(dstNode);
                if (RandomGeometricGraph.isConnect(srcCrd, dstCrd)) {
                    g.setEdge(srcID, dstID);
                    g.setEdge(dstID, srcID);
                }
                dstID++;
            }
            // System.out.println(srcNode);

            Link linkable = (Link) srcNode.getProtocol(pid);
            if (linkable.degree() < 1) {
                relocate(srcID);
            }

            srcID++;
        }

        if (!ConectionManagement.check()) {
            ConectionManagement.ajust();
        }
        ConectionManagement.check();
    }
}