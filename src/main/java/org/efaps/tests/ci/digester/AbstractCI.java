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

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.efaps.tests.ci.ICIItem;

/**
 * The Class AbstractCI.
 *
 * @author The eFaps Team
 */
public abstract class AbstractCI
    implements ICIItem
{

    /** The file. */
    private String file;

    /**
     * Gets the file.
     *
     * @return the file
     */
    @Override
    public String getFile()
    {
        return this.file;
    }

    /**
     * Sets the file.
     *
     * @param _file the new file
     */
    public void setFile(final String _file)
    {
        this.file = _file;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this)
                        .append("file", getFile()).toString();
    }
}
