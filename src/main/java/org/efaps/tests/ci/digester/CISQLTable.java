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


package org.efaps.tests.ci.digester;

import org.apache.commons.digester3.annotations.rules.BeanPropertySetter;
import org.apache.commons.digester3.annotations.rules.ObjectCreate;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 */
@ObjectCreate(pattern = "datamodel-sqltable")
public class CISQLTable
    extends AbstractCI
{
    /**
     * The uuid of this command.
     */
    @BeanPropertySetter(pattern = "datamodel-sqltable/uuid")
    private String uuid;

    /** The file application. */
    @BeanPropertySetter(pattern = "datamodel-sqltable/file-application")
    private String application;

    /**
     * Getter method for the instance variable {@link #uuid}.
     *
     * @return value of instance variable {@link #uuid}
     */
    @Override
    public String getUuid()
    {
        return this.uuid;
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
     * Gets the file application.
     *
     * @return the file application
     */
    @Override
    public String getApplication()
    {
        return this.application;
    }

    /**
     * Sets the file application.
     *
     * @param _fileApplication the new file application
     */
    public void setApplication(final String _fileApplication)
    {
        this.application = _fileApplication;
    }
}
