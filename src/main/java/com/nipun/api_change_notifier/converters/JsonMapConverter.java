package com.nipun.api_change_notifier.converters;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Utility class for converting between Map and JSON string.
 * No longer a JPA AttributeConverter — just a plain helper.
 */
public class JsonMapConverter {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Map<String, Object> map) {
        try {
            return map == null ? null : mapper.writeValueAsString(map);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting map to JSON", e);
        }
    }

    public static Map<String, Object> fromJson(String json) {
        try {
            return json == null ? null : mapper.readValue(json, new TypeReference<>() {
            });
        } catch (Exception e) {
            throw new IllegalArgumentException("Error converting JSON to map", e);
        }
    }

}
