package com.litespring.test.v3_constructor;

import com.litespring.beans.BeanDefinition;
import com.litespring.beans.ConstructorArgument;
import com.litespring.beans.factory.config.RuntimeBeanReference;
import com.litespring.beans.factory.config.TypedStringValue;
import com.litespring.beans.factory.support.DefaultBeanFactory;
import com.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import com.litespring.core.io.ClassPathResource;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class BeanDefinitionTestV3_1 {
    @Test
    public void test() {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinition(new ClassPathResource("petstore-v3.xml"));

        BeanDefinition beanDefinition = factory.getBeanDefinition("petStore");
        Assert.assertEquals("com.litespring.service.v3.PetStoreService", beanDefinition.getBeanClassName());

        ConstructorArgument args = beanDefinition.getConstructorArgument();
        List<ConstructorArgument.ValueHolder> valueHolderList = args.getArgumentValues();
        Assert.assertEquals(3, valueHolderList.size());

        RuntimeBeanReference ref1 = (RuntimeBeanReference) valueHolderList.get(0).getValue();
        Assert.assertEquals("accountDao", ref1.getBeanName());

        RuntimeBeanReference ref2 = (RuntimeBeanReference) valueHolderList.get(1).getValue();
        Assert.assertEquals("itemDao", ref2.getBeanName());

        TypedStringValue strValue = (TypedStringValue) valueHolderList.get(2).getValue();
        Assert.assertEquals("1", strValue.getValue());
    }
}
