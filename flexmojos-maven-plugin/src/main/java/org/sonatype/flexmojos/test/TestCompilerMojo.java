package org.sonatype.flexmojos.test;

 import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.not;
import static org.sonatype.flexmojos.common.FlexExtension.SWC;
import static org.sonatype.flexmojos.common.FlexScopes.CACHING;
import static org.sonatype.flexmojos.common.FlexScopes.COMPILE;
import static org.sonatype.flexmojos.common.FlexScopes.EXTERNAL;
import static org.sonatype.flexmojos.common.FlexScopes.INTERNAL;
import static org.sonatype.flexmojos.common.FlexScopes.MERGED;
import static org.sonatype.flexmojos.common.FlexScopes.RSL;
import static org.sonatype.flexmojos.common.FlexScopes.TEST;
import static org.sonatype.flexmojos.matcher.artifact.ArtifactMatcher.scope;
import static org.sonatype.flexmojos.matcher.artifact.ArtifactMatcher.type;

import java.io.File;
import java.util.List;

import org.sonatype.flexmojos.compiler.IRuntimeSharedLibraryPath;
import org.sonatype.flexmojos.compiler.MxmlcMojo;
import org.sonatype.flexmojos.test.util.PathUtil;
import org.sonatype.flexmojos.utilities.MavenUtils;

/**
 * <p>
 * Goal which compiles the Flex test into a runnable application
 * </p>
 * 
 * @author Marvin Herman Froeder (velo.br@gmail.com)
 * @since 4.0
 * @goal test-compile
 * @requiresDependencyResolution compile
 * @phase test-compile
 * @configurator flexmojos
 */
public class TestCompilerMojo
    extends MxmlcMojo
{

    /**
     * The maven compile source roots
     * <p>
     * Equivalent to -compiler.source-path
     * </p>
     * List of path elements that form the roots of ActionScript class
     * 
     * @parameter expression="${project.testCompileSourceRoots}"
     * @required
     * @readonly
     */
    private List<String> sourcePaths;

    @Override
    public File[] getSourcePath()
    {
        return PathUtil.getFiles( sourcePaths );
    }

    @Override
    public File[] getIncludeLibraries()
    {
        return MavenUtils.getFiles( getDependencies( type( SWC ),// 
                                                     anyOf( scope( INTERNAL ), scope( RSL ), scope( CACHING ),
                                                            scope( TEST ) ),//
                                                     not( GLOBAL_MATCHER ) ) );
    }

    @Override
    public File[] getExternalLibraryPath()
    {
        return MavenUtils.getFiles( getGlobalArtifact() );
    }

    @Override
    public String[] getRuntimeSharedLibraries()
    {
        return null;
    }

    @Override
    public IRuntimeSharedLibraryPath[] getRuntimeSharedLibraryPath()
    {
        return null;
    }

    @Override
    public File[] getLibraryPath()
    {
        return MavenUtils.getFiles( getDependencies( type( SWC ),//
                                                     anyOf( scope( MERGED ), scope( EXTERNAL ), scope( COMPILE ),
                                                            scope( null ) ),//
                                                     not( GLOBAL_MATCHER ) ),//
                                    getCompiledResouceBundles() );
    }
}