package org.einstein.codegen.parse;

import org.apache.commons.lang3.StringUtils;
import org.einstein.codegen.api.IParser;
import org.einstein.codegen.exception.ESynatx;

import java.util.ArrayList;
import java.util.List;

/**
 * @create by kevin
 **/
public class ClassHeaderParser implements IParser<ClassHeaderParser.ProtoHeader,String> {

    @Override
    public ProtoHeader parse(String data) throws ESynatx {
        ProtoHeader header = new ProtoHeader();
        String str = StringUtils.removeEnd(data,"{").trim();
        header.class_name=StringUtils.trim(StringUtils.substringBetween(str,"interface","extends"));
        String exts=StringUtils.substringAfter(str,"extends").trim();
        String[] fields = StringUtils.split(exts,",");
        for (String field:fields){
            header.extends_.add(StringUtils.trim(field));
        }
        return header;
    }

    public class ProtoHeader{
        public String class_name;
        public List<String> extends_ = new ArrayList<>();
    }
}
