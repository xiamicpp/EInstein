package org.einstein.eproto.api;

import org.einstein.eproto.exception.EProtoException;

/**
 * TODO
 * This factory provide only interface to application to create ProtoEntity.
 * @create by xiamicpp
 * @Param <T>
 **/
public interface IEProtoFactory<I extends IEProtoObject, M extends IEProtoObject, E extends Enum<?>> {

    Class<M> getMutableInterface();

    int getClassId();


    M create() throws EProtoException;



}
