package org.einstein.codegen.protostuff;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @create by xiamicpp
 **/
public class protostuffTest {

    @Test
    public void testSeriliazable(){
        RuntimeSchema<EOrder> orderSchema = RuntimeSchema.createFrom(EOrder.class);
        RuntimeSchema<EChildOrder> childOrderSchema = RuntimeSchema.createFrom(EChildOrder.class);
//
//        EOrder order = EOrder;
//        order.setId("0001");
//        order.setDesk("swap");
//        order.setInstrument("Bond");
//        order.setPortfolio("P00001");
//        order.setPrice(100.1);
//        order.setQty(10000);
//        order.setLegs(Arrays.asList("000001.sz","000002.sz"));
//
//        byte[] orderBytes = ProtobufIOUtil.toByteArray(order,orderSchema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
//
//        System.out.println("Order 序列化前: "+order);
//        System.out.println("Order 序列化后: "+orderBytes+", size: "+orderBytes.length);
//        EOrder newUser = orderSchema.newMessage();
//        ProtobufIOUtil.mergeFrom(orderBytes,newUser,orderSchema);
//        System.out.println("Order 反序列化: "+newUser);


        EChildOrder childOrder = new EChildOrder();
        childOrder.setId("0002");
        childOrder.setDesk("IRS");
        childOrder.setInstrument("BOND");
        childOrder.setPortfolio("P00003");
        childOrder.setPrice(101.1);
        childOrder.setQty(10000);
        childOrder.setLegs(Arrays.asList("100002.sh","102330.sh"));
        childOrder.setCompany("cms");
        childOrder.setCounterPartyID("pingan");
        EOrderField orderField = new EOrderField();
        orderField.setId("zzzz1");
        orderField.setName("xxxxx");
        childOrder.setMember(orderField);

        List<EOrderField> orderFields = new ArrayList<>();

        for(int i=0; i<5;i++){
            EOrderField temp = new EOrderField();
            temp.setName("No"+i);
            temp.setId(String.valueOf(i));
            orderFields.add(temp);
        }

        childOrder.setOrderFields(orderFields);


        byte[] childOrderBytes = ProtobufIOUtil.toByteArray(childOrder,childOrderSchema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));

        System.out.println("ChildOrder 序列化前: "+childOrder);
        System.out.println("ChildOrder 序列化后: "+childOrderBytes+", size: "+childOrderBytes.length);
        EChildOrder newChildOrder = childOrderSchema.newMessage();
        ProtobufIOUtil.mergeFrom(childOrderBytes,newChildOrder,childOrderSchema);
        System.out.println("Order 反序列化: "+newChildOrder);


       // EOrder testOrder = new EOrder();

       // ProtobufIOUtil.mergeFrom(orderBytes,testOrder,orderSchema);

       // System.out.println("Order 反序列化："+testOrder);


    }
}
