package org.einstein.proto.implement.test;

import org.einstein.eproto.exception.EProtoException;

/**
 * @create by xiamicpp
 **/
public class test {

    public static void main(String[] args) throws EProtoException {
        ETest t = new ETest();

        ITestImmutable t1 = t.createImmutable();

    }
}
