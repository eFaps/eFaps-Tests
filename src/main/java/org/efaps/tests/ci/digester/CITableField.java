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

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetNext;
import org.apache.commons.digester3.annotations.rules.SetProperty;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 */
@ObjectCreate(pattern = "ui-table/definition/field")
public class CITableField
{

    /** The properties. */
    private final List<CITableFieldProperty> properties = new ArrayList<>();

    /** The properties. */
    private final List<CITableFieldTrigger> triggers = new ArrayList<>();

    /** The name. */
    @SetProperty(pattern = "ui-table/definition/field")
    private String name;

    /** The character. */
    @SetProperty(pattern = "ui-table/definition/field")
    private String character;

    /**
     * Adds the property.
     *
     * @param _property the property
     */
    @SetNext
    public void addProperty(final CITableFieldProperty _property)
    {
        this.properties.add(_property);
    }

    /**
     * Getter method for the instance variable {@link #properties}.
     *
     * @return value of instance variable {@link #properties}
     */
    public List<CITableFieldProperty> getProperties()
    {
        return this.properties;
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
     * Getter method for the instance variable {@link #character}.
     *
     * @return value of instance variable {@link #character}
     */
    public String getCharacter()
    {
        return this.character;
    }

    /**
     * Setter method for instance variable {@link #character}.
     *
     * @param _character value for instance variable {@link #character}
     */
    public void setCharacter(final String _character)
    {
        this.character = _character;
    }

    /**
     * Adds the trigger.
     *
     * @param _trigger the trigger
     */
    @SetNext
    public void addTrigger(final CITableFieldTrigger _trigger)
    {
        this.triggers.add(_trigger);
    }

    /**
     * Gets the properties.
     *
     * @return the properties
     */
    public List<CITableFieldTrigger> getTriggers()
    {
        return this.triggers;
    }

    /**
     * Gets the property.
     *
     * @param _name the name
     * @return the property
     */
    public CITableFieldProperty getProperty(final String _name)
    {

        return IterableUtils.find(getProperties(), new Predicate<CITableFieldProperty>()
        {

            @Override
            public boolean evaluate(final CITableFieldProperty _object)
            {
                return _object.getName().equals(_name);
            }
        });
    }
}
