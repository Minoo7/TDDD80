package com.vinga129.savolax.ui.retrofit.rest_objects;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class groups {

    public enum AddressTypes {
        HOME("Home"), WORK("Work");

        private String value;

        AddressTypes(String value) {
            this.value = value;
        }
    }

    public enum BusinessTypes {
        RESTAURANT("Restaurant"),
        CAFE("Cafe"),
        SHOP("Shop"),
        SUPERMARKET("Supermarket");
        private String value;

        BusinessTypes(String value) {
            this.value = value;
        }
    }

    public enum PostTypes {
        REVIEW("Review"), SOCIAL("Social");

        private String value;

        PostTypes(String value) {
            this.value = value;
        }
    }

    public enum Genders {
        MAN("Man"), WOMAN("Woman"), NON_BINARY("Non_binary"), OTHER("Other");
        private String value;

        Genders(String value) {
            this.value = value;
        }

        public String getValue() {
            return value.replaceAll("_", "");
        }
    }

    public static <T> String[] propsToStringArray(T[] values, Function<T, String> extractor) {
        return Arrays.stream(values).map(extractor).toArray(String[]::new);
    }

    public static String toTitle(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase().replaceAll("_", " ");
    }

    public static <T> String[] enumToStrings(T[] values, Function<T, String> extractor) {
        return Arrays.stream(propsToStringArray(values, extractor)).map(groups::toTitle).toArray(String[]::new);
    }
}
