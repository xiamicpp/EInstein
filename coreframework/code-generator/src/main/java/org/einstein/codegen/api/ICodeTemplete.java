package org.einstein.codegen.api;

import java.lang.reflect.Field;

/**
 * @create by xiamicpp
 **/
public interface ICodeTemplete extends ITemplete{
    String getProtoClassName();
    String getProtoPackageName();
    Field[] getAllFields();
}
