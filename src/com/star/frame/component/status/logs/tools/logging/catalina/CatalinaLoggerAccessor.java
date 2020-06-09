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
package com.star.frame.component.status.logs.tools.logging.catalina;

import com.star.frame.component.status.logs.tools.Instruments;
import com.star.frame.component.status.logs.tools.logging.AbstractLogDestination;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Class CatalinaLoggerAccessor.
 */
public class CatalinaLoggerAccessor extends AbstractLogDestination {

  @Override
  public boolean isContext() {
    return true;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public String getLogType() {
    return "catalina";
  }

  @Override
  public File getFile() {
    String dir = (String) invokeMethod(getTarget(), "getDirectory", null, null);
    String prefix = (String) invokeMethod(getTarget(), "getPrefix", null, null);
    String suffix = (String) invokeMethod(getTarget(), "getSuffix", null, null);
    boolean timestamp = (Boolean) Instruments.getField(getTarget(), "timestamp");
    String date = timestamp ? new SimpleDateFormat("yyyy-MM-dd").format(new Date()) : "";

    File file =
        date != null && dir != null && prefix != null && suffix != null ? new File(dir, prefix
            + date + suffix) : null;
    if (file != null && !file.isAbsolute()) {
      return new File(System.getProperty("catalina.base"), file.getPath());
    }
    return file;
  }

}
