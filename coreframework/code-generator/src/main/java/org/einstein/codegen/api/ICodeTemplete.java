package org.einstein.codegen.api;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @create by xiamicpp
 **/
public interface ICodeTemplete extends ITemplete{
    String getProtoClassName();
    String getProtoPackageName();
    List<Field> getAllFields();
    List<Field> getDeclaredFields();
}
