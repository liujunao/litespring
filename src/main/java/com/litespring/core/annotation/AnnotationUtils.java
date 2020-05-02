package com.litespring.core.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public abstract class AnnotationUtils {
    public static <T extends Annotation> T getAnnotation(AnnotatedElement annotatedElement, Class<T> annotationType) {
        T annotation = annotatedElement.getAnnotation(annotationType);
        if (annotation == null) {
            for (Annotation metaAnnotation : annotatedElement.getAnnotations()) {
                annotation = metaAnnotation.annotationType().getAnnotation(annotationType);
                if (annotation != null) {
                    break;
                }
            }
        }
        return annotation;
    }
}
