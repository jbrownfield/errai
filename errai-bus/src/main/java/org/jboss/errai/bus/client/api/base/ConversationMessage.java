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

package org.jboss.errai.bus.client.api.base;

import org.jboss.errai.bus.client.api.BadlyFormedMessageException;
import org.jboss.errai.bus.client.api.Message;
import org.jboss.errai.common.client.protocols.MessageParts;

/**
 * A ConversationMessage is a message that is to be routed back to the sending client. Conceptually, the use of
 * ConversationMessage negates the need to manually provide routing information to have a two-way conversation with a
 * client. This is particularly important on the server-side of an application, where most messages sent from the client
 * to a server-side service will necessitate a message back to the originating client-side service. ConversationMessage
 * makes this a straightforward process.
 * <p>
 * 
 * <pre>
 * public class SomeService implements MessageCallback {
 *   public void callback(CommandMessage message) {
 *     ConversationMessage.create(message) // create a ConversationMessage referencing the incoming message
 *         .setSubject(&quot;ClientService&quot;) // specify the service on the sending client that should receive the message.
 *         .set(&quot;Text&quot;, &quot;Hello, World!&quot;)
 *              .sendNowWith(messageBusInstance); // send the message.
 *   }
 * }
 * </pre>
 * 
 * It is possible for a message sender to specify a {@link org.jboss.errai.common.client.protocols.MessageParts#ReplyTo}
 * message component, which by default will be used to route the message. We refer to this as a:
 * <em>sender-driven conversation</em> as opposed to a <em>receiver-driven conversation</em> which is demonstrated in
 * the code example above.
 */
public class ConversationMessage extends CommandMessage {

  @Deprecated
  static CommandMessage create(String commandType) {
    throw new BadlyFormedMessageException("You must create a ConversationMessage by specifying an incoming message.");
  }

  @Deprecated
  static CommandMessage create(Enum commandType) {
    throw new BadlyFormedMessageException("You must create a ConversationMessage by specifying an incoming message.");
  }

  /**
   * Calling this method on this class will always result in a
   * {@link org.jboss.errai.bus.client.api.BadlyFormedMessageException}. You must call {@link #create(Message)}.
   * 
   * @return - this method will never return.
   */
  static CommandMessage create() {
    throw new BadlyFormedMessageException("You must create a ConversationMessage by specifying an incoming message.");
  }

  /**
   * Creates a <tt>ConversationMessage</tt> using an incoming message as a reference
   * 
   * @param inReplyTo
   *          - incoming message
   * @return newly created <tt>ConversationMessage</tt>
   */
  public static ConversationMessage create(Message inReplyTo) {
    return new ConversationMessage(inReplyTo);
  }

  private ConversationMessage(Message inReplyTo) {
    super();
    if (inReplyTo.hasResource("Session")) {
      setResource("Session", inReplyTo.getResource(Object.class, "Session"));
    }
    if (inReplyTo.hasPart(MessageParts.ReplyTo)) {
      set(MessageParts.ToSubject, inReplyTo.get(String.class, MessageParts.ReplyTo));
    }

    if (!inReplyTo.hasResource("Session") && !inReplyTo.hasPart(MessageParts.ReplyTo)) {
      if (!inReplyTo.hasResource("Session") && !inReplyTo.hasPart(MessageParts.ReplyTo)) {
        throw new RuntimeException(
            "cannot have a conversation. there is no session data or ReplyTo field. Are you sure you referenced an incoming message?");
      }
    }
  }

  /**
   * Constructs a <tt>ConversationMessage</tt> using a specified type and reference message.
   * 
   * @param commandType
   *          - <tt>Enum</tt> command type
   * @param inReplyTo
   *          - message to reference
   */
  public ConversationMessage(Enum commandType, Message inReplyTo) {
    this(inReplyTo);
    command(commandType.name());
  }

  /**
   * Constructs a <tt>ConversationMessage</tt> using a specified type and reference message.
   * 
   * @param commandType
   *          - <tt>String</tt> command type
   * @param inReplyTo
   *          - message to reference
   */
  public ConversationMessage(String commandType, Message inReplyTo) {
    this(inReplyTo);
    command(commandType);
  }
}
