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
package org.efaps.tests.others;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.efaps.tests.ci.AbstractCIDataProvider;
import org.efaps.tests.ci.ICIItem;
import org.efaps.tests.program.CSSFileProvider;
import org.efaps.tests.program.JasperFileProvider;
import org.efaps.update.Install.InstallFile;
import org.efaps.update.schema.program.jasperreport.JasperReportImporter;
import org.efaps.update.schema.program.staticsource.CSSImporter;
import org.efaps.update.util.InstallationException;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

/**
 * The Class GeneralTests.
 *
 * @author The eFaps Team
 */
public class GeneralTests
{

    /**
     * Check that UUIDs are unique and valid.
     *
     * @param _context the context
     * @throws InstallationException the installation exception
     * @throws MalformedURLException the malformed URL exception
     */
    @Test(description = "Check that UUIDs are unique and valid")
    public void uniqueUUID(final ITestContext _context)
        throws InstallationException, MalformedURLException
    {
        final Set<ICIItem> items = AbstractCIDataProvider.getCIItems();
        final var uuids = new HashMap<UUID, ICIItem>();
        for (final ICIItem item : items) {
            final UUID uuid = UUID.fromString(item.getUuid());
            Assert.assertFalse(uuids.containsKey(uuid),
                String.format("Item: '%s' has duplicated UUID: '%s' with '%s'", item, uuid, uuids.get(uuid)));
            uuids.put(uuid, item);
        }

        final Iterator<Object[]> jasperFilesIter = JasperFileProvider.jasperFiles(_context);
        while (jasperFilesIter.hasNext()) {
            final Object[] object = jasperFilesIter.next();
            final File file = (File) object[0];
            final InstallFile installFile = new InstallFile().setURL(file.toURI().toURL());
            final JasperReportImporter importer = new JasperReportImporter(installFile);
            final UUID uuid = importer.getEFapsUUID();
            Assert.assertFalse(uuids.containsKey(uuid),
                            String.format("Jasper: '%s' has duplicated UUID '%s'", file, uuid));
            uuids.put(uuid, null);
        }

        final Iterator<Object[]> cssFilesIter = CSSFileProvider.cssFiles(_context);
        while (cssFilesIter.hasNext()) {
            final Object[] object = cssFilesIter.next();
            final File file = (File) object[0];
            final InstallFile installFile = new InstallFile().setURL(file.toURI().toURL());
            final CSSImporter importer = new CSSImporter(installFile);
            final UUID uuid = importer.getEFapsUUID();
            Assert.assertFalse(uuids.containsKey(uuid), String.format("CSS: '%s' has duplicated UUID '%s'", file, uuid));
            uuids.put(uuid, null);
        }
    }
}
