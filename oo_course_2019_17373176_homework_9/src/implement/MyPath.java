package implement;

import com.oocourse.specs1.models.Path;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class MyPath implements Path {
    //private final HashMap<Integer, Integer> nodes = new HashMap<>();
    private final ArrayList<Integer> nodes = new ArrayList<>();

    public MyPath(int[] nodes) {
        for (Integer it : nodes) {
            //this.nodes.put(it.hashCode(), it);
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
        if (arr.contains(node)) {
            return true;
        }
        return false;
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
}

