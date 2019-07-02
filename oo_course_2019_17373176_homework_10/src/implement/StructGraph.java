package implement;

import com.oocourse.specs2.models.Path;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 图结构类
 * 说明：
 * 计算连通性，用可达性矩阵存储
 * 计算最短路径，Floyd算法
 * PS:这两个操作可以一起执行，都是在写入指令后重新计算
 */
public class StructGraph {
    private static final int MAX_Node = 250; // max graph nodes number
    private static final int INFINITY = 1000; // infinity
    /* map nodeId to graph */
    private boolean[][] graph = new boolean[MAX_Node][MAX_Node];
    private int[][] dis = new int[MAX_Node][MAX_Node]; // shortest dis
    /* connected */
    private boolean[][] connected = new boolean[MAX_Node][MAX_Node];

    public StructGraph() {
    }

    /* nodeMap<node, 0-249>
     * calculate graph by map and nodeMap
     */
    public void create(HashMap<Integer, Path> map,
                       HashMap<Integer, Integer> nodeMap) { // create graph
        for (int i = 0; i < MAX_Node; i++) {
            for (int j = i; j < MAX_Node; j++) { // double dir assign
                graph[i][j] = false;
                graph[j][i] = false;
                dis[i][j] = INFINITY;
                dis[j][i] = INFINITY;
                connected[i][j] = false;
                connected[j][i] = false;
            }
        }
        for (Path path : map.values()) { // linked the node
            ArrayList<Integer> nodes = ((MyPath) path).getMyPath();
            for (int i = 0; i < nodes.size() - 1; i++) {
                int nodeU = nodeMap.get(nodes.get(i));
                int nodeV = nodeMap.get(nodes.get(i + 1));
                graph[nodeU][nodeV] = true;
                graph[nodeV][nodeU] = true;
            }
        }
        floyd();
    }

    private void floyd() {
        for (int i = 0; i < MAX_Node; i++) {
            for (int j = i; j < MAX_Node; j++) {
                dis[i][i] = 0;
                if (i != j && (graph[i][j] || graph[j][i])) {
                    dis[i][j] = 1;
                    dis[j][i] = 1;
                    connected[i][j] = true;
                    connected[j][i] = true;
                }
            }
        }
        for (int k = 0; k < MAX_Node; k++) {
            for (int i = 0; i < MAX_Node; i++) {
                for (int j = 0; j < MAX_Node; j++) {
                    if (dis[i][j] > dis[i][k] + dis[k][j]) {
                        dis[i][j] = dis[i][k] + dis[k][j];
                        dis[j][i] = dis[i][k] + dis[k][j];
                        connected[i][j] = true;
                        connected[j][i] = true;
                    }
                }
            }
        }
    }

    protected boolean[][] getConnected() {
        return this.connected;
    }

    protected int[][] getDis() {
        return this.dis;
    }
}
