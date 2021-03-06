/*
 * Copyright 2011 JBoss, by Red Hat, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.errai.bus.server.service;

import org.jboss.errai.bus.client.framework.RequestDispatcher;
import org.jboss.errai.bus.server.api.SessionProvider;

/**
 * @author Heiko Braun <hbraun@redhat.com>
 * @date May 4, 2010
 */
public interface ServiceComposite<S> {
  public SessionProvider<S> getSessionProvider();

  public void setSessionProvider(SessionProvider<S> sessionProvider);

  public RequestDispatcher getDispatcher();

  public void setDispatcher(RequestDispatcher dispatcher);
}
