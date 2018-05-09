package org.einstein.util;

import org.apache.commons.lang3.StringUtils;
import org.einstein.framework.IParser;
import org.einstein.framework.ITemplete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @create by kevin
 **/
public class ParserUtil implements IParser<ITemplete, File> {

    private Logger logger = LoggerFactory.getLogger(ParserUtil.class);
    private static final String TYPE_PACKAGE = "package";
    private static final String TYPE_EPROTOENTITY = "@EProtoEntity";
    private static final String TYPE_EPROTOFIELD = "@EProtoFIELD";
    private static final String TYPE_IMPORT = "import";
    private static final String TYPE_PUBLIC = "public";
    private static final String TYPE_PRIVATE = "private";
    private static final String TYPE_PROTECTED = "protected";
    private static final String TYPE_INTERFACE = "interface";

    public ITemplete parse(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                line = StringUtils.trim(line);
            }
        } catch (FileNotFoundException e) {
            logger.error("File not find! file-{},error-{}", file.getAbsolutePath(), e);
            return null;
        } catch (IOException e) {
            logger.error("File read failed! file-{}, error-{}", file.getAbsolutePath(), e);
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("File close failed! file-{},error-{}", file.getAbsolutePath(), e);
                }
            }
        }
        return null;
    }


}
