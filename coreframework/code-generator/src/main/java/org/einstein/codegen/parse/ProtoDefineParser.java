package org.einstein.codegen.parse;

import org.apache.commons.lang3.StringUtils;
import org.einstein.framework.IParser;

import java.util.ArrayList;
import java.util.List;

/**
 * @create by kevin
 **/
public class ProtoDefineParser implements IParser<ProtoDefineParser.ProtoHeader,String>{

    @Override
    public ProtoHeader parse(String data) throws Exception {
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
