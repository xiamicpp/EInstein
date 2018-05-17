package org.einstein.message.eproto;

import org.einstein.eproto.anno.EProtoEntity;
import org.einstein.eproto.anno.EProtoField;

/**
 * @create by xiamicpp
 **/
@EProtoEntity(id = 1,desc = "Query entity")
public interface QueryBO {
    @EProtoField(desc = "ID")
    String id = null;

    @EProtoField(desc = "destination")
    String destination = null;

    @EProtoField(desc = "sequence number")
    int number = 0;
}
