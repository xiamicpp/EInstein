package org.einstein.codegen.generator;
import org.einstein.codegen.parse.ProtoParser;
import org.junit.Test;

/**
 * Created by xiamicpp on 2018/5/15.
 */
public class GoogleProtoGeneratorTest {

    @Test
    public void generate() throws Exception {
        GoogleProtoGenerator generator = new GoogleProtoGenerator();
        ProtoParser parser = new ProtoParser();
        generator.init(parser.parse("/src/main/test/java/org/einstein/test/proto/"),"target/generated-src/");
        generator.initialize();
        generator.generate();
    }

}