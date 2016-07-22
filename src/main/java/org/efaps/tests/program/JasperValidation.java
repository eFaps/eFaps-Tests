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

import java.io.File;
import java.net.MalformedURLException;

import org.efaps.update.Install.InstallFile;
import org.efaps.update.schema.program.jasperreport.JasperReportImporter;
import org.efaps.update.util.InstallationException;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

/**
 * The Class JasperValidation.
 *
 * @author The eFaps Team
 */
public class JasperValidation
{

    /**
     * Check that file-application values are set correctly.
     *
     * @param _context the context
     * @param _file the file
     * @throws MalformedURLException the malformed URL exception
     * @throws InstallationException the installation exception
     */
    @Test(dataProvider = "JasperFiles",
                    dataProviderClass = JasperFileProvider.class,
                    description = "Check that file-application values are set correctly.")
    public void fileApplication(final ITestContext _context,
                                final File _file)
        throws MalformedURLException, InstallationException
    {
        final String application = _context.getCurrentXmlTest().getParameter("application");
        final InstallFile installFile = new InstallFile().setURL(_file.toURI().toURL());
        final JasperReportImporter importer = new JasperReportImporter(installFile);
        Assert.assertEquals(importer.getApplication(), application, String.format(
                        "JasperFile: '%s' has wrong Application", importer.getProgramName()));
    }
}
