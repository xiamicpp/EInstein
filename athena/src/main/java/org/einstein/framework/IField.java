package org.einstein.framework;

/**
 * field interface
 *
 * @author kevin
 */

public interface IField {

    String getAnnotation(); //field annotation
    String getComment(); //field comment
    String getFieldName();
    String getFieldRawType();
    String getGetterMethodName();
    String getSetterMethodName();
    IWrapperType getWrapperType();
    boolean getIsList();
}
