package implement;

import com.oocourse.uml1.models.elements.UmlAssociationEnd;
import com.oocourse.uml1.models.elements.UmlElement;

import java.util.ArrayList;
import java.util.HashMap;

import static com.oocourse.uml1.models.common.ElementType.UML_ASSOCIATION_END;

/**
 * 关联树，仅保存关联对端，而忽略类本身
 * 功能同ClassTree类
 */
public class AssTree {
    private final UmlElement assEle; // association element
    private final String assEleId; // association id
    private final int flag; // 标记当前ass是本类关联，还是被其他类关联
    private final HashMap<String, UmlElement> idMap;
    private HashMap<String, UmlAssociationEnd> assEnds = new HashMap<>();

    public AssTree(UmlElement element, HashMap<String, UmlElement> idMap,
                   int flag) {
        this.assEle = element;
        this.idMap = idMap;
        this.assEleId = assEle.getId();
        this.flag = flag;
        create();
    }

    private void create() { // 注意出现自关联，一个ass里面只有两个assEnd
        String classId = assEle.getParentId();
        for (UmlElement it : idMap.values()) {
            if (it.getElementType() == UML_ASSOCIATION_END &&
                    it.getParentId().equals(assEleId)) {
                assEnds.put(it.getId(), (UmlAssociationEnd) it);
            }
        }
        ArrayList<UmlAssociationEnd> it = new ArrayList<>(assEnds.values());
        // 自关联两个都算
        if (!(it.get(0).getReference().equals(classId)
                && it.get(1).getReference().equals(classId))) {
            if (flag == 1) {
                if (it.get(0).getReference().equals(classId)) {
                    assEnds.remove(it.get(1).getId());
                } else {
                    assEnds.remove(it.get(0).getId());
                }
            } else {
                if (it.get(0).getReference().equals(classId)) {
                    assEnds.remove(it.get(0).getId());
                } else {
                    assEnds.remove(it.get(1).getId());
                }
            }
        }
    }

    protected HashMap<String, UmlAssociationEnd> getAssEnds() {
        return this.assEnds;
    }
}
