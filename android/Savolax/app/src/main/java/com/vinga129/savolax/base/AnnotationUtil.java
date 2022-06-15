package com.vinga129.savolax.base;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class AnnotationUtil {

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface AnnotationContentId {

        int contentId();
    }

    // check if contentId annotation is present in class
    public static int check(Object class_) {
        if (!class_.getClass().isAnnotationPresent(AnnotationContentId.class))
            throw new IllegalStateException("Annotation for content id missing!");
        AnnotationContentId annotationContentId = class_.getClass().getAnnotation(AnnotationContentId.class);
        return annotationContentId.contentId();
    }
}
