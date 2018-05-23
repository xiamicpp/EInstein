package org.einstein.proto.implement.test;

import org.einstein.eproto.api.IEProtoObject;

/**
 * @create by xiamicpp
 **/
public interface ITestImmutable extends IEProtoObject<ITest,ITestImmutable> {

    String getID();

    double getPrice();
}
