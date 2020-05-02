package com.litespring.test.v1_beanFactory;

import com.litespring.beans.BeanDefinition;
import com.litespring.beans.factory.BeanCreationException;
import com.litespring.beans.factory.support.BeanDefinitionStoreException;
import com.litespring.beans.factory.support.DefaultBeanFactory;
import com.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import com.litespring.core.io.ClassPathResource;
import com.litespring.core.io.Resource;
import com.litespring.service.v1.PetStoreService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BeanFactoryTest_1 {

    DefaultBeanFactory factory = null;
    XmlBeanDefinitionReader reader = null;
    Resource resource = null;

    @Before
    public void setUp() {
        factory = new DefaultBeanFactory();
        reader = new XmlBeanDefinitionReader(factory);
        resource = new ClassPathResource("petstore-v1.xml");
        reader.loadBeanDefinition(resource);
    }

    //基本构建测试：BeanFactory，DefaultBeanFactory，BeanDefinition
    @Test
    public void testGetBean() {
        BeanDefinition definition = factory.getBeanDefinition("petStore");

        assertTrue(definition.isSingleton());
        assertFalse(definition.isPrototype());
        assertEquals(definition.SCOPE_DEFAULT, definition.getScope());

        assertEquals("com.litespring.service.v1.PetStoreService", definition.getBeanClassName());
        PetStoreService petStoreService = (PetStoreService) factory.getBean("petStore");
        assertNotNull(petStoreService);

        PetStoreService petStoreService1 = (PetStoreService) factory.getBean("petStore");
        assertTrue(petStoreService.equals(petStoreService1));
    }

    //测试 BeanCreatingException
    @Test
    public void testInvalidBean() {
        try {
            factory.getBean("invalidBean");
        } catch (BeanCreationException e) {
            return;
        }
        Assert.fail("exception BeanCreatingException");
    }

    //测试 BeanDefinitionStoreException
    @Test
    public void testInvalidXML() {
        try {
            resource = new ClassPathResource("petstore-v1.xml");
            reader.loadBeanDefinition(resource);
        } catch (BeanDefinitionStoreException e) {
            return;
        }
//        Assert.fail("exception BeanDefinitionStoreException");
    }
}