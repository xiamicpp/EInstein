package org.einstein.codegen.generator;

import org.einstein.codegen.api.IGenerator;
import org.einstein.codegen.parse.Parser;

import java.util.HashMap;

import static org.junit.Assert.*;

public class JavaCodeGeneratorTest {

    private static HashMap<String,Class<?>> generators = new HashMap<>();
    private String type;
    private String business_object_source;
    private String out_put_dir;

    @org.junit.Before
    public void setUp() throws Exception {
        generators.put("java", JavaCodeGenerator.class);
        this.type = "java";
        this.business_object_source="/home/kevin/Study/EInstein/coreframework/code-generator/src/main/test/java/org/einstein/test/proto/";
        this.out_put_dir="target/generated-codes/";
    }

    @org.junit.After
    public void tearDown() throws Exception {
    }

    @org.junit.Test
    public void generate() {
        try {
            if (type != null) {
                IGenerator generator = (IGenerator) generators.get(type).newInstance();
                Parser parser = new Parser();
                generator.init(parser.parse(this.business_object_source), out_put_dir);

                generator.initialize();
                generator.generate();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}