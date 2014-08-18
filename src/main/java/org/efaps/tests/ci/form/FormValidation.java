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

package org.efaps.tests.ci.form;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.annotations.FromAnnotationsRuleModule;
import org.apache.commons.digester3.binder.DigesterLoader;
import org.efaps.tests.ci.CIFormDataProvider;
import org.efaps.tests.ci.digester.CIForm;
import org.efaps.tests.ci.digester.CIFormDefinition;
import org.efaps.tests.ci.digester.CIFormField;
import org.efaps.tests.ci.digester.CIProperty;
import org.efaps.update.FileType;
import org.efaps.update.Install.InstallFile;
import org.efaps.update.util.InstallationException;
import org.efaps.update.version.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class FormValidation
{

    /**
     * Logging instance used to give logging information of this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(FormValidation.class);

    SoftAssert softAssert = new SoftAssert();


    public void form()
    {
        //final File jar = new File(
       //                 "//home/janmoxter/.m2/repository/org/efaps/apps/accounting/3.2.0-SNAPSHOT/accounting-3.2.0-SNAPSHOT.jar");
        final File jar = new File(
                        "//home/janmoxter/.m2/repository/org/efaps/apps/sales/3.2.0-SNAPSHOT/sales-3.2.0-SNAPSHOT.jar");

        try {
            final Application appl = Application.getApplicationFromJarFile(jar, null);

            final List<InstallFile> files = appl.getInstall().getFiles();
            for (final InstallFile file : files) {
                LOG.info("reading file: '{}'", file);
                if (FileType.XML.equals(file.getType())) {
                    final DigesterLoader loader = DigesterLoader.newLoader(new FromAnnotationsRuleModule()
                    {

                        @Override
                        protected void configureRules()
                        {
                            bindRulesFrom(CIForm.class);
                        }
                    });
                    final Digester digester = loader.newDigester();
                    final URLConnection connection = file.getUrl().openConnection();
                    connection.setUseCaches(false);
                    final InputStream stream = connection.getInputStream();
                    final InputSource source = new InputSource(stream);
                    final Object item = digester.parse(source);
                    stream.close();
                    if (item instanceof CIForm) {
                        hasDataField((CIForm) item);
                    }
                }
            }

        } catch (final InstallationException e) {
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


    @Test(dataProvider = "CIForm",  dataProviderClass = CIFormDataProvider.class)
    public void hasDataField(final CIForm _ciForm)
    {
        for (final CIFormDefinition def : _ciForm.getDefinitions()) {
            for (final CIFormField field : def.getFields()) {
                if (field.getCharacter() == null) {
                    boolean has = false;
                    for (final CIProperty property : field.getProperties()) {
                        switch (property.getName()) {
                            case "UIType":
                            case "UIProvider":
                            case "ClassNameUI":
                            case "Attribute":
                            case "Select":
                                has = true;
                                break;
                            default:
                                break;
                        }
                        if (has) {
                            break;
                        }
                    }
                    this.softAssert.assertEquals(has, true,
                                    String.format("\nForm: '%s', Field: '%s' has no DataField Definition.",
                                                    def.getName(), field.getName()));
                }
            }
        }
    }
    @AfterSuite
    public void printResults()
    {
        this.softAssert.assertAll();
    }

}
