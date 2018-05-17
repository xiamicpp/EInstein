package org.einstein.codegen.generator;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.einstein.codegen.api.ICodeTemplete;
import org.einstein.codegen.api.IGenerator;
import org.einstein.codegen.api.impl.CodeTemplete;
import org.einstein.codegen.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author kevin
 **/
public class GoogleProtoGenerator extends BaseGenerator {
    private static String OS = System.getProperty("os.name").toLowerCase();
    private static final String PROTOBUF_VERSION = "proto3";
    private static  String PB_CLASS_PREFFIX = "PB";
    private static  String PB_CLASS_SUFFIX = ".proto";


    @Override
    protected boolean loadVelocityTemplate() {
        template = ve.getTemplate("templete/proto/proto.vm", "UTF-8");
        return true;
    }

    @Override
    protected boolean generateCode(CodeTemplete code) {
        if(!generateProtoFile(code)) return false;
        logger.debug("start generate protobuf");
        if(!generateGoogelProtoBuf(code)) return false;
        return true;
    }

    private boolean generateProtoFile(CodeTemplete code){
        Writer out =null;
        try {
            String fielName = decorateClassName(code.getProtoClassName(),false,true);
            String dir = generateOutPutDir(code.getProtoPackageName()+GENERATED_PROTO,this.outPutPath);
            out = FileUtil.createFileWriter(fielName, dir);
            VelocityContext ctx = new VelocityContext();
            ctx.put("version",PROTOBUF_VERSION);
            ctx.put("package",code.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_GOOGLE);
            ctx.put("classname",decorateClassName(code.getProtoClassName(),true,false));
            ctx.put("message",decorateClassName(code.getProtoClassName(),false,false));
            generateProtoImports(ctx,code);
            generateProtoFields(ctx,code);
            this.template.merge(ctx,out);
            FileUtil.flush(out);
        } catch (IOException e) {
            logger.error("failed to create writer, {}",e);
            return false;
        }finally {
            FileUtil.close(out);
        }
        return true;
    }


    private void generateProtoImports(VelocityContext ctx, CodeTemplete code){
        List<Field> fields = code.getAllFields();
        List<String> protoImports = new ArrayList<>();
        for(Field field:fields){
            if(!field.getType().isMemberClass())
                continue;
            ICodeTemplete templete = codesMap.get(field.getType().getTypeName());
            if(templete!=null){
                String path = templete.getProtoPackageName()+GENERATED_PROTO;
                path=StringUtils.replace(path,".","/").concat("/")
                        + this.decorateClassName(templete.getProtoClassName(),true,false);
                protoImports.add(path);
            }
        }
        ctx.put("imports",protoImports);
    }

    private void generateProtoFields(VelocityContext ctx,CodeTemplete code){
        List<Field> fields = code.getAllFields();
        List<org.einstein.codegen.api.impl.Field> convertFields = new ArrayList<>(fields.size());
        try {
            for (Field field : fields) {
                if (field.getType().isMemberClass()) {
                    ICodeTemplete templete = codesMap.get(field.getType().getSimpleName());
                    if (templete != null) {
                        org.einstein.codegen.api.impl.Field convertField = new org.einstein.codegen.api.impl.Field();
                        convertField.setType(decorateClassName(field.getType().getSimpleName(),true,false));
                        convertField.setName(field.getName());
                        convertField.setDefaultValue(field.get(null));
                        convertField.setEProtoObject(true);
                        convertFields.add(convertField);
                    }
                }else if(field.getType().isEnum()){
                    org.einstein.codegen.api.impl.Field convertField = new org.einstein.codegen.api.impl.Field();
                    convertField.setEnum(true);
                    convertField.setType(field.getType().getSimpleName());
                    convertField.setName(field.getName());
                    List<org.einstein.codegen.api.IField> items = new ArrayList<>();
                    for(Field subField: field.getType().getDeclaredFields()){
                        if(subField.isEnumConstant()){
                            org.einstein.codegen.api.impl.Field enumField = new org.einstein.codegen.api.impl.Field();
                            enumField.setName(subField.getName());
                            enumField.setType(subField.getType().getSimpleName());
                            enumField.setDefaultValue(subField.get(null));
                            enumField.setEnum(true);
                            items.add(enumField);
                        }
                    }
                    convertField.setFields(items);
                    convertFields.add(convertField);
                }else if(field.getType().isAssignableFrom(List.class)){
                    org.einstein.codegen.api.impl.Field convertField = new org.einstein.codegen.api.impl.Field();
                    convertField.setList(true);
                    Type genericType=field.getGenericType();
                    if(genericType == null) continue;
                    if(genericType instanceof ParameterizedType){
                        Class<?> supper = (Class<?>) ((ParameterizedType)genericType).getActualTypeArguments()[0];
                        ICodeTemplete templete = codesMap.get(supper.getSimpleName());
                        if(templete!=null){
                            convertField.setEProtoObject(true);
                        }
                        convertField.setName(field.getName());
                        convertField.setType(supper.getSimpleName());
                        convertField.setDefaultValue(field.get(null));
                        convertFields.add(convertField);
                    }

                }else{
                    org.einstein.codegen.api.impl.Field convertField = new org.einstein.codegen.api.impl.Field();
                    convertField.setType(field.getType().getSimpleName());
                    convertField.setName(field.getName());
                    convertField.setDefaultValue(field.get(null));
                    convertFields.add(convertField);
                }
            }
        }catch (Exception e){
            logger.error("failed in generateProtoFields: {}",e);
        }
        ctx.put("fields",convertFields);
    }


    private boolean  generateGoogelProtoBuf(CodeTemplete code){
        String fielName = decorateClassName(code.getProtoClassName(),false,true);
        String dir = generateOutPutDir(code.getProtoPackageName()+GENERATED_PROTO,this.outPutPath);
        String outDir = generateOutPutDir(code.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_GOOGLE,this.outPutPath);
        String cmd = "protoc -I="+dir+" --java_out="+this.outPutPath+" "+dir+fielName;
        FileUtil.createDir(outDir);
        logger.info(cmd);
        try {
            Process protoc_process = Runtime.getRuntime().exec(cmd);

            InputStream in = protoc_process.getInputStream();
            InputStream err =protoc_process.getErrorStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            BufferedReader err_br = new BufferedReader(new InputStreamReader(err));

            String line = null;
            String error = null;
            boolean hasError = false;
            while ((line=reader.readLine())!=null||((error=err_br.readLine())!=null)){
                if(!StringUtils.isEmpty(line))
                    logger.info(line);
                if(!StringUtils.isEmpty(error)) {
                    logger.info(error);
                    hasError = true;
                }
            }
            int resultCode = protoc_process.waitFor();
            if(resultCode != 0||hasError){
                logger.error("generate protobuf class failed");
                return false;
            }else {
                logger.info("Generate protobuf class success");
                return true;
            }
        } catch (IOException |InterruptedException e) {
            logger.error("Failed to generate protobuf, {}",e);
            e.printStackTrace();
            return false;
        }
    }

    private String decorateClassName(String classname, boolean preffix, boolean suffix){
        String temp;
        temp = preffix==true? PB_CLASS_PREFFIX+StringUtils.capitalize(classname):StringUtils.capitalize(classname);
        temp = suffix==true? temp+PB_CLASS_SUFFIX:temp;
        return temp;
    }

    private String generateOutPutDir(String packageName,String outPutPath){
        String directory = outPutPath;
        if(packageName!=null){
            directory = directory +packageName.replace(".","/")+"/";
        }
        return directory;
    }
}
