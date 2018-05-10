package org.einstein.util;

import org.apache.commons.lang3.StringUtils;
import org.einstein.framework.IParser;
import org.einstein.framework.ITemplete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.regex.Pattern;

/**
 * @create by kevin
 **/
public class ParserUtil implements IParser<String, String> {

    private Logger logger = LoggerFactory.getLogger(ParserUtil.class);
    private static final String TYPE_PACKAGE = "package";
    private static final String TYPE_EPROTOENTITY = "@EProtoEntity";
    private static final String TYPE_EPROTOFIELD = "@";
    private static final String TYPE_IMPORT = "import";
    private static final String TYPE_PUBLIC = "public";
    private static final String TYPE_PRIVATE = "private";
    private static final String TYPE_PROTECTED = "protected";
    private static final String TYPE_INTERFACE = "interface";

    private static final Pattern pattern_property = Pattern.compile("^[\\S]*\\s*[\\S]*\\s*=\\s*[\\S]*;");
    private static final Pattern pattern_package = Pattern.compile("^(package)\\s*[\\S]*;");
    private static final Pattern pattern_classHeader = Pattern.compile("^(public)\\s*(interface)\\s*[\\S]*\\s*(extends)?\\s*[\\s\\S]*\\{");
    private static final Pattern pattern_classAnno = Pattern.compile("");



    public String parse(String line) {
       if(StringUtils.isBlank(line))
           return " ";
       if(StringUtils.startsWith(line,TYPE_PACKAGE))
           return TYPE_PACKAGE;
       if(StringUtils.startsWith(line,"/*"))
           return "/*";
       if(StringUtils.startsWith(line,"//"))
           return "//";
       if(StringUtils.startsWith(line,TYPE_IMPORT))
           return TYPE_IMPORT;
       if(StringUtils.startsWith(line,TYPE_EPROTOENTITY))
           return TYPE_EPROTOENTITY;
       if(StringUtils.startsWith(line,TYPE_EPROTOFIELD))
           return TYPE_EPROTOFIELD;
       if(StringUtils.startsWith(line,TYPE_PUBLIC+" "+TYPE_INTERFACE))
           return TYPE_PUBLIC + " " +TYPE_INTERFACE;
       return "UNKNOWN_";
    }


    public enum LINETYPE{
        BLANK,PROPERTY,CLASS_HEADER,PROPERTY_ANNOTATION,CLASS_ANNOTATION,CLASS_END,CLASS_COMMENT,FIELD_COMMENT;
    }



}
