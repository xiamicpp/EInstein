package org.einstein.codegen.protostuff;

import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessageV3;
import org.einstein.test.proto.entitys.pb.PBOrder;
import org.junit.Test;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

/**
 * @create by xiamicpp
 **/
public class testDescriptor {


    @Test
    public void testPBBuilderFieldSet(){

        List<Descriptors.FieldDescriptor> fieldDescriptors = PBOrder.Order.getDescriptor().getFields();

        Map<String,Descriptors.FieldDescriptor> fieldMap= new HashMap<String,Descriptors.FieldDescriptor>();
        for(Descriptors.FieldDescriptor fieldDescriptor:fieldDescriptors){
            fieldMap.put(fieldDescriptor.getName(),fieldDescriptor);
        }

        GeneratedMessageV3.Builder builder= PBOrder.Order.newBuilder();
        long start = System.nanoTime();
        for(int i=0; i<10000;i++){
            builder.setField(fieldMap.get("id"),"00001");
        }
        long end = System.nanoTime();

        System.out.println("set by Descriptor: " +String.valueOf((end-start)/1000000.0)+" millis");

    }


    @Test
    public void testPBBuilderSet(){
        PBOrder.Order.Builder buffer = PBOrder.Order.newBuilder();
        long start = System.nanoTime();
        for(int i=0; i<10000;i++){
            buffer.setId("00001");
        }
        long end = System.nanoTime();

        System.out.println("set by set method: " +String.valueOf((end-start)/1000000.0)+" millis");
    }

}
