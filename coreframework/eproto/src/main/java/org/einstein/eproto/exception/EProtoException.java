package org.einstein.eproto.exception;

/**
 * @create by xiamicpp
 **/
public class EProtoException extends Exception{
    private static final String DEFAULT_ERROR_MSG = "EProto Error";

    private ERROR_TYPE type = ERROR_TYPE.UNKNOWN;

    public enum ERROR_TYPE{
        UNKNOWN, CLONEFAILED,SERILIZEFAILED;
    }

    public EProtoException(Throwable e){
        this(ERROR_TYPE.UNKNOWN,null,e);
    }

    public EProtoException(String msg){
        this(ERROR_TYPE.UNKNOWN,msg,null);
    }

    public EProtoException(ERROR_TYPE type){
        this(type,DEFAULT_ERROR_MSG,null);
    }

    public EProtoException(ERROR_TYPE type,Throwable e){
        this(type,null,e);
    }

    public EProtoException(ERROR_TYPE type,String msg){
        this(type,msg,null);
    }

    public EProtoException(ERROR_TYPE type, String msg, Throwable e){
        super(msg,e);
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("EProtoException:{").append("Type:")
                .append(this.type).append(",super:").append(super.toString())
                .append("}");
        return sb.toString();
    }
}
