package com.example.twitterapp.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SharedFunctions {

    public static String generateCustomId() {
        return UUID.randomUUID().toString();
    }
}
