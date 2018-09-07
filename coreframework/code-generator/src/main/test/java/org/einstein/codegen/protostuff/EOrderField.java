package org.einstein.codegen.protostuff;

import org.einstein.test.proto.entitys.api.IOrderField;

import java.lang.reflect.Field;

/**
 * @create by xiamicpp
 **/
public class EOrderField implements IOrderField{
    private String id;
    private String name;


    @Override
    public void setId(String value) {
        id = value;
    }

    @Override
    public void setName(String value) {
        name = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        Field[]  allfields= this.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        sb.append("EOrder:{");
        try {
            for(Field field:allfields){

                sb.append(field.getName()).append(" = ").append(field.get(this)).append(",");
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        sb.append("}");
        return sb.toString();
    }
}
