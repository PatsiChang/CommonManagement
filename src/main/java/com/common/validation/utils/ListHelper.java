package com.common.validation.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ListHelper {
    public static <T> List<T> newList() {
        return new ArrayList<T>();
    }

    public static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[]) obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>) obj);
        }
        return list;
    }

    //Check Field is List of String type
    public static boolean isListOfStrings(Field field) throws IllegalAccessException {
        if (isList(field)) {
            ParameterizedType listType = (ParameterizedType) field.getGenericType();
            return listType.getActualTypeArguments()[0].equals(String.class);
        }
        return false;
    }

    //Check Field is List type
    public static boolean isList(Field field) {
        return field.getType().equals(List.class);
    }



}
