package com.litespring.beans.factory.support;

import com.litespring.beans.BeanDefinition;
import com.litespring.beans.factory.BeanCreationException;
import com.litespring.beans.factory.BeanFactory;
import com.litespring.beans.factory.FactoryBean;
import com.litespring.beans.factory.config.RuntimeBeanReference;
import com.litespring.beans.factory.config.TypedStringValue;

//处理 setter 注入的类型转换，将 reference(ref 属性) 变为 bean 对象
//等同 ConstructorResolver
public class BeanDefinitionValueResolver {
    private final AbstractBeanFactory beanFactory;

    public BeanDefinitionValueResolver(AbstractBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public Object resolveValueIfNecessary(Object value) {
        if (value instanceof RuntimeBeanReference) {
            RuntimeBeanReference ref = (RuntimeBeanReference) value;
            String refName = ref.getBeanName();
            Object bean = beanFactory.getBean(refName);
            return bean;
        } else if (value instanceof TypedStringValue) {
            return ((TypedStringValue) value).getValue();
        } else if (value instanceof BeanDefinition) {
            BeanDefinition beanDefinition = (BeanDefinition) value;
            String innnerBeanName = "(inner bean)" + beanDefinition.getBeanClassName() + "#"
                    + Integer.toHexString(System.identityHashCode(beanDefinition));
            return resolveInnerBean(innnerBeanName, beanDefinition);
        } else {
            return value;
        }
    }

    private Object resolveInnerBean(String innerBeanName, BeanDefinition innerBd) {
        try {
            Object innerBean = beanFactory.createBean(innerBd);
            if (innerBean instanceof FactoryBean) {
                try {
                    return ((FactoryBean<?>) innerBean).getObject();
                } catch (Exception e) {
                    throw new BeanCreationException(innerBeanName, "FactoryBean threw exception on object creation", e);
                }
            } else {
                return innerBean;
            }
        } catch (BeanCreationException ex) {
            throw new BeanCreationException(innerBeanName, "Cannot create inner bean '" + innerBeanName + "' " +
                    (innerBd != null && innerBd.getBeanClassName() != null ? "of type [" + innerBd.getBeanClassName() + "] " : "")
                    , ex);
        }
    }
}
