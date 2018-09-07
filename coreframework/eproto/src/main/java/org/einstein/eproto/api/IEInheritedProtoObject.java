package org.einstein.eproto.api;

import com.google.protobuf.Descriptors;

/**
 * this protoObject  is inheritance hierarchy
 * @create by xiamicpp
 **/
public interface IEInheritedProtoObject<E extends IEProtoObject<?,?>> {
    E getParent();
    Descriptors.FieldDescriptor getFieldInBuffer();
}
