package org.einstein.codegen.generator;

import org.apache.velocity.Template;
import org.apache.velocity.app.VelocityEngine;
import org.einstein.codegen.api.ICodeTemplete;
import org.einstein.codegen.api.IGenerator;
import org.einstein.codegen.api.impl.CodeTemplete;
import org.einstein.codegen.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @create by xiamicpp
 **/
public abstract class BaseGenerator implements IGenerator<ICodeTemplete> {
    protected static Logger logger = LoggerFactory.getLogger(BaseGenerator.class);
    protected List<ICodeTemplete> codes;
    protected Map<String, ICodeTemplete> codesMap = new HashMap<>();
    protected String outPutPath;
    protected VelocityEngine ve;
    protected Template template;

    protected static  String GENERATED_PROTO = ".protobuf";
    protected static  String GENERATED_ENTITYS = ".entitys";
    protected static  String GENERATED_API = ".api";
    protected static  String GENERATED_entity = ".entity";
    protected static  String GENERATED_GOOGLE = ".pb";

    @Override
    public boolean generate() {
        boolean result = true;
        logger.info("Total {} code need to generate",codes.size());
        for (ICodeTemplete code : codes) {
            result = generateCode((CodeTemplete) code);
            if (!result) return result;
        }
        return true;
    }

    @Override
    public void init(List<ICodeTemplete> code_templete, String outputdir) {
        this.codes = code_templete;
        this.outPutPath = FileUtil.USER_DIR + "/" + outputdir;
        for (ICodeTemplete code : code_templete) {
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
    protected abstract boolean generateCode(CodeTemplete code);
}
