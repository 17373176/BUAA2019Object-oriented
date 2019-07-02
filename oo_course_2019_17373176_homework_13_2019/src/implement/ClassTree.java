package implement;

import com.oocourse.uml1.interact.common.AttributeClassInformation;
import com.oocourse.uml1.interact.common.AttributeQueryType;
import com.oocourse.uml1.interact.common.OperationQueryType;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAssociation;
import com.oocourse.uml1.models.elements.UmlAssociationEnd;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import static com.oocourse.uml1.interact.common.AttributeQueryType.SELF_ONLY;
import static com.oocourse.uml1.interact.common.OperationQueryType.ALL;
import static com.oocourse.uml1.interact.common.OperationQueryType.NON_PARAM;
import static com.oocourse.uml1.interact.common.OperationQueryType.NON_RETURN;
import static com.oocourse.uml1.interact.common.OperationQueryType.PARAM;
import static com.oocourse.uml1.interact.common.OperationQueryType.RETURN;
import static com.oocourse.uml1.models.common.ElementType.UML_ASSOCIATION_END;
import static com.oocourse.uml1.models.common.ElementType.UML_CLASS;

/**
 * 类结构树
 * 通过className建立树结构，并对树结构内进行相应计算
 * 一个项目有一个Model容器，容器内有多个类、接口等
 * 每个class下有孩子元素，其中Operation元素下还有Parameter元素
 * 每个元素都有成员_parentId, _id, _type, name, type, visibility, direction, source(源),
 * target(目标), reference(相关的两个类，正常情况分两个end元素), navigable(关联方向)等property.
 * [Model]
 * ---------
 * ---------
 * [class-1]
 * --------- ------------  --------- -------------- --------------------
 * --------- ------------  --------- -------------- --------------------
 * attribute association   operation generalization interfaceRealization
 * -------------- ---------
 * -------------- ---------
 * associationEnd parameter
 */
public class ClassTree {
    private final UmlElement classEle; // class element
    private final String classEleId; // class id
    private final HashMap<String, UmlElement> idMap;
    // classTreeMap<id, tree>
    private HashMap<String, ClassTree> classTreeMap;
    // interfaceTreeMap<id, tree>
    private HashMap<String, InterfaceTree> interfaceTreeMap;
    // 父类集合<id, ClassTree>
    private HashMap<String, ClassTree> classFather = new HashMap<>();
    /* map<id, UML> */
    private HashMap<String, UmlAttribute> attributes = new HashMap<>();
    private HashMap<String, AssTree> associations = new HashMap<>();
    private HashMap<String, OperaTree> operations = new HashMap<>();
    private HashMap<String, UmlGeneralization> gens = new HashMap<>();
    private HashMap<String, UmlInterfaceRealization> infRs = new HashMap<>();
    /* 该类的方法操作，统计和计算，注意初始化，以及是否为空判断 */
    // operaCount<type, count>各类型的操作数, 只考虑类自定义的
    private HashMap<OperationQueryType, Integer> operaCount = new HashMap<>();
    private HashMap<AttributeQueryType, Integer> attrCount = new HashMap<>();
    private int assCount = 0; // 关联需要考虑继承父类的关联
    // assList<className> 关联的类名列表
    private ArrayList<String> assList = new ArrayList<>();
    // operaVisMap<operaName, visMapOpera>
    private HashMap<String, HashMap<Visibility, Integer>>
            operaVisMap = new HashMap<>();
    // attr<name, visibility>
    private HashMap<String, Visibility> attrVisMap = new HashMap<>();
    private String topClassName = ""; // topClassName
    // interfaceRealization list<interfaceName>
    private HashMap<String, String> implementMap = new HashMap<>();
    // 类中未隐藏属性
    private ArrayList<AttributeClassInformation> notHiddenAttr
            = new ArrayList<>();

    public ClassTree(UmlElement element, HashMap<String, UmlElement> idMap) {
        this.classEle = element;
        this.classEleId = element.getId();
        this.idMap = idMap;
        create();
    }

    private void create() {
        for (UmlElement it : idMap.values()) {
            if (it.getParentId().equals(classEleId)) { // must is class's son
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
                    case UML_INTERFACE_REALIZATION:
                        infRs.put(it.getId(), (UmlInterfaceRealization) it);
                        break;
                    default:
                        break;
                }
            } // 对方关联自己但未出现在本类下也算关联
            else if (it.getElementType() == UML_ASSOCIATION_END && ((
                    UmlAssociationEnd) it).getReference().equals(classEleId) &&
                    !idMap.get(it.getParentId()).getParentId().
                            equals(classEleId)) {
                //修改flag，代表被关联
                UmlAssociation ass = (UmlAssociation) idMap.
                        get(it.getParentId());
                AssTree assTree = new AssTree(ass, idMap, 1);
                associations.put(it.getParentId(), assTree);
            }
        }
    }

    /* 其他类get本类属性 */
    public HashMap<OperationQueryType, Integer> thisClassOperaCount() {
        return this.operaCount;
    }

    public HashMap<AttributeQueryType, Integer> thisClassAttrCount() {
        return this.attrCount;
    }

    public int thisClassAssCount() {
        return this.assCount;
    }

    public List<String> getAssList() {
        LinkedHashSet<String> it = new LinkedHashSet<>(this.assList);
        return new ArrayList<>(it);
    }

    public Map<String, HashMap<Visibility, Integer>> getOperaVisMap() {
        return this.operaVisMap;
    }

    public HashMap<String, Visibility> getAttrVisMap() {
        return this.attrVisMap;
    }

    public String getTopClass() {
        return this.topClassName;
    }

    public List<String> getInterfaceRList() {
        return new ArrayList<>(this.implementMap.values());
    }

    public List<AttributeClassInformation> getNotHiddenAttr() {
        LinkedHashSet<AttributeClassInformation> it =
                new LinkedHashSet<>(this.notHiddenAttr);
        return new ArrayList<>(it);
    }

    protected void inputPa(HashMap<String, ClassTree> classTMap,
                           HashMap<String, InterfaceTree> inTreeMap) {
        classTreeMap = classTMap;
        interfaceTreeMap = inTreeMap;
    }

    /* 本类实际操作 */
    protected void calculateOperaCount() { // 只考虑类自定义的操作，操作无传入参数时没有parameter元素
        /* initialize */
        int allCount = 0;
        int nonReturn = 0;
        int returnCount = 0;
        int nonParam = 0;
        int paramCount = 0;
        // 如果这个类没有操作，四种可见性也要赋值为零
        for (OperaTree it : operations.values()) { // is class son's operation
            // visMapOpera<Visibility, value>对于每个操作
            String operaName = it.getOperaEle().getName();
            if (operaVisMap.containsKey(operaName)) { //exist,注意同名操作可以不同类型
                HashMap<Visibility, Integer> visMapOpera =
                        operaVisMap.get(operaName); // get此名操作的可见性映射
                if (!visMapOpera.containsKey(it.getOperaVis())) { //同名操作其他可见性
                    visMapOpera.put(it.getOperaVis(), 1);
                } else {
                    Integer value = visMapOpera.get(it.getOperaVis()); // last
                    visMapOpera.put(
                            it.getOperaVis(), value + 1); // update
                }
                operaVisMap.put(operaName, visMapOpera); // update
            } else { // not exist
                HashMap<Visibility, Integer> visMapOpera = new HashMap<>();
                visMapOpera.put(it.getOperaVis(), 1);
                operaVisMap.put(operaName, visMapOpera);
            }
            HashSet<OperationQueryType> types = it.getOperaType();
            allCount++;
            if (types.isEmpty()) {
                nonReturn++;
            } else {
                for (OperationQueryType type : types) {
                    switch (type) {
                        case RETURN: // 有返回值
                            returnCount++;
                            break;
                        case NON_RETURN: // 无返回值
                            nonReturn++;
                            break;
                        case PARAM: // 有传入参数
                            paramCount++;
                            break;
                        case NON_PARAM: // 无传入参数
                            nonParam++;
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        operaCount.put(ALL, allCount);
        operaCount.put(NON_RETURN, nonReturn);
        operaCount.put(RETURN, returnCount);
        operaCount.put(NON_PARAM, nonParam);
        operaCount.put(PARAM, paramCount);
    }

    private int allAttrCount = 0; // 全局变量，用以递归更新

    protected void calculateAttrCount() { // 并求出顶级父类，并保存所有父类
        int sefCount = 0;
        sefCount = attributes.size();  // 类自身定义的属性数量
        for (UmlAttribute attr : attributes.values()) { // 求属性可见性
            attrVisMap.put(attr.getName(), attr.getVisibility());
            if (attr.getVisibility() != Visibility.PRIVATE) { // 获得未隐藏属性
                AttributeClassInformation it = new AttributeClassInformation(
                        attr.getName(), classEle.getName()); // 创建信息
                notHiddenAttr.add(it);
            }
        }
        allAttrCount = sefCount;
        topClassName = classEle.getName(); // set initial father
        // get parent,所有级父类和所有继承的类
        findGeneralization(classEleId); // 递归
        attrCount.put(AttributeQueryType.ALL, allAttrCount);
        attrCount.put(SELF_ONLY, sefCount);
    }

    private void findGeneralization(String genId) {
        HashMap<String, UmlGeneralization> nowGens =
                classTreeMap.get(genId).getGens(); // get target的继承元素表
        if (nowGens.isEmpty()) { // 顶级父类没有继承元素表
            topClassName = idMap.get(genId).getName(); // update top
            return;
        }
        // 继承元素表isn't empty
        for (UmlGeneralization it : nowGens.values()) {
            String newId = it.getTarget();
            classFather.put(newId, classTreeMap.get(newId)); // 存储父类
            findGeneralization(newId);
            allAttrCount += classTreeMap.get(newId).getAttributes().size();
            for (UmlAttribute attr : classTreeMap.get(newId).
                    getAttributes().values()) { // 求属性可见性
                attrVisMap.put(attr.getName(), attr.getVisibility());
                if (attr.getVisibility() != Visibility.PRIVATE) { // 获得未隐藏属性
                    AttributeClassInformation in = new
                            AttributeClassInformation(attr.getName(),
                            idMap.get(newId).getName()); //创建信息
                    notHiddenAttr.add(in);
                }
            }
        }
    }

    protected void calculateAssCount() { // 计算关联数，同时列出类名表，除去关联的接口
        for (AssTree it : associations.values()) {
            this.assCount += it.getAssEnds().size(); // 类本身的关联和自关联
            // loop AssociationEnd array
            for (UmlAssociationEnd end : it.getAssEnds().values()) {
                if (idMap.get(end.getReference()).getElementType()
                        == UML_CLASS) {
                    String name = idMap.get(end.getReference()).getName();
                    this.assList.add(name);
                }
            }
        }
        findAssGenera(classEleId); // 父类的关联
    }

    private void findAssGenera(String genId) { // 父类的关联类，忽略关联的接口
        HashMap<String, UmlGeneralization> nowGens =
                classTreeMap.get(genId).getGens(); // get target的继承元素表
        if (nowGens.isEmpty()) { // 顶级父类没有继承元素表
            return;
        }
        for (UmlGeneralization it : nowGens.values()) {
            String newId = it.getTarget();
            findAssGenera(newId);
            for (AssTree ass : classTreeMap.get(newId).
                    getAssociations().values()) {
                assCount += ass.getAssEnds().size(); // 类本身的关联和自关联
                for (UmlAssociationEnd end : ass.getAssEnds().values()) {
                    if (idMap.get(end.getReference()).getElementType()
                            == UML_CLASS) {
                        String name = idMap.get(end.getReference()).getName();
                        this.assList.add(name);
                    }
                }
            }
        }
    }

    // 一个类可以同时实现多个接口，且包括父类实现的，接口也有父类接口，同名不同id接口也列出名字
    protected void calculateInterfaceR() {
        for (UmlInterfaceRealization it : infRs.values()) { // 本类的接口实现
            InterfaceTree tree = interfaceTreeMap.get(it.getTarget());
            implementMap.put(tree.getInterfaceEle().getId(),
                    tree.getInterfaceEle().getName());
            // 取得本类直接实现的接口后，得到接口的所有父类接口并添加
            findInterfaceRealGenera(tree.getInterfaceEleId());
        }

        /*for (ClassTree it : classFather.values()) { // 父类的接口实现
            interfaceRList.addAll(it.getInterfaceRList());
        }*/
        findRealGenera(classEleId);
    }

    private void findRealGenera(String genId) { // 寻找父类的接口
        HashMap<String, UmlGeneralization> nowGens =
                classTreeMap.get(genId).getGens(); // get target的继承元素表
        if (nowGens.isEmpty()) { // 顶级父类没有继承元素表
            return;
        }
        for (UmlGeneralization it : nowGens.values()) {
            String newId = it.getTarget();
            findRealGenera(newId);
            for (UmlInterfaceRealization in : classTreeMap.get(newId).
                    getInfRs().values()) {
                InterfaceTree tree = interfaceTreeMap.get(in.getTarget());
                implementMap.put(tree.getInterfaceEle().getId(),
                        tree.getInterfaceEle().getName());
                // 取得本类直接实现的接口后，得到接口的所有父类接口并添加
                findInterfaceRealGenera(tree.getInterfaceEleId());
            }
        }
    }

    private void findInterfaceRealGenera(String genId) { // 寻找接口的父接口
        HashMap<String, UmlGeneralization> nowGens =
                interfaceTreeMap.get(genId).getGens(); // get target的继承元素表
        if (nowGens.isEmpty()) { // 顶级父类没有继承元素表
            return;
        }
        for (UmlGeneralization it : nowGens.values()) {
            String newId = it.getTarget();
            findInterfaceRealGenera(newId);
            String name = idMap.get(newId).getName();
            implementMap.put(newId, name);
        }
    }

    protected HashMap<String, UmlAttribute> getAttributes() {
        return this.attributes;
    }

    protected HashMap<String, AssTree> getAssociations() {
        return this.associations;
    }

    protected HashMap<String, ClassTree> getClassFather() {
        return this.classFather;
    }

    protected HashMap<String, UmlGeneralization> getGens() {
        return this.gens;
    }

    protected HashMap<String, UmlInterfaceRealization> getInfRs() {
        return this.infRs;
    }

    protected String getClassEleName() {
        return this.classEle.getName();
    }
}
