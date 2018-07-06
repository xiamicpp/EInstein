package org.einstein.eproto.api.impl;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import org.einstein.eproto.api.IEKeyed;
import org.einstein.eproto.api.IEProtoObject;

/**
 * @create by xiamicpp
 **/
public abstract class EPAbstractProtoObject<T extends GeneratedMessageV3> extends EPSerializable<T> implements IEProtoObject,IEKeyed<String> {

    public EPAbstractProtoObject(Message.Builder builder, com.google.protobuf.Parser<T> protobufEntity){
        super(builder,protobufEntity);
    }

    @Override
    public int hashCode() {
        final int _base = 31;
        int result = 1;
        result=(getKey() == null)?0:getKey().hashCode();
        result=_base*result+getClassID();
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null||getClass()!=obj.getClass())
            return false;
        EPAbstractProtoObject other = (EPAbstractProtoObject)obj;
        if(getKey()==null&&other.getKey()!=null)
            return false;
        if(getKey()!=null&& other.getKey()==null)
            return false;
        if(getKey()!=other.getKey())
            return false;
        return true;
    }

    /**
     * getFieldValue by propertyName; Current not supported
     * @param fieldName
     * @return
     */
    @Override
    public Object getProperty(String fieldName) {
        return null;
    }
}
