package org.einstein.athena.proto.parse;

import org.apache.commons.lang3.StringUtils;
import org.einstein.exception.ESynatx;
import org.einstein.framework.IParser;
import org.einstein.framework.ITemplete;
import org.einstein.framework.impl.ProtoEntityTemplete;
import org.einstein.util.FileUtil;
import org.einstein.util.ParserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @create by kevin
 **/
public class Parser implements IParser<List<ITemplete>, String> {
    private static Logger logger = LoggerFactory.getLogger(Parser.class);
    private BufferedReader reader = null;
    private String m_dir;
    private List<File> m_files = new ArrayList<>(10);
    private List<ITemplete> m_templetes;
    private ITemplete m_templete;
    private static HashMap<String, IParser> parsers;

    static {
        parsers = new HashMap<>();
        parsers.put("package", new PackageParser());
        parsers.put("lineType", new ParserUtil());
        parsers.put("@EProtoEntity", new ProtoEntityParser());
        parsers.put("ProtoEntityDefine", new ProtoDefineParser());
    }


    @Override
    public List<ITemplete> parse(String data) throws ESynatx {
        this.m_dir = data;
        this.loadFiles();
        Iterator<File> it = this.m_files.iterator();
        this.m_templetes = new ArrayList<>();
        while (it.hasNext()) {
            File file = it.next();
            parseEProto(file);
            this.reader = null;
        }
        return this.m_templetes;
    }


    private void loadFiles() throws ESynatx {
        File file = new File(this.m_dir);
        if (!file.exists())
            throw new ESynatx("File not exists");
        this.m_files = FileUtil.getFileList(file, ".java");
    }

    private void parseEProto(File file) throws ESynatx {
        try {
            this.reader = new BufferedReader(new FileReader(file));
            parseContent();
        } catch (FileNotFoundException e) {
            throw new ESynatx("Can not open eproto file", e);
        } finally {
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
            ParserUtil lineTypeParse = (ParserUtil) parsers.get("lineType");
            ProtoEntityTemplete templete = new ProtoEntityTemplete();

            while ((line = this.reader.readLine()) != null) {
                line = StringUtils.trim(line);
                switch (lineTypeParse.parse(line)) {
                    case BLANK: //skip blank
                        break;
                    case PACKAGE:
                        PackageParser packageParser = (PackageParser) parsers.get("package");
                        templete.setM_packageName(packageParser.parse(line));
                        break;
                    case IMPORTRS:
                        break;
                    case COMMENT_START:
                        StringBuilder sb = new StringBuilder();
                        while (!StringUtils.endsWith(line, "*/")) {
                            sb.append(line);
                            line = this.reader.readLine();
                        }
                        sb.append(line);
                        templete.setM_comments(sb.toString());
                        break;
                    case COMMENT_BODY:
                        break;
                    case COMMENT_END:
                        break;
                    case CLASS_ANNOTATION:
                        ProtoEntityParser eProtoParser = (ProtoEntityParser) parsers.get("@EProtoEntity");
                        templete.setM_id(eProtoParser.parse(line));
                        break;
                    case CLASS_HEADER:
                        ProtoDefineParser protoDefineParser = (ProtoDefineParser) parsers.get("ProtoEntityDefine");
                        ProtoDefineParser.ProtoHeader header = protoDefineParser.parse(line);
                        templete.setM_name(header.class_name);
                        templete.setM_extends(header.extends_);
                        break;
                    case PROPERTY_ANNOTATION:  //proto field
                        //TODO Persist annotation
                        // while(StringUtils.startsWith("@"))
                        break;
                    case PROPERTY:
                        break;
                    case ANNOTATION:
                        break;
                    case CLASS_END:
                        break;
                    case UNKNOWN:
                        break;
                    default:
                        break;

                }
            }
        } catch (IOException e) {
            throw new ESynatx("file content read error", e);
        } catch (Exception e) {
            throw new ESynatx("parse line failed", e);
        }
    }


}
