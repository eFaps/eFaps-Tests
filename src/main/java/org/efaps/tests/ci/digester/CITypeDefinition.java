/*
 * Copyright 2003 - 2011 The eFaps Team
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

import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetProperty;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 * @version $Id: FormCIDefinition.java 7685 2012-06-18 15:34:35Z jan@moxter.net
 *          $
 */

@ObjectCreate(pattern = "datamodel-type/definition")
public class CITypeDefinition

{

    @BeanPropertySetter(pattern = "datamodel-type/definition/name")
    private String name;

    @SetProperty(pattern = "datamodel-type/definition/purpose", attributeName="abstract")
    private boolean abstr;

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
     * Getter method for the instance variable {@link #abstr}.
     *
     * @return value of instance variable {@link #abstr}
     */
    public boolean isAbstr()
    {
        return this.abstr;
    }


    /**
     * Setter method for instance variable {@link #abstr}.
     *
     * @param _abstr value for instance variable {@link #abstr}
     */
    public void setAbstr(final boolean _abstr)
    {
        this.abstr = _abstr;
    }
}
