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


package org.efaps.tests.ci.table;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.EnumUtils;
import org.efaps.admin.ui.field.Field.Display;
import org.efaps.tests.ci.AbstractCIDataProvider;
import org.efaps.tests.ci.CITableDataProvider;
import org.efaps.tests.ci.digester.CITable;
import org.efaps.tests.ci.digester.CITableDefinition;
import org.efaps.tests.ci.digester.CITableField;
import org.efaps.tests.ci.digester.CITableFieldProperty;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 */
public class TableValidation
{
    /**
     * @param _ciTable form to be checked.
     */
    @Test(dataProvider = "CITable",  dataProviderClass = CITableDataProvider.class,
          description = "Properties must have a value")
    public void propertyValidateHasValue(final CITable _ciTable)
    {
        for (final CITableDefinition def : _ciTable.getDefinitions()) {
            for (final CITableField field : def.getFields()) {
                for (final CITableFieldProperty property : field.getProperties()) {
                    final String msg = String.format("Table: '%s', Field: '%s', Property: '%s' missing Value.",
                                    def.getName(), field.getName(), property.getName());
                    Assert.assertNotEquals(property.getValue(), "", msg);
                    Assert.assertNotNull(property.getValue(), msg);
                }
            }
        }
    }

    /**
     * @param _ciTable form to be checked.
     */
    @Test(dataProvider = "CITable",  dataProviderClass = CITableDataProvider.class,
          description = "Property 'ModeCreate', 'ModeEdit', 'ModeView', 'ModePrint', 'ModeSearch'"
                                    + " must have a value from ENUM org.efaps.admin.ui.field.Field.Display")
    public void fieldValidateMode(final CITable _ciTable)
    {
        for (final CITableDefinition def : _ciTable.getDefinitions()) {
            for (final CITableField field : def.getFields()) {
                for (final CITableFieldProperty property : field.getProperties()) {
                    switch (property.getName()) {
                        case "ModeCreate":
                        case "ModeEdit":
                        case "ModeView":
                        case "ModePrint":
                        case "ModeSearch":
                            final Display value = EnumUtils.getEnum(Display.class, property.getValue());
                            Assert.assertNotNull(value,
                                            String.format("Table: '%s', Field: '%s' invalid Display Definition.",
                                                            def.getName(), field.getName()));
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    /**
     * @param _ciForm form to be checked.
     */
    @Test(dataProvider = "CITable", dataProviderClass = CITableDataProvider.class,
          description = "Property 'Label' must have a value in the DBProperties")
    public void labelWithDBProperties(final ITestContext _context,
                                      final CITable _ciTable)
    {
        final String regex4FieldLabelExclude = _context.getCurrentXmlTest().getParameter("regex4FieldLabelExclude");
        Pattern pattern = null;
        if (regex4FieldLabelExclude != null) {
            pattern = Pattern.compile(regex4FieldLabelExclude);
        }
        for (final CITableDefinition def : _ciTable.getDefinitions()) {
            for (final CITableField field : def.getFields()) {
                for (final CITableFieldProperty property : field.getProperties()) {
                    if ("Label".equals(property.getName())) {
                        boolean exclude = false;
                        if (pattern != null) {
                            final Matcher matcher = pattern.matcher(property.getValue());
                            exclude = matcher.find();
                        }
                        if (!exclude) {
                            Assert.assertTrue(AbstractCIDataProvider.DBPROPERTIES.containsKey(property.getValue()),
                                        String.format("Table: '%s', Field: '%s' invalid Label: '%s'.",
                                                        def.getName(), field.getName(), property.getValue()));
                        }
                    }
                }
            }
        }
    }

    /**
     * Does the fields of the form have all a data definition.
     * @param _ciForm form to be checked.
     */
    @Test(dataProvider = "CITable",  dataProviderClass = CITableDataProvider.class,
          description = "Field must have one of this Properties 'UIType', 'UIProvider', 'Attribute', 'Select'")
    public void fieldHasDataConfiguration(final CITable _ciTable)
    {
        for (final CITableDefinition def : _ciTable.getDefinitions()) {
            for (final CITableField field : def.getFields()) {
                if (field.getCharacter() == null) {
                    boolean has = false;
                    boolean hasRef = false;
                    for (final CITableFieldProperty property : field.getProperties()) {
                        switch (property.getName()) {
                            case "UIType":
                            case "UIProvider":
                            case "Attribute":
                            case "Select":
                                has = true;
                                break;
                            case "HRef":
                                hasRef = true;
                                break;
                            default:
                                break;
                        }
                        if (has) {
                            break;
                        }
                    }
                    // if a href was set and no value it is a simple link and can be ignored
                    if (!has && hasRef) {
                        has = field.getTriggers().isEmpty();
                    }
                    Assert.assertEquals(has, true,
                                    String.format("Table: '%s', Field: '%s' has no DataField Definition.",
                                                    def.getName(), field.getName()));
                }
            }
        }
    }
}
