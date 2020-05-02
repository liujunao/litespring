package com.litespring.beans.factory;

import com.litespring.aop.Advice;

import java.util.List;

//bean 工厂接口
public interface BeanFactory {

    Object getBean(String beanID);

    Class<?> getType(String name) throws NoSuchBeanDefinitionException;

    List<Object> getBeansByType(Class<?> type);
}
