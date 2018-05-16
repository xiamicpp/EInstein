package org.einstein.codegen.api.impl;

import org.einstein.codegen.api.ICodeTemplete;
import org.einstein.codegen.exception.ESynatx;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public String getProtoClassName(){
        return this.ProtoClassName;
    }

    @Override
    public String getProtoPackageName(){
        return this.ProtoPackageName;
    }

    @Override
    public List<Field> getAllFields(){
        return  filterField(proto.getFields());
    }

    @Override
    public List<Field> getDeclaredFields() {
        return filterField(proto.getDeclaredFields());
    }

    private List<Field> filterField(Field[] fields){
        List<Field> rets = new ArrayList<>(fields.length);
        for(Field field:fields){
            Annotation[] annotations=field.getDeclaredAnnotations();
            for(Annotation annotation:annotations){
                if(annotation.annotationType().getSimpleName().equalsIgnoreCase("EProtoField"))
                   rets.add(field);
            }
        }
        return rets;
    }


}
