package org.einstein.codegen.parse;

import org.apache.commons.lang3.StringUtils;
import org.einstein.codegen.api.ICodeTemplate;
import org.einstein.codegen.api.IParser;
import org.einstein.codegen.api.impl.CodeTemplate;
import org.einstein.codegen.exception.ESynatx;
import org.einstein.codegen.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @create by xiamicpp
 **/
public class ProtoParser implements IParser<List<ICodeTemplate>, String,String> {
    private static Logger logger = LoggerFactory.getLogger(ProtoParser.class);
    private String projectDir;
    private String protoDir;
    private Set<File> protoSource = new HashSet<>();
    private List<ICodeTemplate> codes = new ArrayList<>();
    private static Pattern classNamePattern = Pattern.compile("^public\\s+interface\\s+([a-zA-Z0-9]*)[\\s\\S]*");
    private static Pattern classPackagePattern = Pattern.compile("^package\\s+(\\S*);");



    @Override
    public List<ICodeTemplate> parse(String dir, String data) throws ESynatx {
        this.projectDir = dir;
        this.protoDir =  data;
        logger.info("==========================CodeParser========================");
        logger.info("parse from {} in {}", dir,data);
        this.loadProtoSource();
        Iterator<File> it = this.protoSource.iterator();
        while (it.hasNext()) {
            File file = it.next();
            parseCode(file);
        }
        logger.info("========================CodeParseEnd========================");
        return codes;
    }


    private void loadProtoSource() throws ESynatx {
        File file = new File(this.projectDir+"/"+this.protoDir);
        if (!file.exists()) {
            throw new ESynatx("File not exists in " + file.getAbsolutePath());
        }
        this.protoSource = FileUtil.getFileList(file, "java");
    }

    private void parseCode(File file) throws ESynatx {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            this.codes.add(parseContent(reader));
        } catch (FileNotFoundException e) {
            throw new ESynatx("Code file not found, ", e);
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private ICodeTemplate parseContent(BufferedReader reader) throws ESynatx {
        try {
            String line="";
            String packageName=null;
            String className=null;
            while((line=reader.readLine())!=null){
                if(StringUtils.isBlank(line)) continue;
                Matcher matcher = classPackagePattern.matcher(StringUtils.trim(line));
                if(matcher.matches()){
                    packageName = matcher.group(1);
                    continue;
                }
                matcher=classNamePattern.matcher(StringUtils.trim(line));
                if(matcher.matches()){
                    className = matcher.group(1);
                    break;
                }
            }
            if(StringUtils.isEmpty(packageName)||StringUtils.isEmpty(className))
                throw  new ESynatx("can not parse packageName or className!");
            CodeTemplate codeTemplete = new CodeTemplate(packageName,className);
            codeTemplete.reflectClass(this.projectDir+"/target/classes/");
            return codeTemplete;
        }catch (IOException e){
            throw new ESynatx("read file failed",e);
        }
    }


}
