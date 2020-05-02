package com.litespring.aop.aspectj;

import com.litespring.aop.Advice;
import com.litespring.aop.MethodMatcher;
import com.litespring.aop.Pointcut;
import com.litespring.aop.framework.AopConfigSupport;
import com.litespring.aop.framework.AopProxyFactory;
import com.litespring.aop.framework.CglibProxyFactory;
import com.litespring.aop.framework.JdkAopProxyFactory;
import com.litespring.beans.BeansException;
import com.litespring.beans.factory.config.BeanPostProcessor;
import com.litespring.beans.factory.config.ConfigurableBeanFactory;
import com.litespring.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AspectJAutoProxyCreator implements BeanPostProcessor {
    ConfigurableBeanFactory beanFactory;

    @Override
    public Object beforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object afterInitialization(Object bean, String beanName) throws BeansException {
        //如果这个Bean本身就是Advice及其子类，那就不要再生成动态代理了。
        if (isInfrastructureClass(bean.getClass())) {
            return bean;
        }
        List<Advice> advices = getCandidateAdvices(bean);
        if (advices.isEmpty()) {
            return bean;
        }
        return createProxy(advices, bean);
    }

    private List<Advice> getCandidateAdvices(Object bean) {
        List<Object> advices = this.beanFactory.getBeansByType(Advice.class);

        List<Advice> result = new ArrayList<>();
        for (Object object : advices) {
            Pointcut pointcut = ((Advice) object).getPointcut();
            if (canApply(pointcut, bean.getClass())) {
                result.add((Advice) object);
            }
        }
        return result;
    }

    protected Object createProxy(List<Advice> advices, Object bean) {
        AopConfigSupport config = new AopConfigSupport();
        for (Advice advice : advices) {
            config.addAdvice(advice);
        }
        Set<Class> targetInterfaces = ClassUtils.getAllInterfacesForClassAsSet(bean.getClass());
        for (Class<?> targetInterface : targetInterfaces) {
            config.addInterface(targetInterface);
        }
        config.setTargetObject(bean);

        AopProxyFactory proxyFactory;
        if (config.getProxiedInterfaces().length == 0) {
            proxyFactory = new CglibProxyFactory(config);
        } else {
            //需要实现JDK代理
            proxyFactory = new JdkAopProxyFactory(config);
        }
        return proxyFactory.getProxy();
    }

    protected boolean isInfrastructureClass(Class<?> beanClass) {
        return Advice.class.isAssignableFrom(beanClass);
    }

    public void setBeanFactory(ConfigurableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public static boolean canApply(Pointcut pointcut, Class<?> targetClass) {
        MethodMatcher methodMatcher = pointcut.getMethodMatcher();
        LinkedHashSet<Class> classes = new LinkedHashSet<>(ClassUtils.getAllInterfacesAsSet(targetClass));
        classes.add(targetClass);
        for (Class<?> clazz : classes) {
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                if (methodMatcher.matches(method)) {
                    return true;
                }
            }
        }
        return false;
    }
}