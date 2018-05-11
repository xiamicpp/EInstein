package org.einstein.codegen.generator;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.einstein.codegen.api.IGenerator;
import org.einstein.codegen.core.ProtoContext;

import java.io.StringWriter;
import java.util.List;
import java.util.Properties;

/**
 * @author kevin
 **/
public class ProtoGenerator implements IGenerator {

    private VelocityEngine ve;

    static final String RESOURCE_HANDLE = "class.resource.loader.class";


    public void init() {
        Properties properties = new Properties();
        String rootPath = ProtoGenerator.class.getResource("/").getPath();
        System.out.println(rootPath);

        ve = new VelocityEngine();
        ve.init(properties);


        VelocityContext ctx = new VelocityContext();
        ProtoContext context = new ProtoContext();
        context.setClassname("Test");
        context.setPakage("com.einstein.test");
        context.setMessage("Test");
        // IField field = new Field("int","id");
        // IField field1 = new Field("boolean","flag");
        //IField field2 = new Field("String","name");
        //context.addField(field);
        //context.addField(field1);
        //context.addField(field2);
        ctx.put("proto", context);
        Template t = ve.getTemplate("templete\\proto\\proto.vm", "UTF-8");
        StringWriter writer = new StringWriter();
        t.merge(ctx, writer);
        System.out.println(writer.toString());
    }


    public static void main(String[] args) {
        ProtoGenerator generator = new ProtoGenerator();

        generator.init();
    }

    public boolean generate() {
        return false;
    }

    @Override
    public void init(List code_templete, String outputdir) {

    }

    public boolean initialize() {
        return false;
    }
}
