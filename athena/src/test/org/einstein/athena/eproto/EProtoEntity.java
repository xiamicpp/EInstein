package org.einstein.athena.eproto;

import java.lang.annotation.*;

/**
 * @create by kevin
 **/

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EProtoEntity {

    int id() default -1;

    String desc() default "";

    boolean persistable() default false;
}
