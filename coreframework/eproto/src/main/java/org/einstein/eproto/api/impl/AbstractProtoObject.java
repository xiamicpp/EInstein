package org.einstein.eproto.api.impl;

import org.einstein.eproto.api.IEKeyed;
import com.google.protobuf.GeneratedMessageV3;
import org.einstein.eproto.serilizable.POSerializable;

/**
 * @create by xiamicpp
 **/
public abstract class AbstractProtoObject extends POSerializable implements IEKeyed<String> {

    private static final long serialVersionUID = 1L;


    @Override
    public int hashCode() {
        final int _base = 31;
        int result = 1;
        result=(getKey() == null)?0:getKey().hashCode();
        result=_base*result;
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null||getClass()!=obj.getClass())
            return false;
        AbstractProtoObject other = (AbstractProtoObject)obj;
        if(getKey()==null&&other.getKey()!=null)
            return false;
        if(getKey()!=null&& other.getKey()==null)
            return false;
        if(getKey()!=other.getKey())
            return false;
        return true;
    }


}
