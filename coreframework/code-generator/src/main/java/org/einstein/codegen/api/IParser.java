package org.einstein.codegen.api;


/**
 * @create by kevin
 **/
public interface IParser<R,I> {
    R parse(I data) throws Exception;
}
