package org.einstein.eproto.api.impl;

import com.google.protobuf.Descriptors;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import org.einstein.eproto.api.ISerializable;
import org.einstein.eproto.exception.EProtoException;

/**
 * @create by xiamicpp
 **/
public abstract class EPSerializable<T extends com.google.protobuf.GeneratedMessageV3> implements ISerializable{
    private Message.Builder builder;
    private com.google.protobuf.Parser<T> parser;

    public EPSerializable(Message.Builder builder, com.google.protobuf.Parser<T> parser){
        this.builder = builder;
        this.parser = parser;
    }

    protected void setPBData(Descriptors.FieldDescriptor descriptor,Object obj){
        builder.setField(descriptor,obj);
    }

    public byte[] serialize(){
        return this.builder.build().toByteArray();
    }

    public T deSerialize(byte[] bytes) throws EProtoException {
        try {
            return parser.parseFrom(bytes);
        } catch (InvalidProtocolBufferException e) {
            throw new EProtoException(EProtoException.ERROR_TYPE.SERILIZEFAILED,"DeSerialize bytes array failed!, cased by: "+e.getMessage(),e);
        }
    }
}
