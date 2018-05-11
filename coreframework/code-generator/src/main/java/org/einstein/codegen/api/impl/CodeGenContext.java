package org.einstein.codegen.api.impl;

import org.einstein.codegen.api.IField;

import java.util.List;

/**
 * @create by xiamicpp
 **/
public class CodeGenContext {

    public void setFields(List<IField> fields) {
        this.fields = fields;
    }

    private List<IField> fields;


    public CodeGenContext(){

    }

    public List<IField> getFields() {
        return fields;
    }

}
