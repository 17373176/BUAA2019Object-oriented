package implement;

import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlMessage;

import java.util.HashMap;

import static com.oocourse.uml2.models.common.ElementType.UML_MESSAGE;

public class Lifeline {
    private final UmlElement lifelineEle; // element
    private final String lifelineEleId; // id
    private final HashMap<String, UmlElement> idMap;

    /* inCome<id, uml> */
    private HashMap<String, UmlElement> inCome = new HashMap<>();

    public Lifeline(UmlElement ele, HashMap<String, UmlElement> idMap) {
        this.lifelineEle = ele;
        this.idMap = idMap;
        this.lifelineEleId = lifelineEle.getId();
        create();
    }

    private void create() {
        for (UmlElement it : idMap.values()) { // lifeline & message 同层次
            if (it.getParentId().equals(lifelineEle.getParentId()) &&
                    it.getElementType() == UML_MESSAGE && ((UmlMessage) it).
                    getTarget().equals(lifelineEleId)) {
                inCome.put(it.getId(), it);
            }
        }
    }

    protected int getIncomeSize() {
        return this.inCome.size();
    }

    protected String getLifelineName() {
        return this.lifelineEle.getName();
    }
}
