/*
 * Copyright © 2003 - 2024 The eFaps Team (-)
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
 */
package org.efaps.tests.ci;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.efaps.tests.ci.digester.CINumberGenerator;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 */

public class CINumberGeneratorDataProvider
    extends AbstractCIDataProvider
{

    /**
     * @param _context context
     * @return iterator with ciform
     */
    @DataProvider(name = "CINumberGenerator")
    public static Iterator<Object[]> ciTables(final ITestContext _context)
    {
        final List<Object[]> ret = new ArrayList<>();

        loadCI(_context);
        for (final CINumberGenerator ciNumGen : AbstractCIDataProvider.NUMGENS) {
            ret.add(new Object[] { ciNumGen });
        }
        return ret.iterator();
    }
}
