/*
 * Copyright © 2003 - 2024 The eFaps Team (-)
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
 */
package org.efaps.tests.program;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import com.github.javaparser.ParserConfiguration.LanguageLevel;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

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
        final String application = _context.getCurrentXmlTest().getParameter("application");

        final String str = Generic4VarArg.readFileToString(_file);
        final Set<String> annotations = new HashSet<>();
        StaticJavaParser.getParserConfiguration().setLanguageLevel(LanguageLevel.BLEEDING_EDGE);
        final CompilationUnit parsed = StaticJavaParser.parse(str);
        parsed.accept(new VoidVisitorAdapter<Void>() {
            @Override
            public void visit(final ClassOrInterfaceDeclaration _n, final Void _arg)
            {
                _n.getAnnotations().forEach(an -> {
                    annotations.add(an.getNameAsString());
                    annotations.add(an.toString());
                });
            }
            @Override
            public void visit(final EnumDeclaration _n, final Void _arg) {
                _n.getAnnotations().forEach(an -> {
                    annotations.add(an.getNameAsString());
                    annotations.add(an.toString());
                });
            }
        }, null);
        LOG.debug("Annotations found {}", annotations);
        Assert.assertEquals(annotations.contains("EFapsUUID"), true,
                        String.format("ESJP '%s' does not contain the EFapsUUID annotation", _file));
        Assert.assertEquals(annotations.contains("@EFapsApplication(\"" + application + "\")"), true,
                        String.format("ESJP '%s' does not contain correect EFapsApplication annotation", _file));
    }
}
