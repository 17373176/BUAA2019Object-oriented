package implement;

import com.oocourse.uml2.models.elements.UmlElement;

import java.util.HashMap;

import static com.oocourse.uml2.models.common.ElementType.UML_REGION;

public class MachineTree {
    private final UmlElement machineEle; // element
    private final String machineEleId; // id
    private final HashMap<String, UmlElement> idMap;
    private Region region;

    public MachineTree(UmlElement ele, HashMap<String, UmlElement> idMap) {
        this.machineEle = ele;
        this.idMap = idMap;
        this.machineEleId = machineEle.getId();
        create();
    }

    private void create() {
        for (UmlElement it : idMap.values()) { // must is machine's son
            if (it.getParentId().equals(machineEleId) &&
                    it.getElementType() == UML_REGION) {
                this.region = new Region(it, idMap);
            }
        }
    }

    protected int getStateNum() {
        return region.getStateNum();
    }

    protected int getStateTranNum() {
        return region.getStateTansNum();
    }

    protected HashMap<String, UmlElement> getStates() {
        return region.getStates();
    }

    protected HashMap<String, Integer> getStateNext() {
        return region.getNextState();
    }
}
