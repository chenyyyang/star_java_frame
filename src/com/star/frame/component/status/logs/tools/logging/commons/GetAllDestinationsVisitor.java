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
package com.star.frame.component.status.logs.tools.logging.commons;

import com.star.frame.component.status.logs.tools.logging.LogDestination;
import com.star.frame.component.status.logs.tools.logging.jdk.Jdk14LoggerAccessor;
import com.star.frame.component.status.logs.tools.logging.log4j.Log4JLoggerAccessor;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class GetAllDestinationsVisitor.
 */
public class GetAllDestinationsVisitor extends AbstractLoggerAccessorVisitor {

  /** The destinations. */
  private final List<LogDestination> destinations = new ArrayList<>();

  /**
   * Gets the destinations.
   *
   * @return the destinations
   */
  public List<LogDestination> getDestinations() {
    return destinations;
  }

  @Override
  public void visit(Log4JLoggerAccessor accessor) {
    destinations.addAll(accessor.getAppenders());
  }

  @Override
  public void visit(Jdk14LoggerAccessor accessor) {
    destinations.addAll(accessor.getHandlers());
  }

}
