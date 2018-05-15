package org.einstein.codegen.parse;

import org.einstein.codegen.api.ICodeTemplete;
import org.einstein.codegen.exception.ESynatx;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class ProtoParserTest {

    @Test
    public void parse() throws ESynatx {
        ProtoParser parser = new ProtoParser();

        List<ICodeTemplete> codes= parser.parse("/src/main/test/java/org/einstein/test/proto/");
    }
}