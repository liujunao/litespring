package com.litespring.beans.factory.config;

import com.litespring.beans.factory.BeanFactory;

//封装隐藏 DependencyDescriptor 的接口
public interface AutowireCapableBeanFactory extends BeanFactory {
    Object resolveDependency(DependencyDescriptor descriptor);
}
