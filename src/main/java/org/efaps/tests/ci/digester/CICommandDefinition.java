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
package org.efaps.tests.ci.digester;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetNext;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id: FormCIDefinition.java 7685 2012-06-18 15:34:35Z jan@moxter.net
 *          $
 */

@ObjectCreate(pattern = "ui-command/definition")
public class CICommandDefinition

{

    @BeanPropertySetter(pattern = "ui-command/definition/name")
    private String name;

    @BeanPropertySetter(pattern = "ui-command/definition/target/form")
    private String targetForm;

    @BeanPropertySetter(pattern = "ui-command/definition/target/table")
    private String targetTable;

    private final List<CICommandProperty> properties = new ArrayList<>();


    @SetNext
    public void addProperty(final CICommandProperty _property)
    {
        this.properties.add(_property);
    }

    /**
     * Getter method for the instance variable {@link #name}.
     *
     * @return value of instance variable {@link #name}
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Setter method for instance variable {@link #name}.
     *
     * @param _name value for instance variable {@link #name}
     */
    public void setName(final String _name)
    {
        this.name = _name;
    }

    /**
     * Getter method for the instance variable {@link #properties}.
     *
     * @return value of instance variable {@link #properties}
     */
    public List<CICommandProperty> getProperties()
    {
        return this.properties;
    }


    /**
     * Getter method for the instance variable {@link #targetForm}.
     *
     * @return value of instance variable {@link #targetForm}
     */
    public String getTargetForm()
    {
        return this.targetForm;
    }


    /**
     * Setter method for instance variable {@link #targetForm}.
     *
     * @param _targetForm value for instance variable {@link #targetForm}
     */
    public void setTargetForm(final String _targetForm)
    {
        this.targetForm = _targetForm;
    }


    /**
     * Getter method for the instance variable {@link #targetTable}.
     *
     * @return value of instance variable {@link #targetTable}
     */
    public String getTargetTable()
    {
        return this.targetTable;
    }


    /**
     * Setter method for instance variable {@link #targetTable}.
     *
     * @param _targetTable value for instance variable {@link #targetTable}
     */
    public void setTargetTable(final String _targetTable)
    {
        this.targetTable = _targetTable;
    }
}
