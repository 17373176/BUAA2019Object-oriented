package implement;

import com.oocourse.uml1.interact.common.AttributeClassInformation;
import com.oocourse.uml1.interact.common.AttributeQueryType;
import com.oocourse.uml1.interact.common.OperationQueryType;
import com.oocourse.uml1.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.interact.format.UmlInteraction;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.oocourse.uml1.models.common.ElementType.UML_CLASS;
import static com.oocourse.uml1.models.common.ElementType.UML_INTERFACE;

/**
 * UML交互接口的实现类
 * 通过className建立树结构，每个class对自己类进行相应的计算
 * 通过className映射类元素
 * 通过元素自身id映射元素(id不会重复的原则)
 */
public class MyUmlInter implements UmlInteraction {
    private final UmlElement[] elements; // elements array
    private int classCount = 0; // 获取类数量，可同名

    /* 各元素树结构及映射 */
    // ClassTree<id, ClassTree>
    private HashMap<String, ClassTree> classTreeMap = new HashMap<>();
    // classIdMap<id, element>
    private HashMap<String, UmlElement> classIdMap = new HashMap<>();
    // classMap<className, element>
    private HashMap<String, UmlElement> classMap = new HashMap<>();
    // idMap<id, element>
    private HashMap<String, UmlElement> idMap = new HashMap<>();
    // InterfaceTree<id, InterfaceTree>
    private HashMap<String, InterfaceTree> interfaceTreeMap = new HashMap<>();
    // interfaceMap<id, element>
    private HashMap<String, UmlElement> interfaceMap = new HashMap<>();

    /* define UmlElement, 每行为每个element*/
    public MyUmlInter(UmlElement... elements) {
        this.elements = elements;
        create();
    }

    /* 建立数据结构 */
    private void create() {
        /* map and create graph */
        for (UmlElement it : elements) {
            idMap.put(it.getId(), it); // store rely on id
        }
        for (UmlElement it : elements) {
            if (it.getElementType() == UML_CLASS) { // class element
                classIdMap.put(it.getId(), it);
                classMap.put(it.getName(), it);
                // create class tree
                ClassTree classT = new ClassTree(it, idMap);
                classTreeMap.put(it.getId(), classT); // map tree
            }
            if (it.getElementType() == UML_INTERFACE) { // interface element
                interfaceMap.put(it.getId(), it);
                InterfaceTree ifT = new InterfaceTree(it, idMap); // create
                interfaceTreeMap.put(it.getId(), ifT); // map
            }

        }
        storeClassCount(); // calculate
        calculate();
    }

    private void calculate() {
        for (InterfaceTree it : interfaceTreeMap.values()) {
            it.calculateFather(interfaceTreeMap);
        }
        for (ClassTree it : classTreeMap.values()) {
            it.inputPa(classTreeMap, interfaceTreeMap);
            it.calculateOperaCount(); // 计算所有类型操作数，只考虑类自定义的，并统计操作可见性
            // 计算所有类型属性数，类自定义+所有父类，求出顶级父类，保存所有父类，统计可见性，求隐藏信息
            it.calculateAttrCount();
            it.calculateAssCount(); // 计算关联类数，考虑本类+父类+自关联，并列出关联类的类名列表
            // 实现的接口，包括父类和接口继承间接实现的，对于同名接口，需要在列表中返回对应数量个接口名
            it.calculateInterfaceR();
        }
    }

    public int getClassCount() {
        return classCount;
    }

    private void storeClassCount() {
        classCount = classIdMap.size();
    }

    /* 判断类名是否重复 */
    private boolean classIsDuplicated(String className) {
        int count = 0;
        for (UmlElement it : classIdMap.values()) {
            if (it.getName().equals(className)) {
                count++;
            }
        }
        return count == 1; // only count = 1 is true
    }

    /* 获取类中指定类型的操作(本类自己定义的)数量, element parent_id需要是该类的操作的id */
    public int getClassOperationCount(String className,
                                      OperationQueryType queryType) throws
            ClassNotFoundException, ClassDuplicatedException {
        if (!classMap.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (!classIsDuplicated(className)) {
            throw new ClassDuplicatedException(className);
        } else {
            String classId = classMap.get(className).getId(); // get class id
            ClassTree classTree = classTreeMap.get(classId);
            return classTree.thisClassOperaCount().get(queryType);
        }
    }

    /* 获取类属性数量,不考虑接口实现 */
    public int getClassAttributeCount(String className,
                                      AttributeQueryType queryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classMap.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (!classIsDuplicated(className)) {
            throw new ClassDuplicatedException(className);
        } else {
            String classId = classMap.get(className).getId(); // get class id
            ClassTree classTree = classTreeMap.get(classId);
            return classTree.thisClassAttrCount().get(queryType);
        }
    }

    /* 获取类关联数量,统计该类和父类的关联，关联的接口也算关联 */
    public int getClassAssociationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classMap.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (!classIsDuplicated(className)) {
            throw new ClassDuplicatedException(className);
        } else {
            String classId = classMap.get(className).getId(); // get class id
            ClassTree classTree = classTreeMap.get(classId);
            return classTree.thisClassAssCount();
        }
    }

    /* 获取与类相关联的类列表，同时考虑父类的关联，List以类名存储 */
    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classMap.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (!classIsDuplicated(className)) {
            throw new ClassDuplicatedException(className);
        } else {
            String classId = classMap.get(className).getId(); // get class id
            ClassTree classTree = classTreeMap.get(classId);
            return classTree.getAssList();
        }
    }

    /* 统计类操作可见性，只考虑本类自定义的操作，但需要统计同名不同可见性或者不同参数的操作 */
    public Map<Visibility, Integer> getClassOperationVisibility(
            String className, String operationName)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classMap.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (!classIsDuplicated(className)) {
            throw new ClassDuplicatedException(className);
        } else {
            String classId = classMap.get(className).getId(); // get class id
            ClassTree classTree = classTreeMap.get(classId);
            if (classTree.getOperaVisMap().containsKey(operationName)) { //操作存在
                return classTree.getOperaVisMap().get(operationName);
            } else { //不存在返回四中可见性为0
                HashMap<Visibility, Integer> visMap = new HashMap<>();
                visMap.put(Visibility.PUBLIC, 0);
                visMap.put(Visibility.PROTECTED, 0);
                visMap.put(Visibility.PACKAGE, 0);
                visMap.put(Visibility.PRIVATE, 0);
                return visMap;
            }
        }
    }

    /* 获取类属性可见性，需要考虑继承的属性，如果父类和本类属性重名则抛出异常 */
    public Visibility getClassAttributeVisibility(String className,
                                                  String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        if (!classMap.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (!classIsDuplicated(className)) {
            throw new ClassDuplicatedException(className);
        } else {
            String classId = classMap.get(className).getId(); // get class id
            ClassTree classTree = classTreeMap.get(classId);
            int flag = attributeException(classTree, attributeName);
            if (flag == 1) { // not found
                throw new AttributeNotFoundException(className, attributeName);
            } else if (flag == 2) { // duplicated
                throw new AttributeDuplicatedException(
                        className, attributeName);
            } else { // success
                return classTree.getAttrVisMap().get(attributeName);
            }
        }
    }

    private int attributeException(ClassTree classTree, String attributeName) {
        int count = 0;
        for (UmlElement it : classTree.getAttributes().values()) {
            if (it.getName().equals(attributeName)) {
                count++;
            }
        }
        for (ClassTree it : classTree.getClassFather().values()) { // 父类与本类是否同名
            for (UmlElement attr : it.getAttributes().values()) {
                if (attr.getName().equals(attributeName)) {
                    count++;
                }
            }
        }
        if (count == 1) {
            return 0; // success
        } else if (count == 0) {
            return 1; // not found
        }
        return 2; // duplicated
    }

    /* 获取顶级父类，返回顶级父类名 */
    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classMap.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (!classIsDuplicated(className)) {
            throw new ClassDuplicatedException(className);
        } else {
            String classId = classMap.get(className).getId(); // get class id
            ClassTree classTree = classTreeMap.get(classId);
            return classTree.getTopClass();
        }
    }

    /* 获取类实现的接口列表 @return 实现的接口列表，包括通过父类或者接口继承等方式间接实现 */
    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classMap.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (!classIsDuplicated(className)) {
            throw new ClassDuplicatedException(className);
        } else {
            String classId = classMap.get(className).getId(); // get class id
            ClassTree classTree = classTreeMap.get(classId);
            return classTree.getInterfaceRList();
        }
    }

    /* 获取类中未隐藏的属性，需要考虑继承自父类的非隐藏属性，子类可以定义与父类同名的属性但不属于同一个 */
    public List<AttributeClassInformation> getInformationNotHidden(
            String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classMap.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (!classIsDuplicated(className)) {
            throw new ClassDuplicatedException(className);
        } else {
            String classId = classMap.get(className).getId(); // get class id
            ClassTree classTree = classTreeMap.get(classId);
            return classTree.getNotHiddenAttr();
        }
    }
}

