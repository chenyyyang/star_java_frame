/**
 * Licensed under the GPL License. You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   https://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * THIS PACKAGE IS PROVIDED "AS IS" AND WITHOUT ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * WITHOUT LIMITATION, THE IMPLIED WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE.
 */
package com.star.frame.component.status.logs.tools;

import java.lang.reflect.Field;

/**
 * The Interface Accessor.
 */
public interface Accessor {

  /**
   * Gets the.
   *
   * @param obj the obj
   * @param field the field
   * @return the object
   */
  Object get(Object obj, Field field);

}
