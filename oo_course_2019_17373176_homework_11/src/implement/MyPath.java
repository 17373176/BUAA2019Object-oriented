package implement;

import com.oocourse.specs3.models.NodeIdNotFoundException;
import com.oocourse.specs3.models.NodeNotConnectedException;
import com.oocourse.specs3.models.Path;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class MyPath implements Path {
    private final ArrayList<Integer> nodes = new ArrayList<>();

    public MyPath(int[] nodes) {
        for (Integer it : nodes) {
            this.nodes.add(it);
        }
    }

    public Iterator<Integer> iterator() {
        return nodes.iterator();
    }

    public int compareTo(Path o) {
        int len = nodes.size();
        if (nodes.size() > o.size()) {
            len = o.size();
        }
        for (int i = 0; i < len; i++) {
            if (this.getNode(i) != o.getNode(i)) {
                return Integer.compare(this.getNode(i), o.getNode(i));
            }
        }
        return Integer.compare(this.size(), o.size()); // equal
    }

    // pure method just gets data, it doesn't change the data.
    public int size() {
        return nodes.size();
    }

    public int getNode(int index) {
        if (index >= 0 && index < size()) {
            return nodes.get(index);
        } else {
            return 0;
        }
    }

    public boolean containsNode(int node) {
        HashSet<Integer> arr = new HashSet<>(nodes);
        if (arr.contains(node)) { // contains method of HashSet is key map
            return true;
        }
        return false;
    }

    public boolean containsEdge(int fromNodeId, int toNodeId) {
        for (int i = 0; i < nodes.size() - 1; i++) {
            if ((nodes.get(i) == fromNodeId && nodes.get(i + 1)
                    == toNodeId) || (nodes.get(i) == toNodeId &&
                    nodes.get(i + 1) == fromNodeId)) {
                return true;
            }
        }
        return false;
    }

    public int getShortestPathLength(int fromNodeId, int toNodeId) throws
            NodeIdNotFoundException, NodeNotConnectedException {
        return 0;
    }

    public int getDistinctNodeCount() { // return set.length
        HashSet<Integer> arr = new HashSet<>(nodes);
        return arr.size();
    }

    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Path) {
            if (((Path) obj).size() == nodes.size()) {
                for (int i = 0; i < nodes.size(); i++) {
                    if (nodes.get(i) != ((Path) obj).getNode(i)) {
                        return false;
                    }
                }
            }
            return ((Path) obj).size() == nodes.size(); // obj.size()==length
        }
        return false;
    }

    public boolean isValid() {
        return nodes.size() >= 2;
    }

    public int getUnpleasantValue(int nodeId) {
        if (!containsNode(nodeId)) {
            return 0;
        } else {
            return (int)Math.pow(4, (nodeId % 5 + 5) % 5);
        }
    }

    public ArrayList<Integer> getMyPath() {
        return this.nodes;
    }
}

