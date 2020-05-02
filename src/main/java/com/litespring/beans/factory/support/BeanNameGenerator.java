package com.litespring.beans.factory.support;

import com.litespring.beans.BeanDefinition;

public interface BeanNameGenerator {
    String generateBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry registry);
}
