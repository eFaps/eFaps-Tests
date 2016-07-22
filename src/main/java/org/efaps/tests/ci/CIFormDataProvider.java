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

package org.efaps.tests.ci;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.efaps.tests.ci.digester.CIForm;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 */

public class CIFormDataProvider
    extends AbstractCIDataProvider
{

    /**
     * @param _context context
     * @return iterator with ciform
     */
    @DataProvider(name = "CIForm")
    public static Iterator<Object[]> ciForms(final ITestContext _context)
    {
        final List<Object[]> ret = new ArrayList<>();

        if (AbstractCIDataProvider.getCIItems().isEmpty()) {
            loadCI(_context);
        }
        for (final CIForm ciform : AbstractCIDataProvider.FORMS) {
            ret.add(new Object[] { ciform });
        }
        return ret.iterator();
    }
}
