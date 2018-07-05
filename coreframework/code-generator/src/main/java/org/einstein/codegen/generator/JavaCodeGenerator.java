package org.einstein.codegen.generator;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.einstein.codegen.api.ICodeTemplate;
import org.einstein.codegen.api.impl.CodeTemplate;
import org.einstein.codegen.exception.ESynatx;
import org.einstein.codegen.util.FileUtil;
import org.einstein.codegen.util.TimeUtil;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @create by xiamicpp
 **/
public class JavaCodeGenerator extends BaseGenerator {

    private Template immutable_interface_template;
    private Template mutable_interface_template;
    private Template immutable_implement_template;
    private Template mutable_implement_template;
    private Template implement_template;
    private static final String interface_template_vm= "template/entity/java/interface.vm";
    private static final String implement_template_vm= "template/entity/java/implement.vm";

    private static final String[] CLASS_PREFIX = {"E","I","E","I"};
    private static final String[] CLASS_SUFFIX = {"","","Immutable","Immutable"};
    private static final String BASE_API_EPOBEJCT = "org.einstein.eproto.api.IEProtoObject";
    private static final String BASE_API_MUTABLE = "org.einstein.eproto.api.IMutable";
    private static final String BASE_API_SERILIZABLE = "org.einstein.eproto.api.ISerializable";

    @Override
    protected boolean loadVelocityTemplate() {
        this.immutable_interface_template = ve.getTemplate(interface_template_vm,ENCODE);
        this.mutable_interface_template = ve.getTemplate(interface_template_vm,ENCODE);
        this.implement_template = ve.getTemplate(implement_template_vm,ENCODE);
        return true;
    }

    @Override
    protected boolean generateCode(CodeTemplate code) {
        try {
            generateImmutableInterface(code);
            generateMutableInterface(code);
            generateImplementClass(code);
        }catch (ESynatx e){
            logger.error("generated failed:{}",e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }


    /**
     * generate immutable interface
     * @param code
     */
    private void generateImmutableInterface(CodeTemplate code) throws ESynatx {
        Writer out = null;
        try {
            String interfaceName = decorateClassName(code.getProtoClassName(),true,true);
            String dir = generateOutPutDir(code.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_API,outPutPath);
            out = FileUtil.createFileWriter(interfaceName+".java",dir);
            VelocityContext ctx = new VelocityContext();
            ctx.put("isMutable",false);
            ctx.put("PROTO_CLASS_ID",code.getProtoClassID());
            ctx.put("current_time", TimeUtil.CurrentTime());
            this.generatePackage(code,ctx,true,true);
            this.generateImports(code,ctx,true,true);
            this.generateClassHeader(code,ctx,true,true);
            this.generateInterfaceFields(code,ctx,true);
            this.immutable_interface_template.merge(ctx,out);
            FileUtil.flush(out);
        }catch (IOException e){
            logger.error("Failed to generate immutableInterface for {}, caused by:{}",code.getProtoClassName(),e.getMessage());
            e.printStackTrace();
            throw new ESynatx("immutbable interface file create failed",e);
        }finally {
            FileUtil.close(out);
        }
    }


    private void generateMutableInterface(CodeTemplate code) throws ESynatx {
        Writer out = null;
        try{
            String interfaceName = decorateClassName(code.getProtoClassName(),true,false);
            String dir = generateOutPutDir(code.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_API,outPutPath);
            out = FileUtil.createFileWriter(interfaceName+".java",dir);
            VelocityContext ctx = new VelocityContext();
            ctx.put("isMutable",true);
            ctx.put("current_time", TimeUtil.CurrentTime());
            this.generatePackage(code,ctx,true,false);
            this.generateImports(code,ctx,true,false);
            this.generateClassHeader(code,ctx,true,false);
            this.generateInterfaceFields(code,ctx,false);
            this.mutable_interface_template.merge(ctx,out);
            FileUtil.flush(out);
        }catch (IOException e){
            logger.error("Failed to generate immutableInterface for {}, caused by:{}",code.getProtoClassName(),e.getMessage());
            e.printStackTrace();
            throw new ESynatx("immutbable interface file create failed",e);
        }
    }

    private void generateImplementClass(CodeTemplate code) throws ESynatx {
        Writer out = null;
        try {
            String className = decorateClassName(code.getProtoClassName(), false, false);
            String dir = generateOutPutDir(code.getProtoPackageName() + GENERATED_ENTITYS + GENERATED_entity, outPutPath);
            out = FileUtil.createFileWriter(className + ".java", dir);
            VelocityContext ctx = new VelocityContext();
            ctx.put("current_time", TimeUtil.CurrentTime());

            this.generatePackage(code,ctx,false,false);
            this.generateImports(code,ctx,false,false);
            this.generateClassHeader(code,ctx,false,false);
            this.generateClassFields(code,ctx);
            this.implement_template.merge(ctx,out);
            FileUtil.flush(out);

        }catch (IOException e){
            logger.error("Failed to generate classImplement for {}, caused by:{}",code.getProtoClassName(),e.getMessage());
            e.printStackTrace();
            throw new ESynatx("class file create failed",e);
        }
    }


    private boolean generatePackage(CodeTemplate code, VelocityContext ctx, boolean isInterface, boolean isImmutable){
        if(isInterface)
            ctx.put("packageName",code.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_API);
        else
            ctx.put("packageName",code.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_entity);
        return true;
    }

    private boolean generateImports(CodeTemplate code, VelocityContext ctx, boolean isInterface, boolean isImmutable){
        List<String> imports = new ArrayList<>();
        if(isInterface&&isImmutable){
            //imports.add(BASE_API_EPOBEJCT);
            imports.add(BASE_API_SERILIZABLE);
        }

        List<Field> fields = code.getDeclaredFields();
        for(Field field:fields){
            ICodeTemplate fieldTypeCode = codesMap.get(field.getType().getSimpleName());
            if(fieldTypeCode!=null){
                String wrapperTypeName = decorateClassName(fieldTypeCode.getProtoClassName(),isInterface,isImmutable);
                String newImport = fieldTypeCode.getProtoPackageName()+GENERATED_ENTITYS;
                if(isInterface) newImport+=GENERATED_API;
                else newImport+=GENERATED_entity;
                imports.add(newImport+"."+wrapperTypeName);
            }
        }
        ctx.put("impots",imports);
        return true;
    }


    private boolean generateClassHeader(CodeTemplate code, VelocityContext ctx, boolean isInterface, boolean isImmutable) throws ESynatx {
        String entityName = decorateClassName(code.getProtoClassName(),isInterface,isImmutable);
        ctx.put("entityName",entityName);
        if(isInterface){
            List<String> ctxInterfaces = new ArrayList<>();
            Class<?>[] interfaces = code.getInterfaces();
            int protoInterfaceCount = 0;
            String extendsProto ="";
            String newImport = "";
            for(Class<?> pinterface:interfaces){
                ICodeTemplate superCode= codesMap.get(pinterface.getSimpleName());
                if(superCode!=null){
                    protoInterfaceCount++;
                    extendsProto =superCode.getProtoClassName();
                    newImport = code.getProtoPackageName()+GENERATED_ENTITYS;
                }
            }
            if(protoInterfaceCount>1){
                logger.error("not allowed extends multiple EProtoObject!, it extends:{}", interfaces.toString());
                throw new ESynatx("proto extends multiple proto!, it extends:"+interfaces.toString());
            }

            if(isImmutable){
                if(protoInterfaceCount>0){
                    ctxInterfaces.add(newImport+GENERATED_API+"."+decorateClassName(extendsProto,true,true));
                }else {
                    //String mutable = code.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_API+"."+decorateClassName(code.getProtoClassName(),true,false);
                    //String immutable = code.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_API+"."+decorateClassName(code.getProtoClassName(),true,true);
                    //ctxInterfaces.add(BASE_API_EPOBEJCT+"<"+mutable+","+immutable+">");
                    ctxInterfaces.add(BASE_API_SERILIZABLE);
                }
            }else{
                if(protoInterfaceCount>0){
                    ctxInterfaces.add(newImport+GENERATED_API+"."+decorateClassName(extendsProto,true,false));
                }
                ctxInterfaces.add(code.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_API+"."+decorateClassName(code.getProtoClassName(),true,true));
            }
            ctx.put("interfaces",ctxInterfaces);
        }else{
            String mutableInterface = code.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_API+"."+decorateClassName(code.getProtoClassName(),true,false);
            ctx.put("mutableInterface",mutableInterface);
        }

        return true;
    }

    private void generateClassFields(CodeTemplate code, VelocityContext ctx) throws ESynatx{
        //childFields
        List<Field> fields=code.getDeclaredFields();
        List<org.einstein.codegen.api.impl.Field> convertFields = new ArrayList<>(fields.size());
        try {
            for (Field field : fields) {
                if (field.getType().isMemberClass()) {
                    ICodeTemplate memeber = codesMap.get(field.getType().getSimpleName());
                    if (memeber == null) {
                        logger.error("can not match member in code map, name:{}", field.getType().getSimpleName());
                        throw new ESynatx("Interface field generate failed, field:" + field.getName());
                    }
                    org.einstein.codegen.api.impl.Field convertField = new org.einstein.codegen.api.impl.Field();
                    convertField.setType(decorateClassName(memeber.getProtoClassName(), true, false));
                    convertField.setName(field.getName());
                    convertField.setDefaultValue(field.get(null));
                    convertField.setDecorateType(memeber.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_API+"."+convertField.getType());
                    convertField.setDecorateMethod(decorateMethod(field.getName(),true,false));
                    convertFields.add(convertField);
                }else if(field.getType().isAssignableFrom(List.class)){
                    org.einstein.codegen.api.impl.Field convertField = new org.einstein.codegen.api.impl.Field();
                    convertField.setList(true);
                    Type genericType=field.getGenericType();
                    if(genericType == null) continue;
                    if(genericType instanceof ParameterizedType){
                        Class<?> supper = (Class<?>) ((ParameterizedType)genericType).getActualTypeArguments()[0];
                        ICodeTemplate templete = codesMap.get(supper.getSimpleName());
                        if(templete!=null){
                            convertField.setEProtoObject(true);
                            convertField.setType(decorateClassName(templete.getProtoClassName(),true,false));
                            convertField.setName(field.getName());
                            convertField.setDecorateType(code.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_API+"."+convertField.getType());
                        }else {
                            convertField.setName(field.getName());
                            convertField.setType(supper.getSimpleName());
                            convertField.setDecorateType(supper.getName());
                        }
                        convertField.setDecorateMethod(decorateMethod(field.getName(),true,false));
                        convertField.setDefaultValue(field.get(null));
                        convertFields.add(convertField);
                    }
                }else{
                    org.einstein.codegen.api.impl.Field convertField = new org.einstein.codegen.api.impl.Field();
                    convertField.setDecorateMethod(decorateMethod(field.getName(),true,false));
                    convertField.setDecorateType(field.getType().getName());
                    convertField.setName(field.getName());
                    convertFields.add(convertField);
                }
            }
        }catch (IllegalAccessException e){
            throw new ESynatx("can not get default value",e);
        }
        ctx.put("childfields",convertFields);

        //supperClass

    }

    private void generateInterfaceFields(CodeTemplate code, VelocityContext ctx, boolean isImmutable) throws ESynatx {

        if(isImmutable){
            ctx.put("getterMethod",true);
            ctx.put("setterMethod",false);
        }else{
            ctx.put("getterMethod",false);
            ctx.put("setterMethod",true);
        }

        List<Field> fields=code.getDeclaredFields();
        List<org.einstein.codegen.api.impl.Field> convertFields = new ArrayList<>(fields.size());
        try {
            for (Field field : fields) {
                if (field.getType().isMemberClass()) {
                    ICodeTemplate memeber = codesMap.get(field.getType().getSimpleName());
                    if (memeber == null) {
                        logger.error("can not match member in code map, name:{}", field.getType().getSimpleName());
                        throw new ESynatx("Interface field generate failed, field:" + field.getName());
                    }
                    org.einstein.codegen.api.impl.Field convertField = new org.einstein.codegen.api.impl.Field();
                    convertField.setType(decorateClassName(memeber.getProtoClassName(), true, isImmutable));
                    convertField.setName(field.getName());
                    convertField.setDefaultValue(field.get(null));
                    convertField.setDecorateType(memeber.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_API+"."+convertField.getType());
                    convertField.setDecorateMethod(decorateMethod(field.getName(),true,isImmutable));
                    convertFields.add(convertField);
                }else if(field.getType().isAssignableFrom(List.class)){
                    org.einstein.codegen.api.impl.Field convertField = new org.einstein.codegen.api.impl.Field();
                    convertField.setList(true);
                    Type genericType=field.getGenericType();
                    if(genericType == null) continue;
                    if(genericType instanceof ParameterizedType){
                        Class<?> supper = (Class<?>) ((ParameterizedType)genericType).getActualTypeArguments()[0];
                        ICodeTemplate templete = codesMap.get(supper.getSimpleName());
                        if(templete!=null){
                            convertField.setEProtoObject(true);
                            convertField.setType(decorateClassName(templete.getProtoClassName(),true,isImmutable));
                            convertField.setName(field.getName());
                            convertField.setDecorateType(code.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_API+"."+convertField.getType());
                        }else {
                            convertField.setName(field.getName());
                            convertField.setType(supper.getSimpleName());
                            convertField.setDecorateType(supper.getName());
                        }
                        convertField.setDecorateMethod(decorateMethod(field.getName(),true,isImmutable));
                        convertField.setDefaultValue(field.get(null));
                        convertFields.add(convertField);
                    }
                }else{
                    org.einstein.codegen.api.impl.Field convertField = new org.einstein.codegen.api.impl.Field();
                    convertField.setDecorateMethod(decorateMethod(field.getName(),true,isImmutable));
                    convertField.setDecorateType(field.getType().getName());
                    convertField.setName(field.getName());
                    convertFields.add(convertField);
                }
            }
        }catch (IllegalAccessException e){
            throw new ESynatx("can not get default value",e);
        }
        ctx.put("fields",convertFields);
    }

    private String decorateMethod(String name,boolean isInterface, boolean isImmutable){
        if(isImmutable){
            return  "get"+ StringUtils.capitalize(name);
        }else
            return  "set"+StringUtils.capitalize(name);
    }

    /**
     * 00 false|false
     * 01 false|true
     * 10 true|false
     * 11 true|true
     * @param name
     * @param isInterface
     * @param isImmutable
     * @return
     */
    private String decorateClassName(String name,boolean isInterface, boolean isImmutable){
        //编码
        int index=(isInterface==true?1:0) + (isImmutable==true?2:0);
        return CLASS_PREFIX[index]+name+CLASS_SUFFIX[index];
    }


}
