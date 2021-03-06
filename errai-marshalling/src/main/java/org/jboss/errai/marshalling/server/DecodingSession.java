package org.jboss.errai.marshalling.server;

import org.jboss.errai.common.client.protocols.SerializationParts;
import org.jboss.errai.marshalling.client.api.AbstractMarshallingSession;
import org.jboss.errai.marshalling.client.api.Marshaller;
import org.jboss.errai.marshalling.client.api.json.EJObject;
import org.jboss.errai.marshalling.client.api.json.EJValue;
import org.jboss.errai.marshalling.rebind.api.model.MappingDefinition;

import java.util.List;
import java.util.Map;

/**
 * @author Mike Brock <cbrock@redhat.com>
 */
public class DecodingSession extends AbstractMarshallingSession {
  private final ServerMappingContext context;

  public DecodingSession(ServerMappingContext context) {
    this.context = context;
  }

  @Override
  public ServerMappingContext getMappingContext() {
    return context;
  }

  @Override
  public Marshaller<Object> getMarshallerInstance(String fqcn) {
    final Marshaller<Object> m = context.getMarshaller(fqcn);
    if (m == null) {
      throw new RuntimeException("no marshalling definition available for type:" + fqcn);
    }

    return m;
  }

  @Override
  public String determineTypeFor(String formatType, Object o) {
    EJValue jsonValue = (EJValue) o;

    if (jsonValue.isObject() != null) {
      EJObject jsonObject = jsonValue.isObject();
      if (jsonObject.containsKey(SerializationParts.ENCODED_TYPE)) {
        return jsonObject.get(SerializationParts.ENCODED_TYPE).isString().stringValue();
      }
      else {
        return Map.class.getName();
      }
    }
    else if (jsonValue.isString() != null) {
      return String.class.getName();
    }
    else if (jsonValue.isNumber() != null) {
      return Double.class.getName();
    }
    else if (jsonValue.isBoolean() != null) {
      return Boolean.class.getName();
    }
    else if (jsonValue.isArray() != null) {
      return List.class.getName();
    }
    else if (jsonValue.isNull()) {
      return null;
    }
    else {
      return jsonValue.getRawValue().getClass().getName();
    }
  }
}

