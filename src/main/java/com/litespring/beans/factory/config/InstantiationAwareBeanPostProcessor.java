package com.litespring.beans.factory.config;

import com.litespring.beans.BeansException;

public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {
    Object beforeInstantiation(Class<?> beanClass, String beanName) throws BeansException;

    boolean afterInstantiation(Object bean, String beanName) throws BeansException;

    void postProcessPropertyValues(Object bean, String beanName) throws BeansException;
}
