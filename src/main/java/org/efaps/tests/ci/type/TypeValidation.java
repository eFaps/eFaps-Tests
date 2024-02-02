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
package org.efaps.tests.ci.type;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.efaps.ci.CIAttribute;
import org.efaps.ci.CIType;
import org.efaps.tests.ci.AbstractCIDataProvider;
import org.testng.ITestContext;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
public class TypeValidation
{

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
                final Map<String, org.efaps.tests.ci.digester.CIType> mapping = new HashMap<>();
                for (final org.efaps.tests.ci.digester.CIType type : AbstractCIDataProvider.TYPES) {
                    mapping.put(type.getUuid(), type);
                }
                for (final Field field : clazz.getDeclaredFields()) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        final Object typeObj = field.get(null);
                        if (typeObj instanceof CIType) {
                            final UUID uuid = ((CIType) typeObj).uuid;
                            final org.efaps.tests.ci.digester.CIType ciType = mapping.get(uuid.toString());
                            if (ciType != null) {
                                final String typeName = ciType.getDefinitions().get(0).getName();
                                final String typeKey =  typeName + ".Label";
                                boolean excludeTypeKey = false;
                                if (pattern != null) {
                                    final Matcher matcher = pattern.matcher(typeKey);
                                    excludeTypeKey = matcher.find();
                                }
                                if (!excludeTypeKey && !ciType.getDefinitions().get(0).isAbstr()) {
                                    softAssert.assertTrue(
                                                    AbstractCIDataProvider.DBPROPERTIES.containsKey(typeKey),
                                                    String.format("\nmissing Label: '%s'", typeKey));
                                }
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
                                                                    AbstractCIDataProvider.DBPROPERTIES
                                                                                    .containsKey(key),
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
