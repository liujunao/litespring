package com.litespring.test.v3_constructor;

import com.litespring.context.ApplicationContext;
import com.litespring.context.support.ClassPathXmlApplicationContext;
import com.litespring.service.v3.PetStoreService;
import org.junit.Assert;
import org.junit.Test;

public class ApplicationContextTestV3 {
    @Test
    public void testGetBeans() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("petstore-v3.xml");
        PetStoreService petStoreService = (PetStoreService) applicationContext.getBean("petStore");

        Assert.assertNotNull(petStoreService.getAccountDao());
        Assert.assertNotNull(petStoreService.getItemDao());

        Assert.assertEquals(1, petStoreService.getVersion());
    }
}
