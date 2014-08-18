/*
 * Copyright 2003 - 2014 The eFaps Team
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
 * Revision:        $Rev$
 * Last Changed:    $Date$
 * Last Changed By: $Author$
 */

package org.efaps.tests.ci;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.annotations.FromAnnotationsRuleModule;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.efaps.tests.ci.digester.CIForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */

public class CIFormDataProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(CIFormDataProvider.class);

    @DataProvider(name = "CIForm")
    public static Iterator<Object[]> ciForms(final ITestContext _context)
    {
        final List<Object[]> ret = new ArrayList<>();

        final File xmlFile = new File(_context.getCurrentXmlTest().getSuite().getFileName());
        final String baseFolderRel = _context.getCurrentXmlTest().getParameter("baseFolder");
        final String baseFolder = FilenameUtils.concat(xmlFile.getPath(), baseFolderRel);
        LOG.debug("basefolder: '{}'", baseFolder);
        final Collection<File> files = FileUtils.listFiles(new File(baseFolder), new String[] {"xml"}, true);

        for ( final File file :files) {
            LOG.debug("file added: '{}'", file);
            final DigesterLoader loader = DigesterLoader.newLoader(new FromAnnotationsRuleModule()
            {
                @Override
                protected void configureRules()
                {
                    bindRulesFrom(CIForm.class);
                }
            });
            try {
                final Digester digester = loader.newDigester();
                final URLConnection connection = file.toURI().toURL().openConnection();
                connection.setUseCaches(false);
                final InputStream stream = connection.getInputStream();
                final InputSource source = new InputSource(stream);
                final Object item = digester.parse(source);
                stream.close();
                if (item instanceof CIForm) {
                    LOG.debug("item added: '{}'", item);
                    ret.add(new Object[]{ item });
                }
            } catch (final MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (final SAXException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return ret.iterator();
    }

}
