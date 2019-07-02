package implement;

import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.interact.common.AttributeQueryType;
import com.oocourse.uml2.interact.common.OperationQueryType;
import com.oocourse.uml2.interact.exceptions.user.AttributeDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.AttributeNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml2.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml2.interact.format.UmlGeneralInteraction;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.oocourse.uml2.models.common.ElementType.UML_CLASS;
import static com.oocourse.uml2.models.common.ElementType.UML_INTERACTION;
import static com.oocourse.uml2.models.common.ElementType.UML_INTERFACE;
import static com.oocourse.uml2.models.common.ElementType.UML_STATE;
import static com.oocourse.uml2.models.common.ElementType.UML_STATE_MACHINE;

/** UML交互接口的实现类
 * 通过className建立树结构，每个class对自己类进行相应的计算,通过className映射类元素
 * 通过元素自身id映射元素(id不会重复的原则) **/
public class MyUmlGeneralInteraction implements UmlGeneralInteraction {
    private final UmlElement[] elements; // elements array

    /* 类图交互接口 */
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
    public MyUmlGeneralInteraction(UmlElement... elements) {
        this.elements = elements;
        create();
    }

    /* 建立数据结构，三种UML图可一起创建，并计算 */
    private void create() { /* map and create graph */
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
            } else if (it.getElementType() == UML_INTERFACE) { // interface
                interfaceMap.put(it.getId(), it);
                InterfaceTree ifT = new InterfaceTree(it, idMap); // create
                interfaceTreeMap.put(it.getId(), ifT); // map
            } else if (it.getElementType() == UML_STATE_MACHINE) { // Machine
                machineIdMap.put(it.getId(), it);
                MachineTree machineTree = new MachineTree(it, idMap);
                machineTreeMap.put(it.getId(), machineTree);
                machineMap.put(it.getName(), it);
            } else if (it.getElementType() == UML_INTERACTION) { // Interaction
                interactionIdMap.put(it.getId(), it);
                Interaction interactionT = new Interaction(it, idMap);
                interactionTree.put(it.getId(), interactionT);
                interactionMap.put(it.getName(), it);
            }
        }
    }

    private void calculateClass() {
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

    /* 判断名称是否重复，三种图均可用 */
    private boolean isDuplicated(String name, HashMap<String, UmlElement> map) {
        int count = 0;
        for (UmlElement it : map.values()) {
            if (it.getName().equals(name)) {
                count++;
            }
        }
        return count == 1; // only count = 1 is true
    }

    public int getClassOperationCount(String className,
                                      OperationQueryType queryType) throws
            ClassNotFoundException, ClassDuplicatedException {
        if (!classMap.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (!isDuplicated(className, classIdMap)) {
            throw new ClassDuplicatedException(className);
        } else {
            String classId = classMap.get(className).getId(); // get class id
            ClassTree classTree = classTreeMap.get(classId);
            return classTree.thisClassOperaCount().get(queryType);
        }
    }

    public int getClassAttributeCount(String className,
                                      AttributeQueryType queryType)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classMap.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (!isDuplicated(className, classIdMap)) {
            throw new ClassDuplicatedException(className);
        } else {
            String classId = classMap.get(className).getId(); // get class id
            ClassTree classTree = classTreeMap.get(classId);
            return classTree.thisClassAttrCount().get(queryType);
        }
    }

    public int getClassAssociationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classMap.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (!isDuplicated(className, classIdMap)) {
            throw new ClassDuplicatedException(className);
        } else {
            String classId = classMap.get(className).getId(); // get class id
            ClassTree classTree = classTreeMap.get(classId);
            return classTree.thisClassAssCount();
        }
    }

    public List<String> getClassAssociatedClassList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classMap.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (!isDuplicated(className, classIdMap)) {
            throw new ClassDuplicatedException(className);
        } else {
            String classId = classMap.get(className).getId(); // get class id
            ClassTree classTree = classTreeMap.get(classId);
            return classTree.getAssList();
        }
    }

    public Map<Visibility, Integer> getClassOperationVisibility(
            String className, String operationName)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classMap.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (!isDuplicated(className, classIdMap)) {
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

    public Visibility getClassAttributeVisibility(String className,
                                                  String attributeName)
            throws ClassNotFoundException, ClassDuplicatedException,
            AttributeNotFoundException, AttributeDuplicatedException {
        if (!classMap.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (!isDuplicated(className, classIdMap)) {
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
        return duplicated(count);
    }

    private int duplicated(int count) { // 判断元素是否重复，三种图均可用
        if (count == 1) {
            return 0; // success
        } else if (count == 0) {
            return 1; // not found
        }
        return 2; // duplicated
    }

    public String getTopParentClass(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classMap.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (!isDuplicated(className, classIdMap)) {
            throw new ClassDuplicatedException(className);
        } else {
            String classId = classMap.get(className).getId(); // get class id
            ClassTree classTree = classTreeMap.get(classId);
            return classTree.getTopClass();
        }
    }

    public List<String> getImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classMap.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (!isDuplicated(className, classIdMap)) {
            throw new ClassDuplicatedException(className);
        } else {
            String classId = classMap.get(className).getId(); // get class id
            ClassTree classTree = classTreeMap.get(classId);
            return classTree.getInterfaceRList();
        }
    }

    public List<AttributeClassInformation> getInformationNotHidden(
            String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classMap.containsKey(className)) {
            throw new ClassNotFoundException(className);
        } else if (!isDuplicated(className, classIdMap)) {
            throw new ClassDuplicatedException(className);
        } else {
            String classId = classMap.get(className).getId(); // get class id
            ClassTree classTree = classTreeMap.get(classId);
            return classTree.getNotHiddenAttr();
        }
    }

    /* UML状态图交互 */
    // MachineTreeMap<id, MachineTree> 状态机树映射
    private HashMap<String, MachineTree> machineTreeMap = new HashMap<>();
    // machineIdMap<id, element>
    private HashMap<String, UmlElement> machineIdMap = new HashMap<>();
    // machineMap<Name, element> 状态机名称映射
    private HashMap<String, UmlElement> machineMap = new HashMap<>();

    /* 获取状态机的状态数 */
    public int getStateCount(String stateMachineName)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException {
        if (!machineMap.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        } else if (!isDuplicated(stateMachineName, machineIdMap)) {
            throw new StateMachineDuplicatedException(stateMachineName);
        } else {
            String machineId = machineMap.get(stateMachineName).getId();
            return machineTreeMap.get(machineId).getStateNum();
        }
    }

    /* 获取状态机转移数 */
    public int getTransitionCount(String stateMachineName)
            throws StateMachineNotFoundException,
            StateMachineDuplicatedException {
        if (!machineMap.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        } else if (!isDuplicated(stateMachineName, machineIdMap)) {
            throw new StateMachineDuplicatedException(stateMachineName);
        } else {
            String machineId = machineMap.get(stateMachineName).getId();
            return machineTreeMap.get(machineId).getStateTranNum();
        }
    }

    /* 获取后继状态数 */
    public int getSubsequentStateCount(String stateMachineName, String
            stateName) throws StateMachineNotFoundException,
            StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        if (!machineMap.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        } else if (!isDuplicated(stateMachineName, machineIdMap)) {
            throw new StateMachineDuplicatedException(stateMachineName);
        } else {
            String machineId = machineMap.get(stateMachineName).getId();
            MachineTree machineTree = machineTreeMap.get(machineId);
            int flag = stateException(machineTree, stateName);
            if (flag == 1) { // not found
                throw new StateNotFoundException(stateMachineName, stateName);
            } else if (flag == 2) { // duplicated
                throw new StateDuplicatedException(
                        stateMachineName, stateName);
            } else { // success
                return machineTree.getStateNext().get(stateName);
            }
        }
    }

    private int stateException(MachineTree machineT, String name) {
        int count = 0;
        for (UmlElement it : machineT.getStates().values()) {
            if (it.getElementType() == UML_STATE && it.getName().equals(name)) {
                count++;
            }
        }
        return duplicated(count);
    }

    /* UML顺序图交互 */
    // InteractionTree<id, MachineTree> 树映射
    private HashMap<String, Interaction> interactionTree = new HashMap<>();
    // interactionIdMap<id, element>
    private HashMap<String, UmlElement> interactionIdMap = new HashMap<>();
    // interactionMap<Name, element> 名称映射
    private HashMap<String, UmlElement> interactionMap = new HashMap<>();

    /* 获取参与对象数 */
    public int getParticipantCount(String interactionName) throws
            InteractionNotFoundException, InteractionDuplicatedException {
        if (!interactionMap.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        } else if (!isDuplicated(interactionName, interactionIdMap)) {
            throw new InteractionDuplicatedException(interactionName);
        } else {
            String interactionId = interactionMap.get(interactionName).getId();
            return interactionTree.get(interactionId).getLifelineSize();
        }
    }

    /* 获取消息数 */
    public int getMessageCount(String interactionName) throws
            InteractionNotFoundException, InteractionDuplicatedException {
        if (!interactionMap.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        } else if (!isDuplicated(interactionName, interactionIdMap)) {
            throw new InteractionDuplicatedException(interactionName);
        } else {
            String interactionId = interactionMap.get(interactionName).getId();
            return interactionTree.get(interactionId).getMessageMapSize();
        }
    }

    /* 获取对象的进入消息数 */
    public int getIncomingMessageCount(String interactionName, String
            lifelineName) throws InteractionNotFoundException,
            InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        if (!interactionMap.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        } else if (!isDuplicated(interactionName, interactionIdMap)) {
            throw new InteractionDuplicatedException(interactionName);
        } else {
            String interactionId = interactionMap.get(interactionName).getId();
            Interaction interactionT = interactionTree.get(interactionId);
            int flag = lifelineException(interactionT, lifelineName);
            if (flag == 1) { // not found
                throw new LifelineNotFoundException(interactionName,
                        lifelineName);
            } else if (flag == 2) { // duplicated
                throw new LifelineDuplicatedException(
                        interactionName, lifelineName);
            } else { // success
                return interactionTree.get(interactionId).
                        getInCome().get(lifelineName);
            }
        }
    }

    private int lifelineException(Interaction interactionT, String
            lifelineName) {
        int count = 0;
        for (UmlElement it : interactionT.getLifeline().values()) {
            if (it.getName().equals(lifelineName)) {
                count++;
            }
        }
        return duplicated(count);
    }

    public void checkForUml002() throws UmlRule002Exception {
        UmlRule002 rule = new UmlRule002(classTreeMap);
        if (!rule.check()) {
            throw new UmlRule002Exception(rule.getAttrSet());
        }
    }

    public void checkForUml008() throws UmlRule008Exception {
        UmlRule008 rule = new UmlRule008(classTreeMap, interfaceTreeMap, idMap);
        if (!rule.check()) {
            throw new UmlRule008Exception(rule.getGens());
        }
    }

    public void checkForUml009() throws UmlRule009Exception {
        UmlRule009 rule = new UmlRule009(classTreeMap, interfaceTreeMap, idMap);
        if (!rule.check()) {
            throw new UmlRule009Exception(rule.getGens());
        }
        storeClassCount(); // calculate class number
        calculateClass();
    }
}
