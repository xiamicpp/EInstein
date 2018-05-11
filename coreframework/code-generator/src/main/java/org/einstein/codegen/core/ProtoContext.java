package org.einstein.codegen.core;


import org.einstein.framework.IField;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kevin
 **/
public class ProtoContext implements IPortoContext {
    private String m_proto_version_ = "proto3";
    private String m_proto_pakage_;
    private String m_proto_classname_;
    private String m_proto_message_;
    private List<IField> m_fields_ = new ArrayList<IField>(10);
    public static String proto_classname_suffix_ = "_pb";

    public String getVersion() {
        return m_proto_version_;
    }

    public String getPackage() {
        return m_proto_pakage_;
    }

    public String getClassname() {
        return m_proto_classname_ + proto_classname_suffix_;
    }

    public String getMessage() {
        return m_proto_message_;
    }

    public List<IField> getFields() {
        return m_fields_;
    }

    public void setVersion(String protoVersion) {
        this.m_proto_version_ = protoVersion;
    }

    public void setPakage(String pakage) {
        this.m_proto_pakage_ = pakage;
    }

    public void setClassname(String classname) {
        this.m_proto_classname_ = classname;
    }

    public void setMessage(String message) {
        this.m_proto_message_ = message;
    }

    public void addField(IField field) {
        this.m_fields_.add(field);
    }
}
