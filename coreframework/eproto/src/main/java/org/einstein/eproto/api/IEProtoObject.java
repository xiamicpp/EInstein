package org.einstein.eproto.api;

import org.einstein.eproto.exception.EProtoException;

/**
 * @create by xiamicpp
 **/
public interface IEProtoObject<MUTABLE_OBJECT extends IEProtoObject<?,?>, IMUTABLE_OBJECT extends IEProtoObject<?,?>> extends IMessage {

    MUTABLE_OBJECT createMutable() throws EProtoException;
    IMUTABLE_OBJECT createImmutable() throws EProtoException;
}
