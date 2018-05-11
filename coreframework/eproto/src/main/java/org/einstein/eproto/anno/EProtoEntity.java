package org.einstein.eproto.anno;

import java.lang.annotation.*;

/**
 * @create by kevin
 **/

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EProtoEntity {

    int id();

    String desc() default "";

    boolean persistable() default false;
}
