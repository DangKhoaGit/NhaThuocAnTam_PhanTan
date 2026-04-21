package com.antam.app.network.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * @description: Utility class cho serialization/deserialization
 * Sử dụng Jackson ObjectMapper để convert giữa objects và JSON
 * @author: Pham Dang Khoa
 * @date: 21/04/2026
 * @version: 1.0
 */
public class SerializationUtil {
    private static final Logger LOGGER = Logger.getLogger(SerializationUtil.class.getName());
    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        // Hỗ trợ Java 8 time types (LocalDate, LocalDateTime, etc.)
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * Convert object sang JSON string
     */
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error serializing object to JSON", e);
            return null;
        }
    }

    /**
     * Convert JSON string sang object
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error deserializing JSON to object", e);
            return null;
        }
    }

    /**
     * Convert object sang một kiểu khác
     */
    public static <T> T convertValue(Object fromValue, Class<T> toValueType) {
        try {
            return objectMapper.convertValue(fromValue, toValueType);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error converting object", e);
            return null;
        }
    }

    /**
     * Lấy ObjectMapper instance (nếu cần)
     */
    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}

