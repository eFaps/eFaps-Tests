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


package org.efaps.tests.ci.digester;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetNext;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.efaps.tests.ci.ICIItem;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id$
 */
@ObjectCreate(pattern = "ui-form")
public class CIForm
    implements ICIItem
{
    /**
     * The uuid of this form.
     */
    @BeanPropertySetter(pattern = "ui-form/uuid")
    private String uuid;

    /**
     * The definitions belonging to this form.
     */
    private final List<CIFormDefinition> definitions = new ArrayList<>();

    /**
     * Getter method for the instance variable {@link #uuid}.
     *
     * @return value of instance variable {@link #uuid}
     */
    public String getUuid()
    {
        return this.uuid;
    }

    /**
     * @param _definition definition to be added
     */
    @SetNext
    public void addDefinition(final CIFormDefinition _definition)
    {
        this.definitions.add(_definition);
    }

    /**
     * Setter method for instance variable {@link #uuid}.
     *
     * @param _uuid value for instance variable {@link #uuid}
     */
    public void setUuid(final String _uuid)
    {
        this.uuid = _uuid;
    }

    /**
     * Getter method for the instance variable {@link #definitions}.
     *
     * @return value of instance variable {@link #definitions}
     */
    public List<CIFormDefinition> getDefinitions()
    {
        return this.definitions;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("uuid", getUuid()).toString();
    }
}
