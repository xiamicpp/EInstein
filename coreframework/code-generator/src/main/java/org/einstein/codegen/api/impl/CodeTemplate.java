package org.einstein.codegen.api.impl;

import org.einstein.codegen.api.ICodeTemplate;
import org.einstein.codegen.exception.ESynatx;
import org.einstein.eproto.anno.EProtoEntity;
import org.einstein.eproto.anno.EProtoField;

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

    @Override
    public List<Field> getSupperFields() {
        Class<?>[] interfaces = getInterfaces();
        List<Field> supperFields = new ArrayList<>();
        for(Class<?> supper:interfaces){
            supperFields.addAll(filterField(supper.getFields()));
        }
        return supperFields;
    }

    public Class<?>[] getInterfaces(){
        return proto.getInterfaces();
    }

    public Class<?> getInterface() throws ESynatx {return  filterInterface(proto.getInterfaces());}

    public List<Field> getKeyField(){
        return filterKeyField(proto.getDeclaredFields());
    }

    private Class<?> filterInterface(Class<?> [] interfaces) throws ESynatx {
        int count = 0;
        Class<?> _interface = null;
        for(Class<?> interface_:interfaces){
            Annotation[] annotations=interface_.getDeclaredAnnotations();
            for(Annotation annotation:annotations){
                if(annotation.annotationType().getSimpleName().equalsIgnoreCase("EProtoEntity")) {
                   count++;
                   _interface = interface_;
                }
            }
        }
        if(count>1)
            throw new ESynatx("Multiple EProtoEntity Interface, Current not supported");
        return _interface;
    }

    private List<Field> filterField(Field[] fields){
        List<Field> rets = new ArrayList<>(fields.length);
        for(Field field:fields){
            EProtoField annotation=field.getDeclaredAnnotation(EProtoField.class);
            if(annotation!=null) {
                rets.add(field);
            }
        }
        return rets;
    }


    private List<Field> filterKeyField(Field[] fields){
        List<Field> rets = new ArrayList<>(fields.length);
        for(Field field:fields){
            EProtoField annotation=field.getDeclaredAnnotation(EProtoField.class);
            if(annotation!=null&&annotation.isKey()) {
                rets.add(field);
            }
        }
        return rets;
    }





}
