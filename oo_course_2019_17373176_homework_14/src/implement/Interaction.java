package implement;

import com.oocourse.uml2.models.elements.UmlElement;

import java.util.HashMap;

import static com.oocourse.uml2.models.common.ElementType.UML_LIFELINE;
import static com.oocourse.uml2.models.common.ElementType.UML_MESSAGE;

public class Interaction {
    private final UmlElement interactionEle; // element
    private final String interactionEleId; // id
    private final HashMap<String, UmlElement> idMap;

    /* <id, uml> */
    private HashMap<String, UmlElement> messageMap = new HashMap<>();
    private HashMap<String, UmlElement> lifelineMap = new HashMap<>();
    private HashMap<String, Lifeline> lifelineTree = new HashMap<>();
    /* inCome<name, int> */
    private HashMap<String, Integer> inCome = new HashMap<>();

    public Interaction(UmlElement ele, HashMap<String, UmlElement> idMap) {
        this.interactionEle = ele;
        this.idMap = idMap;
        this.interactionEleId = interactionEle.getId();
        create();
    }

    private void create() {
        for (UmlElement it : idMap.values()) { // must is interaction's son
            if (it.getParentId().equals(interactionEleId)) {
                if (it.getElementType() == UML_LIFELINE) {
                    lifelineMap.put(it.getId(), it);
                    Lifeline lifelineT = new Lifeline(it, idMap);
                    lifelineTree.put(it.getId(), lifelineT);
                } else if (it.getElementType() == UML_MESSAGE) {
                    messageMap.put(it.getId(), it);
                }
            }
        }
        for (Lifeline it : lifelineTree.values()) {
            inCome.put(it.getLifelineName(), it.getIncomeSize());
        }
    }

    protected int getLifelineSize() {
        return this.lifelineMap.size();
    }

    protected HashMap<String, UmlElement> getLifeline() {
        return this.lifelineMap;
    }

    protected int getMessageMapSize() {
        return this.messageMap.size();
    }

    protected HashMap<String, Integer> getInCome() {
        return this.inCome;
    }
}
