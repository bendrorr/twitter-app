package com.example.twitterapp.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Mapper {

    private static ObjectMapper mapper;

    private Mapper() {

    }

    public static ObjectMapper getMapper() {
        if (mapper == null) {
            return new ObjectMapper();
        }
        return mapper;
    }
}
