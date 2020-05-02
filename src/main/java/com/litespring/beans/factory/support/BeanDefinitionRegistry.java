package com.litespring.beans.factory.support;

import com.litespring.beans.BeanDefinition;

//注册 BeanDefinition 的接口
public interface BeanDefinitionRegistry {
    BeanDefinition getBeanDefinition(String beanID);

    void registerBeanDefinition(String beanID, BeanDefinition beanDefinition);
}
