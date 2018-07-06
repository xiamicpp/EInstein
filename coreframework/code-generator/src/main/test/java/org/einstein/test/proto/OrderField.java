package org.einstein.test.proto;

import org.einstein.eproto.anno.EProtoEntity;
import org.einstein.eproto.anno.EProtoField;

/**
 * @create by xiamicpp
 **/
@EProtoEntity(id=3, desc = "order field")
public interface OrderField {
    @EProtoField(desc = "id")
    String id = null;

    @EProtoField(desc = "name")
    String name = null;
}
