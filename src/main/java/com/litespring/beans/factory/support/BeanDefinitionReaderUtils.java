package com.litespring.beans.factory.support;

import com.litespring.beans.BeanDefinition;
import com.litespring.util.StringUtils;

public class BeanDefinitionReaderUtils {
    public static final String GENERATED_BEAN_NAME_SEPARATOR = "#";

    public static String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry, boolean isInnerBean) throws BeanDefinitionStoreException {
        String generatedBeanName = definition.getBeanClassName();
        if (!StringUtils.hasText(generatedBeanName)) {
            throw new BeanDefinitionStoreException("Unnamed bean definition specifies neither " +
                    "'class' nor 'parent' nor 'factory-bean' - can't generate bean name");
        }
        String id = generatedBeanName;
        if (isInnerBean) {
            id = generatedBeanName + GENERATED_BEAN_NAME_SEPARATOR + Integer.toHexString(System.identityHashCode(definition));
        } else {
            int counter = -1;
            while (counter == -1 || (registry.getBeanDefinition(id) != null)) {
                counter++;
                id = generatedBeanName + GENERATED_BEAN_NAME_SEPARATOR + counter;
            }
        }
        return id;
    }

    public static String generateBeanName(BeanDefinition beanDefinition, BeanDefinitionRegistry registry) throws BeanDefinitionStoreException {
        return generateBeanName(beanDefinition, registry, false);
    }

    public static String registerWithGeneratedName(GenericBeanDefinition definition, BeanDefinitionRegistry registry) {
        String generatedName = generateBeanName(definition, registry, false);
        registry.registerBeanDefinition(generatedName, definition);
        return generatedName;
    }
}
