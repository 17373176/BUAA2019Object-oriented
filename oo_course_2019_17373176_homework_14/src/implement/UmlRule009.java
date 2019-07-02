package implement;

import com.oocourse.uml2.models.elements.UmlClassOrInterface;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlGeneralization;
import com.oocourse.uml2.models.elements.UmlInterfaceRealization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class UmlRule009 {
    private HashSet<UmlClassOrInterface> gens = new HashSet<>();
    private final HashMap<String, ClassTree> classTreeMap;
    private final HashMap<String, InterfaceTree> interfaceTreeMap;
    private final HashMap<String, UmlElement> idMap;

    public UmlRule009(HashMap<String, ClassTree> classTreeMap,
                      HashMap<String, InterfaceTree> interfaceTreeMap,
                      HashMap<String, UmlElement> idMap) {
        this.classTreeMap = classTreeMap;
        this.interfaceTreeMap = interfaceTreeMap;
        this.idMap = idMap;
    }

    protected boolean check() {
        boolean flag = true;
        for (ClassTree classT : classTreeMap.values()) { // 类重复继承
            String classEleId = classT.getClassEle().getId();
            ArrayList<String> myGenera = new ArrayList<>(); // <id>
            // 类对接口的实现
            findGeneralization(classEleId, myGenera); // 递归
            for (int i = 0; i < myGenera.size(); i++) {
                for (int j = i + 1; j < myGenera.size(); j++) {
                    if (myGenera.get(i).equals(myGenera.get(j))) {
                        flag = false;
                        gens.add((UmlClassOrInterface) classT.getClassEle());
                    }
                }
            }
        }
        for (InterfaceTree interT : interfaceTreeMap.values()) { // 接口重复继承
            String interEleId = interT.getInterfaceEleId();
            ArrayList<String> myGenera = new ArrayList<>(); // <id>
            findInterGeneralization(interEleId, myGenera); // 递归
            for (int i = 0; i < myGenera.size(); i++) {
                for (int j = i + 1; j < myGenera.size(); j++) {
                    if (myGenera.get(i).equals(myGenera.get(j))) {
                        flag = false;
                        gens.add((UmlClassOrInterface) interT.
                                getInterfaceEle());
                    }
                }
            }
        }
        return flag;
    }

    // 类继承
    private void findGeneralization(String genId, ArrayList<String>
            myGenera) {
        HashMap<String, UmlGeneralization> nowGens =
                classTreeMap.get(genId).getGens(); // get target的继承元素表
        ArrayList<UmlElement> myReal = new ArrayList<>(
                classTreeMap.get(genId).getInfRs().values());
        if (nowGens.isEmpty() && myReal.isEmpty()) { // 顶级父类没有继承元素表
            return;
        }
        // 继承元素表isn't empty
        for (UmlGeneralization it : nowGens.values()) {
            String newId = it.getTarget();
            findGeneralization(newId, myGenera);
            myGenera.add(newId);
        }
        for (UmlElement it : myReal) {
            myGenera.add(((UmlInterfaceRealization) it).getTarget());
            InterfaceTree tree = interfaceTreeMap.get((
                    (UmlInterfaceRealization) it).getTarget());
            if (tree.getFlag() == 1) {
                findInterReal(((UmlInterfaceRealization) it).
                        getTarget(), myGenera); // 递归
            }
        }
    }

    // 接口继承
    private void findInterGeneralization(String genId, ArrayList<String>
            myGenera) {
        HashMap<String, UmlGeneralization> nowGens =
                interfaceTreeMap.get(genId).getGens(); // get target的继承元素表
        if (nowGens.isEmpty()) { // 顶级父类没有继承元素表
            return;
        }
        // 继承元素表isn't empty
        for (UmlGeneralization it : nowGens.values()) {
            String newId = it.getTarget();
            findInterGeneralization(newId, myGenera);
            myGenera.add(newId);
        }
    }

    private void findInterReal(String genId, ArrayList<String>
            myGenera) {
        InterfaceTree tree = interfaceTreeMap.get(genId);
        if (tree.getFlag() == 1) {
            HashMap<String, UmlGeneralization> nowGens =
                    interfaceTreeMap.get(genId).getGens(); // get target的继承元素表
            if (nowGens.isEmpty()) { // 顶级父类没有继承元素表
                return;
            }
            // 继承元素表isn't empty
            for (UmlGeneralization it : nowGens.values()) {
                String newId = it.getTarget();
                findInterGeneralization(newId, myGenera);
                myGenera.add(newId);
            }
        }
    }

    protected Set<UmlClassOrInterface> getGens() {
        return this.gens;
    }
}
