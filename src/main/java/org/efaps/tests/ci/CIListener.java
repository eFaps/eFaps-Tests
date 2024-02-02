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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.collections4.comparators.ComparatorChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;
import org.testng.annotations.Test;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class CIListener
    extends TestListenerAdapter
{

    /**
     * Logger for this class.
     */
    private static final Logger LOG = LoggerFactory.getLogger(CIListener.class);

    @Override
    public void onFinish(final ITestContext _testContext)
    {
        if (!getFailedTests().isEmpty()) {
            final List<ITestResult> failed = getFailedTests();
            final ComparatorChain<ITestResult> chain = new ComparatorChain<>();
            chain.addComparator(new Comparator<ITestResult>()
            {
                @Override
                public int compare(final ITestResult _o1,
                                   final ITestResult _o2)
                {
                    return _o1.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class)
                                    .description()
                                    .compareTo(_o2.getMethod().getConstructorOrMethod().getMethod()
                                                    .getAnnotation(Test.class).description());
                }
            });
            chain.addComparator(new Comparator<ITestResult>()
            {
                @Override
                public int compare(final ITestResult _o1,
                                   final ITestResult _o2)
                {
                    return _o1.getThrowable().getMessage().compareTo(_o2.getThrowable().getMessage());
                }
            });

            Collections.sort(failed, chain);

            LOG.warn("*************************************************************");
            LOG.warn("***************** Failed CI Tests ***************************");
            String currentDesc = "";
            for (final ITestResult result : failed) {
                final String descr = result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(Test.class)
                .description();
                if (!currentDesc.equals(descr)) {
                    currentDesc = descr;
                    LOG.warn("***** "+ descr);
                }
                LOG.warn(result.getThrowable().getMessage());
            }
            LOG.warn("*************************************************************");
            LOG.warn("*************************************************************");
        }
    }
}
