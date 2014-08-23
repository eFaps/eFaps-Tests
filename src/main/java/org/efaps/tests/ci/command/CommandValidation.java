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

import org.efaps.tests.ci.CICommandDataProvider;
import org.efaps.tests.ci.CIListener;
import org.efaps.tests.ci.digester.CICommand;
import org.efaps.tests.ci.digester.CICommandDefinition;
import org.efaps.tests.ci.digester.CICommandProperty;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

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
}
