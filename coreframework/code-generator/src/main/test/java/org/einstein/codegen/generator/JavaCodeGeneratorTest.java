package org.einstein.codegen.generator;

import org.einstein.codegen.exception.ESynatx;
import org.einstein.codegen.parse.ProtoParser;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.*;

public class JavaCodeGeneratorTest {

    @Test
    public void generateCode() throws ESynatx {
        JavaCodeGenerator generator = new JavaCodeGenerator();
        ProtoParser parser = new ProtoParser();
        File dir = new File("");
        generator.init(parser.parse(dir.getAbsolutePath(),"/src/main/test/java/org/einstein/test/proto/"),"target/generated-src/");
        generator.initialize();
        generator.generate();
    }
}