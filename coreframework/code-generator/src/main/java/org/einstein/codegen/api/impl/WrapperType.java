package org.einstein.codegen.api.impl;


import org.einstein.codegen.api.IWrapperType;

/**
 * @create by kevin
 **/
public class WrapperType implements IWrapperType {
    private String fullName;
    private String simpleName;
    private String m_name;
    private boolean isReservedType = false;
    private String m_field_getterMethod_;
    private String m_field_setterMethod_;

    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public String getSimpleName() {
        return simpleName;
    }

    @Override
    public boolean isReservedType() {
        return isReservedType;
    }

    @Override
    public String getGetterMethodName() {
        return m_field_getterMethod_;
    }

    @Override
    public String getSetterMethodName() {
        return m_field_setterMethod_;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public void setName(String name){
        this.m_name = name;
    }

    public void setGetterMethodName(String name){
        this.m_field_getterMethod_ = name;
    }

    public void setSetterMethodName(String name){
        this.m_field_setterMethod_ = name;
    }
}
