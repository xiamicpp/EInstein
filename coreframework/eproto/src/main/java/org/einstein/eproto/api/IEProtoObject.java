package org.einstein.eproto.api;

/**
 * @create by xiamicpp
 **/
public interface IEProtoObject<MUTABLE_OBJECT extends IEProtoObject<?,?>, IMUTABLE_OBJECT extends IEProtoObject<?,?>> extends IMessage {

    MUTABLE_OBJECT createMutable();
    IMUTABLE_OBJECT createImmutable();

    boolean isMutable();
}
