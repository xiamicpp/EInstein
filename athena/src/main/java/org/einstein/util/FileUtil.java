package org.einstein.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @create by kevin
 **/
public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);


    /**
     * get file list
     *
     * @param root   root dir
     * @param suffix
     * @return
     */
    public static List<File> getFileList(File root, String suffix) {
        List<File> result = new ArrayList<File>();
        File[] dirAndFiles = root.listFiles();
        for (File file : dirAndFiles) {
            if (file.isDirectory()) {
                List<File> tempList = getFileList(file, suffix);
                result.addAll(tempList);
            } else if (suffix == null || file.getName().endsWith(suffix)) {
                result.add(file);
            }
        }
        return result;
    }
}
