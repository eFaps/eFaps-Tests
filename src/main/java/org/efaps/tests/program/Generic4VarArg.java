/*
 * Copyright 2003 - 2016 The eFaps Team
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
package org.efaps.tests.program;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.efaps.db.Insert;
import org.efaps.db.MultiPrintQuery;
import org.efaps.db.PrintQuery;
import org.efaps.db.QueryBuilder;
import org.efaps.db.Update;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

/**
 * The Class VarArgTest.
 *
 * @author The eFaps Team
 */
public class Generic4VarArg
{

    /** The varargmap containingn the vararg methods */
    private static Map<String, Set<String>> VARARGMAP = new HashMap<>();

    /** The genericsmap containin the generic methods. */
    private static Map<String, Set<String>> GENERICSMAP = new HashMap<>();

    static {
        VARARGMAP.put(QueryBuilder.class.getName(), new HashSet<>(Arrays.asList(new String[] {
                        "addWhereSelectEqValue",
                        "addWhereAttrEqValue",
                        "addWhereAttrNotEqValue" })));
        VARARGMAP.put(Insert.class.getName(), new HashSet<>(Arrays.asList(new String[] {
                        "add"})));
        VARARGMAP.put(Update.class.getName(), new HashSet<>(Arrays.asList(new String[] {
                        "add"})));

        GENERICSMAP.put(PrintQuery.class.getName(), new HashSet<>(Arrays.asList(new String[] {
                        "getSelect",
                        "getAttribute"})));
        GENERICSMAP.put(MultiPrintQuery.class.getName(), new HashSet<>(Arrays.asList(new String[] {
                        "getSelect",
                        "getAttribute"})));
    }

    /**
     * Generic with var arg methods.
     *
     * @param _context the context
     * @throws IOException Signals that an I/O exception has occurred.
     */
    @Test(dataProvider = "JavaFiles", dataProviderClass = JavaFileProvider.class,
                    description = "Check correct use of VarArg method invocation with generics.")
    public void genericWithVarArgMethods(final ITestContext _context,
                                         final File _file)
        throws IOException
    {
        final File xmlFile = new File(_context.getCurrentXmlTest().getSuite().getFileName());
        final String baseFolderRel = _context.getCurrentXmlTest().getParameter("baseFolder");
        final String baseFolder = FilenameUtils.concat(xmlFile.getPath(), baseFolderRel);

        final String str = readFileToString(_file);

        final ASTParser parser = ASTParser.newParser(AST.JLS8);

        parser.setKind(ASTParser.K_COMPILATION_UNIT);
        parser.setResolveBindings(true);
        parser.setBindingsRecovery(true);
        @SuppressWarnings("unchecked")
        final Map<String, String> options = JavaCore.getOptions();
        options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
        parser.setCompilerOptions(options);
        parser.setUnitName("any_name");
        // _context.getClass().getClassLoader().
        final String strClassPath = System.getProperty("java.class.path");
        final String[] sources = { baseFolder + "/ESJP/" };
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
                                        final MethodInvocation method = (MethodInvocation) _node.arguments().get(1);
                                        final Expression paraExpression = method.getExpression();
                                        if (paraExpression != null) {
                                            final ITypeBinding paraTypeBinding = paraExpression.resolveTypeBinding();
                                            for (final Entry<String, Set<String>> genEntry : GENERICSMAP.entrySet()) {
                                                if (paraTypeBinding != null
                                                            && genEntry.getKey().equals(paraTypeBinding.getBinaryName())
                                                            && genEntry.getValue().contains(method.getName()
                                                                                .getIdentifier())) {

                                                    Assert.assertFalse( method.typeArguments().isEmpty(),
                                                                    "Missing TypeArgument for VarArg in class "
                                                                    +  _file.getName()
                                                                    + ", Line "
                                                                    + cu.getLineNumber(_node.getStartPosition())
                                                                    + ": " + _node.toString());
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
