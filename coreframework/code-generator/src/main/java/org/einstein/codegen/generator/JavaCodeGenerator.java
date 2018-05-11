package org.einstein.codegen.generator;


import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.einstein.codegen.api.IField;
import org.einstein.codegen.api.IGenerator;
import org.einstein.codegen.api.impl.ProtoEntityTemplete;
import org.einstein.codegen.util.FileUtil;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * @create by kevin
 **/
public class JavaCodeGenerator implements IGenerator<ProtoEntityTemplete> {
    private ProtoEntityTemplete m_proto;
    private List<ProtoEntityTemplete> m_all_proto;
    private Map<String, ProtoEntityTemplete> m_proto_map = new HashMap<>();
    private String m_immutableInterfaceName;
    private String m_interfaceName;
    private String m_className;
    private String m_immutableClassName;
    private String m_outPutDir;
    private VelocityEngine ve;

    private static final String FILE_PREFIX = "IE";
    private static final String[] FILE_SUFFIX = new String[]{"Immutable","Immutable_EP","","_EP"};


    private JavaCodeGenerator() {

    }

    public JavaCodeGenerator(List<ProtoEntityTemplete> allProtos, String outputdir) {
        this.init(allProtos, outputdir);
    }

    @Override
    public boolean generate() {
        for (ProtoEntityTemplete templete : m_all_proto) {
            this.m_proto = templete;
            this.m_immutableInterfaceName  = this.buildfileName(this.m_proto.getM_name(),true,true);
            generateImmutableInterface();
        }

        return true;
    }

    @Override
    public void init(List<ProtoEntityTemplete> code_templete, String outputdir) {
        this.m_all_proto = code_templete;
        this.m_outPutDir = outputdir;
        Iterator<ProtoEntityTemplete> it = m_all_proto.iterator();
        while (it.hasNext()) {
            ProtoEntityTemplete templete = it.next();
            this.m_proto_map.put(templete.getM_name(), templete);
        }
    }

    @Override
    public boolean initialize() {
        ve = new VelocityEngine();
        Properties properties = new Properties();
        properties.setProperty(VelocityEngine.RESOURCE_LOADER, "class");
        properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        properties.put("input.encoding", "UTF-8");
        properties.put("output.encoding", "UTF-8");
        ve.init(properties);
        return true;
    }


    public void generateImmutableInterface() {
        try {
            Writer out = FileUtil.createFile(this.m_immutableInterfaceName,m_proto.getPackageName(),m_outPutDir);
            Template t = ve.getTemplate("templete\\entity\\java\\interface.vm", "UTF-8");
            VelocityContext ctx = new VelocityContext();
            this.initPackage(ctx);
            this.intiImports(ctx,true,true);
            ctx.put("entityName",this.m_proto.getM_name());
            t.merge(ctx,out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void initPackage(VelocityContext ctx){
        ctx.put("packageName",this.m_proto.getPackageName());
    }

    private void intiImports(VelocityContext ctx,boolean isInterface, boolean isIMutable){
        Set<String> importNames = new HashSet<>();
        List<String> allImports = new ArrayList<>();
        if(isInterface){
            List<String> extendNames= this.m_proto.getM_extends();
            for(String superclassName:extendNames){
                ProtoEntityTemplete superClass = this.m_proto_map.get(superclassName);
                if(superClass!=null){
                    String superPackage =superClass.getM_packageName();
                    String newImports = this.buildfileName(superClass.getM_name(),true,true);
                    importNames.add(newImports);
                    allImports.add(superPackage+"."+newImports);
                }
            }
        }else{
            //TODO
        }

       List<IField> propertys= this.m_proto.getM_fields();
        for(IField field:propertys){

        }
    }

    private void initInterfaces(VelocityContext ctx){
        List<String> extends_=this.m_proto.getM_extends();
        if(extends_.size()>0){
            ctx.put("hasInterfaces",true);
            for(String superClass:extends_){

            }
        }
    }


    private String buildfileName(String name,boolean isInterface, boolean isIMutable){
        int index=(isInterface?0:1) +(isIMutable?0:2);
        return (isInterface?FILE_PREFIX+name:name)+FILE_SUFFIX[index];
    }

}
