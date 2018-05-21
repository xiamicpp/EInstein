package org.einstein.codegen.generator;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.einstein.codegen.api.ICodeTemplete;
import org.einstein.codegen.api.impl.CodeTemplete;
import org.einstein.codegen.util.FileUtil;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
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
    private static final String interface_template= "templete/entity/java/interface";

    private static final String[] CLASS_PREFIX = {"E","E","I","I"};
    private static final String[] CLASS_SUFFIX = {"","Immutable","","Immutable"};

    @Override
    protected boolean loadVelocityTemplate() {
        this.immutable_interface_template = ve.getTemplate(interface_template,ENCODE);
        return true;
    }

    @Override
    protected boolean generateCode(CodeTemplete code) {
        if(!generateImmutableInterface(code)) return false;
        logger.debug("immutableInterface generated!!!");
        return true;
    }


    /**
     * generate immutable interface
     * @param code
     * @return generate status
     */
    private boolean generateImmutableInterface(CodeTemplete code){
        Writer out = null;
        try {
            String interfaceName = decorateClassName(code.getProtoClassName(),true,true);
            String dir = generateOutPutDir(code.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_API,outPutPath);
            out = FileUtil.createFileWriter(interfaceName+".java",dir);
            VelocityContext ctx = new VelocityContext();
            this.generatePackage(code,ctx,true,true);
            this.generateImports(code,ctx,true,true);

            this.immutable_interface_template.merge(ctx,out);
            FileUtil.flush(out);

        }catch (IOException e){
            logger.error("Failed to generate immutableInterface for {}, caused by:{}",code.getProtoClassName(),e.getMessage());
            e.printStackTrace();
            return false;
        }finally {
            FileUtil.close(out);
        }
        return true;
    }


    private boolean generatePackage(CodeTemplete code,VelocityContext ctx,boolean isInterface,boolean isImmutable){
        if(isInterface)
            ctx.put("packageName",code.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_API);
        else
            ctx.put("packageName",code.getProtoClassName()+GENERATED_ENTITYS+GENERATED_entity);
        return true;
    }

    private boolean generateImports(CodeTemplete code,VelocityContext ctx, boolean isInterface, boolean isImmutbale){
        List<String> imports = new ArrayList<>();

        Class<?>[] interfaces = code.getInterfaces();
        for(Class<?> pinterface:interfaces){
            ICodeTemplete superCode= codesMap.get(pinterface.getSimpleName());
            if(superCode!=null){
                String wrapperInterface = decorateClassName(superCode.getProtoClassName(),isInterface,isImmutbale);
                String newImport = code.getProtoPackageName()+GENERATED_ENTITYS;
                if(isInterface)
                    newImport +=GENERATED_API;
                else
                    newImport +=GENERATED_entity;
                imports.add(newImport+"."+wrapperInterface);
            }
        }

        List<Field> fields = code.getDeclaredFields();
        for(Field field:fields){
            ICodeTemplete fieldTypeCode = codesMap.get(field.getType().getSimpleName());
            if(fieldTypeCode!=null){
                String wrapperTypeName = decorateClassName(fieldTypeCode.getProtoClassName(),isInterface,isImmutbale);
                String newImport = fieldTypeCode.getProtoPackageName()+GENERATED_ENTITYS;
                if(isInterface) newImport+=GENERATED_API;
                else newImport+=GENERATED_entity;
                imports.add(newImport+"."+wrapperTypeName);
            }
        }
        ctx.put("impots",imports);
        return true;
    }


    private boolean generateClassHeader(CodeTemplete code,VelocityContext ctx,boolean isInterface,boolean isImmutable){
        String entityName = decorateClassName(code.getProtoClassName(),true,true);
        ctx.put("entityName",entityName);



        return true;
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
        int index=(isInterface==true?1:0) + (isImmutable==true?1:0);
        return CLASS_PREFIX[index]+name+CLASS_SUFFIX[index];
    }


}
