package org.einstein.codegen.generator;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.VelocityContext;
import org.einstein.codegen.api.ICodeTemplate;
import org.einstein.codegen.api.impl.CodeTemplate;
import org.einstein.codegen.exception.ESynatx;
import org.einstein.codegen.util.FileUtil;

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

    private static String protobuf_template = "template/proto/proto.vm";

    @Override
    protected boolean loadVelocityTemplate() {
        template = ve.getTemplate(protobuf_template, ENCODE);
        return true;
    }

    @Override
    public boolean generate() {
        boolean result= super.generate();
        if(result == false){
            logger.error("GoogleProtobuf generate failed");
            return false;
        }
        try {
            for (ICodeTemplate code : codes) {
                generateGoogelProtoBuf((CodeTemplate) code);
            }
        }catch (ESynatx e){
            logger.error("Google protobuf generate failed, {}",e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    protected boolean generateCode(CodeTemplate code) {
        try {
            generateProtoFile(code);
        }catch (ESynatx e){
            logger.error("Generate google proto failed!, caused by:{}",e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean generateProtoFile(CodeTemplate code) throws ESynatx {
        Writer out =null;
        try {
            String fielName = decoratePbClassName(code.getProtoClassName(),false,true);
            String dir = generateOutPutDir(code.getProtoPackageName()+GENERATED_PROTO,this.outPutPath);
            out = FileUtil.createFileWriter(fielName, dir);
            VelocityContext ctx = new VelocityContext();
            ctx.put("version",PROTOBUF_VERSION);
            ctx.put("package",code.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_GOOGLE);
            ctx.put("classname",decoratePbClassName(code.getProtoClassName(),true,false));
            ctx.put("message",decoratePbClassName(code.getProtoClassName(),false,false));
            generateProtoImports(ctx,code);
            generateProtoFields(ctx,code);
            this.template.merge(ctx,out);
            FileUtil.flush(out);
        } catch (IOException e) {
            throw new ESynatx("create writer failed",e);
        }finally {
            FileUtil.close(out);
        }
        return true;
    }


    private void generateProtoImports(VelocityContext ctx, CodeTemplate code) throws ESynatx {
        List<Field> fields = code.getDeclaredFields();
        Set<String> protoImports = new HashSet<>();

        Class<?>[] interfaces=code.getInterfaces();
        int protoSupperCount = 0;
        for(Class<?> supper:interfaces){
            ICodeTemplate supperCode = codesMap.get(supper.getSimpleName());
            if(supperCode!=null){
                protoSupperCount++;
                String path = supperCode.getProtoPackageName()+GENERATED_PROTO;
                path = StringUtils.replace(path,".","/").concat("/")
                        + this.decoratePbClassName(supperCode.getProtoClassName(),false,true);
                protoImports.add(path);
                ctx.put("hasSupper",true);
                ctx.put("supperType",decoratePbClassName(supperCode.getProtoClassName(),false,false));
            }
        }
        if(protoSupperCount>1){
            throw new ESynatx("proto extends multiple proto!, extends: "+protoImports.toString());
        }

        for(Field field:fields){
            String __simpleName = field.getType().getSimpleName();
            if(field.getType().isAssignableFrom(List.class)){
                Type genericType=field.getGenericType();
                if(genericType == null) continue;
                if(genericType instanceof ParameterizedType){
                    Class<?> supper = (Class<?>) ((ParameterizedType)genericType).getActualTypeArguments()[0];
                    __simpleName = supper.getSimpleName();
                }
            }
            ICodeTemplate templete = codesMap.get(__simpleName);
            if(templete!=null){
                String path = templete.getProtoPackageName()+GENERATED_PROTO;
                path=StringUtils.replace(path,".","/").concat("/")
                        + this.decoratePbClassName(templete.getProtoClassName(),false,true);
                protoImports.add(path);
            }
        }

        ctx.put("imports",protoImports);
    }

    private void generateProtoFields(VelocityContext ctx,CodeTemplate code){
        List<Field> fields = code.getDeclaredFields();
        List<org.einstein.codegen.api.impl.Field> convertFields = new ArrayList<>(fields.size());
        try {
            for (Field field : fields) {
                String __simpleName = field.getType().getSimpleName();
                ICodeTemplate templete = codesMap.get(__simpleName);
                if (templete !=null) {
                    org.einstein.codegen.api.impl.Field convertField = new org.einstein.codegen.api.impl.Field();
                    convertField.setType(decoratePbClassName(field.getType().getSimpleName(),false,false));
                    convertField.setName(field.getName());
                    convertField.setDefaultValue(field.get(null));
                    convertField.setEProtoObject(true);
                    convertFields.add(convertField);
                }else if(field.getType().isEnum()){
                    org.einstein.codegen.api.impl.Field convertField = new org.einstein.codegen.api.impl.Field();
                    convertField.setEnum(true);
                    convertField.setType(__simpleName);
                    convertField.setName(field.getName());
                    List<org.einstein.codegen.api.IField> items = new ArrayList<>();
                    for(Field subField: field.getType().getDeclaredFields()){
                        if(subField.isEnumConstant()){
                            org.einstein.codegen.api.impl.Field enumField = new org.einstein.codegen.api.impl.Field();
                            enumField.setName(subField.getName());
                            enumField.setType(__simpleName);
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
                        templete = codesMap.get(supper.getSimpleName());
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


    private boolean  generateGoogelProtoBuf(CodeTemplate code) throws ESynatx {
        String fielName = decoratePbClassName(code.getProtoClassName(),false,true);
        String dir = this.outPutPath;
                //generateOutPutDir(code.getProtoPackageName()+GENERATED_PROTO,this.outPutPath);
        String outDir = generateOutPutDir(code.getProtoPackageName()+GENERATED_ENTITYS+GENERATED_GOOGLE,this.outPutPath);
        String cmd = "protoc -I="+dir+" --java_out="+this.outPutPath+" "+generateOutPutDir(code.getProtoPackageName()+GENERATED_PROTO,this.outPutPath)+fielName;
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
                throw  new ESynatx("generate protobuf class failed");
            }else {
                logger.info("Generate protobuf class success");
                return true;
            }
        } catch (IOException |InterruptedException e) {
            throw  new ESynatx("Generate protobuf class failed, causes:"+e.getMessage(),e);
        }
    }




}
