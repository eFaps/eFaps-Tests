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

package org.efaps.tests.ci.command;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.efaps.tests.ci.AbstractCIDataProvider;
import org.efaps.tests.ci.CICommandDataProvider;
import org.efaps.tests.ci.CIListener;
import org.efaps.tests.ci.digester.CICommand;
import org.efaps.tests.ci.digester.CICommandDefinition;
import org.efaps.tests.ci.digester.CICommandProperty;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@Listeners(CIListener.class)
public class CommandValidation
{

    /**
     * @param _ciCmd Command to be checked.
     */
    @Test(dataProvider = "CICommand", dataProviderClass = CICommandDataProvider.class,
                    description = "Properties must have a value")
    public void propertyValidateHasValue(final CICommand _ciCmd)
    {
        for (final CICommandDefinition def : _ciCmd.getDefinitions()) {

            for (final CICommandProperty property : def.getProperties()) {
                final String msg = String.format("Command: '%s', Property: '%s' missing Value.",
                                def.getName(), property.getName());
                Assert.assertNotEquals(property.getValue(), "", msg);
                Assert.assertNotNull(property.getValue(), msg);
            }
        }
    }


   /**
    * @param _context testcontext
    * @param _ciCmd command to be checked.
    */
    @Test(dataProvider = "CICommand", dataProviderClass = CICommandDataProvider.class,
                    description = "Commands must have a default values in the DBProperties")
    public void labelWithDBProperties(final ITestContext _context,
                                      final CICommand _ciCmd)
    {
        final SoftAssert softassert = new SoftAssert();
        final String regex4FieldLabelExclude = _context.getCurrentXmlTest().getParameter("regex4FieldLabelExclude");
        Pattern pattern = null;
        if (regex4FieldLabelExclude != null) {
            pattern = Pattern.compile(regex4FieldLabelExclude);
        }
        for (final CICommandDefinition def : _ciCmd.getDefinitions()) {
            final List<String> keys = new ArrayList<>();
            boolean needsTitle = def.getTargetForm() != null || def.getTargetTable() != null;
            boolean needsLabel = true;
            boolean needsQuestion = false;
            for (final CICommandProperty property : def.getProperties()) {
                switch (property.getName()) {
                    case "TargetTitle":
                        keys.add(property.getValue());
                        needsTitle = false;
                        break;
                    case "Label":
                        keys.add(property.getValue());
                        needsLabel = false;
                        break;
                    case "AskUser":
                        needsQuestion = "true".equalsIgnoreCase(property.getValue());
                        break;
                    default:
                        break;
                }
            }
            if (needsTitle) {
                keys.add(def.getName() + ".Title");
            }
            if (needsLabel) {
                keys.add(def.getName() + ".Label");
            }
            if (needsQuestion) {
                keys.add(def.getName() + ".Question");
            }
            for (final String key : keys) {
                boolean exclude = false;
                if (pattern != null) {
                    final Matcher matcher = pattern.matcher(key);
                    exclude = matcher.find();
                }
                if (!exclude) {
                    softassert.assertTrue(AbstractCIDataProvider.DBPROPERTIES.containsKey(key),
                                    String.format("Command: '%s', invalid Label: '%s'.",
                                                    def.getName(), key));
                }
            }
        }
        softassert.assertAll();
    }

}
