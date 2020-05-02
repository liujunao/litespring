package com.litespring.beans.factory.support;

import com.litespring.beans.BeansException;

// BeanDefinition 信息存储的错误处理类
public class BeanDefinitionStoreException extends BeansException {
    public BeanDefinitionStoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public BeanDefinitionStoreException(String message) {
        super(message);
    }
}
