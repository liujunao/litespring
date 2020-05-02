package com.litespring.test.v4_springAnnotation;

import com.litespring.beans.factory.config.DependencyDescriptor;
import com.litespring.beans.factory.support.DefaultBeanFactory;
import com.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import com.litespring.core.io.ClassPathResource;
import com.litespring.core.io.Resource;
import com.litespring.dao.v4.AccountDao;
import com.litespring.service.v4.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;

public class DependencyDescriptorTest_6 {

    @Test
    public void testResolveDependency() throws NoSuchFieldException {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("petstore-v4.xml");
        reader.loadBeanDefinition(resource);

        Field field = PetStoreService.class.getDeclaredField("accountDao");
        DependencyDescriptor descriptor = new DependencyDescriptor(field, true);
        Object object = factory.resolveDependency(descriptor);
        Assert.assertTrue(object instanceof AccountDao);

    }
}
