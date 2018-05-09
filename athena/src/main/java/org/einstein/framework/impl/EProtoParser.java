package org.einstein.framework.impl;

import org.apache.commons.lang3.StringUtils;
import org.einstein.framework.IParser;
import org.einstein.framework.ITemplete;
import org.einstein.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @create by kevin
 **/
public class EProtoParser {

    private Logger logger = LoggerFactory.getLogger(EProtoParser.class);

    private String eproto_path_ = null;
    private int file_count_ = 0;
    private List<ITemplete> templetes_ = new ArrayList<ITemplete>(10);


    public void parse() {


    }



    private void parseLine(String line) {
        if (StringUtils.isBlank(line))
            return;


    }

    public static void main(String[] args) {
        try {
            Class bo = Class.forName("ProtoGenerator");
            System.out.println(bo.getPackage().getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
