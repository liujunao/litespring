package com.litespring.core.type.classreading;

import com.litespring.core.io.Resource;
import com.litespring.core.type.AnnotationMetadata;
import com.litespring.core.type.ClassMetadata;

public interface MetadataReader {
    Resource getResource();

    ClassMetadata getClassMetadata();

    AnnotationMetadata getAnnotationMetadata();
}
