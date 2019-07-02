package implement;

import java.util.HashMap;

/**
 * 连通块数
 * 并查集，addPath可以增加，但每次remove需要重算 afresh
 * 添加 unio(x, y), (x, y) <==> (nodeMap.get(x), nodeMap.get(y))
 * 考虑路径压缩find
 */
public class ConnectB {
    private static final int MAX_Node = 120; // max graph nodes number
    // <node, father>
    private HashMap<Integer, Integer> father = new HashMap<>();

    public ConnectB() {
    }

    public void create(HashMap<Integer, Integer> nodeMap, boolean[][] graph) {
        father.clear();
        for (int node : nodeMap.values()) { // initial mapped node
            father.put(node, node);
        }
        for (int i = 0; i < MAX_Node; i++) {
            for (int j = i; j < MAX_Node; j++) {
                if (i != j && (graph[i][j] || graph[j][i])) {
                    unio(i, j);
                }
            }
        }
    }

    public void put(int node) { // father put
        if (!getContainsKey(node)) { // not exist then put
            father.put(node, node);
        }
    }

    private boolean getContainsKey(int node) {
        return father.containsKey(node);
    }

    public int getBlockCount(HashMap<Integer, Integer> nodeMap) {
        int blockCount = 0; // clear and update
        for (int node : nodeMap.values()) {
            if (father.get(node) == node) {
                blockCount++;
            }
        } // traversal father map to get unique root
        return blockCount;
    }

    protected void unio(int x, int y) {
        int faX = pressedFind(x);
        int faY = pressedFind(y);
        if (faX != faY) {
            father.put(faX, faY);
        }
    }

    protected int pressedFind(int x) {
        int parent = father.get(x);
        while (parent != father.get(parent)) {
            parent = father.get(parent);
        }
        int pre;
        int cur = x;
        while (cur != father.get(cur)) {
            pre = father.get(cur);
            father.put(cur, parent);
            cur = pre;
        }
        return parent;
    }
}
