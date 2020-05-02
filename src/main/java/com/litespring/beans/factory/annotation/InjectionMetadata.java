package com.litespring.beans.factory.annotation;

import java.util.List;

public class InjectionMetadata {
    private final Class<?> targetClass;
    private List<InjectionElement> injectionElementList;

    public InjectionMetadata(Class<?> targetClass, List<InjectionElement> injectionElementList) {
        this.targetClass = targetClass;
        this.injectionElementList = injectionElementList;
    }

    public List<InjectionElement> getInjectionElementList() {
        return injectionElementList;
    }

    /**
     * 核心的inject,在Bean生命周期的postProcessPropertyValues方法中被调用
     *
     * @param target
     */
    public void inject(Object target) {
        if (injectionElementList == null || injectionElementList.isEmpty()) {
            return;
        }
        for (InjectionElement element : injectionElementList) {
            element.inject(target);
        }
    }
}
