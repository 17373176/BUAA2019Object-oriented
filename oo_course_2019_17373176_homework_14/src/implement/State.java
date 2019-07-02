package implement;

import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.HashMap;

import static com.oocourse.uml2.models.common.ElementType.UML_TRANSITION;

public class State {
    private final UmlElement stateEle; // element
    private final String stateEleId; // id
    private final HashMap<String, UmlElement> idMap;

    private int myTransNum = 0;
    /* <id, uml> */
    private HashMap<String, UmlElement> transitions = new HashMap<>();

    public State(UmlElement ele, HashMap<String, UmlElement> idMap) {
        this.stateEle = ele;
        this.idMap = idMap;
        this.stateEleId = stateEle.getId();
        create();
    }

    private void create() {
        for (UmlElement it : idMap.values()) { // must is transition's son
            if (it.getElementType() == UML_TRANSITION &&
                    ((UmlTransition) it).getSource().equals(stateEleId)) {
                transitions.put(it.getId(), it); // source才是判断条件
            }
        }
        myTransNum = transitions.size();
    }

    protected int getMyTransNum() {
        return this.myTransNum;
    }

    protected HashMap<String, UmlElement> getTransitions() {
        return this.transitions;
    }

    protected String getStateName() {
        return this.stateEle.getName();
    }
}
