package com.litespring.beans.factory.config;

import java.util.List;

//设置和获取 bean 构造器的抽象接口
public interface ConfigurableBeanFactory extends AutowireCapableBeanFactory {
    void setBeanClassLoader(ClassLoader beanClassLoader);

    ClassLoader getBeanClassLoader();

    //PostProcessor 添加到 BeanFactory
    void addBeanPostProcessor(BeanPostProcessor postProcessor);

    List<BeanPostProcessor> getBeanPostProcessors();
}
