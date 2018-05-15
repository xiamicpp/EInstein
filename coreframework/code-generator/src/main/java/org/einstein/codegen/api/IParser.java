package org.einstein.codegen.api;


import org.einstein.codegen.exception.ESynatx;

/**
 * @create by kevin
 **/
public interface IParser<R,I> {
    R parse(I data) throws ESynatx;
}
