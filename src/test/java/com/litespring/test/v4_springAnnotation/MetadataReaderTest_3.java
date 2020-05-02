package com.litespring.test.v4_springAnnotation;

import com.litespring.core.annotation.AnnotationAttributes;
import com.litespring.core.io.ClassPathResource;
import com.litespring.core.type.AnnotationMetadata;
import com.litespring.core.type.classreading.MetadataReader;
import com.litespring.core.type.classreading.SimpleMetadataReader;
import com.litespring.stereotype.Component;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class MetadataReaderTest_3 {
    @Test
    public void testGetMetadata() throws IOException {
        ClassPathResource resource = new ClassPathResource("com/litespring/service/v4/PetStoreService.class");
        MetadataReader reader = new SimpleMetadataReader(resource);

        AnnotationMetadata annotationMetadata = reader.getAnnotationMetadata();

        String annotation = Component.class.getName();

        Assert.assertTrue(annotationMetadata.hasAnnotation(annotation));
        AnnotationAttributes attributes = annotationMetadata.getAnnotationAttributes(annotation);
        Assert.assertEquals("petStore", attributes.get("value"));

        Assert.assertFalse(annotationMetadata.isAbstract());
        Assert.assertFalse(annotationMetadata.isFinal());
        Assert.assertEquals("com.litespring.service.v4.PetStoreService", annotationMetadata.getClassName());
    }
}
