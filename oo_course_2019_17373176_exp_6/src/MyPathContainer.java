import com.oocourse.specs1.models.Path;
import com.oocourse.specs1.models.PathContainer;
import com.oocourse.specs1.models.PathIdNotFoundException;
import com.oocourse.specs1.models.PathNotFoundException;

import java.util.HashMap;

public class MyPathContainer implements PathContainer {
    private final HashMap<Node, Integer> nodeCount;
    private final HashMap<Integer, Path> pathIdMap;
    private final HashMap<Path, Integer> pathMap;
    private int maxId;

    public MyPathContainer() {
        this.maxId = 0;
        this.pathMap = new HashMap<>();
        this.pathIdMap = new HashMap<>();
        this.nodeCount = new HashMap<>();
    }

    public int size() {
        return this.pathMap.size();
    }

    public boolean containsPath(Path path) {
        return this.pathMap.containsKey(path);
    }

    public boolean containsPathId(int pathId) {
        return this.pathIdMap.containsKey(pathId);
    }

    public Path getPathById(int pathId) throws PathIdNotFoundException {
        if (this.pathIdMap.containsKey(pathId)) {
            return this.pathIdMap.get(pathId);
        } else {
            throw new PathIdNotFoundException(pathId);
        }
    }

    @Override
    public int getPathId(Path path) throws PathNotFoundException {
        if (this.pathMap.containsKey(path)) {
            return this.pathMap.get(path);
        } else {
            throw new PathNotFoundException(path);
        }
    }

    public int addPath(Path path) {
        if (!containsPath(path)) {
            this.maxId += 1;
            this.pathMap.put(path, this.maxId);
            this.pathIdMap.put(this.maxId, path);
            for (Integer nodeId : path) {
                Node node = new Node(nodeId);
                int originalCount = this.nodeCount.getOrDefault(node, 0);
                this.nodeCount.put(node, originalCount + 1);
            }
            return this.maxId;
        } else {
            try {
                return this.getPathId(path);
            } catch (Exception e) {
                return 0;
            }
        }
    }

    public int removePath(Path path) throws PathNotFoundException {
        if (containsPath(path)) {
            int pathId = this.pathMap.get(path);
            this.pathMap.remove(path);
            this.pathIdMap.remove(pathId);
            for (Integer nodeId : path) {
                Node node = new Node(nodeId);
                int originalCount = this.nodeCount.getOrDefault(node, 0);
                int newCount = originalCount - 1;
                if (newCount > 0) {
                    this.nodeCount.put(node, newCount);
                } else {
                    this.nodeCount.remove(node);
                }
            }
            return pathId;
        } else {
            throw new PathNotFoundException(path);
        }
    }

    public void removePathById(int pathId) throws PathIdNotFoundException {
        if (containsPathId(pathId)) {
            Path path = getPathById(pathId);
            try {
                removePath(path);
            } catch (PathNotFoundException e) {
                throw new PathIdNotFoundException(pathId);
            }
        } else {
            throw new PathIdNotFoundException(pathId);
        }
    }

    public int getDistinctNodeCount() {
        return this.nodeCount.size();
    }
}
