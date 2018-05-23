package org.einstein.codegen.api.impl;

import org.einstein.codegen.api.IField;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kevin
 **/
public class Field implements IField {
    private String type;
    private String name;
    private Object defaultValue;
    private String comment;
    private String decorateType;
    private String decorateMethod;
    private boolean isList = false;
    private boolean isEProtoObject = false;
    private boolean isEnum = false;
    private boolean isConstant = false;
    private List<IField> fields = new ArrayList<>();

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getDefaultValue() {
        return defaultValue;
    }

    @Override
    public boolean isList() {
        return isList;
    }

    @Override
    public boolean isEProtoObject() {
        return isEProtoObject;
    }

    @Override
    public boolean isEnum() {
        return isEnum;
    }

    @Override
    public boolean isConstant() {
        return isConstant;
    }

    @Override
    public List<IField> getFields() {
        return fields;
    }

    @Override
    public String getDecorateType() {
        return decorateType;
    }

    @Override
    public String getDecorateMethod() {
        return decorateMethod;
    }

    @Override
    public String getComment() {
        return comment;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDefaultValue(Object defaultValue) {
        this.defaultValue = defaultValue;
    }

    public void setList(boolean list) {
        isList = list;
    }

    public void setEProtoObject(boolean EProtoObject) {
        isEProtoObject = EProtoObject;
    }

    public void setEnum(boolean anEnum) {
        isEnum = anEnum;
    }

    public void setConstant(boolean constant) {
        isConstant = constant;
    }

    public void setFields(List<IField> fields){
        this.fields = fields;
    }

    public void setComment(String comment){this.comment = comment;}

    public void setDecorateType(String decorateType){this.decorateType = decorateType;}

    public void setDecorateMethod(String decorateMethod){this.decorateMethod = decorateMethod;}
}
