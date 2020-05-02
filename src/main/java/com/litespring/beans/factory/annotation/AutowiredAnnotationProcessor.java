package com.litespring.beans.factory.annotation;

import com.litespring.beans.BeansException;
import com.litespring.beans.factory.BeanCreationException;
import com.litespring.beans.factory.config.AutowireCapableBeanFactory;
import com.litespring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.litespring.core.annotation.AnnotationUtils;
import com.litespring.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;

public class AutowiredAnnotationProcessor implements InstantiationAwareBeanPostProcessor {

    private AutowireCapableBeanFactory beanFactory;
    private String requiredParameterName = "required";
    private boolean requiredParameterValue = true;
    private final Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<Class<? extends Annotation>>();

    public AutowiredAnnotationProcessor() {
        autowiredAnnotationTypes.add(Autowired.class);
    }

    public InjectionMetadata buildAutowiringMetadata(Class<?> clazz) {
        LinkedList<InjectionElement> elements = new LinkedList<InjectionElement>();
        Class<?> targetClass = clazz;
        //TODO 踩过坑
        do {
            LinkedList<InjectionElement> currentElements = new LinkedList<InjectionElement>();
            for (Field field : targetClass.getDeclaredFields()) {
                Annotation annotation = findAutowiredAnnotation(field);
                if (annotation != null) {
                    if (Modifier.isStatic(field.getModifiers())) {
                        continue;
                    }
                    boolean required = determineRequiredStatus(annotation);
                    currentElements.add(new AutowiredFieldElement(field, required, beanFactory));
                }
            }
            for (Method method : targetClass.getDeclaredMethods()) {
                //TODO 处理方法注入
            }
            elements.addAll(0, currentElements);
            targetClass = targetClass.getSuperclass();
        }
        while (targetClass != null && targetClass != Object.class);
        return new InjectionMetadata(clazz, elements);
    }

    protected boolean determineRequiredStatus(Annotation ann) {
        try {
            Method method = ReflectionUtils.findMethod(ann.annotationType(), this.requiredParameterName);
            if (method == null) {
                // Annotations like @Inject and @Value don't have a method (attribute) named "required"
                // -> default to required status
                return true;
            }
            return (this.requiredParameterValue == (Boolean) ReflectionUtils.invokeMethod(method, ann));
        } catch (Exception ex) {
            // An exception was thrown during reflective invocation of the required attribute
            // -> default to required status
            return true;
        }
    }

    private Annotation findAutowiredAnnotation(AccessibleObject accessibleObject) {
//        容纳其他的注解
        for (Class<? extends Annotation> type : this.autowiredAnnotationTypes) {
            Annotation annotation = AnnotationUtils.getAnnotation(accessibleObject, type);
            if (annotation != null) {
                return annotation;
            }
        }
        return null;
    }

    public void setBeanFactory(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void postProcessPropertyValues(Object bean, String beanName) throws BeansException {
        InjectionMetadata metadata = buildAutowiringMetadata(bean.getClass());
        try {
            metadata.inject(bean);
        } catch (Exception ex) {
            throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", ex);
        }
    }

    @Override
    public Object beforeInitialization(Object bean, String beanName) throws BeansException {
        //do nothing
        return bean;
    }

    @Override
    public Object afterInitialization(Object bean, String beanName) throws BeansException {
        // do nothing
        return bean;
    }

    @Override
    public Object beforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    @Override
    public boolean afterInstantiation(Object bean, String beanName) throws BeansException {
        // do nothing
        return true;
    }
}