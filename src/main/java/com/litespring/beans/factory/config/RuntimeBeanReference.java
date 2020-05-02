package com.litespring.beans.factory.config;

//PropertyValue 的配置类，获取 ref 属性的实例名称
public class RuntimeBeanReference {
    private final String beanName;

    public RuntimeBeanReference(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }
}
