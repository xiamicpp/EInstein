package org.einstein.codegen.api.impl;


import org.einstein.codegen.api.IField;
import org.einstein.codegen.api.ITemplate;

import java.util.List;

/**
 * Created by kevin on 2018/5/6.
 */
public class Template implements ITemplate {
    private String eproto_name_;

    private String templete_name_;
    private List<IField> fields_;
    private int class_entity_id_; //also is the templete id

    private Template() {
    }



    public void setClassEntityId(int id){this.class_entity_id_ = id;}

    public void setEprotoName(String name){this.eproto_name_ = name;}

    public void setTempleteName(String templete_name) {
        this.templete_name_ = templete_name;
    }

    public void addField(IField field) {
        fields_.add(field);
    }

    public String getTempleteName() {
        return templete_name_;
    }

    public String getEprotoName() {
        return eproto_name_;
    }

    public int getClassEntityID() {
        return class_entity_id_;
    }

    public List<IField> getFields() {
        return fields_;
    }

}
