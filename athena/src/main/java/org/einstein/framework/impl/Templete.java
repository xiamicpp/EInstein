package org.einstein.framework.impl;

import org.einstein.framework.IField;
import org.einstein.framework.ITemplete;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevin on 2018/5/6.
 */
public class Templete implements ITemplete {
    private String eproto_name_;
    private ITemplete.EType type_;
    private String templete_name_;
    private List<IField> fields_;
    private int class_entity_id_; //also is the templete id

    private Templete() {
    }

    public Templete(EType type) {
        this.type_ = type;
        fields_ = new ArrayList<IField>(10);
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

    public EType getType() {
        return type_;
    }
}
