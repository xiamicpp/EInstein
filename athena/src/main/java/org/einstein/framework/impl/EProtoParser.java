package org.einstein.framework.impl;

import org.einstein.framework.IParser;
import org.einstein.framework.ITemplete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @create by kevin
 **/
public class EProtoParser implements IParser{

    private Logger logger = LoggerFactory.getLogger(EProtoParser.class);

    private String eproto_path_;
    private int file_count_;
    private List<ITemplete> templetes_;


    public void parse() {
        if(eproto_path_==null){
            logger.error("can not get eproto file path!!!");
            return;
        }
        logger.info("Start to load file..");
        List<String> bos = loadFileNames(eproto_path_);


    }

    private List<String> loadFileNames(String path){
        File root = new File(path);
        List<String> classNames = new ArrayList<String>(10);
        File[] files=root.listFiles();
        for(File file:files){
            if(file.isDirectory()){
                List<String> temp= loadFileNames(file.getAbsolutePath());
                classNames.addAll(temp);
            }else if(file.getName().endsWith(".java")){
                classNames.add(file.getName());
                file_count_++;
            }
        }
        return classNames;
    }

    private void analysis(List<String> list){
        Class c = Class.forName()
    }
}
