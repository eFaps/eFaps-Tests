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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.annotations.FromAnnotationsRuleModule;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.efaps.tests.ci.digester.CICommand;
import org.efaps.tests.ci.digester.CIForm;
import org.efaps.tests.ci.digester.CITable;
import org.efaps.tests.ci.digester.CIType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public abstract class AbstractCIDataProvider
{
    public static Set<CIForm> FORMS = new HashSet<>();
    public static Set<CITable> TABLES = new HashSet<>();
    public static Set<CICommand> COMMANDS = new HashSet<>();
    public static Set<CIType> TYPES = new HashSet<>();

    public static Properties DBPROPERTIES = new Properties();

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCIDataProvider.class);

    public static Set<ICIItem> getCIItems()
    {
        final Set<ICIItem> ret = new HashSet<>();
        CollectionUtils.addAll(ret, FORMS);
        CollectionUtils.addAll(ret, TABLES);
        CollectionUtils.addAll(ret, COMMANDS);
        return ret;
    }


    /**
     * @param _context
     */
    @BeforeSuite
    public static void loadCI(final ITestContext _context)
    {
        final File xmlFile = new File(_context.getCurrentXmlTest().getSuite().getFileName());
        final String baseFolderRel = _context.getCurrentXmlTest().getParameter("baseFolder");
        final String baseFolder = FilenameUtils.concat(xmlFile.getPath(), baseFolderRel);
        LOG.debug("basefolder: '{}'", baseFolder);
        final Collection<File> files = FileUtils.listFiles(new File(baseFolder), new String[] { "xml" }, true);

        for (final File file : files) {
            LOG.debug("file added: '{}'", file);
            final DigesterLoader loader = DigesterLoader.newLoader(new FromAnnotationsRuleModule()
            {

                @Override
                protected void configureRules()
                {
                    bindRulesFrom(CIForm.class);
                    bindRulesFrom(CITable.class);
                    bindRulesFrom(CICommand.class);
                    bindRulesFrom(CIType.class);
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
                    LOG.debug("Form added: '{}'", item);
                    AbstractCIDataProvider.FORMS.add((CIForm) item);
                } else if (item instanceof CITable) {
                    LOG.debug("Table added: '{}'", item);
                    AbstractCIDataProvider.TABLES.add((CITable) item);
                } else if (item instanceof CICommand) {
                    LOG.debug("Command added: '{}'", item);
                    AbstractCIDataProvider.COMMANDS.add((CICommand) item);
                } else if (item instanceof CIType) {
                    LOG.debug("Type added: '{}'", item);
                    AbstractCIDataProvider.TYPES.add((CIType) item);
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

        final Collection<File> propFiles = FileUtils.listFiles(new File(baseFolder), new String[] { "properties" },
                        true);
        for (final File file : propFiles) {
            final Properties props = new Properties();
            try {
                props.load(new FileInputStream(file));
                LOG.debug("properties loaded: '{}'", file);
            } catch (final IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            DBPROPERTIES.putAll(props);
        }

        try {
            final InputStream ignStream = AbstractCIDataProvider.class.getResourceAsStream("/Ignore.properties");
            if (ignStream != null) {
                final Properties ignoreProps = new Properties();
                ignoreProps.load(ignStream);
                DBPROPERTIES.putAll(ignoreProps);
            }
        } catch (final IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
