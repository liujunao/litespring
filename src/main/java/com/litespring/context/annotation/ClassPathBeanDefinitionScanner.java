package com.litespring.context.annotation;

import com.litespring.beans.BeanDefinition;
import com.litespring.beans.factory.support.BeanDefinitionRegistry;
import com.litespring.beans.factory.support.BeanDefinitionStoreException;
import com.litespring.beans.factory.support.BeanNameGenerator;
import com.litespring.core.io.Resource;
import com.litespring.core.io.support.PackageResourceLoader;
import com.litespring.core.type.classreading.MetadataReader;
import com.litespring.core.type.classreading.SimpleMetadataReader;
import com.litespring.stereotype.Component;
import com.litespring.util.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

public class ClassPathBeanDefinitionScanner {
    private final BeanDefinitionRegistry registry;
    private PackageResourceLoader resourceLoader = new PackageResourceLoader();
    private BeanNameGenerator beanNameGenerator = new AnnotationBeanNameGenerator();

    public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
        this.registry = registry;
    }

    /**
     * @param packagesToScan 传入的base-package 字符串
     * @return
     */
    public Set<BeanDefinition> doScan(String packagesToScan) {
        String[] basePackages = StringUtils.tokenizeToStringArray(packagesToScan, ",");
        Set<BeanDefinition> beanDefinitions = new LinkedHashSet<BeanDefinition>();
        //对每一个basePackages进行循环
        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
            for (BeanDefinition candidate : candidates) {
                beanDefinitions.add(candidate);
                registry.registerBeanDefinition(candidate.getID(), candidate);
            }
        }
        return beanDefinitions;
    }

    public Set<BeanDefinition> findCandidateComponents(String basePackage) {
        Set<BeanDefinition> candidates = new LinkedHashSet<>();
        try {
            //先拿到Resource
            Resource[] resources = resourceLoader.getResources(basePackage);
            for (Resource resource : resources) {
                try {
                    MetadataReader metadataReader = new SimpleMetadataReader(resource);
                    //通过metadataReader 获取AnnotationMetadata 看看其是否有Component注解
                    if (metadataReader.getAnnotationMetadata().hasAnnotation(Component.class.getName())) {
                        //实例化一个ScannedGenericBeanDefinition
                        ScannedGenericBeanDefinition scannedGenericBeanDefinition
                                = new ScannedGenericBeanDefinition(metadataReader.getAnnotationMetadata());
                        String beanName = beanNameGenerator.generateBeanName(scannedGenericBeanDefinition, this.registry);
                        scannedGenericBeanDefinition.setId(beanName);
                        candidates.add(scannedGenericBeanDefinition);
                    }
                } catch (Throwable ex) {
                    throw new BeanDefinitionStoreException("Failed to read candidate component class:" + resource, ex);
                }
            }
        } catch (IOException e) {
            throw new BeanDefinitionStoreException("I/O failure during classpath scanning", e);
        }
        return candidates;
    }
}
