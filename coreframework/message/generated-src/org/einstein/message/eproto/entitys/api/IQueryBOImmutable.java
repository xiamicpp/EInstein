package org.einstein.message.eproto.entitys.api;


/**
 * =========================================================
 * +              Auot generated by ethena                 +
 * =========================================================
 *
 * @author Einstein EProto Object Generator(ethena ^_^)
 * 2018-05-22 18:35:22
 **/
public interface IQueryBOImmutable extends org.einstein.eproto.api.IEProtoObject<org.einstein.message.eproto.entitys.api.IQueryBO,org.einstein.message.eproto.entitys.api.IQueryBOImmutable>,org.einstein.eproto.api.ISerializable {
    static final int PROTO_CLASS_ID = 1;

    
    java.lang.String getId();
    
    java.lang.String getDestination();
    
    int getNumber();
}