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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private static final String BASE_API_ABSTRACTOBJECT = "org.einstein.eproto.api.impl.EPAbstractProtoObject";

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
            this.generateFields(code,ctx,true,true);
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
            this.generateFields(code,ctx,false,true);
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
            this.generateFields(code,ctx,false,false);
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
        Set<String> imports = new HashSet<>();
        if(isInterface&&isImmutable){
            imports.add(BASE_API_ABSTRACTOBJECT);
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
            Class<?> p_interface = code.getInterface();
            ICodeTemplate superCode = null;
            if(p_interface !=null){
                superCode= codesMap.get(p_interface.getSimpleName());
            }
            if(superCode!=null){
                String extendsProto =superCode.getProtoClassName();
                String newImport = code.getProtoPackageName()+GENERATED_ENTITYS;
                ctxInterfaces.add(newImport+GENERATED_API+"."+decorateClassName(extendsProto,true,isImmutable));
            }
            if(!isImmutable){
                ctxInterfaces.add(code.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_API+"."+decorateClassName(code.getProtoClassName(),true,true));
            }
            ctx.put("interfaces",ctxInterfaces);
        }else{
            String pbType=code.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_GOOGLE+"."+PB_CLASS_PREFFIX+code.getProtoClassName()+"."+code.getProtoClassName();
            ctx.put("supperClass",BASE_API_ABSTRACTOBJECT+"<"+pbType+">");
            String mutableInterface = code.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_API+"."+decorateClassName(code.getProtoClassName(),true,false);
            ctx.put("mutableInterface",mutableInterface);
        }

        return true;
    }


    private void generateFields(CodeTemplate code, VelocityContext ctx, boolean isImmutable,boolean isInterface) throws ESynatx {
        if(isInterface) {
            if (isImmutable) {
                ctx.put("getterMethod", true);
                ctx.put("setterMethod", false);
            } else {
                ctx.put("getterMethod", false);
                ctx.put("setterMethod", true);
            }
        }
        List<Field> fields=code.getDeclaredFields();
        List<org.einstein.codegen.api.impl.Field> convertFields = analysisProtoField(fields,code,isImmutable);
        ctx.put("fields",convertFields);
        if(!isInterface){
            Class<?> supperInterface = code.getInterface();
            if(supperInterface!=null){
                ICodeTemplate superCode= codesMap.get(supperInterface.getSimpleName());
                if(superCode!=null) {
                    String mutableInterface = superCode.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_API+"."+decorateClassName(superCode.getProtoClassName(),true,false);
                    String imutableInterface = superCode.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_API+"."+decorateClassName(superCode.getProtoClassName(),true,true);
                    String mutableClass = superCode.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_entity+"."+decorateClassName(superCode.getProtoClassName(),false,false);
                    ctx.put("supperClassImmutableInterface",imutableInterface);
                    ctx.put("supperClassMutableInterface",mutableInterface);
                    ctx.put("supperMutableClass",mutableClass);
                    ctx.put("hasSupperClass", true);
                }
            }
            List<Field> supperFields = code.getSupperFields();
            List<org.einstein.codegen.api.impl.Field> supperConvertFields = analysisProtoField(supperFields,code,isImmutable);
            ctx.put("supperFields",supperConvertFields);
        }
    }

    private String decorateMethod(String name,boolean isInterface, boolean isImmutable){

            return  StringUtils.capitalize(name);
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


    private List<org.einstein.codegen.api.impl.Field> analysisProtoField(List<Field> fields,CodeTemplate code,boolean isImmutable) throws ESynatx {
        List<org.einstein.codegen.api.impl.Field> convertFields = new ArrayList<>(fields.size());
        try {
            for (Field field : fields) {
                ICodeTemplate memeber = codesMap.get(field.getType().getSimpleName());
                if (memeber != null) {
                    org.einstein.codegen.api.impl.Field convertField = new org.einstein.codegen.api.impl.Field();
                    convertField.setType(decorateClassName(memeber.getProtoClassName(), true, true));
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
                            convertField.setType(decorateClassName(templete.getProtoClassName(),true,true));

                            convertField.setDecorateType(code.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_API+"."+convertField.getType());
                        }else {
                            convertField.setType(supper.getSimpleName());
                            convertField.setDecorateType(supper.getName());
                        }
                        convertField.setName(field.getName());
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
        return convertFields;
    }

}
