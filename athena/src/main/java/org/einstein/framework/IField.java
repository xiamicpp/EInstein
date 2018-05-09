package org.einstein.framework;

/**
 * field interface
 *
 * @author kevin
 */

public interface IField {
    /**
     * field type
     *
     * @return
     */
    String getType();

    /**
     * field name
     *
     * @return
     */
    String getName();

    FTYPE getFieldType();

    int getTempleteID();

    enum FTYPE {
        UNKNOWN, COMMON, LIST, MAP, ENTITY;
    }
}
