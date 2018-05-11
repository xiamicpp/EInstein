package org.einstein.codegen.parse;

import org.apache.commons.lang3.StringUtils;
import org.einstein.codegen.api.IParser;

/**
 * @create by xiamicpp
 **/
public class PropertyParser implements IParser<PropertyParser.Property,String> {


    @Override
    public Property parse(String data) throws Exception {
        String[] fields = StringUtils.split(data," ");
        Property property = new Property();
        property.type = fields[0].trim();
        property.name = fields[1].trim();
        property.value = fields[3].trim();
        return property;
    }

    public class Property{
        String type;
        String name;
        String value;
    }
}
