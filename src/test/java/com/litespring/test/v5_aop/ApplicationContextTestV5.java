package com.litespring.test.v5_aop;

import com.litespring.context.ApplicationContext;
import com.litespring.context.support.ClassPathXmlApplicationContext;
import com.litespring.service.v5.PetStoreService;
import com.litespring.util.MessageTracker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ApplicationContextTestV5 {

    @Before
    public void setUp() {
        MessageTracker.clearMsgs();
    }

    @Test
    public void testPlaceOrder() {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("petstore-v5.xml");
        PetStoreService petStoreService = (PetStoreService) applicationContext.getBean("petStore");

        Assert.assertNotNull(petStoreService.getAccountDao());
        Assert.assertNotNull(petStoreService.getItemDao());

        petStoreService.placeOrder();
        List<String> msgs = MessageTracker.getMsgs();

        Assert.assertEquals(3, msgs.size());
        Assert.assertEquals("start tx", msgs.get(0));
        Assert.assertEquals("place order", msgs.get(1));
        Assert.assertEquals("commit tx", msgs.get(2));
    }
}
