package com.zeydie.transformer.utils;

import lombok.extern.java.Log;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

@Log
public final class PrintClassUtil {
    public static void printAllField(
            @NotNull final Object object,
            @NotNull final Class<?> clazz
    ) {
        log.info(String.format("==============%s==============", clazz.getSimpleName()));

        for (final Field field : clazz.getFields())
            try {
                if (field != null) {
                    field.setAccessible(true);

                    log.info(
                            String.format("Field %s %s %s %b",
                                    field.getName(),
                                    field.getType(),
                                    field.get(object),
                                    field.get(object) == null
                            )
                    );
                }
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
    }
}
