package com.litespring.test.v2_setter;

import com.litespring.beans.BeanDefinition;
import com.litespring.beans.PropertyValue;
import com.litespring.beans.factory.config.RuntimeBeanReference;
import com.litespring.beans.factory.support.DefaultBeanFactory;
import com.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import com.litespring.core.io.ClassPathResource;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class BeanDefinitionTestV2_1 {

    @Test
    public void testGetBeanDefinition() {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        reader.loadBeanDefinition(new ClassPathResource("petStore-v2.xml"));
        BeanDefinition beanDefinition = factory.getBeanDefinition("petStore");
        List<PropertyValue> propertyValues = beanDefinition.getPropertyValues();

        Assert.assertTrue(propertyValues.size() == 4);
        {
            PropertyValue propertyValue = this.getPropertyValue("accountDao", propertyValues);
            Assert.assertNotNull(propertyValue);
            Assert.assertTrue(propertyValue.getValue() instanceof RuntimeBeanReference);
        }
        {
            PropertyValue propertyValue = this.getPropertyValue("itemDao", propertyValues);
            Assert.assertNotNull(propertyValue);
            Assert.assertTrue(propertyValue.getValue() instanceof RuntimeBeanReference);
        }
    }

    private PropertyValue getPropertyValue(String name, List<PropertyValue> propertyValueList) {
        for (PropertyValue propertyValue : propertyValueList) {
            if (propertyValue.getName().equals(name)) {
                return propertyValue;
            }
        }
        return null;
    }
}
