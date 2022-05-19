package com.vinga129.savolax.ui.retrofit.rest_objects;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class groups {
    public enum BusinessTypes {
        RESTAURANT, CAFE, SHOP, SUPERMARKET
    }
    public enum PostTypes {
        REVIEW
    }
    public enum Genders {
        MAN, WOMAN, NON_BINARY, OTHER
    }

    public static <T> String[] propsToStringArray (T[] values, Function<T, String> extractor) {
        return Arrays.stream(values).map(extractor).toArray(String[]::new);
    }

    public static String toTitle(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase().replaceAll("_", " ");
    }

    public static <T> String[] enumToStrings(T[] values, Function<T, String> extractor) {
        return Arrays.stream(propsToStringArray(values, extractor)).map(groups::toTitle).toArray(String[]::new);
    }
}
