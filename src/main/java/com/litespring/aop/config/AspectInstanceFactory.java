package com.litespring.aop.config;

import com.litespring.beans.BeansException;
import com.litespring.beans.factory.BeanFactory;
import com.litespring.beans.factory.BeanFactoryAware;
import com.litespring.util.StringUtils;

public class AspectInstanceFactory implements BeanFactoryAware {

    private String aspectBeanName;
    private BeanFactory beanFactory;

    public void setAspectBeanName(String aspectBeanName) {
        this.aspectBeanName = aspectBeanName;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        if (!StringUtils.hasText(this.aspectBeanName)) {
            throw new IllegalArgumentException("'aspectBeanName' is required");
        }
    }

    public Object getAspectInstance() {
        return this.beanFactory.getBean(this.aspectBeanName);
    }

}
