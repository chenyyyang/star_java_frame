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
package com.star.frame.component.status.logs.model.jsp;

import java.io.Serializable;
import java.util.Map;

/**
 * The Class Summary.
 */
public class Summary implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The name. */
  private String name;

  /** The out of date count. */
  private int outOfDateCount;

  /**
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name the new name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets the out of date count.
   *
   * @return the out of date count
   */
  public int getOutOfDateCount() {
    return outOfDateCount;
  }

  /**
   * Sets the out of date count.
   *
   * @param outOfDateCount the new out of date count
   */
  public void setOutOfDateCount(int outOfDateCount) {
    this.outOfDateCount = outOfDateCount;
  }

}
