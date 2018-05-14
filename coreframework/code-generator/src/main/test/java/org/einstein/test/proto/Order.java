package org.einstein.test.proto;

import org.einstein.eproto.anno.*;
import org.einstein.eproto.api.IEVersion;

import java.util.List;

/**
 * @create by kevin
 **/
@EProtoEntity(id = 1, desc = "Base Order")
public interface Order extends IEVersion {

    @EProtoField(desc = "ID")
    String id = null;

    @EProtoField(desc = "desk")
    String desk = null;

    @EProtoField(desc = "portfolio")
    String portfolio = null;

    @EProtoField(desc = "instrument")
    String instrument = null;

    @EProtoField(desc = "qty")
    int qty = 0;

    @EProtoField(desc = "price")
    double price = 0.0;

    @EProtoField(desc = "test list")
    List<String> legs = null;

    short d=1;
}
