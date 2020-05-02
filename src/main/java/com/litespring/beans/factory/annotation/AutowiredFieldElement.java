package com.litespring.beans.factory.annotation;

import com.litespring.beans.factory.BeanCreationException;
import com.litespring.beans.factory.config.AutowireCapableBeanFactory;
import com.litespring.beans.factory.config.DependencyDescriptor;
import com.litespring.util.ReflectionUtils;

import java.lang.reflect.Field;

public class AutowiredFieldElement extends InjectionElement {
    boolean required;

    public AutowiredFieldElement(Field field, boolean required, AutowireCapableBeanFactory factory) {
        super(field, factory);
        this.required = required;
    }

    public Field getField() {
        return (Field) this.member;
    }

    @Override
    public void inject(Object target) {
        Field field = this.getField();
        try {
            DependencyDescriptor descriptor = new DependencyDescriptor(field, required);
            //获取Bean的实例
            Object value = factory.resolveDependency(descriptor);
            if (value != null) {
                //将field 置于可访问的
                ReflectionUtils.makeAccessible(field);
                field.set(target, value);
            }
        } catch (Throwable ex) {
            throw new BeanCreationException("Could not autowire field: " + field, ex);
        }
    }
}