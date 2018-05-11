package org.einstein.codegen.parse;

import org.apache.commons.lang3.StringUtils;
import org.einstein.codegen.api.IParser;
import org.einstein.codegen.api.ITemplete;
import org.einstein.codegen.api.impl.Field;
import org.einstein.codegen.api.impl.ProtoEntityTemplete;
import org.einstein.codegen.exception.ESynatx;
import org.einstein.codegen.util.FileUtil;
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
    private static HashMap<TypeParser.PARSER_TYPE, IParser> parsers;

    static {
        parsers = new HashMap<>();
        parsers.put(TypeParser.PARSER_TYPE.PACKAGE, new PackageParser());
        parsers.put(TypeParser.PARSER_TYPE.TYPE, new TypeParser());
        parsers.put(TypeParser.PARSER_TYPE.IMPORTRS,new ImportParser());
        parsers.put(TypeParser.PARSER_TYPE.PROPERTY,new PropertyParser());
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
            TypeParser lineTypeParse = (TypeParser) parsers.get(TypeParser.PARSER_TYPE.TYPE);
            ProtoEntityTemplete templete = new ProtoEntityTemplete();
            Field property_field = new Field();
            while ((line = this.reader.readLine()) != null) {
                line = StringUtils.trim(line);
                switch (lineTypeParse.parse(line)) {
                    case BLANK: //skip blank
                        break;
                    case PACKAGE:
                        PackageParser packageParser = (PackageParser) parsers.get(TypeParser.PARSER_TYPE.PACKAGE);
                        templete.setM_packageName(packageParser.parse(line));
                        break;
                    case IMPORTRS:
                        ImportParser importParser = (ImportParser) parsers.get(TypeParser.PARSER_TYPE.IMPORTRS);
                        templete.addImport(importParser.parse(line));
                        break;
                    case COMMENT_START:
                        StringBuilder coments = new StringBuilder();
                        while (lineTypeParse.parse(line)!= TypeParser.PARSER_TYPE.COMMENT_END){
                            line = this.reader.readLine();
                            coments.append(line);
                        }
                        coments.append(line);
                        templete.setM_comments(coments.toString());
                        break;
                    case CLASS_ANNOTATION:
                        templete.addClassAnnotation(line);
                        break;
                    case CLASS_HEADER:
                        ClassHeaderParser protoDefineParser = (ClassHeaderParser) parsers.get("ProtoEntityDefine");
                        ClassHeaderParser.ProtoHeader header = protoDefineParser.parse(line);
                        templete.setM_name(header.class_name);
                        templete.setM_extends(header.extends_);
                        break;
                    case PROPERTY_ANNOTATION:  //proto field
                        property_field.addFieldAnnotation(line);
                        break;
                    case PROPERTY:
                        PropertyParser propertyParser =(PropertyParser)parsers.get(TypeParser.PARSER_TYPE.PROPERTY);
                        PropertyParser.Property property = propertyParser.parse(line);
                        property_field.setDefaultValue(property.value);
                        property_field.setM_field_type_(property.type);
                        property_field.setM_field_name_(property.name);
                        if(property_field!=null){
                            templete.addField(property_field);
                        }
                        property_field = new Field();
                        break;
                    case ANNOTATION:
                        break;
                    case CLASS_END:
                        this.m_templetes.add(templete);
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
