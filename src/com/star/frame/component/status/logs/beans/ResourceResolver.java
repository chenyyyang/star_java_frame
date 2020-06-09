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
package com.star.frame.component.status.logs.beans;

import org.apache.catalina.Context;

/**
 * Interface of beans that retrieve information about "resources" of application server. Typically
 * those resources would be datasources.
 */
public interface ResourceResolver {

  /**
   * Standalone Tomcat supports declaration of application-local resources. In that case it makes
   * sense to associate display of resource/datasource information with the owner application. JBoss
   * on other hand can only declate "global" resources, which alters the way resource information is
   * displayed (and accessed).
   *
   * @return true if datasources can be associated with applications, otherwise false.
   *
   * @see #getApplicationResources(org.apache.catalina.Context)
   */
  boolean supportsPrivateResources();

  /**
   * Most servlet containers support global resources, but for those that do not, this returns
   * false.
   *
   * @return true if the servlet container supports global resources, otherwise false.
   *
   * @see #getApplicationResources()
   */
  boolean supportsGlobalResources();

  /**
   * Indicates whether this servlet container exposes datasources via
   * {@link #lookupDataSource(Context, String, ContainerWrapperBean) JNDI}.
   *
   * @return true if the servlet container supports datasource lookup
   */
  boolean supportsDataSourceLookup();


}
