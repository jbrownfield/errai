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

package org.jboss.errai.codegen.builder.impl;

import org.jboss.errai.codegen.Context;
import org.jboss.errai.codegen.DefModifiers;
import org.jboss.errai.codegen.Modifier;
import org.jboss.errai.codegen.Statement;
import org.jboss.errai.codegen.builder.BuildCallback;
import org.jboss.errai.codegen.builder.FieldBuildInitializer;
import org.jboss.errai.codegen.builder.FieldBuildName;
import org.jboss.errai.codegen.builder.FieldBuildStart;
import org.jboss.errai.codegen.builder.FieldBuildType;
import org.jboss.errai.codegen.builder.Finishable;
import org.jboss.errai.codegen.builder.callstack.LoadClassReference;
import org.jboss.errai.codegen.meta.MetaClass;
import org.jboss.errai.codegen.meta.MetaClassFactory;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
public class FieldBuilder<T> implements FieldBuildStart<T>, FieldBuildType<T>,
                                        FieldBuildName<T>, FieldBuildInitializer<T> {

  private BuildCallback<T> callback;
  private Scope scope;
  private DefModifiers modifiers;
  private MetaClass type;
  private String name;
  private Statement initializer;

  public FieldBuilder(BuildCallback<T> callback) {
    this.callback = callback;
  }

  public FieldBuilder(BuildCallback<T> callback, Scope scope) {
    this.callback = callback;
    this.scope = scope;
  }

  public FieldBuilder(BuildCallback<T> callback, Scope scope, MetaClass type, String name) {
    this.callback = callback;
    this.scope = scope;
    this.type = type;
    this.name = name;
  }

  @Override
  public Finishable<T> initializesWith(Statement statement) {
    this.initializer = statement;
    return this;
  }

  @Override
  public FieldBuildInitializer<T> modifiers(Modifier... modifiers) {
    this.modifiers = new DefModifiers(modifiers);
    return this;
  }

  @Override
  public FieldBuildName<T> typeOf(Class<?> type) {
    this.type = MetaClassFactory.get(type);
    return this;
  }

  @Override
  public FieldBuildName<T> typeOf(MetaClass type) {
    this.type = type;
    return this;
  }

  @Override
  public FieldBuildInitializer<T> named(String name) {
    this.name = name;
    return this;
  }

  @Override
  public T finish() {
    if (callback != null) {
      return callback.callback(new Statement() {

        private String generatedCache;

        @Override
        public String generate(Context context) {
          if (generatedCache != null) return generatedCache;

          StringBuilder sbuf = new StringBuilder(scope.getCanonicalName())
                  .append(scope == Scope.Package ? "" : " ")
                  .append(modifiers != null ? modifiers.toJavaString() + " " : "")
                  .append(LoadClassReference.getClassReference(type, context, type.getTypeParameters() != null))
                  .append(" ").append(name);

          if (initializer != null) {
            sbuf.append(" = ").append(initializer.generate(context));
          }

          return generatedCache = sbuf.append(";").toString();
        }

        @Override
        public MetaClass getType() {
          return type;
        }
      });
    }
    return null;
  }
}
