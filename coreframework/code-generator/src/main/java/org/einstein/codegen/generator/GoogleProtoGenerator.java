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

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @author kevin
 **/
public class GoogleProtoGenerator implements IGenerator<ICodeTemplete> {
    private static Logger logger = LoggerFactory.getLogger(GoogleProtoGenerator.class);
    private static String OS = System.getProperty("os.name").toLowerCase();
    private static final String USER_DIR = System.getProperty("user.dir");
    private static final String PROTOBUF_VERSION = "proto3";
    private List<ICodeTemplete> codes;
    private Map<String, ICodeTemplete> codesMap = new HashMap<>();
    private String outPutPath;
    private VelocityEngine ve;
    private Template template;
    private static  String PB_CLASS_PREFFIX = "PB";
    private static  String PB_CLASS_SUFFIX = ".proto";
    private static  String GENERATED_PROTO = ".protobuf";
    private static  String GENERATED_ENTITYS = ".entitys";
    private static  String GENERATED_API = ".api";
    private static  String GENERATED_entity = ".entity";

    @Override
    public boolean generate() {
        System.out.println(OS);
        for(ICodeTemplete code:codes){
            //step1 generate proto file
            generateProtoFile((CodeTemplete) code);
            //step2 generate java proto class
            /**
             * user google proto3 tool to generate pb class.
             * current supported java
             */
            generateGoogelProtoBuf();


        }
        return false;
    }

    @Override
    public void init(List<ICodeTemplete> code_templete, String outputdir) {
        this.codes = code_templete;
        this.outPutPath = USER_DIR+"/"+outputdir;
        for(ICodeTemplete code:code_templete){
            codesMap.put(code.getProtoClassName(),code);
        }
    }

    @Override
    public boolean initialize() {
        Properties properties = new Properties();
        properties.setProperty(VelocityEngine.RESOURCE_LOADER, "class");
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        properties.put("input.encoding", "UTF-8");
        properties.put("output.encoding", "UTF-8");
        ve = new VelocityEngine();
        ve.init(properties);
        template = ve.getTemplate("templete/proto/proto.vm", "UTF-8");
        return true;
    }

    private void generateProtoFile(CodeTemplete code){
        Writer out =null;
        try {
            out = FileUtil.createFileWriter(decorateClassName(code.getProtoClassName(),true,true)
                    ,code.getProtoPackageName()+GENERATED_PROTO,this.outPutPath);
            VelocityContext ctx = new VelocityContext();
            ctx.put("version",PROTOBUF_VERSION);
            ctx.put("package",code.getProtoPackageName());
            ctx.put("classname",decorateClassName(code.getProtoClassName(),true,false));
            ctx.put("message",decorateClassName(code.getProtoClassName(),true,false));
            generateProtoImports(ctx,code);
            generateProtoFields(ctx,code);
            this.template.merge(ctx,out);
            FileUtil.flush(out);
        } catch (IOException e) {
            logger.error("failed to create writer, {}",e);
        }finally {
            FileUtil.close(out);
        }
    }


    private void generateProtoImports(VelocityContext ctx, CodeTemplete code){
        List<Field> fields = code.getAllFields();
        List<String> protoImports = new ArrayList<>();
        for(Field field:fields){
            if(!field.getType().isMemberClass())
                continue;
            ICodeTemplete templete = codesMap.get(field.getType().getTypeName());
            if(templete!=null){
                String path = templete.getProtoPackageName();
                path=StringUtils.replace(path,".","/").concat("/")
                        + this.decorateClassName(templete.getProtoClassName(),true,true);
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


    private void  generateGoogelProtoBuf(){
        System.out.println(USER_DIR);

        System.out.println();
    }

    private String decorateClassName(String classname, boolean preffix, boolean suffix){
        String temp;
        temp = preffix==true? PB_CLASS_PREFFIX+StringUtils.capitalize(classname):StringUtils.capitalize(classname);
        temp = suffix==true? temp+PB_CLASS_SUFFIX:temp;
        return temp;
    }
}
