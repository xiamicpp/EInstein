package org.einstein.codegen.api;


/**
 * root templete interface
 * Created by kevin on 2018/5/6.
 */
public interface ITemplete {

    EType getType();

    enum EType {
        UNKNOWN, ENTITY, MAP, LIST, INTERFACE;
    }
}
