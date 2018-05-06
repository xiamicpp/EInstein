package org.einstein.framework;

import java.util.List;

/**
 * root templete interface
 * Created by kevin on 2018/5/6.
 */
public interface ITemplete {
    String getTempleteName();
    String getEprotoName();
    int getClassEntityID();
    List<IField> getFields();
    EType getType();

    enum EType{
        UNKNOWN,ENTITY,MAP,LIST;
    }
}
