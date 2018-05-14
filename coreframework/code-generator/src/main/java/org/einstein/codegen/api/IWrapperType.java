package org.einstein.codegen.api;

/**
 * @create by kevin
 **/
public interface IWrapperType {
    String getFullName();
    String getSimpleName();
    boolean isReservedType();
    String getGetterMethodName();
    String getSetterMethodName();
}
