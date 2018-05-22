package org.einstein.codegen.api;

import java.util.List;

/**
 * @author kevin
 **/
public interface IGenerator<T extends ITemplate> extends IInitialize {

    boolean generate();

    void init(List<T> code_templete,String outputdir);
}
