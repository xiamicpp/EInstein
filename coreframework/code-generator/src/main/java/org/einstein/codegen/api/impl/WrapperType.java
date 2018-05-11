package org.einstein.codegen.api.impl;

import org.einstein.framework.IWrapperType;

/**
 * @create by kevin
 **/
public class WrapperType implements IWrapperType{
    private String fullName;
    private String simpleName;



    @Override
    public String getFullName() {
        return fullName;
    }

    @Override
    public String getSimpleName() {
        return simpleName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setSimpleName(String simpleName) {
        this.simpleName = simpleName;
    }
}
