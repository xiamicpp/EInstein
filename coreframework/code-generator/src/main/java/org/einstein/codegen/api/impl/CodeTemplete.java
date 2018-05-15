package org.einstein.codegen.api.impl;

import org.einstein.codegen.api.ICodeTemplete;
import org.einstein.codegen.exception.ESynatx;

import java.lang.reflect.Field;

/**
 * @create by xiamicpp
 **/
public class CodeTemplete implements ICodeTemplete{
    private String CodeSourceFileName;
    private String CodeSourceFilePath;
    private String ProtoPackageName;
    private String ProtoClassName;
    private Class<?> proto;

    private CodeTemplete(){}

    public CodeTemplete(String protoPackageName, String protoClassName) throws ESynatx {
        this.ProtoPackageName = protoPackageName;
        this.ProtoClassName = protoClassName;
        try {
            this.proto = Class.forName(this.ProtoPackageName+"."+this.ProtoClassName);
        } catch (ClassNotFoundException e) {
            throw new ESynatx("Can not reflect proto class",e);
        }
    }

    public String getProtoClassName(){
        return this.ProtoClassName;
    }

    public String getProtoPackageName(){
        return this.ProtoPackageName;
    }

    public Field[] getAllFields(){
        return  proto.getFields();
    }


}
