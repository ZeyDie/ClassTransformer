package ru.zoom4ikdan4ik.utils;

import java.lang.reflect.Field;

public class PrintClassUtil {
    public static void printAllField(Object object, Class clazz) {
        debug(String.format("==============%s==============", clazz.getSimpleName()));

        for (Field field : clazz.getFields())
            try {
                if (field != null) {
                    field.setAccessible(true);

                    debug(String.format("Field %s %s %s %b", field.getName(), field.getType(),
                            field.get(object), field.get(object) == null));
                }
            } catch (IllegalArgumentException e) {
                e.printStackTrace();

                continue;
            } catch (IllegalAccessException e) {
                e.printStackTrace();

                continue;
            }
    }

    public static final void debug(String string) {
        System.out.println(String.format("%s %s", "[ZD]", string));
    }
}
