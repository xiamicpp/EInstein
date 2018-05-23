package org.einstein.codegen.generator;
import org.einstein.codegen.parse.ProtoParser;
import org.einstein.test.proto.Order;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertEquals;

/**
 * Created by xiamicpp on 2018/5/15.
 */
public class GoogleProtoGeneratorTest {

    @Test
    public void generate() throws Exception {
        GoogleProtoGenerator generator = new GoogleProtoGenerator();
        ProtoParser parser = new ProtoParser();
        File dir = new File("");
        generator.init(parser.parse(dir.getAbsolutePath(),"/src/main/test/java/org/einstein/test/proto/"),dir.getAbsolutePath()+"/target/generated-src/");
        generator.initialize();
        assertEquals(true,generator.generate());
    }

}