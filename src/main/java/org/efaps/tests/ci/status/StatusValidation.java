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


package org.efaps.tests.ci.status;

import org.efaps.tests.ci.AbstractCIDataProvider;
import org.efaps.tests.ci.CIStatusDataProvider;
import org.efaps.tests.ci.digester.CIStatus;
import org.efaps.tests.ci.digester.CIStatusGroup;
import org.efaps.tests.ci.digester.CIStatusGroupDefinition;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class StatusValidation
{
    /**
     * @param _ciForm form to be checked.
     */
    @Test(dataProvider = "CIStatus", dataProviderClass = CIStatusDataProvider.class,
          description = "Status  must have a value for each key in the DBProperties")
    public void statusWithDBProperties(final ITestContext _context,
                                       final CIStatusGroup _ciStatusGroup)
    {
        for (final CIStatusGroupDefinition def : _ciStatusGroup.getDefinitions()) {
            for (final CIStatus status : def.getStatus()) {
                final String dbKey = def.getName() + "/Key.Status." + status.getKey();
                    Assert.assertTrue(AbstractCIDataProvider.DBPROPERTIES.containsKey(dbKey),
                                        String.format("StatusGroup: '%s', Status: '%s' missing Label: '%s'.",
                                                        def.getName(), status.getKey(), dbKey));
            }
        }
    }
}
