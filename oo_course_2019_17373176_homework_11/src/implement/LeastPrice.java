package implement;

import com.oocourse.specs3.models.Path;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 最低票价
 * x为经过的边数，y为换乘次数，票价PPath = x + 2 * y
 * 存储边权值，权值为该边的票价，即 1 + 2，将该path构造为完全图
 * 用Floyd算法求带权图最短路
 */
public class LeastPrice {
    private static final int MAX_Node = 120; // max graph nodes number
    private static final int INFINITY = 100000; // infinity edge weight
    private static final int PATHNUM = 50; // path max number
    // edge weight
    private int[][][] weightPath = new int[PATHNUM][MAX_Node][MAX_Node];
    private int[][] weight = new int[MAX_Node][MAX_Node];

    public LeastPrice() {
    }

    public void create(HashMap<Integer, Path> map,
                       HashMap<Integer, Integer> nodeMap) {
        for (int i = 0; i < MAX_Node; i++) {
            for (int j = i; j < MAX_Node; j++) { // double weight assign
                weight[i][j] = INFINITY;
                weight[j][i] = INFINITY;
                weight[i][i] = 0;
                for (int k = 0; k < PATHNUM; k++) {
                    weightPath[k][i][j] = INFINITY;
                    weightPath[k][j][i] = INFINITY;
                    weightPath[k][i][i] = INFINITY;
                }
            }
        }
        ArrayList<Path> paths = new ArrayList<>(map.values());
        for (int k = 0; k < paths.size(); k++) { // initial
            ArrayList<Integer> nodes = ((MyPath) paths.get(k)).getMyPath();
            for (int i = 0; i < nodes.size() - 1; i++) {
                int nodeU = nodeMap.get(nodes.get(i));
                int nodeV = nodeMap.get(nodes.get(i + 1));
                if (nodeU != nodeV) {
                    weightPath[k][nodeU][nodeV] = 1;
                    weightPath[k][nodeV][nodeU] = 1;
                }
            }
            floydPath(paths, nodeMap, k);
        }
        for (int i = 0; i < MAX_Node; i++) {
            for (int j = i + 1; j < MAX_Node; j++) {
                int min = INFINITY;
                for (int k = 0; k < PATHNUM; k++) {
                    int w = weightPath[k][i][j];
                    if (i != j && w != INFINITY && w < min) {
                        min = w;
                    }
                }
                if (weight[i][j] != 0 && min != INFINITY) {
                    weight[i][j] = 2 + min;
                    weight[j][i] = 2 + min;
                }
            }
        }
        floyd();
    }

    private void floydPath(ArrayList<Path> paths, HashMap<Integer, Integer>
            nodeMap, int index) {
        ArrayList<Integer> nodes = ((MyPath) paths.get(index)).getMyPath();
        for (int k = 0; k < nodes.size(); k++) {
            int w = nodeMap.get(nodes.get(k));
            for (int i = 0; i < nodes.size(); i++) {
                int u = nodeMap.get(nodes.get(i));
                for (int j = 0; j < nodes.size(); j++) {
                    int v = nodeMap.get(nodes.get(j));
                    if (weightPath[index][u][v] > weightPath[index][u][w] +
                            weightPath[index][w][v]) {
                        weightPath[index][u][v] = weightPath[index][u][w] +
                                weightPath[index][w][v];
                        weightPath[index][v][u] = weightPath[index][u][w] +
                                weightPath[index][w][v];
                    }
                }
            }
        }
    }

    private void floyd() {
        for (int k = 0; k < MAX_Node; k++) {
            for (int i = 0; i < MAX_Node; i++) {
                for (int j = 0; j < MAX_Node; j++) {
                    if (weight[i][j] > weight[i][k] + weight[k][j]) {
                        weight[i][j] = weight[i][k] + weight[k][j];
                        weight[j][i] = weight[i][k] + weight[k][j];
                    }
                }
            }
        }
    }

    protected int[][] getWeight() {
        return this.weight;
    }
}
