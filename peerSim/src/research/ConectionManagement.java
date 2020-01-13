package research;

import peersim.core.*;
import java.util.*;

public class ConectionManagement {
    private static ArrayList<Node> addedNode;
    private static ArrayList<ArrayList<Node>> addedList;
    private static HashMap<Node, Integer> nodeTTL;
    private static Queue<Node> queue;

    public static boolean contains(Node node) {
        for (ArrayList<Node> list : addedList) {
            if (list.contains(node)) {
                return true;
            }
        }

        return false;
    }

    public static void search(Node node, int ttl) {
        queue.add(node); // キューに引数のノードを入れる
        nodeTTL.put(node, ttl); // ノードとttlを関連付け
        addedNode.add(node); // キューに追加したノードを保持．再調査防止

        // キューが空でない場合探し続ける
        while (Objects.nonNull(queue.peek())) {
            node = queue.poll(); // キューからノードを取り出す
            int nowTTL = nodeTTL.get(node); // 取り出したノードに関連付けられたTTLを取り出す

            // System.out.println("TTL: " + nowTTL);
            // 取り出したノードのTTLが0であった場合
            if (nowTTL <= 0) {
                if (Objects.isNull(queue.peek())) {
                    return;
                }
                continue;
            }

            int newTTL = nowTTL - 1;
            Link linkable = SharedResource.getLink(node); // 繋がっているノードを取得
            for (int nodeID = 0; nodeID < linkable.degree(); nodeID++) {
                Node neighbor = linkable.getNeighbor(nodeID);

                if (!addedNode.contains(neighbor)) {
                    if (newTTL > 0) {
                        nodeTTL.put(neighbor, newTTL); // 隣接ノードとttlを関連付け
                        queue.add(neighbor);
                        addedNode.add(neighbor); // キューに追加したノードを保持
                    }
                }
            }
        }

        return;
    }

    public static boolean check() {
        addedList = new ArrayList<ArrayList<Node>>();
        nodeTTL = new HashMap<Node, Integer>();
        queue = new ArrayDeque<Node>();

        int loops = 0;
        for (int i = 0; i < Network.size(); i++) {
            Node node = Network.get(i);

            if (!contains(node)) {
                addedNode = new ArrayList<Node>();
                addedList.add(addedNode);

                search(node, Network.size());

                addedList.set(loops, addedNode);
                loops++;
            }
        }

        // System.out.println(loops);

        if (loops == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static void addLink(Link srcLink, Node addNode) {
        srcLink.addNeighbor(addNode);
    }

    public static void removeLink(Node srcNode) {
        Queue<Node> queue = new ArrayDeque<Node>();
        Link srcLinkSet = SharedResource.getLink(srcNode);

        // 削除するノードのリンクを取得し、隣接ノードをキューに入れる
        for (int i = 0; i < srcLinkSet.degree(); i++) {
            queue.add(srcLinkSet.getNeighbor(i));
        }

        // 隣接ノードに削除するノードを持つ,ノードから、該当ノードを削除する
        while (Objects.nonNull(queue.peek())) {
            Node dstNode = queue.poll();
            Link dstLinkSet = SharedResource.getLink(dstNode);

            for (int i = 0; i < dstLinkSet.degree(); i++) {
                if (dstLinkSet.getNeighbor(i).getIndex() == srcNode.getIndex()) {
                    dstLinkSet.removeNeighbor(i);
                }
            }
        }
        srcLinkSet.onKill();
    }

    public static void relocate(ArrayList<Node> baseSet, Node srcNode) {
        int srcID = srcNode.getIndex();
        NodeCoordinate srcCrd = SharedResource.getCoordinate(srcNode);
        Link srcLinkSet = (Link) SharedResource.getLink(srcNode);

        while (true) {
            int dstID = 0;
            while (dstID < Network.size()) {
                if (srcID == dstID) {
                    dstID++;
                    continue;
                }
                Node dstNode = Network.get(dstID);
                NodeCoordinate dstCrd = SharedResource.getCoordinate(dstNode);
                Link dstLinkSet = SharedResource.getLink(dstNode);

                if (RandomGeometricGraph.isConnect(srcCrd, dstCrd)) {
                    if (baseSet.contains(dstNode)) {
                        addLink(srcLinkSet, dstNode);
                        addLink(dstLinkSet, srcNode);
                    }
                }
                dstID++;
            }

            if (srcLinkSet.degree() > 0) {
                return;
            } else {
                srcCrd.setCoordinate();
            }
        }
    }

    public static ArrayList<Node> which() {
        ArrayList<Node> baseSet;
        baseSet = addedList.get(0);

        for (ArrayList<Node> list : addedList) {
            if (list.size() > baseSet.size()) {
                baseSet = list;
            }
        }

        return baseSet;
    }

    public static void ajust() {
        ArrayList<Node> baseSet = which();

        for (ArrayList<Node> list : addedList) {
            if (list.size() != baseSet.size()) {
                for (Node node : list) {
                    removeLink(node);
                    relocate(baseSet, node);
                }
            }
        }
    }
}