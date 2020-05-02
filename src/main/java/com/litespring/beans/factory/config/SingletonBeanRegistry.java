package com.litespring.beans.factory.config;

//bean 模式：singleton 和 prototype
public interface SingletonBeanRegistry {
    void registerSingleton(String beanName, Object singletonObject);

    Object getSingleton(String beaName);
}
