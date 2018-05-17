package org.beta.einstein;

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
import org.einstein.codegen.exception.ESynatx;
import org.einstein.codegen.generator.GoogleProtoGenerator;
import org.einstein.codegen.parse.ProtoParser;

import java.io.File;

/**
 *
 * @goal compile-proto
 * @phase generate-sources
 * @execute phase="compile"
 */
public class EProtoGen
        extends AbstractMojo {
    /**
     * @parameter property = "protodir"
     */
    private String proto_dir;

    /**
     * @parameter expression = "${project.basedir}"
     * @readonly
     * @required
     */
    private File project_dir;

    /**
     * @parameter property = "outputdir"
     * @readonly
     */
    private String outputdir = "generated-src/";

    public void execute()
            throws MojoExecutionException {
        if(proto_dir == null)
            throw new MojoExecutionException("can not find proto resource");
        try {
            System.out.println(project_dir.getAbsolutePath());
            boolean result = false;
            GoogleProtoGenerator generator = new GoogleProtoGenerator();
            ProtoParser parser = new ProtoParser();
            generator.init(parser.parse(project_dir.getAbsolutePath(),proto_dir), project_dir.getAbsolutePath()+"/"+outputdir);
            generator.initialize();
            result=generator.generate();
            CheckResult(result,"GoogleProtoGenerate");
        }catch (ESynatx e){
            throw new MojoExecutionException("synatx exception "+e.getMessage());
        }
    }


    private void CheckResult(boolean result, String phase) throws MojoExecutionException {
        if(!result)
            throw  new MojoExecutionException(phase+" generate failed!");
    }
}
