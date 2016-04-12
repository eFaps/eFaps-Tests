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

package org.efaps.tests.ci.form;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.EnumUtils;
import org.efaps.admin.ui.field.Field.Display;
import org.efaps.api.ui.FilterBase;
import org.efaps.api.ui.FilterType;
import org.efaps.api.ui.UIType;
import org.efaps.tests.ci.AbstractCIDataProvider;
import org.efaps.tests.ci.CIFormDataProvider;
import org.efaps.tests.ci.CIListener;
import org.efaps.tests.ci.digester.CIForm;
import org.efaps.tests.ci.digester.CIFormDefinition;
import org.efaps.tests.ci.digester.CIFormField;
import org.efaps.tests.ci.digester.CIFormProperty;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@Listeners(CIListener.class)
public class FormValidation
{

    /**
     * Does the fields of the form have all a data definition.
     * @param _ciForm form to be checked.
     */
    @Test(dataProvider = "CIForm",  dataProviderClass = CIFormDataProvider.class,
          description = "Field must have one of this Properties 'UIType', 'UIProvider', 'Attribute',"
                          + "'Select'")
    public void fieldHasDataConfiguration(final CIForm _ciForm)
    {
        for (final CIFormDefinition def : _ciForm.getDefinitions()) {
            for (final CIFormField field : def.getFields()) {
                if (field.getCharacter() == null) {
                    boolean has = false;
                    for (final CIFormProperty property : field.getProperties()) {
                        switch (property.getName()) {
                            case "UIType":
                            case "UIProvider":
                            case "Attribute":
                            case "Select":
                                has = true;
                                break;
                            default:
                                break;
                        }
                        if (has) {
                            break;
                        }
                    }
                    Assert.assertEquals(has, true,
                                    String.format("Form: '%s', Field: '%s' has no DataField Definition.",
                                                    def.getName(), field.getName()));
                }
            }
        }
    }

    /**
     * Does the fields of the form have all a data definition.
     * @param _ciForm form to be checked.
     */
    @Test(dataProvider = "CIForm", dataProviderClass = CIFormDataProvider.class,
          description = "Property 'UIType' must have a value from ENUM  org.efaps.api.ui.UIType")
    public void fieldWithUIType(final CIForm _ciForm)
    {
        for (final CIFormDefinition def : _ciForm.getDefinitions()) {
            for (final CIFormField field : def.getFields()) {
                for (final CIFormProperty property : field.getProperties()) {
                    if ("UIType".equals(property.getName())) {
                        final UIType value = EnumUtils.getEnum(UIType.class, property.getValue());
                        Assert.assertNotNull(value,
                                        String.format("Form: '%s', Field: '%s' invalid UIType Definition.",
                                                        def.getName(), field.getName()));
                    }
                }
            }
        }
    }

    /**
     * @param _ciForm form to be checked.
     */
    @Test(dataProvider = "CIForm",  dataProviderClass = CIFormDataProvider.class,
          description = "Property 'ModeCreate', 'ModeEdit', 'ModeView', 'ModePrint', 'ModeSearch'"
                                    + " must have a value from ENUM org.efaps.admin.ui.field.Field.Display")
    public void fieldValidateMode(final CIForm _ciForm)
    {
        for (final CIFormDefinition def : _ciForm.getDefinitions()) {
            for (final CIFormField field : def.getFields()) {
                for (final CIFormProperty property : field.getProperties()) {
                    switch (property.getName()) {
                        case "ModeCreate":
                        case "ModeEdit":
                        case "ModeView":
                        case "ModePrint":
                        case "ModeSearch":
                            final Display value = EnumUtils.getEnum(Display.class, property.getValue());
                            Assert.assertNotNull(value,
                                            String.format("Form: '%s', Field: '%s' invalid Display Definition.",
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
    @Test(dataProvider = "CIForm",  dataProviderClass = CIFormDataProvider.class,
          description = "Properties must have a value")
    public void propertyValidateHasValue(final CIForm _ciForm)
    {
        for (final CIFormDefinition def : _ciForm.getDefinitions()) {
            for (final CIFormField field : def.getFields()) {
                for (final CIFormProperty property : field.getProperties()) {
                    final String msg = String.format("Form: '%s', Field: '%s', Property: '%s' missing Value.",
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
    @Test(dataProvider = "CIForm", dataProviderClass = CIFormDataProvider.class,
          description = "Property 'FilterType' must have a value from ENUM  org.efaps.admin.ui.field.Filter.FilterType")
    public void fieldWithFilterType(final CIForm _ciForm)
    {
        for (final CIFormDefinition def : _ciForm.getDefinitions()) {
            for (final CIFormField field : def.getFields()) {
                for (final CIFormProperty property : field.getProperties()) {
                    if ("FilterType".equals(property.getName())) {
                        final FilterType value = EnumUtils.getEnum(FilterType.class, property.getValue());
                        Assert.assertNotNull(value,
                                        String.format("Form: '%s', Field: '%s' invalid FilterType.",
                                                        def.getName(), field.getName()));
                    }
                }
            }
        }
    }

    /**
     * @param _ciForm form to be checked.
     */
    @Test(dataProvider = "CIForm", dataProviderClass = CIFormDataProvider.class,
          description = "Property 'FilterBase' must have a value from ENUM  org.efaps.admin.ui.field.Filter.Base")
    public void fieldWithFilterBase(final CIForm _ciForm)
    {
        for (final CIFormDefinition def : _ciForm.getDefinitions()) {
            for (final CIFormField field : def.getFields()) {
                for (final CIFormProperty property : field.getProperties()) {
                    if ("FilterBase".equals(property.getName())) {
                        final FilterBase value = EnumUtils.getEnum(FilterBase.class, property.getValue());
                        Assert.assertNotNull(value,
                                        String.format("Form: '%s', Field: '%s' invalid Base.",
                                                        def.getName(), field.getName()));
                    }
                }
            }
        }
    }

    /**
     * @param _ciForm form to be checked.
     */
    @Test(dataProvider = "CIForm", dataProviderClass = CIFormDataProvider.class,
          description = "Property 'Label' must have a value in the DBProperties")
    public void labelWithDBProperties(final ITestContext _context,
                                      final CIForm _ciForm)
    {
        final String regex4FieldLabelExclude = _context.getCurrentXmlTest().getParameter("regex4FieldLabelExclude");
        Pattern pattern = null;
        if (regex4FieldLabelExclude != null) {
            pattern = Pattern.compile(regex4FieldLabelExclude);
        }
        for (final CIFormDefinition def : _ciForm.getDefinitions()) {
            for (final CIFormField field : def.getFields()) {
                for (final CIFormProperty property : field.getProperties()) {
                    if ("Label".equals(property.getName())) {
                        boolean exclude = false;
                        if (pattern != null) {
                            final Matcher matcher = pattern.matcher(property.getValue());
                            exclude = matcher.find();
                        }
                        if (!exclude) {
                            Assert.assertTrue(AbstractCIDataProvider.DBPROPERTIES.containsKey(property.getValue()),
                                        String.format("Form: '%s', Field: '%s' invalid Label: '%s'.",
                                                        def.getName(), field.getName(), property.getValue()));
                        }
                    }
                }
            }
        }
    }

}
