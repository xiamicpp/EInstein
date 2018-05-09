package org.einstein.athena.proto.parse;

import org.apache.commons.lang3.StringUtils;
import org.einstein.framework.IParser;

/**
 * @create by kevin
 **/
public class PackageParser implements IParser<String,String>{

    private static final String split_ = " ";

    @Override
    public String parse(String str) {
        String[] vars=StringUtils.split(str,split_);
        return StringUtils.removeEnd(vars[1],";");
    }
}
