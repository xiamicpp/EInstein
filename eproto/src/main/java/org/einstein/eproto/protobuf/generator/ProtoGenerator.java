package org.einstein.eproto.protobuf.generator;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.einstein.eproto.core.Field;
import org.einstein.eproto.core.IField;
import org.einstein.eproto.core.IGenerator;
import org.einstein.eproto.protobuf.ProtoContext;

import java.io.StringWriter;
import java.util.Properties;

/**
 * @author kevin
 **/
public class ProtoGenerator implements IGenerator{

    private VelocityEngine ve;

    static final String RESOURCE_HANDLE = "class.resource.loader.class";



    public void init(){
        Properties properties = new Properties();
        //properties.setProperty(RuntimeConstants.RESOURCE_LOADER,"class");
        //properties.setProperty("class.resource.loader.class","org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        properties.put("input.encoding", "UTF-8");
        properties.put("output.encoding", "UTF-8");
        ve = new VelocityEngine(properties);
        ve.init();

        VelocityContext ctx = new VelocityContext();
        ProtoContext context = new ProtoContext();
        context.setClassname("Test");
        context.setPakage("com.einstein.test");
        context.setMessage("Test");
        IField field = new Field("int","id");
        IField field1 = new Field("bool","flag");
        IField field2 = new Field("string","name");
        context.addField(field);
        context.addField(field1);
        context.addField(field2);
        ctx.put("proto",context);
       // Template t = ve.getTemplate("Study/EInstein/eproto/src/main/java/org/einstein/eproto/protobuf/generator/proto.vm","UTF-8");
        StringWriter writer = new StringWriter();
        t.merge(ctx,writer);
        System.out.println(writer.toString());
    }


    public static void main(String[] args) {
        ProtoGenerator generator = new ProtoGenerator();

        generator.init();
    }
}
