package org.einstein.test.temp;

import com.google.protobuf.Descriptors;
import org.einstein.test.proto.ChildOrder;
import org.einstein.test.proto.entitys.pb.PBChildOrder;
import org.einstein.test.proto.entitys.pb.PBOrderField;


/**
 * @create by xiamicpp
 **/
public class test {
    public interface A{

    }

    public interface B extends A{

    }

    public class C implements B{

    }


    public static void main(String[] args) {
        PBChildOrder.ChildOrder.Builder builder = PBChildOrder.ChildOrder.newBuilder();
        Descriptors.FieldDescriptor descriptor=PBChildOrder.ChildOrder.getDescriptor().findFieldByName("member");
        System.out.println(descriptor.getName());
        PBOrderField.OrderField.Builder orderFieldBuilder = PBOrderField.OrderField.newBuilder();
        orderFieldBuilder.setId("haha");
        //builder.setMember(orderFieldBuilder);
        builder.setField(descriptor,orderFieldBuilder.build());
       // builder
        System.out.println(builder.build().toString());

        builder.setCompany("hehe");

        System.out.println(builder.build().toString());

        Descriptors.FieldDescriptor orderFieldsDescriptor=PBChildOrder.ChildOrder.getDescriptor().findFieldByName("orderFields");

    }
}



