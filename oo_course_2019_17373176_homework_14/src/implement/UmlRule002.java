package implement;

import com.oocourse.uml2.interact.common.AttributeClassInformation;
import com.oocourse.uml2.models.elements.UmlAssociationEnd;
import com.oocourse.uml2.models.elements.UmlAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class UmlRule002 {
    private HashSet<AttributeClassInformation> attrSet = new HashSet<>();
    private final HashMap<String, ClassTree> classTreeMap;

    public UmlRule002(HashMap<String, ClassTree> classTreeMap) {
        this.classTreeMap = classTreeMap;
    }

    protected boolean check() {
        boolean flag = true;
        for (ClassTree classT : classTreeMap.values()) {
            ArrayList<UmlAttribute> attr = new ArrayList<>(
                    classT.getAttributes().values());
            for (int i = 0; i < attr.size(); i++) {
                for (int j = i + 1; j < attr.size(); j++) { //类自身属性重名
                    if (attr.get(i).getName().equals(attr.get(j).getName())) {
                        flag = false;
                        AttributeClassInformation information = new
                                AttributeClassInformation(attr.get(i).getName()
                                , classT.getClassEle().getName());
                        attrSet.add(information);
                    }
                }
                //类关联对端End名称
                for (AssTree ass : classT.getAssociations().values()) {
                    for (UmlAssociationEnd end : ass.getAssEnds().values()) {
                        if (end.getName() == null) {
                            continue;
                        }
                        if (attr.get(i).getName().equals(end.getName())) {
                            flag = false;
                            AttributeClassInformation information = new
                                    AttributeClassInformation(attr.get(i).
                                    getName(), classT.getClassEle().getName());
                            attrSet.add(information);
                        }
                    }
                }
            }
        }
        return flag;
    }

    protected HashSet<AttributeClassInformation> getAttrSet() {
        return this.attrSet;
    }
}
