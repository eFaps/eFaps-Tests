package org.efaps.tests.program;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

public class ESJPValidation
{

    /**
     * Logger for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(ESJPValidation.class);

    @Test(dataProvider = "JavaFiles",
                    dataProviderClass = JavaFileProvider.class,
                    description = "Check that the Annotations are set.")
    public void test4Annotations(final ITestContext _context,
                                 final File _file)
        throws IOException
    {
        final File xmlFile = new File(_context.getCurrentXmlTest().getSuite().getFileName());
        final String application = _context.getCurrentXmlTest().getParameter("application");
        final String baseFolderRel = _context.getCurrentXmlTest().getParameter("baseFolder");
        final String baseFolder = FilenameUtils.concat(xmlFile.getPath(), baseFolderRel);

        final String str = Generic4VarArg.readFileToString(_file);
        final ASTParser parser = ASTParser.newParser(AST.JLS8);

        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        final Map<?, ?> options = JavaCore.getOptions();
        parser.setCompilerOptions(options);
        parser.setUnitName("any_name");
        // _context.getClass().getClassLoader().
        final String strClassPath = System.getProperty("java.class.path");
        final String[] sources = { baseFolder + "/ESJP/" };
        final String[] classpath = strClassPath.split(":");

        parser.setEnvironment(classpath, sources, new String[] { "UTF-8" }, true);
        parser.setSource(str.toCharArray());

        final Set<String> annotations = new HashSet<>();
        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        cu.accept(new ASTVisitor()
        {

            @Override
            public boolean visit(final SingleMemberAnnotation _node)
            {
                annotations.add(_node.getTypeName().toString());
                annotations.add(_node.toString());
                return super.visit(_node);
            }
        });
        LOG.debug("Annotations found {}", annotations);
        Assert.assertEquals(annotations.contains("EFapsUUID"), true,
                        String.format("ESJP '%s' does not contain the EFapsUUID annotation", _file));
        Assert.assertEquals(annotations.contains("@EFapsApplication(\"" + application + "\")"), true,
                        String.format("ESJP '%s' does not contain correect EFapsApplication annotation", _file));

    }
}
