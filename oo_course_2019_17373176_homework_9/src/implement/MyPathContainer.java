package implement;

import com.oocourse.specs1.models.Path;
import com.oocourse.specs1.models.PathContainer;
import com.oocourse.specs1.models.PathIdNotFoundException;
import com.oocourse.specs1.models.PathNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MyPathContainer implements PathContainer {
    // 正常情况下能保证两个容器长度一样大
    private ArrayList<Path> paList = new ArrayList<>();
    private ArrayList<Integer> pidList = new ArrayList<>();
    private HashMap<Integer, Path> map = new HashMap<>(); // <idPath, ath>
    private static int idPath = 0;
    private static int count = 0;

    public MyPathContainer() {
    }

    public int size() {
        return map.size();
    }

    public boolean containsPath(Path path) {
        if (path != null) {
            if (map.containsValue(path)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsPathId(int pathId) {
        if (map.containsKey(pathId)) {
            return true;
        }
        return false;
    }

    public Path getPathById(int pathId) throws PathIdNotFoundException {
        if (!containsPathId(pathId)) {
            throw new PathIdNotFoundException(pathId); // return error pathId
        } else {
            return map.get(pathId);
        }
    }

    public int getPathId(Path path) throws PathNotFoundException {
        if (path == null || !path.isValid() || !containsPath(path)) {
            throw new PathNotFoundException(path); // return error path
        } else {
            return pidList.get(paList.indexOf(path));
        }
    }

    public int addPath(Path path) {
        if (path != null && path.isValid()) {
            boolean hasPath = containsPath(path); // \old(containsPath(path))
            if (!hasPath) { // Do not add already exist path
                paList.add(path);
                idPath++;
                pidList.add(idPath);
                map.put(idPath, path);
                distinctNodeCount();
                return idPath;
            }
            return pidList.get(paList.indexOf(path)); // path exist, return id
        }
        return 0;
    }

    public int removePath(Path path) throws PathNotFoundException {
        if (path == null || !path.isValid() || !containsPath(path)) {
            throw new PathNotFoundException(path);
        } else {
            int id = getPathId(path);
            paList.remove(path);
            // ps: id isn't index, it is object
            pidList.remove(pidList.lastIndexOf(id));
            map.remove(id, path);
            distinctNodeCount();
            return id;
        }
    }

    public void removePathById(int pathId) throws PathIdNotFoundException {
        if (!containsPathId(pathId)) {
            throw new PathIdNotFoundException(pathId);
        } else {
            Path path = getPathById(pathId);
            paList.remove(path);
            pidList.remove(pidList.lastIndexOf(pathId));
            map.remove(pathId, path);
            distinctNodeCount();
        }
    }

    public int getDistinctNodeCount() { //在容器全局范围内查找不同的节点数
        return count;
    }

    private void distinctNodeCount() {
        HashSet<Integer> arr = new HashSet<>();
        for (Path it : paList) {
            for (Integer node : it) {
                arr.add(node);
            }
        }
        count = arr.size();
    }
}

