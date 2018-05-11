package org.einstein.codegen.generator;

import org.einstein.framework.IGenerator;
import org.einstein.framework.impl.ProtoEntityTemplete;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @create by kevin
 **/
public class JavaCodeGenerator implements IGenerator{
    private ProtoEntityTemplete m_proto;
    private List<ProtoEntityTemplete> m_all_proto;
    private Map<String,ProtoEntityTemplete> m_proto_map = new HashMap<>();
    private String m_immutableInterfaceName;
    private String m_interfaceName;
    private String m_className;
    private String m_immutableClassName;
    private String m_outPutDir;


    public JavaCodeGenerator(List<ProtoEntityTemplete> allProtos){
        this.m_all_proto = allProtos;

        Iterator<ProtoEntityTemplete> it = m_all_proto.iterator();
        while (it.hasNext()){
            ProtoEntityTemplete templete=it.next();
            this.m_proto_map.put(templete.getM_name(),templete);
        }

    }

    @Override
    public boolean generate() {
        for (ProtoEntityTemplete templete :m_all_proto){
            this.m_proto = templete;

        }

        return true;
    }

    @Override
    public boolean initialize() {
        return false;
    }


    public void generateImmutableInterface(){
       // Writer out = FileUtil.createFile(this.m_immutableInterfaceName,m_proto.getPackageName(),m_outPutDir);
    }


}
