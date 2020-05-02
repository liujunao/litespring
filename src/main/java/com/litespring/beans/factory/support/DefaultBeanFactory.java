package com.litespring.beans.factory.support;

import com.litespring.beans.BeanDefinition;
import com.litespring.beans.PropertyValue;
import com.litespring.beans.SimpleTypeConverter;
import com.litespring.beans.factory.BeanCreationException;
import com.litespring.beans.factory.BeanFactoryAware;
import com.litespring.beans.factory.NoSuchBeanDefinitionException;
import com.litespring.beans.factory.config.BeanPostProcessor;
import com.litespring.beans.factory.config.DependencyDescriptor;
import com.litespring.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.litespring.util.ClassUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//默认 bean 创建工厂类
public class DefaultBeanFactory extends AbstractBeanFactory implements BeanDefinitionRegistry {
    private static final Log logger = LogFactory.getLog(DefaultBeanFactory.class);

    //存放从 xml 文件读取的 bean 信息
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<String, BeanDefinition>();
    private ClassLoader beanClassLoader;
    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<BeanPostProcessor>();

    /**
     * 存放 bean 信息
     *
     * @param beanID
     * @param beanDefinition
     */
    @Override
    public void registerBeanDefinition(String beanID, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanID, beanDefinition);
    }

    /**
     * 根据 id 读取 bean 信息
     *
     * @param beanID
     * @return
     */
    @Override
    public BeanDefinition getBeanDefinition(String beanID) {
        return this.beanDefinitionMap.get(beanID);
    }

    @Override
    public Class<?> getType(String name) throws NoSuchBeanDefinitionException {
        BeanDefinition beanDefinition = getBeanDefinition(name);
        if (beanDefinition == null) {
            throw new NoSuchBeanDefinitionException(name);
        }
        resolveBeanClass(beanDefinition);
        return beanDefinition.getBeanClass();
    }

    @Override
    public List<Object> getBeansByType(Class<?> type) {
        List<Object> result = new ArrayList<>();
        List<String> beanIDs = getBeanIDsByType(type);
        for (String beanID : beanIDs) {
            result.add(getBean(beanID));
        }
        return result;
    }

    private List<String> getBeanIDsByType(Class<?> type) {
        List<String> result = new ArrayList<>();
        for (String beanName : beanDefinitionMap.keySet()) {
            Class<?> beanClass = null;
            try {
                beanClass = getType(beanName);
            } catch (Exception e) {
                logger.warn("can't  load class for bean: " + beanName + ", skip it.");
                continue;
            }
            if (beanClass != null && type.isAssignableFrom(beanClass)) {
                result.add(beanName);
            }
        }
        return result;
    }

    /**
     * 通过反射创建 bean 实例
     *
     * @param beanID
     * @return
     */
    @Override
    public Object getBean(String beanID) {
        BeanDefinition beanDefinition = this.getBeanDefinition(beanID);
        if (beanDefinition == null) {
            throw new BeanCreationException("Bean Definition does not exist");
        }
        if (beanDefinition.isSingleton()) {
            Object bean = this.getSingleton(beanID);
            if (bean == null) {
                bean = createBean(beanDefinition);
                this.registerSingleton(beanID, bean);
            }
            return bean;
        }
        return createBean(beanDefinition);
    }

    protected Object createBean(BeanDefinition beanDefinition) {
        //创建实例
        Object bean = instantiateBean(beanDefinition);
        //设置属性
        populateBean(beanDefinition, bean);
        //populateBeanUseCommonBeanUtils(beanDefinition,bean);
        //初始化
        bean = initializeBean(beanDefinition, bean);
        return bean;
    }

    //创建实例
    private Object instantiateBean(BeanDefinition beanDefinition) {
        //处理构造器注入
        if (beanDefinition.hasConstructorArgumentValues()) { //已有 bean 注入
            ConstructorResolver resolver = new ConstructorResolver(this);
            return resolver.autowireConstructor(beanDefinition);
        } else { //bean 还未注入
            String beanClassName = beanDefinition.getBeanClassName();
            try {
                Class<?> clazz = getBeanClassLoader().loadClass(beanClassName);
                return clazz.newInstance(); //实例化的类须有无参构造器
            } catch (Exception e) {
                throw new BeanCreationException("create bean for " + beanClassName + " failed", e);
            }
        }
    }

    //设置属性
    private void populateBean(BeanDefinition beanDefinition, Object bean) {
        //调用 PostProcessor
        for (BeanPostProcessor processor : this.getBeanPostProcessors()) {
            if (processor instanceof InstantiationAwareBeanPostProcessor) {
                ((InstantiationAwareBeanPostProcessor) processor).postProcessPropertyValues(bean, beanDefinition.getID());
            }
        }
        // 获取一个 bean下所有的 PropertyValue
        List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();
        if (propertyValues == null || propertyValues.isEmpty()) {
            return;
        }
        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this);
        SimpleTypeConverter converter = new SimpleTypeConverter();
        try {
            for (PropertyValue propertyValue : propertyValues) {
                String propertyValueName = propertyValue.getName();
                Object originalValue = propertyValue.getValue();
                Object resolvedValue = valueResolver.resolveValueIfNecessary(originalValue);

                BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                    if (propertyDescriptor.getName().equals(propertyValueName)) {
                        //类型转换
                        Object convertedValue = converter.convertIfNecessary(resolvedValue, propertyDescriptor.getPropertyType());
                        propertyDescriptor.getWriteMethod().invoke(bean, convertedValue);
                        break;
                    }
                }
            }
        } catch (Exception ex) {
            throw new BeanCreationException("Failed to obtain BeanInfo for class [" + beanDefinition.getBeanClassName() + "]");
        }
    }

    //使用 commons-Utils 完成 populateBean 功能
    private void populateBeanUseCommonBeanUtils(BeanDefinition bd, Object bean) {
        List<PropertyValue> propertyValues = bd.getPropertyValues();
        if (propertyValues == null || propertyValues.isEmpty()) {
            return;
        }
        BeanDefinitionValueResolver valueResolver = new BeanDefinitionValueResolver(this);
        try {
            for (PropertyValue propertyValue : propertyValues) {
                String propertyValueName = propertyValue.getName();
                Object originalValue = propertyValue.getValue();
                Object resolvedValue = valueResolver.resolveValueIfNecessary(originalValue);
                BeanUtils.copyProperty(bean, propertyValueName, resolvedValue); //类型转换
            }
        } catch (Exception e) {
            throw new BeanCreationException("Populate bean property failed for[" + bd.getBeanClassName() + "");
        }
    }

    protected Object initializeBean(BeanDefinition beanDefinition, Object bean) {
        invokeAwareMethods(bean);
        //对Bean做初始化
        //创建代理
        if (!beanDefinition.isSynthetic()) {
            return applyBeanPostProcessorsAfterInitialization(bean, beanDefinition.getID());
        }
        return bean;
    }

    private void invokeAwareMethods(final Object bean) {
        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }
    }

    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) {
        Object result = existingBean;
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            result = beanPostProcessor.afterInitialization(result, beanName);
            if (result == null) {
                return result;
            }
        }
        return result;
    }

    @Override
    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    @Override
    public ClassLoader getBeanClassLoader() {
        return this.beanClassLoader != null ? this.beanClassLoader : ClassUtils.getDefaultClassLoader();
    }

    //PostProcessor 添加到 BeanFactory
    @Override
    public void addBeanPostProcessor(BeanPostProcessor postProcessor) {
        beanPostProcessors.add(postProcessor);
    }

    @Override
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }

    @Override
    public Object resolveDependency(DependencyDescriptor descriptor) {
        Class<?> typeToMatch = descriptor.getDependencyType();
        for (BeanDefinition beanDefinition : beanDefinitionMap.values()) {
            //确保BeanDefinition 有class对象
            resolveBeanClass(beanDefinition);
            Class<?> beanClass = beanDefinition.getBeanClass();
            if (typeToMatch.isAssignableFrom(beanClass)) {
                return getBean(beanDefinition.getID());
            }
        }
        return null;
    }

    public void resolveBeanClass(BeanDefinition beanDefinition) {
        if (beanDefinition.hasBeanClass()) {
            return;
        } else {
            try {
                beanDefinition.resolveBeanClass(this.getBeanClassLoader());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("can't load class:" + beanDefinition.getBeanClassName());
            }
        }
    }

}