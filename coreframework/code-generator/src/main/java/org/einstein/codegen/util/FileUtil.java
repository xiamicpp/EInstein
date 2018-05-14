package org.einstein.codegen.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
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


    public static void close(Writer out){
        if(out!=null){
            try {
                out.close();
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    public static void flush(Writer out){
        if(out!=null){
            try {
                out.flush();
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    public static Writer createFile(String fileName,String packageName, String outPutDirectory) throws IOException {
        String directory = outPutDirectory;
        if(packageName!=null){
            directory = directory +"/"+packageName.replace(".","/");
        }
        (new File(directory)).mkdirs();
        return new PrintWriter(new BufferedWriter(new FileWriter(directory+"/"+fileName+".java")));
    }

}
