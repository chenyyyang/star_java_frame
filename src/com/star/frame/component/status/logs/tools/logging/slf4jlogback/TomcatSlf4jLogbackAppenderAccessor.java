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
package com.star.frame.component.status.logs.tools.logging.slf4jlogback;

import com.star.frame.component.status.logs.tools.logging.AbstractLogDestination;

import java.io.File;

/**
 * A wrapper for a TomcatSlf4jLogback appender for a specific logger.
 */
public class TomcatSlf4jLogbackAppenderAccessor extends AbstractLogDestination {

  /** The logger accessor. */
  private TomcatSlf4jLogbackLoggerAccessor loggerAccessor;

  /**
   * Gets the logger accessor.
   *
   * @return the logger accessor
   */
  public TomcatSlf4jLogbackLoggerAccessor getLoggerAccessor() {
    return loggerAccessor;
  }

  /**
   * Sets the logger accessor.
   *
   * @param loggerAccessor the new logger accessor
   */
  public void setLoggerAccessor(TomcatSlf4jLogbackLoggerAccessor loggerAccessor) {
    this.loggerAccessor = loggerAccessor;
  }

  @Override
  public boolean isContext() {
    return getLoggerAccessor().isContext();
  }

  @Override
  public boolean isRoot() {
    return getLoggerAccessor().isRoot();
  }

  @Override
  public String getName() {
    return getLoggerAccessor().getName();
  }

  /**
   * Returns the log type, to distinguish tomcatSlf4jLogback appenders from other types like log4j
   * appenders or jdk handlers.
   * 
   * @return the log type
   */
  @Override
  public String getLogType() {
    return "tomcatSlf4jLogback";
  }

  /**
   * Returns the name of this appender.
   * 
   * @return the name of this appender.
   */
  @Override
  public String getIndex() {
    return (String) getProperty(getTarget(), "name", null);
  }

  /**
   * Returns the file that this appender writes to by accessing the {@code file} bean property of
   * the appender.
   * 
   * <p>
   * If no such property exists, we assume the appender to write to stdout or stderr so the output
   * will be contained in catalina.out.
   * </p>
   * 
   * @return the file this appender writes to
   */
  @Override
  public File getFile() {
    String fileName = (String) getProperty(getTarget(), "file", null);
    return fileName != null ? new File(fileName) : getStdoutFile();
  }

  /**
   * Gets the level of the associated logger.
   * 
   * @return the logger's level
   */
  @Override
  public String getLevel() {
    return getLoggerAccessor().getLevel();
  }

  /**
   * Returns the valid log level names.
   * 
   * <p>
   * Note that Logback has no FATAL level.
   * </p>
   * 
   * @return the valid log level names
   */
  @Override
  public String[] getValidLevels() {
    return new String[] {"OFF", "ERROR", "WARN", "INFO", "DEBUG", "TRACE", "ALL"};
  }

}
