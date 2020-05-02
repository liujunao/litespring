package com.litespring.test.v4_springAnnotation;

import com.litespring.context.ApplicationContext;
import com.litespring.context.support.ClassPathXmlApplicationContext;
import com.litespring.service.v4.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

//测试 spring 注解
public class ApplicationContextTestV4 {
    @Test
    public void testGetBeanProperty() {
        ApplicationContext ctx = new ClassPathXmlApplicationContext("petstore-v4.xml");
        PetStoreService petStoreService = (PetStoreService) ctx.getBean("petStore");

        Assert.assertNotNull(petStoreService.getAccountDao());
        Assert.assertNotNull(petStoreService.getItemDao());
    }
}
