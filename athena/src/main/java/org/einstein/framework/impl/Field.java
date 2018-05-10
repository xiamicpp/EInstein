package org.einstein.framework.impl;

import org.einstein.framework.IField;
import org.einstein.framework.IWrapperType;

/**
 * @author kevin
 **/
public class Field implements IField {

    private String m_field_type_;
    private String m_field_name_;
    private String m_field_annotation_;
    private String m_field_comment_;
    private boolean m_isList_ = false;
    private String m_field_getterMethod_;
    private String m_field_setterMethod_;
    private IWrapperType m_field_wrapperType_;


    public Field() { }

    @Override
    public String getAnnotation() {
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

    public void setM_field_annotation_(String m_field_annotation_) {
        this.m_field_annotation_ = m_field_annotation_;
    }

    public void setM_field_comment_(String m_field_comment_) {
        this.m_field_comment_ = m_field_comment_;
    }

    public void setM_isList_(boolean m_isList_) {
        this.m_isList_ = m_isList_;
    }

    public void setM_field_getterMethod_(String m_field_getterMethod_) {
        this.m_field_getterMethod_ = m_field_getterMethod_;
    }

    public void setM_field_setterMethod_(String m_field_setterMethod_) {
        this.m_field_setterMethod_ = m_field_setterMethod_;
    }

    public void setM_field_wrapperType_(IWrapperType m_field_wrapperType_) {
        this.m_field_wrapperType_ = m_field_wrapperType_;
    }
}
