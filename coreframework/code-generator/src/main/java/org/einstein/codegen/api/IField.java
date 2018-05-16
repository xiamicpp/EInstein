package org.einstein.codegen.api;


import java.util.List;

/**
 * field interface
 *
 * @author kevin
 */

public interface IField {
    /**
     * field type
     * @return
     */
    String getType(); //double,float,int,long,boolean,String,ByteString

    /**
     * field name
     * @return
     */
    String getName();

    Object getDefaultValue();

    boolean isList();

    boolean isEProtoObject();

    boolean isEnum();

    boolean isConstant();

    /**
     * when is object or enum, return supper fields
     * @return
     */
    List<IField> getFields();
}
