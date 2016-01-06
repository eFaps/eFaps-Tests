/*
 * Copyright 2003 - 2015 The eFaps Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.efaps.tests.others;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.efaps.db.PrintQuery;
import org.efaps.db.QueryBuilder;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.Test;

/**
 * The Class VarArgTest.
 *
 * @author The eFaps Team
 */
public class Generic4VarArg
{

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(Generic4VarArg.class);

    /** The varargmap containingn the vararg methods */
    private static Map<String, Set<String>> VARARGMAP = new HashMap<>();

    /** The genericsmap containin the generic methods. */
    private static Map<String, Set<String>> GENERICSMAP = new HashMap<>();

    static {
        VARARGMAP.put(QueryBuilder.class.getName(), new HashSet<String>(Arrays.asList(new String[] {
                        "addWhereSelectEqValue",
                        "addWhereAttrEqValue",
                        "addWhereAttrNotEqValue" })));

        GENERICSMAP.put(PrintQuery.class.getName(), new HashSet<String>(Arrays.asList(new String[] {
                        "getSelect" })));
    }

    /**
     * Generic with var arg methods.
     *
     * @param _context the context
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test()
    public void genericWithVarArgMethods(final ITestContext _context)
        throws IOException
    {
        final File xmlFile = new File(_context.getCurrentXmlTest().getSuite().getFileName());
        final String baseFolderRel = _context.getCurrentXmlTest().getParameter("baseFolder");
        final String baseFolder = FilenameUtils.concat(xmlFile.getPath(), baseFolderRel);
        LOG.debug("basefolder: '{}'", baseFolder);
        final Collection<File> files = FileUtils.listFiles(new File(baseFolder), new String[] { "java" }, true);
        for (final File file : files) {
            check(baseFolder, file);
        }
    }

    /**
     * Check.
     *
     * @param _baseFolder the base folder
     * @param _file the file
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private void check(final String _baseFolder,
                       final File _file)
        throws IOException
    {
        final String str = readFileToString(_file);

        final ASTParser parser = ASTParser.newParser(AST.JLS8);

        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        final Map<?,?> options = JavaCore.getOptions();
        parser.setCompilerOptions(options);
        parser.setUnitName("any_name");
        // _context.getClass().getClassLoader().
        final String strClassPath = System.getProperty("java.class.path");
        final String[] sources = { _baseFolder + "/ESJP/" };
        final String[] classpath = strClassPath.split(":");

        parser.setEnvironment(classpath, sources, new String[] { "UTF-8" }, true);

        parser.setSource(str.toCharArray());
        final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
        cu.accept(new ASTVisitor()
        {

            @Override
            public boolean visit(final MethodInvocation _node)
            {
                for (final Entry<String, Set<String>> entry : VARARGMAP.entrySet()) {
                    if (entry.getValue().contains(_node.getName().getIdentifier())) {
                        final Expression expression = _node.getExpression();
                        if (expression != null) {
                            final ITypeBinding typeBinding = expression.resolveTypeBinding();
                            if (typeBinding != null) {
                                if (entry.getKey().equals(typeBinding.getBinaryName())) {
                                    if (_node.arguments().get(1) instanceof MethodInvocation) {
                                        final MethodInvocation method = ((MethodInvocation) _node.arguments().get(1));
                                        final Expression paraExpression = method.getExpression();
                                        if (paraExpression != null) {
                                            final ITypeBinding paraTypeBinding = paraExpression.resolveTypeBinding();
                                            for (final Entry<String, Set<String>> genEntry : GENERICSMAP.entrySet()) {

                                                if (genEntry.getKey().equals(paraTypeBinding.getBinaryName())
                                                                && genEntry.getValue().contains(method.getName()
                                                                                .getIdentifier())) {

                                                    Assert.assertFalse("Missing TypeArgument for VarArg in class "
                                                                    +  _file.getName()
                                                                    + ", Line "
                                                                    + cu.getLineNumber(_node.getStartPosition())
                                                                    + ": " + _node.toString(),
                                                                    method.typeArguments().isEmpty());
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return super.visit(_node);
            }
        });
    }

    /**
     * Read file to string.
     *
     * @param _file the file
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    // read file content into a string
    public static String readFileToString(final File _file)
        throws IOException
    {
        final StringBuilder fileData = new StringBuilder(1000);
        final BufferedReader reader = new BufferedReader(new FileReader(_file));
        char[] buf = new char[10];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            final String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
            buf = new char[1024];
        }
        reader.close();
        return fileData.toString();
    }

}
