package com.litespring.beans.factory;

import com.litespring.beans.BeansException;

public interface BeanFactoryAware {
    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
