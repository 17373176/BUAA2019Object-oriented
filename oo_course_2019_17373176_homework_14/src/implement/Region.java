package implement;

import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.HashMap;
import java.util.HashSet;

import static com.oocourse.uml2.models.common.ElementType.UML_FINAL_STATE;
import static com.oocourse.uml2.models.common.ElementType.UML_PSEUDOSTATE;
import static com.oocourse.uml2.models.common.ElementType.UML_STATE;

public class Region {
    private final UmlElement regionEle; // element
    private final String regionEleId; // id
    private final HashMap<String, UmlElement> idMap;

    /* 功能 */
    private int stateNum = 0; // state number
    private int tranNum = 0; // transition number
    /* states<id, State> */
    private HashMap<String, State> stateTree = new HashMap<>();
    private HashMap<String, UmlElement> states = new HashMap<>();
    /* states<id, Uml> */
    private HashMap<String, UmlElement> stateFinal = new HashMap<>();
    private HashMap<String, UmlElement> statePre = new HashMap<>();
    /* <name, int> */
    private HashMap<String, Integer> nextStateNum = new HashMap<>();
    /* next<State, set<State>> */
    private HashMap<State, HashSet<State>> next = new HashMap<>();

    public Region(UmlElement ele, HashMap<String, UmlElement> idMap) {
        this.regionEle = ele;
        this.idMap = idMap;
        this.regionEleId = regionEle.getId();
        create();
    }

    private void create() {
        for (UmlElement it : idMap.values()) { // must is region's son
            if (it.getParentId().equals(regionEleId)) {
                if (it.getElementType() == UML_STATE ||
                        it.getElementType() == UML_FINAL_STATE ||
                        it.getElementType() == UML_PSEUDOSTATE) {
                    State state = new State(it, idMap);
                    stateTree.put(it.getId(), state); // 状态树
                    states.put(it.getId(), it); // 状态数组
                }
            }
        }
        calculate();
    }

    private void calculate() { // 求状态数，迁移数，后继状态集合
        int finalNum = 0;
        int preNum = 0;
        if (!statePre.isEmpty()) {
            preNum = 1;
        }
        if (!stateFinal.isEmpty()) {
            finalNum = 1;
        }
        stateNum = states.size() + preNum + finalNum;
        for (State state : stateTree.values()) {
            tranNum += state.getMyTransNum(); // 迁移数
        }
        // 递归求可达状态
        for (State state : stateTree.values()) {
            next.put(state, new HashSet<>());
            for (UmlElement it : state.getTransitions().values()) {
                String targetId = ((UmlTransition) it).getTarget();
                State newState = stateTree.get(targetId);
                HashSet<State> set = next.get(state);
                set.add(newState);
                next.put(state, set);
                findNext(targetId, state);
            }
            nextStateNum.put(state.getStateName(),
                    next.get(state).size());// 统计后继状态
        }
    }

    private void findNext(String targetId, State root) {
        HashMap<String, UmlElement> nowTrans =
                stateTree.get(targetId).getTransitions();
        if (nowTrans.isEmpty()) { // 末端没有迁移
            return;
        }
        HashSet<State> set = next.get(root);
        for (UmlElement it : nowTrans.values()) {
            String newId = ((UmlTransition) it).getTarget();
            if (set.contains(stateTree.get(newId))) {
                continue;
            }
            State newState = stateTree.get(newId);
            set.add(newState);
            next.put(root, set);
            findNext(newId, root);
        }
    }

    protected int getStateNum() {
        return this.stateNum;
    }

    protected int getStateTansNum() {
        return this.tranNum;
    }

    protected HashMap<String, UmlElement> getStates() {
        return this.states;
    }

    protected HashMap<String, Integer> getNextState() {
        return this.nextStateNum;
    }
}
