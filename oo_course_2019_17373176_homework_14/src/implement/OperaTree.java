package implement;

import com.oocourse.uml2.interact.common.OperationQueryType;
import com.oocourse.uml2.models.common.Direction;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlOperation;
import com.oocourse.uml2.models.elements.UmlParameter;

import java.util.HashMap;
import java.util.HashSet;

import static com.oocourse.uml2.models.common.ElementType.UML_PARAMETER;

/**
 * 操作树
 * 功能同ClassTree类
 */
public class OperaTree {
    private final UmlElement operaEle; // association element
    private final String operaEleId; // operation id
    private final HashMap<String, UmlElement> idMap;
    private final Visibility operaVis; // 可见性
    // operation types
    private HashSet<OperationQueryType> operaType = new HashSet<>();
    // parameters<id, Parameter>
    private HashMap<String, UmlParameter> params = new HashMap<>();

    public OperaTree(UmlElement element, HashMap<String, UmlElement> idMap) {
        this.operaEle = element;
        this.idMap = idMap;
        this.operaVis = ((UmlOperation) operaEle).getVisibility();
        this.operaEleId = operaEle.getId();
        create();
    }

    private void create() {
        for (UmlElement it : idMap.values()) {
            if (it.getElementType() == UML_PARAMETER &&
                    it.getParentId().equals(operaEleId)) {
                params.put(it.getId(), (UmlParameter) it);
            }
        }
        isOperationType(); // 求操作的类型，以及操作可见性
    }

    protected HashSet<OperationQueryType> getOperaType() {
        return this.operaType;
    }

    protected Visibility getOperaVis() {
        return this.operaVis;
    }

    protected UmlElement getOperaEle() {
        return this.operaEle;
    }

    /* 求该操作的类型，需要对所有param判断 */
    private void isOperationType() {
        boolean returnFlag = false;
        boolean paramFlag = false;
        for (UmlParameter it : params.values()) { // 获得param成员
            if (it.getDirection() == Direction.RETURN) {
                returnFlag = true;
                operaType.add(OperationQueryType.RETURN);
            } else if (it.getDirection() == Direction.IN || it.getDirection()
                    == Direction.OUT || it.getDirection() == Direction.INOUT) {
                paramFlag = true;
                operaType.add(OperationQueryType.PARAM);
            }
        }
        if (!returnFlag) {
            operaType.add(OperationQueryType.NON_RETURN);
        }
        if (!paramFlag) {
            operaType.add(OperationQueryType.NON_PARAM);
        }
    }
}
