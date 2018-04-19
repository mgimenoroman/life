package com.mgr.life;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgr.life.client.Client;
import com.mgr.life.item.Item;

import java.io.IOException;

public class RestEntityDeserializer extends JsonDeserializer<RestEntity> {

    @Override
    public RestEntity deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        ObjectMapper objectMapper = (ObjectMapper) jsonParser.getCodec();
        JsonNode json = objectMapper.readTree(jsonParser);
        Class<? extends RestEntity> instanceClass;

        if (json.has("price")) {
            instanceClass = Item.class;
        } else {
            instanceClass = Client.class;
        }

        return objectMapper.readValue(json.toString(), instanceClass);
    }
}
