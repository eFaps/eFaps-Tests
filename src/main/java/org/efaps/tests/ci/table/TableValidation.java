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


package org.efaps.tests.ci.table;

import org.apache.commons.lang3.EnumUtils;
import org.efaps.admin.ui.field.Field.Display;
import org.efaps.tests.ci.CITableDataProvider;
import org.efaps.tests.ci.digester.CITable;
import org.efaps.tests.ci.digester.CITableDefinition;
import org.efaps.tests.ci.digester.CITableField;
import org.efaps.tests.ci.digester.CITableProperty;
import org.testng.Assert;
import org.testng.annotations.Test;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class TableValidation
{
    /**
     * @param _ciForm form to be checked.
     */
    @Test(dataProvider = "CITable",  dataProviderClass = CITableDataProvider.class,
          description = "Properties must have a value")
    public void propertyValidateHasValue(final CITable _ciTable)
    {
        for (final CITableDefinition def : _ciTable.getDefinitions()) {
            for (final CITableField field : def.getFields()) {
                for (final CITableProperty property : field.getProperties()) {
                    final String msg = String.format("Table: '%s', Field: '%s', Property: '%s' missing Value.",
                                    def.getName(), field.getName(), property.getName());
                    Assert.assertNotEquals(property.getValue(), "", msg);
                    Assert.assertNotNull(property.getValue(), msg);
                }
            }
        }
    }

    /**
     * @param _ciForm form to be checked.
     */
    @Test(dataProvider = "CITable",  dataProviderClass = CITableDataProvider.class,
          description = "Property 'ModeCreate', 'ModeEdit', 'ModeView', 'ModePrint', 'ModeSearch'"
                                    + " must have a value from ENUM org.efaps.admin.ui.field.Field.Display")
    public void fieldValidateMode(final CITable _ciTable)
    {
        for (final CITableDefinition def : _ciTable.getDefinitions()) {
            for (final CITableField field : def.getFields()) {
                for (final CITableProperty property : field.getProperties()) {
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
}
