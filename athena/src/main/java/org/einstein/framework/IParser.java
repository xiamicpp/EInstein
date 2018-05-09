package org.einstein.framework;


/**
 * @create by kevin
 **/
public interface IParser<R,I> {
    R parse(I data) throws Exception;
}
