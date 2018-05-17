package org.beta.einstein;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.einstein.codegen.util.FileUtil;

import java.io.File;


/**
 *
 * @goal clean-proto
 * @phase clean
 */
public class EProtoClean extends AbstractMojo {

    private String outputdir = "/generated-src/";

    /**
     * @parameter expression = "${project.basedir}"
     * @readonly
     * @required
     */
    private File project_dir;

    @Override
    public void execute() throws MojoExecutionException {
        FileUtil.deleteDir(project_dir.getAbsolutePath()+outputdir);
    }
}
