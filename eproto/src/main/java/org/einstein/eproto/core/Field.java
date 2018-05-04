package org.einstein.eproto.core;

/**
 * @author kevin
 **/
public class Field implements IField {

    private String m_field_type_;
    private String m_field_name_;

    public Field(String type, String name){
        this.m_field_name_ = name;
        this.m_field_type_ = type;
    }

    public String getType() {
        return m_field_type_;
    }

    public String getName() {
        return m_field_name_;
    }
}
