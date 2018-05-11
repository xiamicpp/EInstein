package org.einstein.codegen.parse;

import org.apache.commons.lang3.StringUtils;
import org.einstein.framework.IParser;

/**
 * @create by kevin
 **/
public class ProtoEntityParser implements IParser<Integer,String> {

    @Override
    public Integer parse(String data) throws Exception {
        String line=StringUtils.removeEnd(data,")");
        line = StringUtils.removeStart(line,"@EProtoEntity(");
        line = StringUtils.trim(line);
        String [] fields = StringUtils.split(line,",");
        int classId = -1;
        for(String field:fields){
          String [] subFields = StringUtils.split(field,"=");
          if(StringUtils.equalsIgnoreCase(subFields[0].trim(),"=")){
              classId = Integer.valueOf(subFields[1].trim());
              break;
          }
        }
        return classId;
    }
}
