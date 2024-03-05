package ru.otus.util;

import static java.util.Objects.isNull;

import ru.otus.model.ObjectForMessage;

public class MessageUtil {

    private MessageUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static ObjectForMessage cloneField13(ObjectForMessage field13) {
        if (!isNull(field13)) {
            return field13.clone();
        }
        return null;
    }
}
