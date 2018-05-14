package org.einstein.codegen.api;

import java.util.List;

/**
 * field interface
 *
 * @author kevin
 */

public interface IField {

    List<String> getAnnotation(); //field annotation
    String getComment(); //field comment
    String getFieldName();
    String getFieldRawType();
    String getDefaultValue();
    IWrapperType getWrapperType();
    void setWrapperType(IWrapperType type);
    boolean getIsList();
    boolean isReserverdType();
}
