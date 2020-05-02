package com.litespring.context.annotation;

import com.litespring.beans.factory.annotation.AnnotatedBeanDefinition;
import com.litespring.beans.factory.support.GenericBeanDefinition;
import com.litespring.core.type.AnnotationMetadata;

public class ScannedGenericBeanDefinition extends GenericBeanDefinition implements AnnotatedBeanDefinition {
    private final AnnotationMetadata metadata;

    public ScannedGenericBeanDefinition(AnnotationMetadata metadata) {
        super();
        this.metadata = metadata;
        setBeanClassName(this.metadata.getClassName());
    }

    @Override
    public AnnotationMetadata getMetadata() {
        return this.metadata;
    }
}
