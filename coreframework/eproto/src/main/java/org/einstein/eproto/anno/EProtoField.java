package org.einstein.eproto.anno;

import java.lang.annotation.*;

/**
 * @create by kevin
 **/
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EProtoField {

    String desc();
}
