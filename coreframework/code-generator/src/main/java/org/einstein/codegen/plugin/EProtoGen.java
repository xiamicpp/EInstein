package org.einstein.codegen.plugin;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.velocity.VelocityContext;
import org.einstein.codegen.api.IGenerator;
import org.einstein.codegen.generator.JavaCodeGenerator;
import org.einstein.codegen.parse.Parser;

import java.util.HashMap;


/**
 * Goal which touches a timestamp file.
 *
 * @goal generate google proto file
 * @phase compile
 * @requiresProject false：
 */
public class EProtoGen
        extends AbstractMojo {
    /**
     * templete source
     *
     * @parameter expression="${source}"
     * @required
     */
    private String business_object_source;
    /**
     * out put dir
     *
     * @parameter expression= "${outPut}"
     * @required
     */
    private String out_put_dir;

    /**
     * @parameter expression="${type}"
     * @required
     */
    private String type;

    private static HashMap<String,Class<?>> generators = new HashMap<>();

    static {
        generators.put("java", JavaCodeGenerator.class);
    }

    public void execute()
            throws MojoExecutionException {
        try {
            if (type != null) {
                IGenerator generator = (IGenerator) generators.get(type).newInstance();
                Parser parser = new Parser();
                generator.init(parser.parse(this.business_object_source),out_put_dir);

                generator.initialize();
                generator.generate();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
