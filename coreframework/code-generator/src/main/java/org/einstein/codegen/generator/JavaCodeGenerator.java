package org.einstein.codegen.generator;

import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.einstein.codegen.api.IField;
import org.einstein.codegen.api.IGenerator;
import org.einstein.codegen.api.impl.ProtoEntityTemplete;
import org.einstein.codegen.api.impl.WrapperType;
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


    public JavaCodeGenerator() {

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
        Writer out = null;
        try {
            out = FileUtil.createFile(this.m_immutableInterfaceName,m_proto.getPackageName(),m_outPutDir);
            Template t = ve.getTemplate("templete/entity/java/interface.vm", "UTF-8");
            VelocityContext ctx = new VelocityContext();
            this.initPackage(ctx);
            this.intiImports(ctx,true,true);
            ctx.put("entityName",this.m_immutableInterfaceName);
            this.initExtends(ctx,true,true);
            ctx.put("isMutable",false);
            ctx.put("PROTO_CLASS_ID",this.m_proto.getM_id());
            this.initFields(ctx,true,true);
            FileUtil.flush(out);
            t.merge(ctx,out);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            FileUtil.close(out);
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
                    String newImports = this.buildfileName(superClass.getM_name(),isInterface,isIMutable);
                    importNames.add(newImports);
                    allImports.add(superPackage+"."+newImports);
                }
            }
        }else{
            //TODO
        }

       List<IField> propertys= this.m_proto.getM_fields();
        for(IField field:propertys){
            if(!field.isReserverdType()){
                String typeName = field.getFieldRawType();
                String wrapperName = this.buildfileName(typeName,isInterface,isIMutable);
                if(!importNames.contains(wrapperName)){
                    ProtoEntityTemplete templete = m_proto_map.get(typeName);
                    if(templete!=null){
                        String packageName = templete.getPackageName();
                        importNames.add(wrapperName);
                        allImports.add(packageName+"."+wrapperName);
                    }
                }
            }
        }
        ctx.put("imports",allImports);
    }

    private void initExtends(VelocityContext ctx,boolean isInterface,boolean isImutable){
        List<String> extends_=this.m_proto.getM_extends();
        List<String> allExtends = new ArrayList<>();
        if(extends_.size()>0){
            ctx.put("hasInterfaces",true);
            for(String superClassName:extends_){
                String wrapperName = this.buildfileName(superClassName,isInterface,isImutable);
                ProtoEntityTemplete superClass = this.m_proto_map.get(superClassName);
                if(superClass!=null){
                    allExtends.add(wrapperName);
                }else
                    allExtends.add(superClassName);
            }
            StringBuilder superInterfaces= new StringBuilder();
            allExtends.forEach(t->{
                superInterfaces.append(",").append(t);
            });
            superInterfaces.replace(0,1,"");
            ctx.put("interfaces",superInterfaces.toString());
        }else
            ctx.put("hasInterfaces",false);
    }

    private void initFields(VelocityContext ctx,boolean isInterface,boolean isImutable){
        List<IField> fields =this.m_proto.getM_fields();
        if(isImutable){
            ctx.put("getterMethod",true);
            ctx.put("setterMethod",false);
            for(IField field:fields){
                WrapperType wrapperType = new WrapperType();
                wrapperType.setName(field.getFieldRawType());
                String typeName = field.getFieldRawType();
                String wrapperName = this.buildfileName(typeName,isInterface,isImutable);
                ProtoEntityTemplete templete = m_proto_map.get(typeName);
                if(templete!=null){
                    wrapperType.setFullName(templete.getPackageName()+"."+wrapperName);
                    wrapperType.setSimpleName(wrapperName);
                }else {
                    wrapperType.setFullName(typeName);
                    wrapperType.setSimpleName(typeName);
                }
                wrapperType.setGetterMethodName("get"+ StringUtils.capitalize(field.getFieldName()));
                field.setWrapperType(wrapperType);
            }
        }else {
            ctx.put("getterMethod", false);
            ctx.put("setterMethod",true);
            //Todo
        }
        ctx.put("fields",fields);
    }

    private String buildfileName(String name,boolean isInterface, boolean isIMutable){
        int index=(isInterface?0:1) +(isIMutable?0:2);
        return (isInterface?FILE_PREFIX+name:name)+FILE_SUFFIX[index];
    }

}
