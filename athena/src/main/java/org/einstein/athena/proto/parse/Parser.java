package org.einstein.athena.proto.parse;

import org.einstein.exception.ESynatx;
import org.einstein.framework.IParser;
import org.einstein.framework.ITemplete;
import org.einstein.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @create by kevin
 **/
public class Parser implements IParser<List<ITemplete>,String>{
    private static Logger logger = LoggerFactory.getLogger(Parser.class);
    private BufferedReader reader = null;
    private String m_dir;
    private List<File> m_files = new ArrayList<>(10);
    private List<ITemplete> templetes;
    private ITemplete templete;


    @Override
    public List<ITemplete> parse(String data) throws ESynatx {
        this.m_dir = data;
        this.loadFiles();
        Iterator<File> it = this.m_files.iterator();
        this.templetes = new ArrayList<>();
        while (it.hasNext()){
            File file = it.next();
            parseEProto(file);
            this.reader =null;
        }
        return this.templetes;
    }


    private void loadFiles() throws ESynatx {
        File file = new File(this.m_dir);
        if(!file.exists())
            throw new ESynatx("File not exists");
        this.m_files = FileUtil.getFileList(file, ".java");
    }

    private void parseEProto(File file) throws ESynatx {
        try {
            this.reader = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException e) {
            throw new ESynatx("Can not open eproto file",e);
        }finally {
            try {
                this.reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseContent() throws ESynatx {
        String line = null;
        try {
            while ((line = this.reader.readLine()) != null) {

            }
        }catch (IOException e){
            throw new ESynatx("file content read error",e);
        }
    }


}
