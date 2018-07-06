package org.einstein.test.proto;

import org.einstein.eproto.anno.EProtoEntity;
import org.einstein.eproto.anno.EProtoField;

import java.util.List;

/**
 * @create by xiamicpp
 **/
@EProtoEntity(id=2, desc = "child order")
public interface ChildOrder extends Order{

    @EProtoField(desc = "counterPartyID")
    String counterPartyID = null;

    @EProtoField(desc = "company")
    String company = null;

    @EProtoField(desc = "fields")
    List<OrderField> orderFields = null;

    @EProtoField(desc = "memberField")
    OrderField member=null;
}
