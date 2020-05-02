package com.litespring.test.v5_aop;

import com.litespring.aop.config.AspectInstanceFactory;
import com.litespring.beans.factory.BeanFactory;
import com.litespring.beans.factory.support.DefaultBeanFactory;
import com.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import com.litespring.core.io.ClassPathResource;
import com.litespring.tx.TransactionManager;

import java.lang.reflect.Method;

public class AbstractV5Test {
    protected BeanFactory getBeanFactory(String configFile) {
        DefaultBeanFactory defaultBeanFactory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(defaultBeanFactory);
        ClassPathResource resource = new ClassPathResource(configFile);
        reader.loadBeanDefinition(resource);
        return defaultBeanFactory;
    }

    protected Method getAdviceMethod(String methodName) throws NoSuchMethodException {
        return TransactionManager.class.getMethod(methodName);
    }

    protected AspectInstanceFactory getAspectInstanceFactory(String targetBeanName) {
        AspectInstanceFactory factory = new AspectInstanceFactory();
        factory.setAspectBeanName(targetBeanName);
        return factory;
    }
}
