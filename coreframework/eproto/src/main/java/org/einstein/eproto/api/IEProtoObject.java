package org.einstein.eproto.api;


/**
 * Main Entity Object Interface
 * @param <IMMUTABLE>
 *     Immutable interface
 * @param <MUTABLE>
 *     mutable interface
 * @create by xiamicpp
 **/
public interface IEProtoObject<IMMUTABLE extends IEProtoObject<?,?>, MUTABLE extends IEProtoObject<?,?>> extends IMessage {

    /**
     *Create a copy of the current object in immutable mode
     * @return immutable object
     */
    IMMUTABLE createImmutable();

    /**
     * Create a copy of the current object in mutable mode
     * @return mutable object
     */
    MUTABLE createMutable();


    /**
     * Check current object is mutable or not
     * @return
     */
    boolean isMutable();
}
