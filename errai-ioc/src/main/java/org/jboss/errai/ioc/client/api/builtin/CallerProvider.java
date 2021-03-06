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

package org.jboss.errai.ioc.client.api.builtin;

import org.jboss.errai.bus.client.api.ErrorCallback;
import org.jboss.errai.bus.client.api.RemoteCallback;
import org.jboss.errai.bus.client.framework.RPCStub;
import org.jboss.errai.bus.client.framework.RemoteServiceProxyFactory;
import org.jboss.errai.ioc.client.api.Caller;
import org.jboss.errai.ioc.client.api.ContextualTypeProvider;
import org.jboss.errai.ioc.client.api.IOCProvider;

import javax.inject.Singleton;
import java.lang.annotation.Annotation;

/**
 * @author Mike Brock
 */
@IOCProvider @Singleton
public class CallerProvider implements ContextualTypeProvider<Caller> {
  private static final RemoteServiceProxyFactory factory = new RemoteServiceProxyFactory();

  @Override
  public Caller provide(final Class<?>[] typeargs, final Annotation[] qualifiers) {
    return new Caller<Object>() {
      @Override
      public Object call(RemoteCallback<?> callback) {
        Object proxy = factory.getRemoteProxy(typeargs[0]);
        ((RPCStub) proxy).setRemoteCallback(callback);
        ((RPCStub) proxy).setQualifiers(qualifiers);
        return proxy;
      }

      @Override
      public Object call(RemoteCallback<?> callback, ErrorCallback errorCallback) {
        Object proxy = factory.getRemoteProxy(typeargs[0]);
        ((RPCStub) proxy).setRemoteCallback(callback);
        ((RPCStub) proxy).setErrorCallback(errorCallback);
        ((RPCStub) proxy).setQualifiers(qualifiers);
        return proxy;
      }
    };
  }
}
