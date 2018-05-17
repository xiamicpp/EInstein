package org.einstein.codegen.generator;

import org.einstein.codegen.api.impl.CodeTemplete;

/**
 * @create by xiamicpp
 **/
public class JavaCodeGenerator extends BaseGenerator {

    @Override
    protected boolean loadVelocityTemplate() {
        return true;
    }

    @Override
    protected boolean generateCode(CodeTemplete code) {
        return false;
    }
}
