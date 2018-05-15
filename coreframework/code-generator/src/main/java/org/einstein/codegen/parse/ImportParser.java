package org.einstein.codegen.parse;

import org.apache.commons.lang3.StringUtils;
import org.einstein.codegen.api.IParser;
import org.einstein.codegen.exception.ESynatx;

/**
 * @create by xiamicpp
 **/
public class ImportParser implements IParser<String,String> {
    private static String split_ = " ";
    @Override
    public String parse(String data) throws ESynatx {
        String[] fields=StringUtils.split(data,split_);
        return StringUtils.trim(fields[1]);
    }
}
