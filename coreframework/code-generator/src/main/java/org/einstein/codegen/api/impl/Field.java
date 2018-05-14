package org.einstein.codegen.api.impl;


import org.apache.commons.lang3.StringUtils;
import org.einstein.codegen.api.IField;
import org.einstein.codegen.api.IWrapperType;
import org.einstein.codegen.parse.PropertyParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kevin
 **/
public class Field implements IField {

    private String m_field_type_;
    private String m_field_name_;
    private List<String> m_field_annotation_ = new ArrayList<>();
    private String m_field_comment_;
    private boolean m_isList_ = false;
    private IWrapperType m_field_wrapperType_;
    private String m_default_value;
    private boolean m_reserverdType = true;

    private static final String[] s_reserverdType = new String[]{
            "byte","short","int","long","float","double","char","boolean","string"
    };


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
    public IWrapperType getWrapperType() {
        return m_field_wrapperType_;
    }

    @Override
    public void setWrapperType(IWrapperType type) {
        this.m_field_wrapperType_ = type;
    }

    @Override
    public boolean getIsList() {
        return m_isList_;
    }

    @Override
    public boolean isReserverdType() {
        return this.m_reserverdType;
    }

    public void setM_field_type_(String m_field_type_) {
        this.m_field_type_ = m_field_type_;
        if(StringUtils.equalsIgnoreCase(this.m_field_type_,"LIST"))
            this.m_isList_ = true;
        this.m_reserverdType = true;
        for(String type:s_reserverdType){
            if(StringUtils.equalsIgnoreCase(type,this.m_field_type_)) {
                this.m_reserverdType = true;
                break;
            }
        }
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
