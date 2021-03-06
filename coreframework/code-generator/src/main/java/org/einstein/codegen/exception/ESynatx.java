package org.einstein.codegen.exception;

/**
 * @create by kevin
 **/
public class ESynatx extends Exception{
    private static final String DEFAULT_ERROR_MSG = "Syntax Error";

    public ESynatx(String msg, String name){
        super(DEFAULT_ERROR_MSG);
    }

    public ESynatx(String msg){
        super(msg);
    }

    public ESynatx(String msg,Throwable e){
        super(msg,e);
    }

    public ESynatx(Throwable e){
        super(DEFAULT_ERROR_MSG,e);
    }
}
