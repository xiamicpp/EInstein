package org.einstein.codegen.api.impl;

import org.einstein.framework.IEntityTemplete;
import org.einstein.framework.IField;

import java.util.ArrayList;
import java.util.List;

/**
 * @create by kevin
 **/
public class ProtoEntityTemplete implements IEntityTemplete{
    private String m_name; //entity name
    private List<String> m_extends;
    private List<IField> m_fields = new ArrayList<>();
    private int m_id;
    private String m_packageName;
    private String m_comments;

    private EType type;
    @Override
    public String getPackageName() {
        return m_packageName;
    }

    @Deprecated
    @Override
    public String getVersion() {
        return null;
    }

    @Override
    public EType getType() {
        return type;
    }

    public void setM_packageName(String m_packageName) {
        this.m_packageName = m_packageName;
    }

    public void setType(EType type) {
        this.type = type;
    }

    public String getM_name() {
        return m_name;
    }

    public void setM_name(String m_name) {
        this.m_name = m_name;
    }

    public List<String> getM_extends() {
        return m_extends;
    }

    public void setM_extends(List<String> m_extends) {
        this.m_extends = m_extends;
    }

    public List<IField> getM_fields() {
        return m_fields;
    }

    public void setM_fields(List<IField> m_fields) {
        this.m_fields = m_fields;
    }

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    public String getM_packageName() {
        return m_packageName;
    }

    public String getM_comments() {
        return m_comments;
    }

    public void setM_comments(String m_comments) {
        this.m_comments = m_comments;
    }
}
