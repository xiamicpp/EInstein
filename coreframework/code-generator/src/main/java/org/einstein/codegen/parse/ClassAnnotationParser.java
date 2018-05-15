package org.einstein.codegen.parse;

import org.apache.commons.lang3.StringUtils;
import org.einstein.codegen.api.IParser;
import org.einstein.codegen.exception.ESynatx;

/**
 * @create by kevin
 **/
public class ClassAnnotationParser implements IParser<ClassAnnotationParser.ClassAnnotation,String> {

    @Override
    public ClassAnnotationParser.ClassAnnotation parse(String data) throws ESynatx {
        String rawdata=StringUtils.substringBetween(data,"@EProtoEntity(",")");
        String[] fields = StringUtils.split(rawdata,",");
        ClassAnnotation annotation = new ClassAnnotation();
        for(String field:fields){
            if(validate("id",field)){
                annotation.classId = Integer.valueOf(StringUtils.trim(StringUtils.substringAfter(field,"=")));
            }else if(validate("desc",field)){
                annotation.desc = StringUtils.substringBetween(StringUtils.substringAfter(field,"="),"'","'");
            }
        }
        return annotation;
    }


    private boolean validate(String mode,String str){
        return StringUtils.equals(mode,StringUtils.trim(StringUtils.substringBefore(str,"=")));
    }

    public class ClassAnnotation{
        public int classId;
        public String desc;
    }
}
