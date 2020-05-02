package com.litespring.aop.framework;

import com.litespring.aop.Advice;
import com.litespring.aop.Pointcut;
import com.litespring.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class AopConfigSupport implements AopConfig {
    private boolean proxyTargetClass = false;
    private Object targetObject = null;
    private List<Advice> advices = new ArrayList<Advice>();
    private List<Class> interfaces = new ArrayList<Class>();

    @Override
    public void setTargetObject(Object targetObject) {
        this.targetObject = targetObject;
    }

    @Override
    public Object getTargetObject() {
        return this.targetObject;
    }

    @Override
    public Class<?> getTargetClass() {
        return this.targetObject.getClass();
    }

    public void addInterface(Class<?> intf) {
        Assert.notNull(intf, "Interface must not be null");
        if (!intf.isInterface()) {
            throw new IllegalArgumentException("[" + intf.getName() + "] is not an interface");
        }
        if (!this.interfaces.contains(intf)) {
            this.interfaces.add(intf);

        }
    }

    @Override
    public Class<?>[] getProxiedInterfaces() {
        return this.interfaces.toArray(new Class[this.interfaces.size()]);
    }

    @Override
    public boolean isInterfaceProxied(Class<?> intf) {
        for (Class proxyIntf : this.interfaces) {
            if (intf.isAssignableFrom(proxyIntf)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addAdvice(Advice advice) {
        this.advices.add(advice);
    }

    @Override
    public boolean isProxyTargetClass() {
        return proxyTargetClass;
    }

    public void setProxyTargetClass(boolean proxyTargetClass) {
        this.proxyTargetClass = proxyTargetClass;
    }

    @Override
    public List<Advice> getAdvices() {
        return this.advices;
    }

    @Override
    public List<Advice> getAdvices(Method method) {
        List<Advice> result = new ArrayList<Advice>();
        for (Advice advice : this.getAdvices()) {
            Pointcut pointcut = advice.getPointcut();
            if (pointcut.getMethodMatcher().matches(method)) {
                result.add(advice);
            }
        }
        return result;
    }
}
