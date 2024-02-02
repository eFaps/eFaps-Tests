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
package org.efaps.tests.ci;


/**
 * TODO comment!
 *
 * @author The eFaps Team
 */
public interface ICIItem
{

    /**
     * Gets the uuid.
     *
     * @return the uuid
     */
    String getUuid();

    /**
     * Gets the application.
     *
     * @return the application
     */
    String getApplication();

    /**
     * Gets the file .
     *
     * @return the file
     */
    String getFile();

    /**
     * Sets the file.
     *
     * @param _file the new file
     */
    void setFile(final String _file);
}
