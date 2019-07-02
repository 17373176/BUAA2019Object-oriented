package implement;

import com.oocourse.specs3.models.Path;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 最少换乘
 * 存储每个node所在路线号,并存储边权，权值初始化为INFINITY
 * 对于每条path内，边权值初始化为1，即不需要换乘，将该path构造为完全图
 * Floyd算法求最短路，(weight求和 - 1)为最少换乘次数
 */
public class LeastTrans {
    private static final int MAX_Node = 120; // max graph nodes number
    private static final int INFINITY = 120; // infinity edge weight
    private int[][] weight = new int[MAX_Node][MAX_Node]; // edge weight

    public LeastTrans() {
    }

    public void create(HashMap<Integer, Path> map,
                       HashMap<Integer, Integer> nodeMap) {
        for (int i = 0; i < MAX_Node; i++) {
            for (int j = i; j < MAX_Node; j++) { // double weight assign
                weight[i][j] = INFINITY;
                weight[j][i] = INFINITY;
            }
        }
        for (Path path : map.values()) { // initial
            ArrayList<Integer> nodes = ((MyPath) path).getMyPath();
            for (int i = 0; i < nodes.size() - 1; i++) { // complete path
                for (int j = i + 1; j <= nodes.size() - 1; j++) {
                    int nodeU = nodeMap.get(nodes.get(i));
                    int nodeV = nodeMap.get(nodes.get(j));
                    weight[nodeU][nodeV] = 1;
                    weight[nodeV][nodeU] = 1;
                }
            }
        }
        floyd();
    }

    public void put(Path path, HashMap<Integer, Integer> nodeMap) {
        ArrayList<Integer> nodes = ((MyPath) path).getMyPath();
        for (int i = 0; i < nodes.size() - 1; i++) { // complete path
            for (int j = i + 1; j <= nodes.size() - 1; j++) {
                int nodeU = nodeMap.get(nodes.get(i));
                int nodeV = nodeMap.get(nodes.get(j));
                weight[nodeU][nodeV] = 1;
                weight[nodeV][nodeU] = 1;
            }
        }
        floyd();
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
