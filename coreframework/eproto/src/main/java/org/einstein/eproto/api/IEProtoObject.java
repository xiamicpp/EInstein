package org.einstein.eproto.api;

import org.einstein.eproto.exception.EProtoException;

/**
 * @create by xiamicpp
 **/
public interface IEProtoObject extends IMessage {
      Object getProperty(String fieldName);
      int getClassID();
}
