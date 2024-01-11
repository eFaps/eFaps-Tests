/*
 * Copyright 2003 - 2023 The eFaps Team
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
import org.efaps.tests.ci.digester.CIAccessSet;
import org.efaps.tests.ci.digester.CICommand;
import org.efaps.tests.ci.digester.CIForm;
import org.efaps.tests.ci.digester.CIJasperImage;
import org.efaps.tests.ci.digester.CIMenu;
import org.efaps.tests.ci.digester.CIMsgPhrase;
import org.efaps.tests.ci.digester.CINumberGenerator;
import org.efaps.tests.ci.digester.CISQLTable;
import org.efaps.tests.ci.digester.CISearch;
import org.efaps.tests.ci.digester.CIStatusGroup;
import org.efaps.tests.ci.digester.CITable;
import org.efaps.tests.ci.digester.CIType;
import org.efaps.tests.ci.digester.CIUIImage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 */
public abstract class AbstractCIDataProvider
{

    private static boolean LOADED = false;

    /** The forms. */
    public static Set<CIForm> FORMS = new HashSet<>();

    /** The tables. */
    public static Set<CITable> TABLES = new HashSet<>();

    /** The commands. */
    public static Set<CICommand> COMMANDS = new HashSet<>();

    /** The menus. */
    public static Set<CIMenu> MENUS = new HashSet<>();

    /** The commands. */
    public static Set<CISearch> SEARCHS = new HashSet<>();

    /** The types. */
    public static Set<CIType> TYPES = new HashSet<>();

    /** The types. */
    public static Set<CISQLTable> SQLTABLES = new HashSet<>();

    /** The statusgrps. */
    public static Set<CIStatusGroup> STATUSGRPS = new HashSet<>();

    /** The statusgrps. */
    public static Set<CINumberGenerator> NUMGENS = new HashSet<>();

    /** The statusgrps. */
    public static Set<CIUIImage> UIIMG = new HashSet<>();

    /** The statusgrps. */
    public static Set<CIJasperImage> JASPERIMG = new HashSet<>();

    /** The statusgrps. */
    public static Set<CIAccessSet> ACCESSSET = new HashSet<>();

    /** The dbproperties. */
    public static Properties DBPROPERTIES = new Properties();

    /** The statusgrps. */
    public static Set<CIMsgPhrase> MSGPHRASES = new HashSet<>();

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractCIDataProvider.class);

    /**
     * Gets the CI items.
     *
     * @return the CI items
     */
    public static Set<ICIItem> getCIItems()
    {
        final Set<ICIItem> ret = new HashSet<>();
        CollectionUtils.addAll(ret, AbstractCIDataProvider.FORMS);
        CollectionUtils.addAll(ret, AbstractCIDataProvider.TABLES);
        CollectionUtils.addAll(ret, AbstractCIDataProvider.COMMANDS);
        CollectionUtils.addAll(ret, AbstractCIDataProvider.SEARCHS);
        CollectionUtils.addAll(ret, AbstractCIDataProvider.MENUS);
        CollectionUtils.addAll(ret, AbstractCIDataProvider.TYPES);
        CollectionUtils.addAll(ret, AbstractCIDataProvider.STATUSGRPS);
        CollectionUtils.addAll(ret, AbstractCIDataProvider.NUMGENS);
        CollectionUtils.addAll(ret, AbstractCIDataProvider.UIIMG);
        CollectionUtils.addAll(ret, AbstractCIDataProvider.JASPERIMG);
        CollectionUtils.addAll(ret, AbstractCIDataProvider.ACCESSSET);
        CollectionUtils.addAll(ret, AbstractCIDataProvider.SQLTABLES);
        CollectionUtils.addAll(ret, AbstractCIDataProvider.MSGPHRASES);
        return ret;
    }

    public static void loadCI(final ITestContext _context)
    {
        if (!LOADED) {
            LOG.info("Loading CIItems for Suite");
            final File xmlFile = new File(_context.getCurrentXmlTest().getSuite().getFileName());
            final String baseFolderRel = _context.getCurrentXmlTest().getParameter("baseFolder");
            final String baseFolder = FilenameUtils.concat(xmlFile.getPath(), baseFolderRel);
            AbstractCIDataProvider.LOG.debug("basefolder: '{}'", baseFolder);
            final Collection<File> files = FileUtils.listFiles(new File(baseFolder), new String[] { "xml" }, true);

            for (final File file : files) {
                AbstractCIDataProvider.LOG.debug("file added: '{}'", file);
                final DigesterLoader loader = DigesterLoader.newLoader(new FromAnnotationsRuleModule()
                {

                    @Override
                    protected void configureRules()
                    {
                        bindRulesFrom(CIForm.class);
                        bindRulesFrom(CITable.class);
                        bindRulesFrom(CICommand.class);
                        bindRulesFrom(CISearch.class);
                        bindRulesFrom(CIMenu.class);
                        bindRulesFrom(CIType.class);
                        bindRulesFrom(CIStatusGroup.class);
                        bindRulesFrom(CINumberGenerator.class);
                        bindRulesFrom(CIUIImage.class);
                        bindRulesFrom(CIJasperImage.class);
                        bindRulesFrom(CIAccessSet.class);
                        bindRulesFrom(CISQLTable.class);
                        bindRulesFrom(CIMsgPhrase.class);
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
                    if (item instanceof ICIItem) {
                        ((ICIItem) item).setFile(file.getPath());
                    }

                    if (item instanceof CIForm) {
                        AbstractCIDataProvider.LOG.debug("Form added: '{}'", item);
                        AbstractCIDataProvider.FORMS.add((CIForm) item);
                    } else if (item instanceof CITable) {
                        AbstractCIDataProvider.LOG.debug("Table added: '{}'", item);
                        AbstractCIDataProvider.TABLES.add((CITable) item);
                    } else if (item instanceof CICommand) {
                        AbstractCIDataProvider.LOG.debug("Command added: '{}'", item);
                        AbstractCIDataProvider.COMMANDS.add((CICommand) item);
                    } else if (item instanceof CIType) {
                        AbstractCIDataProvider.LOG.debug("Type added: '{}'", item);
                        AbstractCIDataProvider.TYPES.add((CIType) item);
                    } else if (item instanceof CIStatusGroup) {
                        AbstractCIDataProvider.LOG.debug("CIStatusGroup added: '{}'", item);
                        AbstractCIDataProvider.STATUSGRPS.add((CIStatusGroup) item);
                    } else if (item instanceof CIMenu) {
                        AbstractCIDataProvider.LOG.debug("CIMenu added: '{}'", item);
                        AbstractCIDataProvider.MENUS.add((CIMenu) item);
                    } else if (item instanceof CINumberGenerator) {
                        AbstractCIDataProvider.LOG.debug("CINumberGenerator added: '{}'", item);
                        AbstractCIDataProvider.NUMGENS.add((CINumberGenerator) item);
                    } else if (item instanceof CIJasperImage) {
                        AbstractCIDataProvider.LOG.debug("CIJasperImage added: '{}'", item);
                        AbstractCIDataProvider.JASPERIMG.add((CIJasperImage) item);
                    } else if (item instanceof CIUIImage) {
                        AbstractCIDataProvider.LOG.debug("CIUIImage added: '{}'", item);
                        AbstractCIDataProvider.UIIMG.add((CIUIImage) item);
                    } else if (item instanceof CIAccessSet) {
                        AbstractCIDataProvider.LOG.debug("CIAccessSet added: '{}'", item);
                        AbstractCIDataProvider.ACCESSSET.add((CIAccessSet) item);
                    } else if (item instanceof CISearch) {
                        AbstractCIDataProvider.LOG.debug("CISearch added: '{}'", item);
                        AbstractCIDataProvider.SEARCHS.add((CISearch) item);
                    } else if (item instanceof CISQLTable) {
                        AbstractCIDataProvider.LOG.debug("CISearch added: '{}'", item);
                        AbstractCIDataProvider.SQLTABLES.add((CISQLTable) item);
                    } else if (item instanceof CIMsgPhrase) {
                        AbstractCIDataProvider.LOG.debug("CIMsgPhrase added: '{}'", item);
                        AbstractCIDataProvider.MSGPHRASES.add((CIMsgPhrase) item);
                    }
                } catch (final MalformedURLException e) {
                    AbstractCIDataProvider.LOG.error("MalformedURLException", e);
                } catch (final IOException e) {
                    AbstractCIDataProvider.LOG.error("IOException", e);
                } catch (final SAXException e) {
                    AbstractCIDataProvider.LOG.error("SAXException", e);
                }
            }

            final Collection<File> propFiles = FileUtils.listFiles(new File(baseFolder), new String[] { "properties" },
                            true);
            for (final File file : propFiles) {
                final Properties props = new Properties();
                try {
                    props.load(new FileInputStream(file));
                    AbstractCIDataProvider.LOG.debug("properties loaded: '{}'", file);
                } catch (final IOException e) {
                    AbstractCIDataProvider.LOG.error("IOException", e);
                }
                AbstractCIDataProvider.DBPROPERTIES.putAll(props);
            }

            try {
                final InputStream ignStream = AbstractCIDataProvider.class.getResourceAsStream("/Ignore.properties");
                if (ignStream != null) {
                    final Properties ignoreProps = new Properties();
                    ignoreProps.load(ignStream);
                    AbstractCIDataProvider.DBPROPERTIES.putAll(ignoreProps);
                }
            } catch (final IOException e) {
                AbstractCIDataProvider.LOG.error("IOException", e);
            }
            LOADED = true;
        }
    }
}
