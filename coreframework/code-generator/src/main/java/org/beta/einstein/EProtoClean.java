package org.beta.einstein;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.einstein.codegen.util.FileUtil;


/**
 *
 * @goal clean-proto
 * @phase clean
 */
public class EProtoClean extends AbstractMojo {

    private String outputdir = "generated-src/";

    @Override
    public void execute() throws MojoExecutionException {
        FileUtil.deleteDir(FileUtil.USER_DIR+"/"+outputdir);
    }
}
