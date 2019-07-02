package implement;

import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlGeneralization;

import java.util.HashMap;

/**
 * 接口树
 * 功能同ClassTree类
 */
public class InterfaceTree {
    private final UmlElement interfaceEle; // interface element
    private final String interfaceEleId; // class id
    private HashMap<String, UmlElement> idMap;
    //private final UmlElement[] eleArray; // elements array
    // interfaceTreeMap<id, tree>
    private HashMap<String, InterfaceTree> interfaceTreeMap;
    // 父类接口集合<id, interfaceTree>
    private HashMap<String, InterfaceTree> interfaceFather = new HashMap<>();
    /* map<id, UML> */
    private HashMap<String, UmlAttribute> attributes = new HashMap<>();
    private HashMap<String, AssTree> associations = new HashMap<>();
    private HashMap<String, OperaTree> operations = new HashMap<>();
    private HashMap<String, UmlGeneralization> gens = new HashMap<>();

    public InterfaceTree(UmlElement ele, HashMap<String, UmlElement> idMap) {
        this.interfaceEle = ele;
        this.interfaceEleId = ele.getId();
        this.idMap = idMap;
        create();
    }

    private void create() {
        for (UmlElement it : idMap.values()) {
            if (it.getParentId().equals(interfaceEleId)) { // is class's son
                switch (it.getElementType()) {
                    case UML_ATTRIBUTE:
                        attributes.put(it.getId(), (UmlAttribute) it);
                        break;
                    case UML_ASSOCIATION:
                        AssTree assTree = new AssTree(it, idMap, 0);
                        associations.put(it.getId(), assTree);
                        break;
                    case UML_OPERATION:
                        OperaTree operaTree = new OperaTree(it, idMap);
                        operations.put(it.getId(), operaTree);
                        break;
                    case UML_GENERALIZATION:
                        gens.put(it.getId(), (UmlGeneralization) it);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    protected void calculateFather(HashMap<String, InterfaceTree> inTreeMap) {
        this.interfaceTreeMap = inTreeMap;
        //topClassName = classEle.getName();// set initial father
        findGeneralization(interfaceEleId); // 递归
    }

    private void findGeneralization(String genId) { // 接口可以多继承，类可以实现多个接口
        HashMap<String, UmlGeneralization> nowGens =
                interfaceTreeMap.get(genId).getGens(); // get target的继承元素表
        if (nowGens.isEmpty()) { // 顶级父类没有继承元素表
            return;
        }
        // 继承元素表isn't empty
        for (UmlGeneralization it : nowGens.values()) {
            String newId = it.getTarget();
            interfaceFather.put(newId, interfaceTreeMap.get(newId)); // 存储父类
            findGeneralization(newId);
        }
    }

    protected UmlElement getInterfaceEle() {
        return this.interfaceEle;
    }

    protected HashMap<String, UmlGeneralization> getGens() {
        return this.gens;
    }

    protected HashMap<String, InterfaceTree> getInterfaceFather() {
        return this.interfaceFather;
    }

    protected String getInterfaceEleId() {
        return this.interfaceEleId;
    }
}
