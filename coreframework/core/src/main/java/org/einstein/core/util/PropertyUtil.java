package org.einstein.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Common tools to read system property
 * @create by xiamicpp
 **/
public class PropertyUtil {

    private static final Logger logger = LoggerFactory.getLogger(PropertyUtil.class);

    private PropertyUtil(){

    }

    public static boolean getBooleanProperty(String name,boolean defaultValue){
        String value = System.getProperty(name);
        if(value == null)
            return defaultValue;
        else{
            try {
                return Boolean.parseBoolean(value);
            }catch (NumberFormatException e){
                logger.warn("getBooleanProperty failed, will use default value: ", defaultValue);
                return defaultValue;
            }
        }
    }


    public static int getIntProperty(String name, int defaultValue){
        String value = System.getProperty(name);
        if(value == null)
            return defaultValue;
        else{
            try {
                return Integer.parseInt(value);
            }catch (NumberFormatException e){
                logger.warn("getIntProperty failed, will use default value: ", defaultValue);
                return defaultValue;
            }
        }
    }


    public static double getDoubleProperty(String name,double defaultValue){
        String value = System.getProperty(name);
        if(value == null)
            return defaultValue;
        else{
            try {
                return Double.parseDouble(value);
            }catch (NumberFormatException e){
                logger.warn("getDoubleProperty failed, will use default value: ", defaultValue);
                return defaultValue;
            }
        }
    }


    public static long getLongProperty(String name,long defaultValue){
        String value = System.getProperty(name);
        if(value == null)
            return defaultValue;
        else{
            try {
                return Long.parseLong(value);
            }catch (NumberFormatException e){
                logger.warn("getLongProperty failed, will use default value: ", defaultValue);
                return defaultValue;
            }
        }
    }


    public static String getStringProperty(String name,String defaultValue){
        String value = System.getProperty(name);
        if(value == null)
            return defaultValue;
        else{
            try {
                return value;
            }catch (NumberFormatException e){
                logger.warn("getLongProperty failed, will use default value: ", defaultValue);
                return defaultValue;
            }
        }
    }
}
