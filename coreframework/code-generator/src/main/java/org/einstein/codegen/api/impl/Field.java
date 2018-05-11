package org.einstein.codegen.api.impl;


import org.apache.commons.lang3.StringUtils;
import org.einstein.codegen.api.IField;
import org.einstein.codegen.api.IWrapperType;
import org.einstein.codegen.parse.PropertyParser;

import java.util.List;

/**
 * @author kevin
 **/
public class Field implements IField {

    private String m_field_type_;
    private String m_field_name_;
    private List<String> m_field_annotation_;
    private String m_field_comment_;
    private boolean m_isList_ = false;
    private String m_field_getterMethod_;
    private String m_field_setterMethod_;
    private IWrapperType m_field_wrapperType_;
    private String m_default_value;


    public Field() { }

    @Override
    public List<String> getAnnotation() {
        return m_field_annotation_;
    }

    @Override
    public String getComment() {
        return m_field_comment_;
    }

    @Override
    public String getFieldName() {
        return m_field_name_;
    }

    @Override
    public String getFieldRawType() {
        return m_field_type_;
    }

    @Override
    public String getDefaultValue() {
        return m_default_value;
    }

    @Override
    public String getGetterMethodName() {
        return m_field_getterMethod_;
    }

    @Override
    public String getSetterMethodName() {
        return m_field_setterMethod_;
    }

    @Override
    public IWrapperType getWrapperType() {
        return m_field_wrapperType_;
    }

    @Override
    public boolean getIsList() {
        return m_isList_;
    }

    public void setM_field_type_(String m_field_type_) {
        this.m_field_type_ = m_field_type_;
    }

    public void setM_field_name_(String m_field_name_) {
        this.m_field_name_ = m_field_name_;
    }

    public void setM_field_comment_(String m_field_comment_) {
        this.m_field_comment_ = m_field_comment_;
    }


    public void addFieldAnnotation(String annotation){
        this.m_field_annotation_.add(annotation);
    }

    public void setDefaultValue(String value){
        this.m_default_value = value;
    }
}
