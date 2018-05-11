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

    private static final String[] s_reserverdType = new String[]{
            "byte","short","int","long","float","double","char","boolean"
    };

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

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }

    public void setName(String name){
        this.m_name = name;
    }
}
