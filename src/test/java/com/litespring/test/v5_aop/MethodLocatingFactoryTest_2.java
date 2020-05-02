package com.litespring.test.v5_aop;

import com.litespring.aop.config.MethodLocatingFactory;
import com.litespring.beans.factory.support.DefaultBeanFactory;
import com.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import com.litespring.core.io.ClassPathResource;
import com.litespring.core.io.Resource;
import com.litespring.tx.TransactionManager;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Method;

public class MethodLocatingFactoryTest_2 {

    @Test
    public void testGetMethod() throws Exception {
        DefaultBeanFactory beanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        Resource resource = new ClassPathResource("petstore-v5.xml");
        reader.loadBeanDefinition(resource);

        MethodLocatingFactory methodLocatingFactory = new MethodLocatingFactory();
        methodLocatingFactory.setTargetBeanName("tx");
        methodLocatingFactory.setMethodName("start");
        methodLocatingFactory.setBeanFactory(beanFactory);
        Method method = methodLocatingFactory.getMethod();

        Assert.assertTrue(TransactionManager.class.equals(method.getDeclaringClass()));
        Assert.assertTrue(method.equals(TransactionManager.class.getMethod("start")));
    }
}
