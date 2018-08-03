package org.einstein.eproto.api;

import org.einstein.eproto.exception.EProtoException;

import java.io.Serializable;

/**
 * @create by xiamicpp
 **/
public interface ISerializable<T> extends Serializable{

    public byte[] serialize();
    public T deSerialize(byte[] bytes) throws EProtoException;


}
