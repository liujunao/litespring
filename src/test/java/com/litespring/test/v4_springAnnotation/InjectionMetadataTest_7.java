package com.litespring.test.v4_springAnnotation;

import com.litespring.beans.factory.annotation.AutowiredFieldElement;
import com.litespring.beans.factory.annotation.InjectionElement;
import com.litespring.beans.factory.annotation.InjectionMetadata;
import com.litespring.beans.factory.support.DefaultBeanFactory;
import com.litespring.beans.factory.xml.XmlBeanDefinitionReader;
import com.litespring.core.io.ClassPathResource;
import com.litespring.core.io.Resource;
import com.litespring.dao.v4.AccountDao;
import com.litespring.dao.v4.ItemDao;
import com.litespring.service.v4.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.LinkedList;

public class InjectionMetadataTest_7 {

    @Test
    public void testInjection() throws NoSuchFieldException {
        DefaultBeanFactory factory = new DefaultBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
        Resource resource = new ClassPathResource("petstore-v4.xml");
        reader.loadBeanDefinition(resource);

        Class<?> clazz = PetStoreService.class;
        LinkedList<InjectionElement> elements = new LinkedList<>();
        {
            Field field = PetStoreService.class.getDeclaredField("accountDao");
            InjectionElement injectionElement = new AutowiredFieldElement(field, true, factory);
            elements.add(injectionElement);
        }
        {
            Field field = PetStoreService.class.getDeclaredField("itemDao");
            InjectionElement injectionElement = new AutowiredFieldElement(field, true, factory);
            elements.add(injectionElement);
        }
        InjectionMetadata metadata = new InjectionMetadata(clazz, elements);
        PetStoreService petStoreService = new PetStoreService();
        metadata.inject(petStoreService);
        Assert.assertTrue(petStoreService.getAccountDao() instanceof AccountDao);
        Assert.assertTrue(petStoreService.getItemDao() instanceof ItemDao);
    }
}
