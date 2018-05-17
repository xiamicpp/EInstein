package org.einstein.codegen.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @create by kevin
 **/
public class FileUtil {
    private static Logger logger = LoggerFactory.getLogger(FileUtil.class);
    public static final String USER_DIR = System.getProperty("user.dir");

    /**
     * get file list
     *
     * @param root   root dir
     * @param suffix
     * @return
     */
    public static Set<File> getFileList(File root, String suffix) {
        Set<File> result = new HashSet<>();
        File[] dirAndFiles = root.listFiles();
        for (File file : dirAndFiles) {
            if (file.isDirectory()) {
                Set<File> tempList = getFileList(file, suffix);
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

    public static Writer createFileWriter(String fileName,String outPutDirectory) throws IOException {
        String directory = outPutDirectory;
        createDir(directory);
        return new PrintWriter(new BufferedWriter(new FileWriter(directory+"/"+fileName)));
    }

    public static void createDir(String directory){
        (new File(directory)).mkdirs();
    }

    public static void deleteDir(String directory){
        File dir = new File(directory);
        if(!dir.exists()) return;
        if(dir.isDirectory()){
            File[] allfiles = dir.listFiles();
            for(File file:allfiles){
                if(file.isDirectory()){
                    deleteDir(file.getAbsolutePath());
                }else if(file.isFile()){
                    file.delete();
                }
            }
        }
        dir.delete();
    }

}
