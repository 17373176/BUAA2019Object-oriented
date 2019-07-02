package implement;

import com.oocourse.uml2.models.elements.UmlClassOrInterface;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlGeneralization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class UmlRule008 {
    private HashSet<UmlClassOrInterface> gens = new HashSet<>();
    private final HashMap<String, ClassTree> classTreeMap;
    private final HashMap<String, InterfaceTree> interfaceTreeMap;
    private final HashMap<String, UmlElement> idMap;

    public UmlRule008(HashMap<String, ClassTree> classTreeMap,
                      HashMap<String, InterfaceTree> interfaceTreeMap,
                      HashMap<String, UmlElement> idMap) {
        this.classTreeMap = classTreeMap;
        this.interfaceTreeMap = interfaceTreeMap;
        this.idMap = idMap;
    }

    protected boolean check() {
        ArrayList<Integer> size = new ArrayList<>();
        for (ClassTree classT : classTreeMap.values()) { // 类继承
            String classEleId = classT.getClassEle().getId();
            HashSet<UmlClassOrInterface> thisGen = new HashSet<>();
            thisGen.add((UmlClassOrInterface) classT.getClassEle());
            findGeneralization(classEleId, classT, size, thisGen); // 递归
        }
        for (InterfaceTree interT : interfaceTreeMap.values()) { // 接口继承
            String interEleId = interT.getInterfaceEleId();
            HashSet<UmlClassOrInterface> thisGen = new HashSet<>();
            thisGen.add((UmlClassOrInterface) interT.getInterfaceEle());
            findInterGeneralization(interEleId, interT, size, thisGen);
        }
        return size.isEmpty();
    }

    // 类继承
    private void findGeneralization(String genId, ClassTree root, ArrayList<
            Integer> size, HashSet<UmlClassOrInterface> thisGen) {
        HashMap<String, UmlGeneralization> nowGens =
                classTreeMap.get(genId).getGens(); // get target的继承元素表
        if (nowGens.isEmpty()) { // 顶级父类没有继承元素表
            thisGen.remove(idMap.get(genId));
            return;
        }
        // 继承元素表isn't empty
        for (UmlGeneralization it : nowGens.values()) {
            String newId = it.getTarget();
            if (newId.equals(root.getClassEle().getId())) { // 循环继承
                size.add(1);
                gens.addAll(thisGen);
                return;
            }
            for (UmlClassOrInterface gen : thisGen) {
                if (newId.equals(gen.getId())) { // 循环继承
                    return;
                }
            }
            thisGen.add((UmlClassOrInterface) idMap.get(newId));
            findGeneralization(newId, root, size, thisGen);
            thisGen.remove(idMap.get(newId));
        }
    }

    // 接口继承
    private void findInterGeneralization(String genId, InterfaceTree root
            , ArrayList<Integer> size
            , HashSet<UmlClassOrInterface> thisGen) {
        HashMap<String, UmlGeneralization> nowGens =
                interfaceTreeMap.get(genId).getGens(); // get target的继承元素表
        if (nowGens.isEmpty()) { // 顶级父类没有继承元素表
            return;
        }
        // 继承元素表isn't empty
        for (UmlGeneralization it : nowGens.values()) {
            String newId = it.getTarget();
            if (newId.equals(root.getInterfaceEleId())) { // 循环继承
                size.add(1);
                gens.addAll(thisGen);
                return;
            }
            for (UmlClassOrInterface gen : thisGen) {
                if (newId.equals(gen.getId())) { // 循环继承
                    return;
                }
            }
            thisGen.add((UmlClassOrInterface) idMap.get(newId));
            findInterGeneralization(newId, root, size, thisGen);
            thisGen.remove(idMap.get(newId));
        }
    }

    protected Set<UmlClassOrInterface> getGens() {
        return this.gens;
    }
}
