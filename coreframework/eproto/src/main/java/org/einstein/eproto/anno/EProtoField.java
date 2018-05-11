package org.einstein.eproto.anno;

import java.lang.annotation.*;

/**
 * @create by kevin
 **/
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EProtoField {

    String desc() default "";
}
