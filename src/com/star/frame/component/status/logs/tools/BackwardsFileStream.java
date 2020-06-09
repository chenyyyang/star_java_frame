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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * The Class BackwardsFileStream.
 */
public class BackwardsFileStream extends InputStream {

  /** The raf. */
  private final RandomAccessFile raf;

  /** The seek pos. */
  private long seekPos;

  /**
   * Instantiates a new backwards file stream.
   *
   * @param file the file
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public BackwardsFileStream(File file) throws IOException {
    raf = new RandomAccessFile(file, "r");
    seekPos = raf.length();
  }

  /**
   * Instantiates a new backwards file stream.
   *
   * @param file the file
   * @param pos the pos
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public BackwardsFileStream(File file, long pos) throws IOException {
    raf = new RandomAccessFile(file, "r");
    seekPos = pos;
  }

  @Override
  public int read() throws IOException {
    if (seekPos > 0) {
      raf.seek(--seekPos);
      return raf.read();
    }
    // return EOF (so to speak)
    return -1;
  }

  @Override
  public void close() throws IOException {
    if (raf != null) {
      raf.close();
    }
  }

}
