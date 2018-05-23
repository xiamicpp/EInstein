package org.einstein.test.proto;

import org.einstein.eproto.anno.EProtoEntity;
import org.einstein.eproto.anno.EProtoField;

/**
 * @create by xiamicpp
 **/
@EProtoEntity(id=2, desc = "child order")
public interface ChildOrder extends Order{

    @EProtoField(desc = "counterPartyID")
    String counterPartyID = null;

    @EProtoField(desc = "company")
    String company = null;

}
