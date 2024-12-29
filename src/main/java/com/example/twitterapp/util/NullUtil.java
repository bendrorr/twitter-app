package com.example.twitterapp.util;

import java.util.Optional;
import java.util.function.Supplier;

public class NullUtil {
    private NullUtil() {

    }

    public static <T> T getOrNull(Supplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> Optional<T> getNonNull(Supplier<T> supplier) {
        try {
            return Optional.of(supplier.get());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static <T> T getOrDefault(Supplier<T> supplier, T defaultValue) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
