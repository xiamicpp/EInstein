package org.einstein.codegen.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @create by xiamicpp
 **/
public class TimeUtil {
    private static SimpleDateFormat DATETIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String CurrentTime(){
        return  DATETIME.format(new Date());
    }
}
