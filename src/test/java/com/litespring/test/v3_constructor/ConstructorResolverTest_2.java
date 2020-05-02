package com.litespring.test.v3_constructor;

import com.litespring.beans.BeanDefinition;
import com.litespring.beans.factory.support.ConstructorResolver;
import com.litespring.beans.factory.support.DefaultBeanFactory;
import com.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import com.litespring.core.io.ClassPathResource;
import com.litespring.service.v3.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

//测试 ConstructorResolver(处理多个构造器时的参数个数匹配、参数类型适配、参数类型转换)
public class ConstructorResolverTest_2 {

    @Test
    public void test() {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinition(new ClassPathResource("petstore-v3.xml"));

        BeanDefinition beanDefinition = factory.getBeanDefinition("petStore");
        ConstructorResolver resolver = new ConstructorResolver(factory);
        PetStoreService petStoreService = (PetStoreService) resolver.autowireConstructor(beanDefinition);

        Assert.assertEquals(1, petStoreService.getVersion());
        Assert.assertNotNull(petStoreService.getAccountDao());
        Assert.assertNotNull(petStoreService.getItemDao());
    }
}
