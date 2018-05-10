package org.einstein.util;
import org.einstein.framework.IParser;
import java.util.regex.Pattern;

/**
 * @create by kevin
 **/
public class ParserUtil implements IParser<ParserUtil.LINETYPE, String> {
    private static final Pattern pattern_property = Pattern.compile("^[\\S]*\\s*[\\S]*\\s*=\\s*[\\S]*;");
    private static final Pattern pattern_package = Pattern.compile("^(package)\\s*[\\S]*;");
    private static final Pattern pattern_classHeader = Pattern.compile("^(public)\\s*(interface)\\s*[\\S]*\\s*(extends)?\\s*[\\s\\S]*\\{");
    private static final Pattern pattern_classAnno = Pattern.compile("^@[\\S]*\\([\\s]*id[\\s]*=[\\s]*[1-9]\\d*[\\s]*,[\\s\\S]*\\)");
    private static final Pattern pattern_import = Pattern.compile("^(import)[\\s]*[\\S]*;");
    private static final Pattern pattern_blank = Pattern.compile("^[\\s]*");
    private static final Pattern pattern_propertyAnno = Pattern.compile("^(@)[\\S]*\\([\\s\\S]*\\)");
    private static final Pattern pattern_Comment_Start = Pattern.compile("^/\\*\\*");
    private static final Pattern pattern_Comment_Body = Pattern.compile("^\\*[\\s\\S]*");
    private static final Pattern pattern_Comment_End = Pattern.compile("^\\*\\*/");
    private static final Pattern pattern_annotation = Pattern.compile("^\\\\[\\s\\S]*");
    private static final Pattern pattern_classEnd = Pattern.compile("^}");

    public ParserUtil.LINETYPE parse(String line) {
        if(pattern_blank.matcher(line).matches())
            return LINETYPE.BLANK;
        else if(pattern_propertyAnno.matcher(line).matches())
            return LINETYPE.PROPERTY_ANNOTATION;
        else if(pattern_property.matcher(line).matches())
            return LINETYPE.PROPERTY;
        else if(pattern_annotation.matcher(line).matches())
            return LINETYPE.ANNOTATION;
        else if(pattern_Comment_Body.matcher(line).matches())
            return LINETYPE.COMMENT_BODY;
        else if(pattern_Comment_Start.matcher(line).matches())
            return LINETYPE.COMMENT_START;
        else if(pattern_Comment_End.matcher(line).matches())
            return LINETYPE.COMMENT_END;
        else if(pattern_import.matcher(line).matches())
            return LINETYPE.IMPORTRS;
        else if(pattern_package.matcher(line).matches())
            return LINETYPE.PACKAGE;
        else if(pattern_classAnno.matcher(line).matches())
            return LINETYPE.CLASS_ANNOTATION;
        else if(pattern_classHeader.matcher(line).matches())
            return LINETYPE.CLASS_HEADER;
        else if(pattern_classEnd.matcher(line).matches())
            return LINETYPE.CLASS_END;
        else
            return LINETYPE.UNKNOWN;
    }


    public enum LINETYPE{
        UNKNOWN,
        BLANK,
        PROPERTY,
        CLASS_HEADER,
        PROPERTY_ANNOTATION,
        CLASS_ANNOTATION,
        CLASS_END,
        ANNOTATION,
        IMPORTRS,
        PACKAGE,
        COMMENT_START,
        COMMENT_BODY,
        COMMENT_END;
    }



}
