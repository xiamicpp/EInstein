package org.einstein.codegen.protostuff;

import org.einstein.test.proto.entitys.api.IOrder;
import org.einstein.test.proto.entitys.api.IOrderImmutable;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @create by xiamicpp
 **/
public class EOrder implements IOrder{
    private String id;
    private String desk;
    private String portfolio;
    private String instrument;
    private int qty;
    private double price;
    private List<String> legs;



    @Override
    public void setId(String value) {
        this.id = value;
    }

    @Override
    public void setDesk(String value) {
        desk = value;
    }

    @Override
    public void setPortfolio(String value) {
        portfolio = value;
    }

    @Override
    public void setInstrument(String value) {
        instrument = value;
    }

    @Override
    public void setQty(int value) {
        qty = value;
    }

    @Override
    public void setPrice(double value) {
        price = value;
    }

    @Override
    public void setLegs(List<String> value) {
        legs = value;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getDesk() {
        return desk;
    }

    @Override
    public String getPortfolio() {
        return portfolio;
    }

    @Override
    public String getInstrument() {
        return instrument;
    }

    @Override
    public int getQty() {
        return qty;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public List<String> getLegs() {
        return legs;
    }


    @Override
    public String toString() {
        Field []  allfields= this.getClass().getDeclaredFields();
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

    @Override
    public IOrderImmutable createImmutable() {
        return null;
    }

    @Override
    public IOrder createMutable() {
        return null;
    }

    @Override
    public boolean isMutable() {
        return false;
    }
}
