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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

/**
 * The Class CSSFileProvider.
 *
 * @author The eFaps Team
 */
public class CSSFileProvider
{

    /**
     * CSS Files.
     *
     * @param _context the context
     * @return the iterator< object[]>
     */
    @DataProvider(name = "CSSFiles")
    public static Iterator<Object[]> cssFiles(final ITestContext _context)
    {
        final List<Object[]> ret = new ArrayList<>();

        final File xmlFile = new File(_context.getCurrentXmlTest().getSuite().getFileName());
        final String baseFolderRel = _context.getCurrentXmlTest().getParameter("baseFolder");
        final String baseFolder = FilenameUtils.concat(xmlFile.getPath(), baseFolderRel);
        final Collection<File> files = FileUtils.listFiles(new File(baseFolder), new String[] { "css" }, true);
        for (final File file : files) {
            ret.add(new Object[] { file });
        }
        return ret.iterator();
    }

}
