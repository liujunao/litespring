package com.litespring.test.v4_springAnnotation;

import com.litespring.beans.BeanDefinition;
import com.litespring.beans.factory.support.DefaultBeanFactory;
import com.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import com.litespring.context.annotation.ScannedGenericBeanDefinition;
import com.litespring.core.annotation.AnnotationAttributes;
import com.litespring.core.io.ClassPathResource;
import com.litespring.core.io.Resource;
import com.litespring.core.type.AnnotationMetadata;
import com.litespring.stereotype.Component;
import org.junit.Assert;
import org.junit.Test;

public class XmlBeanDefinitionReaderTest_5 {
    @Test
    public void testParseScannedBean() {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("petstore-v4.xml");
        reader.loadBeanDefinition(resource);
        String annotation = Component.class.getName();
        {
            BeanDefinition beanDefinition = factory.getBeanDefinition("petStore");
            Assert.assertTrue(beanDefinition instanceof ScannedGenericBeanDefinition);
            ScannedGenericBeanDefinition sbd = (ScannedGenericBeanDefinition) beanDefinition;
            AnnotationMetadata annotationMetadata = sbd.getMetadata();
            Assert.assertTrue(annotationMetadata.hasAnnotation(annotation));
            AnnotationAttributes attributes = annotationMetadata.getAnnotationAttributes(annotation);
            Assert.assertEquals("petStore", attributes.get("value"));
        }
        {
            BeanDefinition beanDefinition = factory.getBeanDefinition("accountDao");
            Assert.assertTrue(beanDefinition instanceof ScannedGenericBeanDefinition);
            ScannedGenericBeanDefinition scannedGenericBeanDefinition = (ScannedGenericBeanDefinition) beanDefinition;
            AnnotationMetadata annotationMetadata = scannedGenericBeanDefinition.getMetadata();
            Assert.assertTrue(annotationMetadata.hasAnnotation(annotation));
        }
        {
            BeanDefinition beanDefinition = factory.getBeanDefinition("itemDao");
            Assert.assertTrue(beanDefinition instanceof ScannedGenericBeanDefinition);
            ScannedGenericBeanDefinition scannedGenericBeanDefinition = (ScannedGenericBeanDefinition) beanDefinition;
            AnnotationMetadata annotationMetadata = scannedGenericBeanDefinition.getMetadata();
            Assert.assertTrue(annotationMetadata.hasAnnotation(annotation));
        }
    }
}
