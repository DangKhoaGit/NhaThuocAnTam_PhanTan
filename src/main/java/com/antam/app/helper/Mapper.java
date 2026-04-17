package com.antam.app.helper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/*
 * @description:
 * @author: Pham Dang Khoa
 * @date: 08/04/2026
 * @version: 1.0
 */
public class Mapper {

    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <S, T> T map(S source, Class<T> targetClass) {
        if (source == null) {
            return null;
        }
        return MAPPER.convertValue(source, targetClass);
    }
}
