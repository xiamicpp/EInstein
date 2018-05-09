package org.einstein.framework.impl;

import org.einstein.framework.IField;

/**
 * @author kevin
 **/
public class Field implements IField {

    private String m_field_type_;
    private String m_field_name_;
    private FTYPE m_type_ = FTYPE.UNKNOWN;
    private int templete_id_;

    public Field(FTYPE type) {
        this.m_type_ = type;
    }

    public void setField(String type, String name) {
        this.m_field_name_ = name;
        this.m_field_type_ = type;
    }

    public void setTemplete_id(int id) {
        this.templete_id_ = id;
    }

    public String getType() {
        return m_field_type_;
    }

    public String getName() {
        return m_field_name_;
    }

    public FTYPE getFieldType() {
        return m_type_;
    }

    public int getTempleteID() {
        return templete_id_;
    }
}
