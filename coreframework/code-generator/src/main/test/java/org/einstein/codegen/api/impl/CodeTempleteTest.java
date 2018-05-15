package org.einstein.codegen.api.impl;

import org.einstein.codegen.exception.ESynatx;
import org.junit.Test;

import static org.junit.Assert.*;

public class CodeTempleteTest {

    @Test
    public void getProto() throws ESynatx {
        CodeTemplete codeTemplete = new CodeTemplete("org.einstein.test.proto","Order");

       // Class t = codeTemplete.getProto();

        //t.getInterfaces();
    }
}