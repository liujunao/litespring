package com.litespring.beans.factory.support;

import com.litespring.beans.BeanDefinition;
import com.litespring.beans.ConstructorArgument;
import com.litespring.beans.PropertyValue;

import java.util.ArrayList;
import java.util.List;

//BeanDefinition 接口的实现类
public class GenericBeanDefinition implements BeanDefinition {
    private String id;
    private String beanClassName;
    private Class<?> beanClass;
    private boolean singleton = true;
    private boolean prototype = false;
    private String scope = SCOPE_DEFAULT;
    //表明 Bean 定义是不是自己合成
    private boolean isSynthetic = false;

    List<PropertyValue> propertyValueList = new ArrayList<>();
    private ConstructorArgument constructorArgument = new ConstructorArgument();

    public GenericBeanDefinition() {
    }

    public GenericBeanDefinition(String id, String beanClassName) {
        this.id = id;
        this.beanClassName = beanClassName;
    }

    public GenericBeanDefinition(Class<?> clazz) {
        this.beanClass = clazz;
        this.beanClassName = clazz.getName();
    }

    public String getBeanClassName() {
        return this.beanClassName;
    }

    public void setBeanClassName(String beanClassName) {
        this.beanClassName = beanClassName;
    }

    @Override
    public List<PropertyValue> getPropertyValues() {
        return this.propertyValueList;
    }

    @Override
    public ConstructorArgument getConstructorArgument() {
        return constructorArgument;
    }

    @Override
    public String getID() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean hasConstructorArgumentValues() {
        return !constructorArgument.isEmpty();
    }

    @Override
    public Class<?> resolveBeanClass(ClassLoader classLoader) throws ClassNotFoundException {
        String className = getBeanClassName();
        if (className == null) {
            return null;
        }
        Class<?> resolvedClass = classLoader.loadClass(className);
        beanClass = resolvedClass;
        return resolvedClass;
    }

    @Override
    public Class<?> getBeanClass() throws IllegalStateException {
        if (beanClass == null) {
            throw new IllegalStateException("Bean class name[" + this.getBeanClassName() + "] has not beanclass");
        }
        return beanClass;
    }

    @Override
    public boolean hasBeanClass() {
        return beanClass != null;
    }

    @Override
    public boolean isSingleton() {
        return this.singleton;
    }

    @Override
    public boolean isPrototype() {
        return this.prototype;
    }

    @Override
    public String getScope() {
        return this.scope;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
        this.singleton = SCOPE_SINGLETON.equals(scope) || SCOPE_DEFAULT.equals(scope);
        this.prototype = SCOPE_PROTOTYPE.equals(scope);
    }

    @Override
    public boolean isSynthetic() {
        return isSynthetic;
    }

    public void setSynthetic(boolean synthetic) {
        isSynthetic = synthetic;
    }
}
