package org.einstein.codegen.generator;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.einstein.codegen.api.ICodeTemplete;
import org.einstein.codegen.api.IGenerator;
import org.einstein.codegen.api.impl.CodeTemplete;
import org.einstein.codegen.core.ProtoContext;
import org.einstein.codegen.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Field;
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

    @Override
    public boolean generate() {
        for(ICodeTemplete code:codes){
            //step1 generate proto file
            generateProtoFile((CodeTemplete) code);
            //step2 generate java proto class

        }
        return false;
    }

    @Override
    public void init(List<ICodeTemplete> code_templete, String outputdir) {
        this.codes = code_templete;
        this.outPutPath = USER_DIR+"/"+outputdir;
        code_templete.forEach(code->{
            codesMap.put(code.getProtoClassName(),code);
        });
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
                    ,code.getProtoPackageName(),this.outPutPath);
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
        Field[] fields = code.getAllFields();
        List<String> protoImports = new ArrayList<>();
        for(Field field:fields){
            if(!field.getType().isMemberClass())
                continue;
            String path = code.getProtoPackageName();
            path=StringUtils.replace(path,".","/").concat("/")
                    + this.decorateClassName(code.getProtoClassName(),true,true);
            protoImports.add(path);
        }
        ctx.put("imports",protoImports);
    }

    private void generateProtoFields(VelocityContext ctx,CodeTemplete code){
        //TODO
    }

    private String decorateClassName(String classname, boolean preffix, boolean suffix){
        String temp;
        temp = preffix==true? PB_CLASS_PREFFIX+StringUtils.capitalize(classname):StringUtils.capitalize(classname);
        temp = suffix==true? temp+PB_CLASS_SUFFIX:temp;
        return temp;
    }
}
