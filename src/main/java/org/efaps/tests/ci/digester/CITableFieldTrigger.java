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

import org.apache.commons.digester3.annotations.rules.ObjectCreate;
import org.apache.commons.digester3.annotations.rules.SetProperty;

/**
 * TODO comment!
 *
 * @author The eFaps Team
 */
@ObjectCreate(pattern = "ui-table/definition/field/trigger")
public class CITableFieldTrigger
{

    /** The program. */
    @SetProperty(pattern = "ui-table/definition/field/trigger")
    private String program;

    /** The method. */
    @SetProperty(pattern = "ui-table/definition/field/trigger")
    private String method;

    /** The name. */
    @SetProperty(pattern = "ui-table/definition/field/trigger")
    private String name;

    /** The event. */
    @SetProperty(pattern = "ui-table/definition/field/trigger")
    private String event;

    /**
     * Gets the program.
     *
     * @return the program
     */
    public String getProgram()
    {
        return this.program;
    }

    /**
     * Sets the program.
     *
     * @param _program the new program
     */
    public void setProgram(final String _program)
    {
        this.program = _program;
    }

    /**
     * Gets the method.
     *
     * @return the method
     */
    public String getMethod()
    {
        return this.method;
    }

    /**
     * Sets the method.
     *
     * @param _method the new method
     */
    public void setMethod(final String _method)
    {
        this.method = _method;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Sets the name.
     *
     * @param _name the new name
     */
    public void setName(final String _name)
    {
        this.name = _name;
    }

    /**
     * Gets the event.
     *
     * @return the event
     */
    public String getEvent()
    {
        return this.event;
    }

    /**
     * Sets the event.
     *
     * @param _event the new event
     */
    public void setEvent(final String _event)
    {
        this.event = _event;
    }
}
