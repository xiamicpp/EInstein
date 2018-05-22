package org.einstein.codegen.api;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @create by xiamicpp
 **/
public interface ICodeTemplate extends ITemplate {
    String getProtoClassName();
    String getProtoPackageName();
    int getProtoClassID();
    List<Field> getAllFields();
    List<Field> getDeclaredFields();
}
