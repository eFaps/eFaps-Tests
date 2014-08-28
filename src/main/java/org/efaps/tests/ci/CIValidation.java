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

package org.efaps.tests.ci;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.efaps.ci.CIAttribute;
import org.efaps.ci.CIType;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class CIValidation
{

    /**
     *
     */
    @Test(description = "Check that UUIDs are unique and valid")
    public void uniqueUUID()
    {
        final Set<ICIItem> items = AbstractCIDataProvider.getCIItems();
        final Set<UUID> uuids = new HashSet<>();
        for (final ICIItem item : items) {
            final UUID uuid = UUID.fromString(item.getUuid());
            Assert.assertFalse(uuids.contains(uuid), String.format("Item: '%s' has duplicated UUID", item));
        }
    }

    /**
     * @param _context context of the testrunner
     *
     */
    @Test(description = "Validate DBProperties for Types.")
    public void typeLabels(final ITestContext _context)
    {
        try {
            final SoftAssert softAssert = new SoftAssert();
            final String ciClass = _context.getCurrentXmlTest().getParameter("ciClass");
            final String regex4TypeLabelExclude = _context.getCurrentXmlTest().getParameter("regex4TypeLabelExclude");
            if (ciClass != null) {
                Pattern pattern = null;
                if (regex4TypeLabelExclude != null) {
                    pattern = Pattern.compile(regex4TypeLabelExclude);
                }
                final Class<?> clazz = Class.forName(ciClass);
                final Map<String, String> mapping = new HashMap<>();
                for (final org.efaps.tests.ci.digester.CIType type : AbstractCIDataProvider.TYPES) {
                    mapping.put(type.getUuid(), type.getDefinitions().get(0).getName());
                }
                for (final Field field : clazz.getDeclaredFields()) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        final Object typeObj = field.get(null);
                        if (typeObj instanceof CIType) {
                            final UUID uuid = ((CIType) typeObj).uuid;
                            final String typeName = mapping.get(uuid.toString());
                            if (typeName != null) {
                                for (final Field attrField : typeObj.getClass().getFields()) {
                                    final Object attrObj = attrField.get(typeObj);
                                    if (attrObj instanceof CIAttribute) {
                                        final String name = ((CIAttribute) attrObj).name;
                                        switch (name) {
                                            case "ID":
                                            case "OID":
                                                break;
                                            default:
                                                final String key = typeName + "/" + name + ".Label";
                                                boolean exclude = false;
                                                if (pattern != null) {
                                                    final Matcher matcher = pattern.matcher(key);
                                                    exclude = matcher.find();
                                                }
                                                if (!exclude) {
                                                    softAssert.assertTrue(
                                                                AbstractCIDataProvider.DBPROPERTIES.containsKey(key),
                                                                String.format("\nmissing Label: '%s'", key));
                                                }
                                                break;
                                        }

                                    }
                                }
                            }
                        }
                    }
                }
            }
            softAssert.assertAll();
        } catch (final ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
