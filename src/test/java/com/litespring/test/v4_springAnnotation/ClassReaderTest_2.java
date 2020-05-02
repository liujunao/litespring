package com.litespring.test.v4_springAnnotation;

import com.litespring.core.annotation.AnnotationAttributes;
import com.litespring.core.io.ClassPathResource;
import com.litespring.core.type.classreading.AnnotationMetadataReadingVisitor;
import com.litespring.core.type.classreading.ClassMetadataReadingVisitor;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.asm.ClassReader;

import java.io.IOException;

public class ClassReaderTest_2 {

    //测试 ASM
    @Test
    public void testGetClassMetaData() throws IOException {
        ClassPathResource resource = new ClassPathResource("com/litespring/service/v4/PetStoreService.class");
        ClassReader reader = new ClassReader(resource.getInputStream()); //asm 的 Class Reader
        ClassMetadataReadingVisitor visitor = new ClassMetadataReadingVisitor(); //自己实现
        reader.accept(visitor, ClassReader.SKIP_DEBUG);

        Assert.assertFalse(visitor.isAbstract());
        Assert.assertFalse(visitor.isInterface());
        Assert.assertFalse(visitor.isFinal());
        Assert.assertEquals("com.litespring.service.v4.PetStoreService", visitor.getClassName());
        Assert.assertEquals("java.lang.Object", visitor.getSuperClassName());
        Assert.assertEquals(0, visitor.getInterfaceNames().length);
    }

    @Test
    public void testGetAnnotation() throws IOException {
        ClassPathResource resource = new ClassPathResource("com/litespring/service/v4/PetStoreService.class");
        ClassReader reader = new ClassReader(resource.getInputStream());

        AnnotationMetadataReadingVisitor visitor = new AnnotationMetadataReadingVisitor();
        reader.accept(visitor, ClassReader.SKIP_DEBUG);

        String annotation = "com.litespring.stereotype.Component";
        Assert.assertTrue(visitor.hasAnnotation(annotation));

        AnnotationAttributes attributes = visitor.getAnnotationAttributes(annotation);
        Assert.assertEquals("petStore", attributes.get("value"));
    }
}
