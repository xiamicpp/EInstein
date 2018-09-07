package org.einstein.codegen.protostuff;


import java.lang.reflect.Field;
import java.util.List;

/**
 * @create by xiamicpp
 **/
public class EChildOrder extends EOrder {

    private String counterPartyId;
    private String company;
    private List<EOrderField> orderFields;
    private EOrderField memeber;



    public void setCounterPartyID(String value) {
        counterPartyId = value;
    }


    public void setCompany(String value) {
        company = value;
    }


    public void setOrderFields(List<EOrderField> value) {
        orderFields = value;
    }

    public void setMember(EOrderField value) {
        memeber = value;
    }

    public String getCounterPartyID() {
        return counterPartyId;
    }

    public String getCompany() {
        return company;
    }

    public List<EOrderField> getOrderFields() {
        return orderFields;
    }

    public EOrderField getMember() {
        return memeber;
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
