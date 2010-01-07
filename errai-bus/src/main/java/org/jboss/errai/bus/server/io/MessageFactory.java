/*
 * Copyright 2009 JBoss, a divison Red Hat, Inc
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

package org.jboss.errai.bus.server.io;

import org.jboss.errai.bus.server.QueueSession;
import org.jboss.errai.bus.client.CommandMessage;
import org.jboss.errai.bus.client.Message;
import org.jboss.errai.bus.client.json.JSONUtilCli;
import org.jboss.errai.bus.client.protocols.MessageParts;

import java.util.Map;

public class MessageFactory {
    public static Map<String, Object> decodeToMap(String in) {
        //noinspection unchecked
        return (Map<String, Object>) new JSONDecoder(in).parse();
    }

    public static Message[] createCommandMessage(QueueSession session, String json) {
        if (json.length() == 0) return new CommandMessage[0];
        String[] pkg = json.split(JSONUtilCli.MULTI_PAYLOAD_SEPER_REGEX);
        Message[] c = new Message[pkg.length];

        for (int i = 0; i < pkg.length; i++) {
            Map<String, Object> parts = decodeToMap(pkg[i]);
            parts.remove(MessageParts.SessionID.name());

            Message msg = CommandMessage.createWithParts(parts)
                    .setResource("Session", session);

            if (parts.containsKey("__MarshalledTypes")) {
                TypeDemarshallHelper.demarshallAll((String) parts.get("__MarshalledTypes"), msg);
            }

            c[i] = msg;
        }

        return c;
    }
}