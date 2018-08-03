package org.einstein.codegen.generator;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.einstein.codegen.api.ICodeTemplate;
import org.einstein.codegen.api.IGenerator;
import org.einstein.codegen.api.impl.CodeTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @create by xiamicpp
 **/
public abstract class BaseGenerator implements IGenerator<ICodeTemplate> {
    protected static Logger logger = LoggerFactory.getLogger(BaseGenerator.class);
    protected List<ICodeTemplate> codes;
    protected Map<String, ICodeTemplate> codesMap = new HashMap<>();
    protected String outPutPath;
    protected VelocityEngine ve;
    protected Template template;

    protected static  String PB_CLASS_PREFFIX = "PB";
    protected static  String PB_CLASS_SUFFIX = ".proto";

    protected static String ENCODE = "UTF-8";

    protected static  String GENERATED_PROTO = ".protobuf";
    protected static  String GENERATED_ENTITYS = ".entitys";
    protected static  String GENERATED_API = ".api";
    protected static  String GENERATED_entity = ".entity";
    protected static  String GENERATED_GOOGLE = ".pb";

    @Override
    public boolean generate() {
        boolean result = true;
        logger.info("Total {} code need to generate",codes.size());
        for (ICodeTemplate code : codes) {
            result = generateCode((CodeTemplate) code);
            if (!result) return result;
        }
        return true;
    }

    @Override
    public void init(List<ICodeTemplate> code_templete, String outputdir) {
        this.codes = code_templete;
        this.outPutPath = outputdir;
        for (ICodeTemplate code : code_templete) {
            codesMap.put(code.getProtoClassName(), code);
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
        return loadVelocityTemplate();
    }

    protected abstract boolean loadVelocityTemplate();
    protected abstract boolean generateCode(CodeTemplate code);

    protected String generateOutPutDir(String packageName,String outPutPath){
        String directory = outPutPath;
        if(packageName!=null){
            directory = directory +packageName.replace(".","/")+"/";
        }
        return directory;
    }

    protected String decoratePbClassName(String classname, boolean preffix, boolean suffix){
        String temp;
        temp = preffix==true? PB_CLASS_PREFFIX+ StringUtils.capitalize(classname):StringUtils.capitalize(classname);
        temp = suffix==true? temp+PB_CLASS_SUFFIX:temp;
        return temp;
    }
}
