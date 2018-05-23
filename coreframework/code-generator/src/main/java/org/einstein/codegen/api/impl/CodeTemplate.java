package org.einstein.codegen.api.impl;

import org.einstein.codegen.api.ICodeTemplate;
import org.einstein.codegen.exception.ESynatx;
import org.einstein.eproto.anno.EProtoEntity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

/**
 * @create by xiamicpp
 **/
public class CodeTemplate implements ICodeTemplate {
    private String CodeSourceFileName;
    private String CodeSourceFilePath;
    private String ProtoPackageName;
    private String ProtoClassName;
    private Class<?> proto;

    private CodeTemplate(){}

    public CodeTemplate(String protoPackageName, String protoClassName) throws ESynatx {
        this.ProtoPackageName = protoPackageName;
        this.ProtoClassName = protoClassName;
    }

    public void reflectClass(String dir) throws ESynatx {
        try {
            System.out.println(dir);
            URL url = new URL("file:" + dir);
            ClassLoader loader = new URLClassLoader(new URL[]{url},this.getClass().getClassLoader());
            this.proto = loader.loadClass(this.ProtoPackageName+"."+this.ProtoClassName);
            //this.proto = Class.forName(this.ProtoPackageName+"."+this.ProtoClassName,false,loader);
        }catch (Exception e){
            e.printStackTrace();
            throw new ESynatx("failed to reflect proto class:"+e.getMessage(),e);
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
    public int getProtoClassID() {
        EProtoEntity annotation=this.proto.getDeclaredAnnotation(EProtoEntity.class);
        return annotation.id();
    }

    @Override
    public List<Field> getAllFields(){
        return  filterField(proto.getFields());
    }

    @Override
    public List<Field> getDeclaredFields() {
        return filterField(proto.getDeclaredFields());
    }

    public Class<?>[] getInterfaces(){
        return proto.getInterfaces();
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
