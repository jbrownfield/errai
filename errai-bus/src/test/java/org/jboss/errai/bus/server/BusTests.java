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

package org.jboss.errai.bus.server;

import junit.framework.TestCase;
import org.jboss.errai.bus.client.api.QueueSession;
import org.jboss.errai.bus.client.api.SessionEndListener;
import org.jboss.errai.bus.server.io.buffers.BufferColor;
import org.jboss.errai.bus.server.io.buffers.TransmissionBuffer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;

/**
 * @author Mike Brock
 */
public class BusTests extends TestCase {
  public void testNewClientsDontReceiveBackBroadcasts() throws IOException {
    TransmissionBuffer buffer = TransmissionBuffer.create();
    QueueSession session = new QueueSession() {
      @Override
      public String getSessionId() {
        return "ID";
      }

      @Override
      public String getParentSessionId() {
        return "ParentID";
      }

      @Override
      public boolean isValid() {
        return false;
      }

      @Override
      public boolean endSession() {
        return false;
      }

      @Override
      public void setAttribute(String attribute, Object value) {
      }

      @Override
      public <T> T getAttribute(Class<T> type, String attribute) {
        return null;
      }

      @Override
      public Collection<String> getAttributeNames() {
        return null;
      }

      @Override
      public boolean hasAttribute(String attribute) {
        return false;
      }

      @Override
      public boolean removeAttribute(String attribute) {
        return false;
      }

      @Override
      public void addSessionEndListener(SessionEndListener listener) {
      }
    };

    BufferColor global = BufferColor.getAllBuffersColor();
    String bufData = "writeIn";

    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bufData.getBytes());
    buffer.write(byteArrayInputStream, global);

    MessageQueueImpl messageQueue = new MessageQueueImpl(buffer, session);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    messageQueue.poll(false, outputStream);

    assertEquals("[]", new String(outputStream.toByteArray()));
  }
}
