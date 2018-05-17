package org.einstein.codegen.api;


import org.einstein.codegen.exception.ESynatx;

/**
 * @create by kevin
 **/
public interface IParser<R,I,D> {
    R parse(I dir, D data) throws ESynatx;
}
